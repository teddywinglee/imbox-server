package org.imbox.server.jsonreaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;


public class Modifyfilereader
{
	private HttpExchange request;
	private String jsonstring;
	private String token;
	private String MAC;
	private String filename;
	private String newmd5;
	
	public Modifyfilereader(HttpExchange httpconnection)
	{
		request = httpconnection;
		jsonstring = new String();
		MAC = new String();
		newmd5 = new String();
		filename = new String();
		token = new String();
	}
	
	public void readjson() throws IOException, IMBOXNW_jsonException
	{
		try {
			InputStreamReader requestreader =  new InputStreamReader(request.getRequestBody(),"utf-8");
			BufferedReader br = new BufferedReader(requestreader);
			jsonstring = br.readLine();
			br.close();
			requestreader.close();
			parsejson();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Modifyfilereader.readjson");
		}
	}
	
	private void parsejson() throws IMBOXNW_jsonException
	{
		try
		{
			JSONObject obj = new JSONObject(jsonstring);
			token = obj.getString("token");
			MAC = obj.getString("MAC");
			filename = obj.getString("filename");
			newmd5 = obj.getString("newmd5"); 
			
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Modifyfilereader.parsejson");
		}
	}
	
	public String gettoken()
	{
		return token;
	}
	
	public String getmac()
	{
		return MAC;
	}
	
	public String getfilename()
	{
		return filename;
	}
	
	public String getnewmd5()
	{
		return newmd5;
	}
}