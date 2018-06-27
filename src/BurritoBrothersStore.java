import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Collections;

public class BurritoBrothersStore implements Runnable{
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
	
	public void lineActions(Customer currentCustomer, boolean orderTaken){
		try {
			lineCount.acquire();
			if (!orderTaken){
				currentCustomerInLine += 1;
				System.out.println("\nCustomer Number " + customerNumber + " has entered the line. \n");
				line.add(currentCustomer);
			}
			
			else{
				line.add(currentCustomer);
				currentCustomerInLine += 1;
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
	
	public void cookBurritos(int numberOfBurritos, int customerNum, int serverNum){
		System.out.println("Customer Number " + customerNum + "'s burritos are being made by Server Number " + serverNum);
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
	           System.out.println("Customer Number " + payingCustomer.getCustomerNumber() + " is paying.");
	           try {
	        	   Thread.sleep(25);
	           }
	           catch (InterruptedException e) {
	        	   e.printStackTrace();
	           }
	           
	           System.out.println("Customer Number " + payingCustomer.getCustomerNumber() + " has left.");
	           currentCustomerCount -= 1;
	       }  
	       register.release();
	   }
	 
	 public void run(){
		enterCustomer();
	 }
}
