import java.util.concurrent.TimeUnit;

public class Server implements Runnable{

	 private static int serverNumber;
	 private static Customer customerAtCounter;
	 private static int servers = 3;
	   
	   public Server(int serverNumber)
	   {
	       this.serverNumber = serverNumber;
	   }  
	  
	
	   public static void freeServer()
	   {
	       try
	       {
	    	   
	           BurritoBrothersStore.getStore().serving.acquire();
	           BurritoBrothersStore.getStore().counter.acquire();
	           customerAtCounter = BurritoBrothersStore.getStore().currentCustomerAtCounter(serverNumber);
	           BurritoBrothersStore.getStore().counter.release();
	          
	           if (customerAtCounter.getOrderSize() > 3)
	           {
	        	   System.out.println("Customer number has ordered " + customerAtCounter.getOrderSize() + " burritos.");
	               customerAtCounter.partialFill();         
	               BurritoBrothersStore.getStore().cookBurritos(3, serverNumber); 
	               System.out.println("Customer number has " + customerAtCounter.getOrderSize() + " burritos left in their order.");
	               
	               BurritoBrothersStore.getStore().lineActions(customerAtCounter, true);  
	               BurritoBrothersStore.getStore().serving.release();
	           }  
	           
	           else
	           {
	               BurritoBrothersStore.getStore().cookBurritos(customerAtCounter.getOrderSize(), serverNumber);
	               BurritoBrothersStore.pay(customerAtCounter);      
	               if (!BurritoBrothersStore.getStore().Register.isEmpty() && BurritoBrothersStore.getStore().register.tryAcquire())
	               {
	                   BurritoBrothersStore.getStore().checkout();
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
	               if (BurritoBrothersStore.getStore().serving.tryAcquire(25, TimeUnit.MILLISECONDS))
	               {
	                   freeServer();
	               }
	               else
	               {
	                   //close store
	               }
	           }
	           catch (InterruptedException e) {
	        	   e.printStackTrace();   
	           }
	                      
	       }      
	      
	   }

}
