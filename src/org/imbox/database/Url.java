package org.imbox.database;
import java.sql.ResultSet;
import java.sql.Statement;

/*author: zhong-ting chen*/

public class Url extends db_connect{
	static String urlname ;
	static String domain = "54.254.1.241:8080";
	public Url(String account,String filename){
		try{
//		InetAddress IP=InetAddress.getLocalHost();
//		domain = IP.getHostAddress();
		Statement stmt = connect.createStatement();	
		String md5 = "SELECT f_MD5 FROM " +account+ " WHERE fileName = '"+filename+"'";
		ResultSet md5_result = stmt.executeQuery(md5);
		md5_result.next();
		String md5name = md5_result.getString(1);
		urlname = "http://"+domain+"/generatefile" + "."+md5name+ "."+filename; 
		System.out.println(urlname);
		}catch(Exception e){
		System.out.println("exception:"+e.toString()); 
	    }
	}
	
	public String geturlname()
	{
		return urlname;
	}

	
//public static void main(String[] args) {
//	Scanner input = new Scanner(System.in);
//	String account = input.next();
//	String filename = input.next();
//	//String account = args[0];
//    //String filename = args[1];	
//	new Url(account,filename);
//	}
     
	}
