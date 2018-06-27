public class Line {

	/*
	 * payAtRegister function handles the register acquire and release of the register semaphore. There is no timeout for the simulation, as we assume the customer will be near the checkout area.
	 * Also adds the customer to the register line for further processing when paying.
	 * 
	 */
	public static void payAtRegister(Customer customerAtCounter){
	    try {
	    	   BurritoBrothersStore.getStore().register.acquire();
	           BurritoBrothersStore.getStore().lineForTheRegister.add(customerAtCounter);    
	           BurritoBrothersStore.getStore().register.release();
	      }
	    catch (InterruptedException e) {
	    	e.printStackTrace();
	    }
	}

}
