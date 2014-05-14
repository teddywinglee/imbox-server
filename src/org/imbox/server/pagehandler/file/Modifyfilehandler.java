package org.imbox.server.pagehandler.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.imbox.database.Filefidgetter;
import org.imbox.database.Modify_File;
import org.imbox.database.search_block;
import org.imbox.infrastructure.Workspace;
import org.imbox.server.functions.Authenticator;
import org.imbox.server.functions.Httpresponser;
import org.imbox.server.functions.LOCK.Returntype;
import org.imbox.server.jsonreaders.Modifyfilereader;
import org.imbox.server.main.IMboxserver;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Modifyfilehandler implements HttpHandler
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
					System.out.println("this is a http get method @ Modifyfilehandler");
					String response = "this is a http get method";
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						System.out.println("this is a http post method @ Modifyfilehandler");
						Modifyfilereader reader = new Modifyfilereader(httpconnection);
						reader.readjson();
						System.out.println("[Modifyfilehandler]filename = " + reader.getfilename());
						System.out.println("[Modifyfilehandler]token = " + reader.gettoken());
						System.out.println("[Modifyfilehandler]mac = " + reader.getmac());
						System.out.println("[Modifyfilehandler]newmd5 = " + reader.getnewmd5());
						Authenticator auth = new Authenticator();
						if (auth.Authenticatebytoken(reader.gettoken(), reader.getmac(), connectionIP))
						{
							Returntype lockresult = IMboxserver.lockthread.lock(reader.getmac());
							if (lockresult.islock())
							{
								if (lockresult.mac().equals(reader.getmac()))
								{
									IMboxserver.lockthread.resettimer();
									Modify_File modifyfile = new Modify_File(auth.getaccountname(), reader.getfilename(), reader.getnewmd5(),  reader.getfilename(),  reader.getnewmd5());
									Filefidgetter ffg = new Filefidgetter(auth.getaccountname(), reader.getfilename());
									ffg.preparefileid();
									String fid = ffg.getfileid();
									search_block sb = new search_block(fid);
									sb.searchBlock();
									List<String> deletetarget = sb.getList();
									modifyfile.ModifyFile();
									if (modifyfile.getFileModify())
									{
										if (modifyfile.getDeleteFromHD())
										{
											for (int i =0;i<deletetarget.size();i++)
											{
												System.out.println("deleting target = "+deletetarget.get(i));
												System.out.println("delete job status = " + FileUtils.deleteQuietly(new File(Workspace.SYSDIRs+deletetarget.get(i))));
											}
										}
										JSONObject obj=new JSONObject();
										obj.put("succ", true);
										obj.put("errorcode", 0);
										String response = obj.toString();
										Httpresponser res = new Httpresponser(httpconnection, response);
										res.execute();
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
					System.out.println("the below error has happen in 'Modifyfilehandler'");
					JSONObject obj=new JSONObject();
					obj.put("succ", false);
					obj.put("errorcode", 4);
					String response = obj.toString();
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
					e.printStackTrace();
				}catch(Exception layer2e)
				{
					System.out.println("the below error has happen in 'Modifyfilehandler',layer2exception");
					layer2e.printStackTrace();
				}
			}
		}
	}
	
}