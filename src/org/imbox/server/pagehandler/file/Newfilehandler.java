package org.imbox.server.pagehandler.file;

import java.io.IOException;

import org.imbox.database.Insert_File;
import org.imbox.server.functions.Authenticator;
import org.imbox.server.functions.Httpresponser;
import org.imbox.server.functions.LOCK.Returntype;
import org.imbox.server.jsonreaders.Newfilereader;
import org.imbox.server.main.IMboxserver;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Newfilehandler implements HttpHandler
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
					System.out.println("this is a http get method @ newfilehandler");
					String response = "this is a http get method";
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						System.out.println("this is a http post method @ newfilehandler");
						Newfilereader reader = new Newfilereader(httpconnection);
						reader.readjson();
						System.out.println("[newfilehandler]token = " + reader.gettoken());
						System.out.println("[newfilehandler]mac = " + reader.getmac());
						System.out.println("[newfilehandler]filename = " + reader.getfilename());
						System.out.println("[newfilehandler]md5 = " + reader.getmd5());
						Authenticator auth = new Authenticator();
						if (auth.Authenticatebytoken(reader.gettoken(), reader.getmac(), connectionIP))
						{
							Returntype lockresult = IMboxserver.lockthread.lock(reader.getmac());
							if (lockresult.islock())
							{
								if (lockresult.mac().equals(reader.getmac()))
								{
									IMboxserver.lockthread.resettimer();
									//db
									Insert_File newfile = new Insert_File(auth.getaccountname(), reader.getfilename(), reader.getmd5(),  reader.getmd5(),  reader.getmd5());
									newfile.InsertFile();
									if (newfile.getFileInsert())
									{
										if (newfile.getBlockInsert())
										{
											JSONObject obj=new JSONObject();
											obj.put("succ", true);
											obj.put("errorcode", 0);
											String response = obj.toString();
											Httpresponser res = new Httpresponser(httpconnection, response);
											res.execute();
										}else
										{
											JSONObject obj=new JSONObject();
											obj.put("succ", true);
											obj.put("errorcode", 40);
											String response = obj.toString();
											Httpresponser res = new Httpresponser(httpconnection, response);
											res.execute();
										}
										
									}else
									{
										JSONObject obj=new JSONObject();
										obj.put("succ", false);
										obj.put("errorcode", 5);
										String response = obj.toString();
										Httpresponser res = new Httpresponser(httpconnection, response);
										res.execute();
									}
									IMboxserver.lockthread.resettimer();
									//response
								}else
								{
									JSONObject obj=new JSONObject();
									obj.put("succ", false);
									obj.put("errorcode", 7);
									String response = obj.toString();
									Httpresponser res = new Httpresponser(httpconnection, response);
									res.execute();
								}
							}else
							{
								JSONObject obj=new JSONObject();
								obj.put("succ", false);
								obj.put("errorcode", 8);
								String response = obj.toString();
								Httpresponser res = new Httpresponser(httpconnection, response);
								res.execute();
							}
							
						}else
						{
							JSONObject obj=new JSONObject();
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
					System.out.println("the below error has happen in 'Newfilehandler'");
					JSONObject obj=new JSONObject();
					obj.put("succ", false);
					obj.put("errorcode", 4);
					String response = obj.toString();
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
					e.printStackTrace();
				}catch(Exception layer2e)
				{
					System.out.println("the below error has happen in 'Newfilehandler',layer2exception");
					layer2e.printStackTrace();
				}
			}
		}
	}
	
}