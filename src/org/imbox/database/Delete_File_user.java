package org.imbox.database;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
/*author: cindyboy*/
public class Delete_File_user extends db_connect{
	private String acc,MD5;
	private boolean userFileDelete,DeleteFromHD;
	//input:account, file_MD5
    public Delete_File_user(String acc,String MD5){
        this.MD5 = MD5; this.acc = acc;
    }
    public void setFileDelete(boolean userFileDelete){    //set if user has file for delete
    	this.userFileDelete = userFileDelete;
    }
    public boolean getFileDelete(){                  //get if user has file for delete
    	return userFileDelete;
    }
    public void setDeleteFromHD(boolean DeleteFromHD){    //set if user has file for delete
    	this.DeleteFromHD = DeleteFromHD;
    }
    public boolean getDeleteFromHD(){                  
    	return DeleteFromHD;
    }
    public void DeleteFileUser(){
    	try{  
    		String searchMD5 = "SELECT counter FROM server_file WHERE f_MD5 = '"+MD5+"'";
    		Statement stmt = connect.createStatement();
			ResultSet MD5result = stmt.executeQuery(searchMD5);
			if(MD5result.next()){
				setFileDelete(true);
	    		//String delete = "DELETE FROM "+ acc +" WHERE f_MD5 = '"+MD5+"'";
				String delete = "UPDATE " + acc + " SET f_MD5='" + "ffffffffffffffffffffffffffffffff'" + " WHERE f_MD5='" + MD5 + "'";
	    		stmt.executeUpdate(delete);
				Delete_File delete1 = new Delete_File(MD5);
				delete1.DeleteFile();
				setDeleteFromHD(delete1.getDeleteFromHD());
			}
			else
				setFileDelete(false);
    	}catch(Exception e){
			System.out.println(e.toString()); 
		}
    }
	public static void main(String[] args) {
		System.out.println("input: account, file_MD5");
		Scanner input = new Scanner(System.in);
		String acc = input.next();      String MD5 = input.next();
		Delete_File_user delete = new Delete_File_user(acc,MD5);
		delete.DeleteFileUser();
		System.out.println(delete.getDeleteFromHD());
		input.close();
	}

}
