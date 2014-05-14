package org.imbox.database;
import java.sql.*;
//import java.util.Scanner;
/*author: cindyboy*/
public class Insert_File extends db_connect{
    private String acc,FileName,FID,old_MD5,new_MD5;
    private boolean userFileInsert,userBlockInsert;
	//input: account, fileName, fileID, old_MD5, new_MD5
	public Insert_File(String acc,String FileName,String FID,String old_MD5,String new_MD5){
		this.acc = acc;   this.FileName = FileName;
		this.FID = FID;   this.old_MD5 = old_MD5;   this.new_MD5=new_MD5;
	}
	public void setBlockInsert(boolean userBlockInsert){    //set if user has file for delete
    	this.userBlockInsert = userBlockInsert;
    }
    public boolean getBlockInsert(){                  //get if user has file for delete
    	return userBlockInsert;
    }
	public void setFileInsert(boolean userFileInsert){    //set if user has file for delete
    	this.userFileInsert = userFileInsert;
    }
    public boolean getFileInsert(){                  //get if user has file for delete
    	return userFileInsert;
    }
    
	public void InsertFile(){
		try{          
			//search FID for same FID of server's file
			String searchFID = "SELECT fid FROM server_file WHERE fid = '"+FID+"'";
			Statement stmt = connect.createStatement();
			ResultSet FIDresult = stmt.executeQuery(searchFID);
			if(FIDresult.next()){    
				//if same, add server_file counter of that file
				setBlockInsert(false);
				String counter_plus = "UPDATE server_file SET counter=counter+1 WHERE fid = '"+FID+"'";
				stmt.executeUpdate(counter_plus);
			}
			else{    //new file
				//insert into server_file table，counter=1
				setBlockInsert(true);
				String insert = "INSERT INTO server_file VALUES ('"+ FID +"', 1,'"+ new_MD5 +"')";
				stmt.executeUpdate(insert);
				//insert block?
			}
			//insert into user's table, if same fileName return false
			String searchFN = "SELECT fileName FROM "+acc+" WHERE fileName = '"+FileName+"'";
			ResultSet FNresult = stmt.executeQuery(searchFN);
			if(FNresult.next()){
				setFileInsert(false);
			}
			else{
				setFileInsert(true);
				String owner = "INSERT INTO "+acc+" VALUES ('"+FileName+"','"+FID+"','"+new_MD5 +"','"+old_MD5+"')";
				stmt.executeUpdate(owner);
			}//close database
		}catch(Exception e){
			System.out.println(e.toString()); 
		}
	}
	public static void main(String[] args) {
		/*System.out.println("input: account, fileName, fileID, old_MD5, new_MD5");
		Scanner input = new Scanner(System.in);
		String acc = input.next();  	String FileName = input.next();
		String FID = input.next();		String old_MD5 = input.next();
		String new_MD5 = input.next();
		Insert_File insert = new Insert_File(acc, FileName, FID, old_MD5, new_MD5);
		insert.InsertFile();
		System.out.println(insert.userBlockInsert);
		input.close();*/
	}

}
