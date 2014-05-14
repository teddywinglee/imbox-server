package org.imbox.database;

import java.sql.ResultSet;
import java.sql.Statement;

public class Filefidgetter extends db_connect
{
	private String account;
	private String filename;
	private String fid;
	public Filefidgetter(String account,String filename)
	{
		this.account = account;
		this.filename = filename;
	}
	public void preparefileid()
	{
		try
		{
			Statement stmt = connect.createStatement();
			String query = "SELECT fid FROM " + account + " WHERE fileName = " + "'" + filename + "'";
			ResultSet result = stmt.executeQuery(query);
			if (result.next())
			{
				for (result.first();!result.isAfterLast();result.next())
				{
					fid = result.getString("fid");
				}
			}else
			{
				fid = "";
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public String getfileid()
	{
		return fid;
	}
}