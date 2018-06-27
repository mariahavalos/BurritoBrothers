import java.util.concurrent.TimeUnit;

public class Server implements Runnable{

	 private int serverNumber;
	 private Customer customerAtCounter;
	   
	 /*
	  * Constructor for the server class, assigns each server a number 1-3 based off the simulation class.
	  */
	   public Server(int serverNumber)
	   {
	       this.serverNumber = serverNumber;
	   }  
	  
	
	   /*
	    * Partially fills orders greater than three burritos, pops the customer back in line and confirming they are not new customers (so their
	    * arrival/inital order is not re-printed to the console), and adds one to the line count (which was decremented in the currentCustomerAtCounter function. 
	    * Otherwise is the order was three or less burritos, the customer will be added to the register line and checked out when a server becomes free. 
	    */
	   public void free()
	   {
	       try
	       {
	    	   //acquire and release a server (semaphore) for the customer
	           BurritoBrothersStore.getStore().counter.acquire();
	           customerAtCounter = BurritoBrothersStore.getStore().currentCustomerAtCounter(serverNumber);
	           BurritoBrothersStore.getStore().counter.release();
	          
	           //keep making burritos, pop the customer back in line
	           if (customerAtCounter.getOrderSize() > 3)
	           {
	               customerAtCounter.partialFill();  
	               BurritoBrothersStore.getStore().currentCustomerInLine += 1;
	               BurritoBrothersStore.getStore().cookBurritos(3, customerAtCounter.getCustomerNumber(), serverNumber); 
	               System.out.println("Customer Number " + customerAtCounter.getCustomerNumber() + " has " + customerAtCounter.getOrderSize() + " burritos left in their order.");
	               
	               BurritoBrothersStore.getStore().lineActions(customerAtCounter, true);  
	               BurritoBrothersStore.getStore().serving.release();
	           }  
	           
	           //add to line for register and checkout when ready
	           else
	           {
	               BurritoBrothersStore.getStore().cookBurritos(customerAtCounter.getOrderSize(), customerAtCounter.getCustomerNumber(), serverNumber);
	               BurritoBrothersStore.pay(customerAtCounter);      
	               if (BurritoBrothersStore.getStore().register.tryAcquire() && !BurritoBrothersStore.getStore().lineForTheRegister.isEmpty() )
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
	       boolean isBusy = true; 
	       while (isBusy)
	       {
	           try
	           {
	               //wait for a customer to get in line, then serve. adds complexity to queue/asynchronousness/concurrency
	               if (BurritoBrothersStore.getStore().serving.tryAcquire(300, TimeUnit.MILLISECONDS))
	               {
	                   free();
	               }
	               else
	               {
	            	   isBusy = false;
	               }
	           }
	           catch (InterruptedException e) {
	        	   e.printStackTrace();   
	           }
	                      
	       }      
	      
	   }

}
