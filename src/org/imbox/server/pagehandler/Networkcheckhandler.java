package org.imbox.server.pagehandler;
import java.io.IOException;

import org.imbox.server.functions.Httpresponser;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class Networkcheckhandler implements HttpHandler
{
	private HttpExchange httpconnection;
	@Override
	public void handle(HttpExchange httpconnection) throws IOException 
	{
		this.httpconnection = httpconnection;
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
				System.out.println("client performing network checking");
				String response = "network is fine.";
				Httpresponser res = new Httpresponser(httpconnection, response);
				res.execute();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
}