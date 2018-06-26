import java.util.ArrayList;


public class Line {

	public static void payAtRegister(Customer customerAtCounter){
		ArrayList<Customer> Register = new ArrayList<Customer>();
	    try {
	    	   BurritoBrothersStore.getStore().register.acquire();
	           BurritoBrothersStore.getStore().Register.add(customerAtCounter); 
	           Register = BurritoBrothersStore.getStore().Register;       
	          
	           BurritoBrothersStore.getStore().register.release();
	      }
	    catch (InterruptedException e) {
	    	e.printStackTrace();
	    }
	}

}
