package org.imbox.database;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.util.Scanner;
/*author: cindyboy*/
public class new_login extends db_connect{
	private String acc,pwd;
	private String device,IP;
	private boolean getIfPwdCorrect;            //variable for password correct
    public new_login(String acc,String pwd,String device,String IP){ //set variable
    	this.acc=acc;         this.pwd=pwd;  
    	this.device=device;   this.IP=IP;
    }
    public void setPwdCorrect(boolean ifCorrect){    //set if password correct
    	this.getIfPwdCorrect = ifCorrect;
    }
    public boolean getPwdCorrect(){                  //get if password correct
    	return getIfPwdCorrect;
    }
    public void acc_insert(){
    	//input�Gaccount, password, device, ip, insert to account and device table
    	try{
    		Statement stmt = connect.createStatement();
    		String oldacc = "SELECT acc,pwd FROM account WHERE acc = '"+acc+"'";
    		ResultSet acc_result = stmt.executeQuery(oldacc);
     		if(acc_result.next()){ //old account
     			//search if password correct
     			String db_pwd = acc_result.getString("pwd");
     			if(db_pwd.equals(pwd)){
	     			//search if same device and ip
     				setPwdCorrect(true);
	     			String olddevice_IP = "SELECT device FROM device WHERE device = '"+device+"' AND last_IP = '"+IP+"'";
	        		ResultSet result = stmt.executeQuery(olddevice_IP);
	        		if(!result.next()){
	        			String insert_d = "INSERT INTO device VALUES (NULL, '"+acc+"', '"+device+"', '"+IP+"')";
	            		stmt.executeUpdate(insert_d);
	        		}
     			}
     			else{
     				setPwdCorrect(false);
     			}
     		}
     		else{
     			//new account move to create_account
	    		setPwdCorrect(false);
     		}
    	}catch(Exception e){
			e.printStackTrace(); 
		}
    }
	public static void main(String[] args) {
		/*System.out.println("input: acc, pwd, device, ip");
		Scanner input = new Scanner(System.in);
		String acc = input.next();     	String pwd = input.next();
		String device = input.next();	String IP = input.next();
		new_login login = new new_login(acc,pwd,device,IP);
		login.acc_insert();
        System.out.println(login.getPwdCorrect());
        input.close();*/
	}
}
