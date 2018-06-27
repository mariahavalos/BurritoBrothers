public class Line {

	public static void payAtRegister(Customer customerAtCounter){
	    try {
	    	   BurritoBrothersStore.getStore().register.acquire();
	           BurritoBrothersStore.getStore().Register.add(customerAtCounter);    
	           BurritoBrothersStore.getStore().register.release();
	      }
	    catch (InterruptedException e) {
	    	e.printStackTrace();
	    }
	}

}
