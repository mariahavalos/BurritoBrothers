import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Collections;

public class BurritoBrothersStore {
   private int currentCustomerCount = 0;
   protected int currentCustomerInLine = 0;
   protected int customerNumber = 0;
   protected ArrayList<Customer> line = new ArrayList<Customer>();
   protected ArrayList<Customer> Register = new ArrayList<Customer>();  
   
   protected Semaphore customerEntered = new Semaphore(1);
   protected Semaphore serving = new Semaphore(0);
   protected Semaphore register = new Semaphore(1);
   protected Semaphore registerLine = new Semaphore(1);
   protected Semaphore counter = new Semaphore(1);
   protected Semaphore lineCount = new Semaphore(1);
   protected Semaphore ingredients = new Semaphore(1);
   
   private static BurritoBrothersStore burritoBrothers = new BurritoBrothersStore();
   
	public static BurritoBrothersStore getStore() {
		return burritoBrothers;
	}
	
	public void enterCustomer(){
		try {
			customerEntered.acquire();
			customerNumber += 1; 
			
			if (currentCustomerCount < 15){
				Customer currentCustomer = new Customer();
				currentCustomerCount += 1;
				currentCustomer.setCustomerNumber(customerNumber);	
				
				lineActions(currentCustomer, false); 
				serving.release();	
				customerEntered.release();
				
			}
			
			else{
				System.out.println("Store is full. Customer must come back later.");
				customerEntered.release(); 
			}
		} 
		
		catch (InterruptedException e) {

			e.printStackTrace();
		} 
	}
	
	public void lineActions(Customer currentCustomer, boolean orderTaken){
		try {
			lineCount.acquire();
			
			//add customer to line
			if (!orderTaken){
				System.out.println("Order taken for customer: " + customerNumber);
				line.add(currentCustomer);
			}
			
			else{
			}	
			
			//sort line for smallest order size
			Collections.sort(line);
			lineCount.release();
		} 
		catch (InterruptedException e) {

			e.printStackTrace();
		} 
		
	}
	
	public Customer currentCustomerAtCounter(int serverNumber){
		System.out.println("Customer Number " + customerNumber + " at the counter.");
		Customer customerAtCounter = new Customer();
		
		customerAtCounter = line.get(0);
		
		for (int i = 0; i < currentCustomerInLine; i++){
			line.set(i, line.get(i + 1));
		}
		
		//line.set(currentCustomerInLine, null);
		currentCustomerInLine -= 1;
		return customerAtCounter;
	}
	
	public void cookBurritos(int numberOfBurritos, int serverNumber){
		System.out.println("Customer Number " + customerNumber + "'s burritos are being made.");
		BurritoPrep.makeBurritos();
	}
	
	public static void pay(Customer customerAtRegister){
		  Line.payAtRegister(customerAtRegister);
	}
	
	 public void checkout()
	   { 
		 
	       while (!Register.isEmpty())
	       {          
	    	   Customer payingCustomer; 
	           payingCustomer = Register.remove(0);
	           System.out.println("Customer Number " + customerNumber + " is paying.");
	           try {
	        	   Thread.sleep(25);
	           }
	           catch (InterruptedException e) {
	        	   e.printStackTrace();
	           }
	                          
	          
	           currentCustomerCount -= 1;
	           System.out.println("Customer Number " + customerNumber + " has left.");
	       }  
	       register.release();
	   }
	 
	 //public void run(){
		//enterCustomer();
	// }
}
