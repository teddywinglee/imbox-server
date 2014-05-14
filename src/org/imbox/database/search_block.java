package org.imbox.database;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
//import java.util.Scanner;
/*author: cindyboy*/
public class search_block extends db_connect{
    private String FID;
	public ArrayList<String> myList = new ArrayList<String>();
    //input: fid,  output block myList
    public search_block(String FID){
    	this.FID = FID;
    }
    public void setList(ArrayList<String> List){    //set block list
    	this.myList = List;
    }
    public ArrayList<String> getList(){             //get block list
    	return myList;
    }
    public void searchBlock(){
    	try{  
    		String searchFID = "SELECT blockName FROM block_map WHERE FID = '"+FID+"' ORDER BY sequence";
    		Statement stmt = connect.createStatement();
    		ResultSet FIDresult = stmt.executeQuery(searchFID);
            //if file has block, output one by one
			while(FIDresult.next()){
				String result_b = FIDresult.getString("blockName");
				myList.add(result_b);
			}
			setList(myList);
        }catch(Exception e){
        	e.printStackTrace(); 
	    }
    }
	public static void main(String[] args) {
		/*System.out.println("input: fileID");
		Scanner input = new Scanner(System.in);
		String FID = input.next();
		search_block sb = new search_block(FID);
		sb.searchBlock();
		System.out.println(sb.getList());
        input.close();*/
	}

}
