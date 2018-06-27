public class BurritoPrep {
	/*
	 * makeBurritos function handles the semaphore acquire and release for ingredient gathering and simulation of cooking of burritos. Includes time out
	 * functions to help demonstrate the concurrency and asynchronous nature in a non-happy path manner.
	 * 
	 */
	public static void makeBurritos() { 
	       try
	       {
	    	   //semaphore acquire and release for ingredients. 
	           BurritoBrothersStore.getStore().ingredients.acquire();
	           BurritoBrothersStore.getStore().ingredients.release();
	           try {
	        	   
	        	   //allows for the queue to build up realistically while the "burritos" are being made. this helps to complicate the queue/order/asynchronous nature
	        	   Thread.sleep(30);
	           } 
	           catch (InterruptedException e) 
	           {
	        	   e.printStackTrace();
	           }
	           
	       }
	       catch (InterruptedException e){
	    	   e.printStackTrace();
	       }

	}

}
