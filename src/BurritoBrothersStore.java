import java.util.concurrent.Semaphore;
import java.util.LinkedList;

public class BurritoBrothersStore {
   private int currentCustomerCount = 0;
   protected int currentCustomerinLine = 0;
   protected int customerNumber = 0;
   protected Customer line[] = new Customer[1];
   protected LinkedList<Customer> Register = new LinkedList<Customer>();  
   
   protected Semaphore customerEntered = new Semaphore(1);
   protected Semaphore serving = new Semaphore(0);
   protected Semaphore register = new Semaphore(1);
   protected Semaphore registerLine = new Semaphore(1);
   protected Semaphore counter = new Semaphore(1);
   protected Semaphore lineCount = new Semaphore(1);
   protected Semaphore ingredients = new Semaphore(1);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void enterCustomer(){
		try {
			customerEntered.acquire();
			customerNumber += 1; 
			
			if (currentCustomerCount < 15){
				Customer currentCustomer = new Customer();
				currentCustomerCount += 1;
				currentCustomer.setCustomerNumber(customerNumber);	
				
				//function here
				
				serving.acquire();
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

}
