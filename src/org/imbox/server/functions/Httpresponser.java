package org.imbox.server.functions;

import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;


public class Httpresponser
{
	private HttpExchange httpconnection;
	private String response;
	
	public Httpresponser(HttpExchange http, String resString)
	{
		httpconnection = http;
		response = resString;
	}
	
	public void execute()
	{
		try
		{
			httpconnection.sendResponseHeaders(200, response.length());
			OutputStream os = httpconnection.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}