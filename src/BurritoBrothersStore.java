import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Collections;

public class BurritoBrothersStore implements Runnable{
	
   private int currentCustomerCount = 0;
   protected int currentCustomerInLine = 0, customerNumber = 0;
   protected ArrayList<Customer> line = new ArrayList<Customer>();
   protected ArrayList<Customer> lineForTheRegister = new ArrayList<Customer>();  
   
   protected Semaphore customerEntered = new Semaphore(1), serving = new Semaphore(0), 
		   register = new Semaphore(1), registerLine = new Semaphore(1), counter = new Semaphore(1), 
		   lineCount = new Semaphore(1), ingredients = new Semaphore(1);
   
   private static BurritoBrothersStore burritoBrothers = new BurritoBrothersStore();
   
   /*
    * Instance of the burrito brothers store, used for referencing in other classes to access the variables.
    */
	public static BurritoBrothersStore getStore() {
		
		return burritoBrothers;
	}
	
	/* enterCustomer function acquires and releases the customerEntered semaphore, checking capacity of the room as well as the order size and presenting
	 * it via the console. Also releases serving semaphore, so that servers are free to assist new customer.
	 * 
	 */
	public void enterCustomer(){
		try {
			customerEntered.acquire();
			customerNumber += 1; 
			
			if (currentCustomerCount < 15){
				Customer currentCustomer = new Customer();
				currentCustomerCount += 1;
				currentCustomer.setCustomerNumber(customerNumber);	
				
				lineActions(currentCustomer, false); 
				System.out.println("Customer Number " + currentCustomer.getCustomerNumber() + " has ordered " + currentCustomer.getOrderSize() + " burritos.");
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
	
	/*
	 * Announces when customers first order, sorts the line, and adds customers into the line when they return after a partially filled order.
	 */
	public void lineActions(Customer currentCustomer, boolean orderTaken){
		try {
			lineCount.acquire();
			
			//if the customer hasn't had their order first taken yet, take the order and log it to the console for readability
			if (!orderTaken){
				currentCustomerInLine += 1;
				System.out.println("\nCustomer Number " + customerNumber + " has entered the line. \n");
				line.add(currentCustomer);
			}
			
			//add to line without notifying that they have arrived, since their order was already taken. this is just for readability
			else{
				line.add(currentCustomer);
				currentCustomerInLine += 1;
			}	
			
			//sort line for smallest order size using custom comparable
			Collections.sort(line);
			lineCount.release();
		} 
		catch (InterruptedException e) {

			e.printStackTrace();
		} 
		
	}
	
	/*
	 * Keeps track of the current customer at the counter, as well as the entire line. moves the line up by one when a customer approaches the counter and removes the customer
	 * at the counter from the line.
	 */
	public Customer currentCustomerAtCounter(int serverNumber){
		Customer customerAtCounter = new Customer();
		
		customerAtCounter = line.get(0);
		System.out.println("\nCustomer Number " + customerAtCounter.getCustomerNumber() + " at the counter.");
		System.out.println("Server Number " + serverNumber + " assisting. \n");
		
		for (int i = 0; i < line.size()-1; i++){
			line.set(i, line.get(i + 1));
		}
		
		currentCustomerInLine -= 1;
		line.remove(0);
		
		return customerAtCounter;
	}
	
	/*
	 * CookBurritos function calls the BurritoPrep class that handles the semaphores for ingredients and cooking. 
	 */
	public void cookBurritos(int numberOfBurritos, int customerNum, int serverNum){
		System.out.println("Customer Number " + customerNum + "'s burritos are being made by Server Number " + serverNum);
		BurritoPrep.makeBurritos();
	}
	
	/*
	 * pay function calls the Line class to add the customer to the checkout line and utilize the acquire and release for the register semaphore. 
	 */
	public static void pay(Customer customerAtRegister){
		  System.out.println("Customer Number " + customerAtRegister.getCustomerNumber() + " is inline to pay.");
		  Line.payAtRegister(customerAtRegister);
	}
	
	/*
	 * checkout function actually checkouts out the customer, removes them from the line, and releases the register semaphore.
	 */
	 public void checkout()
	   { 
		 
		 //while the line isn't empty
	       while (!lineForTheRegister.isEmpty())
	       {          
	    	   Customer payingCustomer; 
	    	   
	    	   //remove the customer from the register line (they will have already been removed from the line as per currentCustomerAtCounter.
	           payingCustomer = lineForTheRegister.remove(0);
	           System.out.println("Customer Number " + payingCustomer.getCustomerNumber() + " is paying.");
	           
	           try {
	        	   //simulate checkout time, because people like to pay with pennies.
	        	   Thread.sleep(25);
	           }
	           catch (InterruptedException e) {
	        	   e.printStackTrace();
	           }
	           
	           System.out.println("Customer Number " + payingCustomer.getCustomerNumber() + " has left.");
	           currentCustomerCount -= 1;
	       }  
	       //releases the register semaphore
	       register.release();
	   }
	 
	 /*
	  * (non-Javadoc)
	  * @see java.lang.Runnable#run()
	  */
	 public void run(){
		enterCustomer();
	 }
}
