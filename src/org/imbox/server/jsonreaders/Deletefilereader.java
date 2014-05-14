package org.imbox.server.jsonreaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.imbox.server.exception.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class Deletefilereader
{
	private HttpExchange request;
	private String jsonstring;
	private String token;
	private String MAC;
	private String filename;
	public Deletefilereader(HttpExchange httpconnection)
	{
		this.request = httpconnection;
		jsonstring = new String();
		token = new String();
		MAC = new String();
		filename = new String();
	}
	
	public void readjson() throws IOException, IMBOXNW_jsonException
	{
		try
		{
			InputStreamReader requestreader =  new InputStreamReader(request.getRequestBody(),"utf-8");
			BufferedReader br = new BufferedReader(requestreader);
			jsonstring = br.readLine();
			br.close();
			requestreader.close();
			parsejson();
		}catch(IOException e)
		{
			e.printStackTrace();
			throw new IOException("deletefilereader.readjson");
		}
	}
	
	private void parsejson() throws IMBOXNW_jsonException
	{
		try
		{
			JSONObject obj= new JSONObject(jsonstring);
			MAC = obj.getString("MAC");
			token = obj.getString("token");
			filename = obj.getString("filename");
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("deletefilereader.parsejson");
		}
	}
	
	public String getmac()
	{
		return MAC;
	}
	public String gettoken()
	{
		return token;
	}
	public String getfilename()
	{
		return filename;
	}
}