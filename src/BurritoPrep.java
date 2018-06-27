
public class BurritoPrep {

	public static void makeBurritos() { 
	       try
	       {
	           BurritoBrothersStore.getStore().ingredients.acquire();
	           BurritoBrothersStore.getStore().ingredients.release();
	           try {
	        	   Thread.sleep(10);
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
