package org.imbox.server.functions.LOCK;
/*author: zhong-ting chen*/
public class Returntype {

	boolean islocked;
    String mac;
    
    public Returntype(boolean islocked,String mac)
    {
    	this.islocked=islocked;
    	this.mac = mac;
    }
    
    public boolean islock(){
    	return islocked;
    }
    public String mac(){
    	return mac;
    }    
}
