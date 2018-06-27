
public class Simulation {

	public static void main(String[] args) {	
		 for (int i=1; i < 4; i++){
			 Thread Server = new Thread(new Server(i));           
			 Server.start();   
	      }
	  
	      for (int i=0; i < 5; ++i)
	      {
	      	Thread Customers = new Thread(BurritoBrothersStore.getStore());
	       	Customers.start();  
	          
	          
	      try {
	    	  Thread.sleep((50));
	      }
	      catch (InterruptedException e) {
	    	  e.printStackTrace();
	      }
	  }  
	}

}
