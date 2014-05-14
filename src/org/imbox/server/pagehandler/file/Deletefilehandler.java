package org.imbox.server.pagehandler.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.imbox.database.Delete_File_user;
import org.imbox.database.Filefidgetter;
import org.imbox.database.search_block;
import org.imbox.infrastructure.Workspace;
import org.imbox.server.functions.Authenticator;
import org.imbox.server.functions.Httpresponser;
import org.imbox.server.functions.LOCK.Returntype;
import org.imbox.server.jsonreaders.Deletefilereader;
import org.imbox.server.main.IMboxserver;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Deletefilehandler implements HttpHandler
{
	private HttpExchange httpconnection;
	private String IP;
	@Override
	public void handle(HttpExchange httpconnection) throws IOException {
		this.httpconnection = httpconnection;
		IP = httpconnection.getRemoteAddress().getAddress().toString();
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
					System.out.println("this is a http get method @ deletefilehandler");
					String response = "this is a http get method";
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						Authenticator auth = new Authenticator();
						Deletefilereader reader = new Deletefilereader(httpconnection);
						reader.readjson();
						System.out.println("this is a http post method @ deletefilehandler");
						System.out.println("[deletefilehandler]filename = "+reader.getfilename());
						System.out.println("[deletefilehandler]token = "+reader.gettoken());
						System.out.println("[deletefilehandler]mac = "+reader.getmac());
						if (auth.Authenticatebytoken(reader.gettoken(), reader.getmac(), IP))
						{
							Returntype lockresult = IMboxserver.lockthread.lock(reader.getmac());
							if (lockresult.islock())
							{
								if (lockresult.mac().equals(reader.getmac()))
								{
									IMboxserver.lockthread.resettimer();
									Filefidgetter ffg = new Filefidgetter(auth.getaccountname(), reader.getfilename());
									ffg.preparefileid();
									String fid = ffg.getfileid();
									search_block sb = new search_block(fid);
									sb.searchBlock();
									List<String> deletetarget = sb.getList();
									//TODO: becuase file md5 currently = fid
									Delete_File_user dfu = new Delete_File_user(auth.getaccountname(), fid);
									dfu.DeleteFileUser();
									if (dfu.getFileDelete())
									{
										if (dfu.getDeleteFromHD())
										{
											for (int i =0;i<deletetarget.size();i++)
											{
												System.out.println("deleting target = "+deletetarget.get(i));
												System.out.println("delete job status = " + FileUtils.deleteQuietly(new File(Workspace.SYSDIRs+deletetarget.get(i))));
											}
										}
										JSONObject obj = new JSONObject();
										obj.put("succ", true);
										obj.put("errorcode", 0);
										String response = obj.toString();
										Httpresponser res = new Httpresponser(httpconnection, response);
										res.execute();
									}else
									{
										JSONObject obj = new JSONObject();
										obj.put("succ", false);
										obj.put("errorcode", 6);
										String response = obj.toString();
										Httpresponser res = new Httpresponser(httpconnection, response);
										res.execute();
									}
									IMboxserver.lockthread.resettimer();
								}else
								{
									JSONObject obj = new JSONObject();
									obj.put("succ", false);
									obj.put("errorcode", 7);
									String response = obj.toString();
									Httpresponser res = new Httpresponser(httpconnection, response);
									res.execute();
								}
							}else
							{
								JSONObject obj = new JSONObject();
								obj.put("succ", false);
								obj.put("errorcode", 8);
								String response = obj.toString();
								Httpresponser res = new Httpresponser(httpconnection, response);
								res.execute();
							}
							
						}else
						{
							JSONObject obj = new JSONObject();
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
					System.out.println("the below error has happen in 'Deletefilehandler'");
					JSONObject obj=new JSONObject();
					obj.put("succ", false);
					obj.put("errorcode", 4);
					String response = obj.toString();
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
					e.printStackTrace();
				}catch(Exception layer2e)
				{
					System.out.println("the below error has happen in 'Deletefilehandler',layer2exception");
					layer2e.printStackTrace();
				}
			}
		}
	}
	
}