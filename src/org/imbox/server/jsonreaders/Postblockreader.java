package org.imbox.server.jsonreaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class Postblockreader
{
	private String token;
	private String MAC;
	private String blockdata;
	private String filename;
	private int sequence;
	private String jsonstring;
	private HttpExchange request;
	public Postblockreader(HttpExchange httpconnection)
	{
		request = httpconnection;
		token = new String();
		MAC= new String();
		blockdata = new String();
		filename = new String();
		sequence = 0;
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
			throw new IOException("Postblockreader.getjson");
		}
	}
	
	private void parsejson() throws IMBOXNW_jsonException
	{
		try {
			JSONObject obj = new JSONObject(jsonstring);
			token = obj.getString("token");
			MAC = obj.getString("MAC");
			blockdata = obj.getString("blockdata");
			sequence = obj.getInt("seq");
			filename = obj.getString("filename");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Postblockreader.parsejson");
		}
		
	}
	
	public String gettoken()
	{
		return token;
	}
	
	public String getdatastring()
	{
		return blockdata;
	}
	
	public int getsequence()
	{
		return sequence;
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