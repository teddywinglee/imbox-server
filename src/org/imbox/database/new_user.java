package org.imbox.database;
import java.sql.*;
//import java.util.Scanner;
/*author: cindyboy*/
public class new_user extends db_connect{
	private String userName;
    public new_user(String userName){
    	//give userName,create table�Aprimary key:fileName
    	this.userName = userName; 
    }
    public void insertUserTable(){
    	try{
     		Statement stmt = connect.createStatement();
			stmt.executeUpdate("CREATE TABLE "+userName+" (" + "fileName VARCHAR(40) "+", fid VARCHAR(40)"
     		        +", f_MD5 VARCHAR(40)"+", antedent_f_MD5 VARCHAR(40)"+", PRIMARY KEY(fileName)"
					+", INDEX (fid))ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_unicode_ci;");
			//user fid reference to server_file fid
			stmt.executeUpdate("ALTER TABLE "+userName+" ADD FOREIGN KEY (fid) REFERENCES "
    				+" " + dbname + ".server_file (fid) ON DELETE CASCADE ON UPDATE CASCADE ;");
    	}catch(Exception e){
			e.printStackTrace();
		}
    }
//	public static void main(String[] args) {
//		/*System.out.println("input�Gaccount");
//		Scanner input = new Scanner(System.in);
//		String userName = input.next();
//		new_user insert = new new_user(userName);
//		insert.insertUserTable();
//		input.close();*/
//	}

}
