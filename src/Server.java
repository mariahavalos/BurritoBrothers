import java.util.concurrent.TimeUnit;

public class Server {

	 private int serverNumber;
	 private Customer customerAtCounter;
	 static private int servers = 3;
	   
	   public Server(int serverNumber)
	   {
	       this.serverNumber = serverNumber;
	   }  
	  
	
	   public void FreeServer()
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
	               BurritoBrothersStore.getStore().cookBurritos(3, serverNumber);  
	               
	               BurritoBrothersStore.getStore().lineActions(customerAtCounter,false);  
	               BurritoBrothersStore.getStore().serving.release();
	           }  
	           
	           else
	           {
	               BurritoBrothersStore.getStore().cookBurritos(customerAtCounter.getOrderSize(), serverNumber);
	               BurritoBrothersStore.getStore().pay(customerAtCounter);      
	               if (!BurritoBrothersStore.getStore().Register.isEmpty() && BurritoBrothersStore.getStore().register.tryAcquire())
	               {
	                   BurritoBrothersStore.getStore().approachRegister();
	               }              
	           }
	       }
	       catch (InterruptedException e1) {e1.printStackTrace();}
	  
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
	                   FreeServer();
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
