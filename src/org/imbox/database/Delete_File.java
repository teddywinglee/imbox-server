package org.imbox.database;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.util.Scanner;
/*author: cindyboy*/
public class Delete_File extends db_connect{
	private String MD5;
	private boolean hasFileDelete,DeleteFromHD;
	//input: file_MD5
    public Delete_File(String MD5){
        this.MD5 = MD5;
    }
    public void setDeleteFromHD(boolean DeleteFromHD){    //set if server has file for delete
    	this.DeleteFromHD = DeleteFromHD;
    }
    public boolean getDeleteFromHD(){                  //get if server has file for delete
    	return DeleteFromHD;
    }
    public void setFileDelete(boolean hasFileDelete){    //set if server has file for delete
    	this.hasFileDelete = hasFileDelete;
    }
    public boolean getFileDelete(){                  //get if server has file for delete
    	return hasFileDelete;
    }
    public void DeleteFile(){
    	try{  
    		String searchFID = "SELECT counter FROM server_file WHERE f_MD5 = '"+MD5+"'";
    		Statement stmt = connect.createStatement();
			ResultSet FIDresult = stmt.executeQuery(searchFID);
			if(FIDresult.next()){                               //server has file
				setFileDelete(true);
				int counter = FIDresult.getInt("counter");
				//counter=1  =>  delete server file
				if(counter<=1){
					setDeleteFromHD(true);
					String delete = "DELETE FROM server_file WHERE f_MD5 = '"+MD5+"'";
					stmt.executeUpdate(delete);   //user and block auto delete
				}
				else{  //>1  =>  counter-1
					setDeleteFromHD(false);
					String counter_minus = "UPDATE server_file SET counter=counter-1 WHERE f_MD5 = '"+MD5+"'";
					stmt.executeUpdate(counter_minus);
				}
			}
			else{
				setFileDelete(false);
			}
			//connect.close();  //close database
    	}catch(Exception e){
			System.out.println(e.toString()); 
		}
    }
	public static void main(String[] args) {
		/*System.out.println("input: file_MD5");
		Scanner input = new Scanner(System.in);
		String MD5 = input.next();
		Delete_File delete = new Delete_File(MD5);
		delete.DeleteFile();
		System.out.println(delete.getDeleteFromHD());
		input.close();*/
	}

}
