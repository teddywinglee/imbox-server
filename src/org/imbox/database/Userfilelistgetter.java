package org.imbox.database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Userfilelistgetter extends db_connect
{
	private String username;
	private List<String> datalist;
	public Userfilelistgetter(String username)
	{
		this.username = username;
		datalist = new ArrayList<>();
	}
	
	public void preparefilelist()
	{
		try
		{
			Statement stmt = connect.createStatement();
			String getfilelist = "SELECT * FROM "+ username;
			ResultSet result = stmt.executeQuery(getfilelist);
			if (result.next())
			{
				for (result.first();!result.isAfterLast();result.next())
				{
					datalist.add(result.getString("fileName"));
					datalist.add(result.getString("f_MD5"));
					datalist.add(result.getString("antedent_f_MD5"));
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public List<String> getlist()
	{
		return datalist;
	}
}