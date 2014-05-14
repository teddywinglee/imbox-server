package org.imbox.server.jsonreaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class Newfilereader
{
	private String token;
	private String MAC;
	private String filename;
	private String md5;
	private HttpExchange request;
	private String jsonstring;
	
	public Newfilereader(HttpExchange httpconnection)
	{
		request = httpconnection;
		token = new String();
		MAC = new String();
		filename = new String();
		md5 = new String();
		jsonstring = new String();
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
			throw new IOException("newfilereader.readjson");
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
			md5 = obj.getString("md5"); 
			
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("newfilereader.parsejson");
		}
	}
	
	public String getfilename()
	{
		return filename;
	}
	public String getmd5()
	{
		return md5;
	}
	public String gettoken()
	{
		return token;
	}
	public String getmac()
	{
		return MAC;
	}
}