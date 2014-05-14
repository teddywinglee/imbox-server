package org.imbox.server.pagehandler.block;

import java.io.IOException;

import org.imbox.database.Filefidgetter;
import org.imbox.database.search_block;
import org.imbox.server.functions.Authenticator;
import org.imbox.server.functions.Blocklistgetter;
import org.imbox.server.functions.Httpresponser;
import org.imbox.server.jsonreaders.Getblockrecordreader;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class Getblockrecordhandler implements HttpHandler
{
	private HttpExchange httpconnection;
	private String connectionIP;
	@Override
	public void handle(HttpExchange httpconnection) throws IOException {
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
					System.out.println("this is a http get method @ Getblockrecordhandler, post should be used");
					String response = "this is a http get method @ Getblockrecordhandler";
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						System.out.println("this is a post method @ Getblockrecordhandler");
						Getblockrecordreader reader = new Getblockrecordreader(httpconnection);
						reader.getjson();
						System.out.println("[Getblockrecordhandler]MAC = " + reader.getmac());
						System.out.println("[Getblockrecordhandler]token = " + reader.gettoken());
						System.out.println("[Getblockrecordhandler]filename = " + reader.getfilename());
						Authenticator auth = new Authenticator();
						if (auth.Authenticatebytoken(reader.gettoken(), reader.getmac(),connectionIP))
						{
							//authenticate success
							Filefidgetter ffg = new Filefidgetter(auth.getaccountname(), reader.getfilename());
							ffg.preparefileid();
							search_block sb = new search_block(ffg.getfileid());
							sb.searchBlock();
							Blocklistgetter blg = new Blocklistgetter(sb.getList());
							blg.preparejsonarray();
							JSONObject obj=new JSONObject();
							obj.put("blocklist", blg.getjsonarray());
							obj.put("succ", true);
							obj.put("errorcode", 0);
							String response = obj.toString();
							Httpresponser res = new Httpresponser(httpconnection, response);
							res.execute();
						}else
						{
							String data = new String();
							JSONObject obj=new JSONObject();
							obj.put("data", data);
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
					System.out.println("the below error has happen in 'Getblockrecordhandler'");
					JSONObject obj=new JSONObject();
					obj.put("blocklist", new JSONArray());
					obj.put("succ", false);
					obj.put("errorcode", 4);
					String response = obj.toString();
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
					e.printStackTrace();
				}catch(Exception layer2e)
				{
					System.out.println("the below error has happen in 'Getblockrecordhandler',layer2exception");
					layer2e.printStackTrace();
				}
			}
		}
	}
}