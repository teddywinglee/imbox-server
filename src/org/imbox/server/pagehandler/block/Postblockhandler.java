package org.imbox.server.pagehandler.block;
import java.io.IOException;
import java.util.List;

import org.imbox.database.Filefidgetter;
import org.imbox.database.Insert_block_V2;
import org.imbox.database.broadcast_IP;
import org.imbox.infrastructure.Casting;
import org.imbox.infrastructure.Hash;
import org.imbox.infrastructure.Workspace;
import org.imbox.infrastructure.file.Block;
import org.imbox.server.functions.Authenticator;
import org.imbox.server.functions.Httpresponser;
import org.imbox.server.functions.LOCK.Returntype;
import org.imbox.server.functions.boardcast.SocketClient;
import org.imbox.server.jsonreaders.Postblockreader;
import org.imbox.server.main.IMboxserver;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class Postblockhandler implements HttpHandler
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
					System.out.println("this is a http get method @ postblcok, post should be used");
					String response = "this is a http get method @ postblcok";
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						System.out.println("this is a post method @ postblcok");
						Postblockreader reader = new Postblockreader(httpconnection);
						reader.getjson();
						System.out.println("[postblcok]token = " + reader.gettoken());
						System.out.println("[postblcok]MAC = " + reader.getmac());
						System.out.println("[postblcok]filename = " + reader.getfilename());
						//System.out.println("blockdata = " + reader.getdatastring());
						System.out.println("[postblcok]sequence = " + Integer.toString(reader.getsequence()));
						Authenticator auth = new Authenticator();
						if (auth.Authenticatebytoken(reader.gettoken(), reader.getmac(),connectionIP))
						{
							Returntype lockresult = IMboxserver.lockthread.lock(reader.getmac());
							if (lockresult.islock())
							{
								if (lockresult.mac().equals(reader.getmac()))
								{
									IMboxserver.lockthread.resettimer();
									byte[] blockdata = Casting.stringToBytes(reader.getdatastring());
									Filefidgetter ffg = new Filefidgetter(auth.getaccountname(), reader.getfilename());
									ffg.preparefileid();
									Insert_block_V2 ib2 = new Insert_block_V2(Hash.getMD5String(blockdata), reader.getsequence(), ffg.getfileid());
									ib2.InsertBlock();
									Block.writeBlock(Workspace.SYSDIRs, blockdata);
									JSONObject obj=new JSONObject();
									obj.put("succ", true);
									obj.put("errorcode", 0);
									String response = obj.toString();
									Httpresponser res = new Httpresponser(httpconnection, response);
									res.execute();
									broadcast_IP bip = new broadcast_IP(auth.getaccountname());
									bip.Broadcast();
									List<String> broadcasttarget = bip.getList();
									for (int i =0;i<broadcasttarget.size();i++)
									{
										new SocketClient(broadcasttarget.size(),broadcasttarget.get(i));
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
					System.out.println("the below error has happen in 'Postblockhandler'");
					JSONObject obj=new JSONObject();
					obj.put("succ", false);
					obj.put("errorcode", 1);
					String response = obj.toString();
					Httpresponser res = new Httpresponser(httpconnection, response);
					res.execute();
					e.printStackTrace();
					}catch(Exception layer2e)
				{
					System.out.println("the below error has happen in 'Postblockhandler',layer2exception");
					layer2e.printStackTrace();
				}
			}
		}
		
	}
}