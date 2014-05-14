package org.imbox.server.pagehandler.sharelink;
import java.io.IOException;

import org.imbox.database.Url;
import org.imbox.server.functions.Authenticator;
import org.imbox.server.functions.Httpresponser;
import org.imbox.server.jsonreaders.Deletefilereader;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class GenerateURLhandler implements HttpHandler
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
					System.out.println("this is a http get method @ generateurl, post should be used");
					String response = "this is a http get method @ generateurl";
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						System.out.println("this is a post method @ generateurl");
						Deletefilereader reader = new Deletefilereader(httpconnection);
						reader.readjson();
						System.out.println("[generateurl]filename = " + reader.getfilename());
						System.out.println("[generateurl]MAC = " + reader.getmac());
						System.out.println("[generateurl]token = " + reader.gettoken());
						Authenticator auth = new Authenticator();
						if (auth.Authenticatebytoken(reader.gettoken(), reader.getmac(),connectionIP))
						{
							Url returnurl =new Url(auth.getaccountname(), reader.getfilename());
							JSONObject obj=new JSONObject();
							obj.put("URL", returnurl.geturlname());
							obj.put("succ", true);
							obj.put("errorcode", 0);  // TODO: change errorcode if needed
							String response = obj.toString();
							Httpresponser res = new Httpresponser(httpconnection, response);
							res.execute();
						}else
						{
							//authenticate fail
							JSONObject obj=new JSONObject();
							obj.put("URL", new String());
							obj.put("succ", false);
							obj.put("errorcode", 1);
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
					System.out.println("the below error has happen in 'GenerateURLhandler'");
					JSONObject obj=new JSONObject();
					obj.put("URL", new String());
					obj.put("succ", false);
					obj.put("errorcode", 4);
					String response = obj.toString();
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
					e.printStackTrace();
				}catch(Exception layer2e)
				{
					System.out.println("the below error has happen in 'GenerateURLhandler',layer2exception");
					layer2e.printStackTrace();
				}
			}
		}
		
	}
}