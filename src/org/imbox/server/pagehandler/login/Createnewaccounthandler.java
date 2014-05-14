package org.imbox.server.pagehandler.login;
import java.io.IOException;

import org.imbox.server.functions.Authenticator;
import org.imbox.server.functions.Httpresponser;
import org.imbox.server.jsonreaders.Loginrequestreader;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class Createnewaccounthandler implements HttpHandler
{
	private HttpExchange httpconnection;
	private String connectionIP;
	@Override
	public void handle(HttpExchange httpconnection) throws IOException 
	{
		this.httpconnection = httpconnection;
		connectionIP = httpconnection.getRemoteAddress().getAddress().toString();
		Handlerthread multithread = new Handlerthread();
		multithread.setName("clientconnection");
		multithread.start();
		
		
	}
	private class Handlerthread extends Thread
	{
		@Override
		public void run() 
		{
			try
			{
				if (httpconnection.getRequestMethod().equals("GET"))
				{
					System.out.println("this is a http get method @ newaccount");
					String response = "this is a http get method";
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						System.out.println("this is a post method @ newaccount");
						Loginrequestreader requestreader = new Loginrequestreader(httpconnection);
						requestreader.getjson();
						System.out.println("[newaccount]account = " + requestreader.getaccount());
						System.out.println("[newaccount]password = " + requestreader.getpassword());
						System.out.println("[newaccount]MAC = " + requestreader.getMAC());
						Authenticator auth = new Authenticator();
						String token = auth.Createaccount(requestreader.getaccount(), requestreader.getpassword(), requestreader.getMAC(),connectionIP);
						if (token.length() >0)
						{
							JSONObject obj=new JSONObject();
							obj.put("token", token);
							obj.put("succ", true);
							obj.put("errorcode", 0);
							String response = obj.toString();
							Httpresponser res = new Httpresponser(httpconnection, response);
							res.execute();
							System.out.println("[new account] create account success: " + requestreader.getaccount());
						}else
						{
							JSONObject obj=new JSONObject();
							obj.put("token", new String());
							obj.put("succ", false);
							obj.put("errorcode", 19);		 //account registered
							String response = obj.toString();
							Httpresponser res = new Httpresponser(httpconnection, response);
							res.execute();
							System.out.println("[new account] create account fail" );
						}
					}else
					{
						System.out.println("@newaccount - unknown method:" + httpconnection.getRequestMethod());
						String response = "this is a unkown method"+ httpconnection.getRequestMethod();
						Httpresponser res = new Httpresponser(httpconnection, response);
						res.execute();
					}
				}
			}catch(Exception e)
			{
				try
				{
					System.out.println("the below error has happen in 'Createnewaccounthandler'");
					JSONObject obj=new JSONObject();
					obj.put("token", new String());
					obj.put("succ", false);
					obj.put("errorcode", 4);		 //server exception error
					String response = obj.toString();
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
					e.printStackTrace();
				}catch(Exception layer2e)
				{
					System.out.println("the below error has happen in 'Createnewaccounthandler', layer2exception");
					layer2e.printStackTrace();
				}
			}
		}
		
	}
}