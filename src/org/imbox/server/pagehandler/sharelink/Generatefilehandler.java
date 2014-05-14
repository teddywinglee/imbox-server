package org.imbox.server.pagehandler.sharelink;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.imbox.server.functions.Httpresponser;
import org.imbox.server.functions.Uriparser;
import org.imbox.server.functions.blocktofile;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class Generatefilehandler implements HttpHandler
{
	private HttpExchange httpconnection;
	//private String connectionIP;
	@Override
	public void handle(HttpExchange httpconnection) throws IOException 
	{
		this.httpconnection = httpconnection;
		//connectionIP = httpconnection.getRemoteAddress().getAddress().toString();
		Handlerthread multithread = new Handlerthread();
		multithread.setName("clientconnection");
		multithread.start();
	}
	
	
	private class Handlerthread extends Thread
	{

		@Override
		public void run() {
			try
			{
				if (httpconnection.getRequestMethod().equals("GET"))
				{
					System.out.println("this is a http get method @ generatefile");
					URI responsemap;
					responsemap = httpconnection.getRequestURI();
					System.out.println(responsemap.toString());
					Uriparser up = new Uriparser(responsemap.toString());
					String md5 = up.getmd5();
					String filename = up.getfilename();
					System.out.println(md5 +"\t" + filename);
					blocktofile b2f = new blocktofile(md5);
					b2f.reconstruct();
					byte[] file = b2f.getfileinbyte();
					httpconnection.sendResponseHeaders(200, file.length);       
				    System.out.println("file length : "+ file.length + " bytes.");
				    OutputStream os = httpconnection.getResponseBody();
				    os.write(file);
				    os.flush();
				    os.close();
				}else
				{
					if (httpconnection.getRequestMethod().equals("POST"))
					{
						System.out.println("this is a post method @ generatefile, Get mothod should be used");
						String response = "this is a post method @ generatefile, Get mothod should be used";
						Httpresponser res = new Httpresponser(httpconnection, response);
						res.execute();
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
				e.printStackTrace();
			}
			
		}
		
	}
}