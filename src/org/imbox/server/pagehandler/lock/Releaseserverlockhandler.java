package org.imbox.server.pagehandler.lock;
import java.io.IOException;

import org.imbox.server.functions.Authenticator;
import org.imbox.server.functions.Httpresponser;
import org.imbox.server.jsonreaders.TokenMACreader;
import org.imbox.server.main.IMboxserver;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class Releaseserverlockhandler implements HttpHandler
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
					System.out.println("this is a http get method @ releaseserverlock");
					String response = "this is a http get method @ releaseserverlock";
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						System.out.println("this is a post method @ releaseserverlock");
						TokenMACreader requestreader = new TokenMACreader(httpconnection);
						requestreader.getjson();
						System.out.println("[releaseserverlock]token = " + requestreader.gettoken());
						System.out.println("[releaseserverlock]MAC = " + requestreader.getMAC());
						Authenticator auth = new Authenticator();
						if (auth.Authenticatebytoken(requestreader.gettoken(), requestreader.getMAC(),connectionIP))
						{
							IMboxserver.lockthread.release(requestreader.getMAC());
							JSONObject obj=new JSONObject();
							obj.put("succ", true);
							obj.put("errorcode", 0);        //TODO: error code here if any
							String response = obj.toString();
							Httpresponser res = new Httpresponser(httpconnection, response);
							res.execute();
						}else
						{
							JSONObject obj=new JSONObject();
							obj.put("succ", false);
							obj.put("errorcode", 1);		 //TODO: error code here if any
							String response = obj.toString();
							Httpresponser res = new Httpresponser(httpconnection, response);
							res.execute();
						}
					}else
					{
						System.out.println("unknown method:" + httpconnection.getRequestMethod());
						String response = "this is a unkown method"+ httpconnection.getRequestMethod();
						Httpresponser res = new Httpresponser(httpconnection, response);
						res.execute();
					}
				}
			}catch(Exception e)
			{
				try
				{
					System.out.println("the below error has happen in 'Releaseserverlockhandler'");
					JSONObject obj=new JSONObject();
					obj.put("succ", false);
					obj.put("errorcode", 4);
					String response = obj.toString();
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
					e.printStackTrace();
				}catch(Exception layer2e)
				{
					System.out.println("the below error has happen in 'Releaseserverlockhandler',layer2exception");
					layer2e.printStackTrace();
				}
			}
		}
		
	}
}