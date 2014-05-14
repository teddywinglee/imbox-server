package org.imbox.server.jsonreaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class TokenMACreader 
{
	private String token;
	private String MAC;
	private HttpExchange request;
	private String jsonstring;
	public TokenMACreader(HttpExchange httpconnection)
	{
		request = httpconnection;
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
			throw new IOException("tokenmacreader.getjson");
		}
	}
	
	private void parsejson() throws IMBOXNW_jsonException
	{
		try {
			JSONObject obj = new JSONObject(jsonstring);
			token = obj.getString("token");
			MAC = obj.getString("MAC");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IMBOXNW_jsonException("tokenmacreader.parsejson");
		}
	}
	
	public String gettoken()
	{
		return token;
	}
	
	public String getMAC()
	{
		return MAC;
	}
}