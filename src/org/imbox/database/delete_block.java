package org.imbox.database;
import java.sql.Statement;
import java.sql.ResultSet;
//import java.util.Scanner;
/*author: cindyboy*/
public class delete_block extends db_connect{
	private String blockName,fid;
	private boolean ifDelete;
	//input: blockName, fid,   delete form block_map
    public delete_block(String blockName,String fid){
    	this.blockName=blockName; this.fid=fid;
    }
    public void setIfDelete(boolean delete){    //set if successfully delete
    	this.ifDelete = delete;
    }
    public boolean getIfDelete(){               //get if successfully delete
    	return ifDelete;
    }
    public void DeleteBlock(){
    	try{
	    	String search_bN = "SELECT blockName FROM block_map WHERE blockName = '"+blockName+"' AND fid = '"+fid+"'";
			Statement stmt = connect.createStatement();
			ResultSet bNresult = stmt.executeQuery(search_bN);
			if(bNresult.next()){  //if db has block, delete it
				String delete = "DELETE FROM block_map WHERE blockName = '"+blockName+"' AND fid = '"+fid+"'";
				stmt.executeUpdate(delete);
				setIfDelete(true);
			}
			else{
				setIfDelete(false);
			}
    	}catch(Exception e){
			System.out.println("�ҥ~:"+e.toString()); 
		}
    }
	public static void main(String[] args) {
		/*System.out.println("input: blockName,fileID");
		Scanner input = new Scanner(System.in);
		String blockName = input.next();
		String fid = input.next();
		delete_block delete = new delete_block(blockName,fid);
		delete.DeleteBlock();
		System.out.println(delete.getIfDelete());
		input.close();*/
	}
}
