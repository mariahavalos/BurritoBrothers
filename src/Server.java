import java.util.concurrent.TimeUnit;

public class Server implements Runnable{

	 private int serverNumber;
	 private Customer customerAtCounter;
	   
	   public Server(int serverNumber)
	   {
	       this.serverNumber = serverNumber;
	   }  
	  
	
	   public void freeServer()
	   {
	       try
	       {
	           BurritoBrothersStore.getStore().counter.acquire();
	           customerAtCounter = BurritoBrothersStore.getStore().currentCustomerAtCounter(serverNumber);
	           BurritoBrothersStore.getStore().counter.release();
	          
	           if (customerAtCounter.getOrderSize() > 3)
	           {
	               customerAtCounter.partialFill();  
	               BurritoBrothersStore.getStore().currentCustomerInLine += 1;
	               BurritoBrothersStore.getStore().cookBurritos(3, customerAtCounter.getCustomerNumber(), serverNumber); 
	               System.out.println("Customer Number " + customerAtCounter.getCustomerNumber() + " has " + customerAtCounter.getOrderSize() + " burritos left in their order.");
	               
	               BurritoBrothersStore.getStore().lineActions(customerAtCounter, true);  
	               BurritoBrothersStore.getStore().serving.release();
	           }  
	           
	           else
	           {
	               BurritoBrothersStore.getStore().cookBurritos(customerAtCounter.getOrderSize(), customerAtCounter.getCustomerNumber(), serverNumber);
	               BurritoBrothersStore.pay(customerAtCounter);      
	               if (!BurritoBrothersStore.getStore().Register.isEmpty() && BurritoBrothersStore.getStore().register.tryAcquire())
	               {
	            	   System.out.println("\nServer Number " + serverNumber + " checking out Customer Number " + customerAtCounter.getCustomerNumber());
	                   BurritoBrothersStore.getStore().checkout();
	                   System.out.println("\nServer Number " + serverNumber + " is ready for more work!");
	               }      
	           }
	       }
	       catch (InterruptedException e) {
	    	   e.printStackTrace();
	    }
	  
	   }
	  
	   public void run()
	   {
	       boolean working=true; 
	       while (working)
	       {
	           try
	           {
	               // try to serve customer if no customer in line wait
	               if (BurritoBrothersStore.getStore().serving.tryAcquire(500, TimeUnit.MILLISECONDS))
	               {
	                   freeServer();
	               }
	               else
	               {
	            	   working = false;
	               }
	           }
	           catch (InterruptedException e) {
	        	   e.printStackTrace();   
	           }
	                      
	       }      
	      
	   }

}
