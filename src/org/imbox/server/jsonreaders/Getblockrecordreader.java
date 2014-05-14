package org.imbox.server.jsonreaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.imbox.server.exception.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;


public class Getblockrecordreader
{
	private String token;
	private String MAC;
	private String filename;
	private HttpExchange request;
	private String jsonstring;
	
	public Getblockrecordreader(HttpExchange httpconnection)
	{
		request = httpconnection;
		token = new String();
		MAC = new String();
		filename = new String();
		jsonstring = new String();
		
	}
	
	public void getjson() throws IOException, IMBOXNW_jsonException
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
			throw new IOException("getblockrecordreader.getjson");
		}
	}
	
	private void parsejson() throws IMBOXNW_jsonException
	{
		try {
			JSONObject obj = new JSONObject(jsonstring);
			token = obj.getString("token");
			MAC = obj.getString("MAC");
			filename = obj.getString("filename");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IMBOXNW_jsonException("getblockrecordreader.parsejson");
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
}