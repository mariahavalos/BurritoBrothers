import java.util.concurrent.TimeUnit;

public class Server {

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
	               customerAtCounter.partialFill();         
	               BurritoBrothersStore.getStore();
	               BurritoBrothersStore.cookBurritos(3, serverNumber);  
	               
	               BurritoBrothersStore.getStore().lineActions(customerAtCounter,false);  
	               BurritoBrothersStore.getStore().serving.release();
	           }  
	           
	           else
	           {
	               BurritoBrothersStore.getStore();
	               BurritoBrothersStore.cookBurritos(customerAtCounter.getOrderSize(), serverNumber);
	               BurritoBrothersStore.pay(customerAtCounter);      
	               if (!BurritoBrothersStore.getStore().Register.isEmpty() && BurritoBrothersStore.getStore().register.tryAcquire())
	               {
	                   BurritoBrothersStore.getStore();
	                   BurritoBrothersStore.approachRegister();
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
	           catch (InterruptedException e) {e.printStackTrace();   }
	                      
	       }      
	      
	   }

}
