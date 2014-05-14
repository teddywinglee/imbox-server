package org.imbox.server.jsonreaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class Loginrequestreader
{
	private HttpExchange request;
	private String account;
	private String password;
	private String jsonstring;
	private String MAC;
	public Loginrequestreader(HttpExchange httpconnection)
	{
		request = httpconnection;
		account = new String();
		password = new String();
		MAC = new String();
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
			throw new IOException("Loginrequestreader.getjson");
		}
	}
	
	private void parsejson() throws IMBOXNW_jsonException
	{
		 try {
			JSONObject obj = new JSONObject(jsonstring);
			account = obj.getString("account");
			password = obj.getString("password");
			MAC = obj.getString("MAC");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Loginrequestreader.parsejson");
			
			
		}
		 
	}
	
	public String getaccount()
	{	
		return account;
	}
	
	public String getpassword()
	{
		return password;
	}
	
	public String getMAC()
	{
		return MAC;
	}

}