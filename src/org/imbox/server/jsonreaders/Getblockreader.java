package org.imbox.server.jsonreaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.imbox.server.exception.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class Getblockreader
{
	private String token;
	private String blockname;
	private String MAC;
	private int sequence;
	private HttpExchange request;
	private String jsonstring;
	
	public Getblockreader(HttpExchange httpconnection)
	{
		request = httpconnection;
		token = new String();
		blockname = new String();
		MAC = new String();
		sequence =0;
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
			throw new IOException("getblockreader.getjson");
		}
	}
	
	private void parsejson() throws IMBOXNW_jsonException
	{
		try {
			JSONObject obj = new JSONObject(jsonstring);
			token = obj.getString("token");
			MAC = obj.getString("MAC");
			blockname = obj.getString("blockname");
			sequence = obj.getInt("seq");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IMBOXNW_jsonException("getblockreader.parsejson");
		}
	}
	
	public String gettoken()
	{
		return token;
	}
	
	public String getblockname()
	{
		return blockname;
	}
	
	public int getsequence()
	{
		return sequence;
	}
	
	public String getmac()
	{
		return MAC;
	}
}