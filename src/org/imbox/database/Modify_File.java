package org.imbox.database;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.util.Scanner;
/*author: cindyboy*/
public class Modify_File extends db_connect{
	private String acc,oldName,new_FID,newName,new_MD5;
	private boolean userFileModify,DeleteFromHD;
	//input: account, old_fileName, new_FID, new_fileName,new_fileMD5
    public Modify_File(String acc,String oldName,String new_FID,String newName,String new_MD5){
    	this.acc = acc;            this.oldName = oldName;    
    	this.new_FID = new_FID;    this.newName=newName;     this.new_MD5 = new_MD5;
    }
    public void setDeleteFromHD(boolean DeleteFromHD){    //set if user has file for update
    	this.DeleteFromHD = DeleteFromHD;
    }
    public boolean getDeleteFromHD(){                  //get if user has file for update
    	return DeleteFromHD;
    }
    public void setFileModify(boolean userFileModify){    //set if user has file for update
    	this.userFileModify = userFileModify;
    }
    public boolean getFileModify(){                  //get if user has file for update
    	return userFileModify;
    }
    public void ModifyFile(){
    	try{  
    		//get oldFileMD5
    		String searchMD5 = "SELECT f_MD5 FROM "+acc+" WHERE fileName = '"+oldName+"'";
    		Statement stmt = connect.createStatement();
			ResultSet searchMD5_result = stmt.executeQuery(searchMD5);
			if(searchMD5_result.next()){ 
				setFileModify(true);
				String old_MD5 = searchMD5_result.getString("f_MD5");
	    	    //delete old file
	    		Delete_File_user delete = new Delete_File_user(acc,old_MD5);
	    		delete.DeleteFileUser();
	    		setDeleteFromHD(delete.getDeleteFromHD());
				//insert new file
	    		Insert_File insert = new Insert_File(acc,newName,new_FID,old_MD5,new_MD5);
	    		insert.InsertFile();
	    		//put old MD5 into antedent_MD5(wrong)
	    		//String newMD5 = "UPDATE "+acc+" SET antedent_f_MD5='"+old_MD5+"' WHERE f_MD5 = '"+new_MD5+"'";
	    		//stmt.executeUpdate(newMD5);
			}
			else{
				setFileModify(false);
			}
			//connect.close();  //close database
    	}catch(Exception e){
			System.out.println(e.toString()); 
		}
    }
	public static void main(String[] args) {
		/*System.out.println("input: account, old_fileName, new_fid, new_Name, new_fileMD5");
		Scanner input = new Scanner(System.in);
		String acc = input.next();          String oldName = input.next(); 
        String new_FID = input.next();		
		String newName = input.next();  	String new_MD5 = input.next();
		Modify_File modify = new Modify_File(acc,oldName,new_FID,newName,new_MD5);
		modify.ModifyFile();
		System.out.println(modify.getFileModify());
    	input.close();*/
	}

}
