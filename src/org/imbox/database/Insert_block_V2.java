package org.imbox.database;
import java.sql.Statement;
import java.sql.ResultSet;
//import java.util.Scanner;
/*author: cindyboy*/
public class Insert_block_V2 extends db_connect{
	String blockName,FID;  int seq;
    public Insert_block_V2(String blockName,int seq,String FID){
    	//input: blockName,blockSeq,fid) 
    	this.blockName=blockName; this.seq=seq;  this.FID=FID;
    }
    public void InsertBlock(){
    	try{
    		//insert blockName,sequence,fid into database(if old FID => replace old block
    		String count_seq = "SELECT count(*) FROM block_map WHERE fid = '"+FID+"'"; //count blocks
			Statement stmt = connect.createStatement();
			ResultSet count_result = stmt.executeQuery(count_seq);
			//old file's block insert/update
			count_result.next();
			int count = count_result.getInt(1);
			if(count>0){
				//file's old block update
				if(seq <= count-1){
					String update = "UPDATE block_map SET blockName='"+blockName+"' WHERE FID = '"+FID+"' AND sequence ="+seq;
					stmt.executeUpdate(update);
				}
				else{  //file's old block insert
					String insert = "INSERT INTO block_map VALUES (NULL,'"+blockName+"', "+seq+",'"+FID+"')";
	        		stmt.executeUpdate(insert);
				}
			}
			//new file's block insert
			else{
				String insert = "INSERT INTO block_map VALUES (NULL,'"+blockName+"', "+seq+",'"+FID+"')";
        		stmt.executeUpdate(insert);
			}
    		//connect.close();  //close database
       	}catch(Exception e){
    		System.out.println(e.toString());
    	}
    }
	public static void main(String[] args) {
		/*System.out.println("input: blockName, sequence, fid");
		Scanner input = new Scanner(System.in);
		String blockName = input.next();
		int seq = input.nextInt();
		String fid = input.next();
        Insert_block_V2 insert = new Insert_block_V2(blockName,seq,fid);
		insert.InsertBlock();
        input.close();*/
	}

}

