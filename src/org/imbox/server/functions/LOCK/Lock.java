package org.imbox.server.functions.LOCK;
import java.util.Date;
/*author: zhong-ting chen*/
public class Lock extends Thread{
	
	boolean lock;
	String mac;
	boolean timeout;
	Date t1;
	Date t2;
	long diff;
	

	
    public Lock(){
    	this.lock = false;
    	this.mac = "";
    	this.timeout = false;
    	this.diff = 0;
    }
	
    public Returntype lock(String MAC)
    {
    	if(!lock)
	      {
	    	lock = true;
	    	mac = MAC;
	    	t1 = new Date();
	      }
     Returntype k = new Returntype(lock,MAC);
     return k;
    }
    
    public void release(String MAC)
    { if (mac == MAC)
      {lock = false;
       mac = "";
       timeout = false;
       diff=0; 
      }
    }
    
    public void test(){
    timeout = true;	
    }
    
    public void resettimer()
    {
    	t1 = new Date();
    }
    
    public void run()
    {
      while(true)
      {  t2 = new Date();
         if(lock)
          if(t1 != null)	 
           diff = t2.getTime() - t1.getTime();

         if ((diff/1000) >= 30)// 3 sec
    	   timeout=true;
         
         if(timeout)
	     {lock = false;
	      mac = "";
	      timeout = false;
	      diff=0;
	      System.out.println("time out release"); 
         }
      }
    }

}
