package org.imbox.database;

import java.sql.Statement;
/*author: cindyboy*/
public class create_account extends db_connect
{
	private String acc,pwd;
	private String device,IP;
	private boolean getIfPwdCorrect; 
	public create_account(String acc,String pwd,String device,String IP){ //set variable
    	this.acc=acc;         this.pwd=pwd;  
    	this.device=device;   this.IP=IP;
    }
    public void setPwdCorrect(boolean ifCorrect){    //set if password correct
    	this.getIfPwdCorrect = ifCorrect;
    }
    public boolean getPwdCorrect(){                  //get if password correct
    	return getIfPwdCorrect;
    }
    
    public void ac_create()
    {
    	try
    	{
	    	Statement stmt = connect.createStatement();
	    	//new account, established "user" table
			new_user insert_t = new new_user(acc);
			insert_t.insertUserTable();
	 		//insert data to "account" table
	 		String insert = "INSERT INTO account VALUES ('"+acc+"', '"+pwd+"')";
			stmt.executeUpdate(insert);
			//insert data to "device" table
			String insert_d = "INSERT INTO device VALUES (NULL, '"+acc+"', '"+device+"', '"+IP+"')";
			stmt.executeUpdate(insert_d);
			setPwdCorrect(true);
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    		setPwdCorrect(false);
    	}
    }
}