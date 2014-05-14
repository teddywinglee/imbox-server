package org.imbox.server.pagehandler.block;
import java.io.IOException;

import org.imbox.infrastructure.Casting;
import org.imbox.infrastructure.Workspace;
import org.imbox.infrastructure.file.Block;
import org.imbox.server.functions.Authenticator;
import org.imbox.server.functions.Httpresponser;
import org.imbox.server.functions.LOCK.Returntype;
import org.imbox.server.jsonreaders.Getblockreader;
import org.imbox.server.main.IMboxserver;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class Getblockhandler implements HttpHandler
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
					System.out.println("this is a http get method @ getfile, post should be used");
					String response = "this is a http get method @ getfile";
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						System.out.println("this is a post method @ getfile");
						Getblockreader reader = new Getblockreader(httpconnection);
						reader.getjson();
						System.out.println("[getfile]token = " + reader.gettoken());
						System.out.println("[getfile]MAC = "+ reader.getmac());
						System.out.println("[getfile]blockname = " + reader.getblockname());
						System.out.println("[getfile]sequence = " + Integer.toString(reader.getsequence()));
						Authenticator auth = new Authenticator();
						if (auth.Authenticatebytoken(reader.gettoken(), reader.getmac(),connectionIP))
						{
							Returntype lockresult = IMboxserver.lockthread.lock(reader.getmac());
							if (lockresult.islock())
							{
								if (lockresult.mac().equals(reader.getmac()))
								{
									IMboxserver.lockthread.resettimer();
									try
									{
										byte[] bytedata = Block.readBlockFromHD(Workspace.SYSDIRs, reader.getblockname());
										String data = Casting.bytesToString(bytedata);
										//return result
										if (data.length() >0)
										{
											JSONObject obj=new JSONObject();
											obj.put("succ", true);
											obj.put("data", data);
											obj.put("errorcode", 0); 
											String response = obj.toString();
											Httpresponser res = new Httpresponser(httpconnection, response);
											res.execute();
										}else
										{
											JSONObject obj=new JSONObject();
											obj.put("succ", false);
											obj.put("data", new String());
											obj.put("errorcode", 3);
											String response = obj.toString();
											Httpresponser res = new Httpresponser(httpconnection, response);
											res.execute();
										}
									}catch(Exception e)
									{
										e.printStackTrace();
										JSONObject obj=new JSONObject();
										obj.put("succ", false);
										obj.put("data", new String()); 
										obj.put("errorcode", 4);
										String response = obj.toString();
										Httpresponser res = new Httpresponser(httpconnection, response);
										res.execute();
									}
									IMboxserver.lockthread.resettimer();
								}else
								{
									JSONObject obj=new JSONObject();
									obj.put("succ", false);
									obj.put("data", new String()); 
									obj.put("errorcode", 7);
									String response = obj.toString();
									Httpresponser res = new Httpresponser(httpconnection, response);
									res.execute();
								}
							}else
							{
								JSONObject obj=new JSONObject();
								obj.put("succ", false);
								obj.put("data", new String()); 
								obj.put("errorcode", 8);
								String response = obj.toString();
								Httpresponser res = new Httpresponser(httpconnection, response);
								res.execute();
							}
							
							
						}else
						{
							JSONObject obj=new JSONObject();
							obj.put("succ", false);
							obj.put("data", new String());
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
					System.out.println("the below error has happen in 'Getblockhandler'");
					JSONObject obj=new JSONObject();
					obj.put("succ", false);
					obj.put("data", new String());
					obj.put("errorcode", 4);
					String response = obj.toString();
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
					e.printStackTrace();
				}catch(Exception layer2e)
				{
					System.out.println("the below error has happen in 'Getblockhandler',layer2exception");
					layer2e.printStackTrace();
				}
			}
		}
		
	}
	
}