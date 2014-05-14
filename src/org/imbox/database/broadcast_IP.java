package org.imbox.database;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Statement;

/*author: cindyboy*/

public class broadcast_IP extends db_connect{
	private String acc;
	private ArrayList<String> myList = new ArrayList<String>();
    public broadcast_IP(String acc){
    	//input: account   
    	this.acc=acc;
    }
    public void setList(ArrayList<String> List){    //set broadcast list
    	this.myList = List;
    }
    public ArrayList<String> getList(){             //get broadcast list
    	return myList;
    }
    public void Broadcast(){
    	try{
	    	Statement stmt = connect.createStatement();
	    	myList.add("server");
			String bc_size = "SELECT count(*) FROM device WHERE account = '"+acc+"'";
			ResultSet bcsize_result = stmt.executeQuery(bc_size);
			bcsize_result.next();
			String count = Integer.toString(bcsize_result.getInt(1));
			myList.add(count);
			String bc_IP = "SELECT DISTINCT last_IP FROM device WHERE account = '"+acc+"'";
			ResultSet bcIP_result = stmt.executeQuery(bc_IP);
			while(bcIP_result.next()){
				String IP = bcIP_result.getString("last_IP");
				myList.add(IP);
			}
    	}catch(Exception e){
			System.out.println(e.toString()); 
		}
    }
	public static void main(String[] args) {
		/*System.out.println("input: account");
		Scanner input = new Scanner(System.in);
		String acc = input.next();
		broadcast_IP bc = new broadcast_IP(acc);
		bc.Broadcast();
		System.out.println(bc.getList());
        input.close();*/
    }
}
