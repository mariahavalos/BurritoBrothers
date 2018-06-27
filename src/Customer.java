public class Customer implements Comparable<Customer> {

	/*
	 * Customer class, which creates a customer object with a custom sorting function using comparable. Reduces order size on partial fills. 
	 */
	
	   private int customerNumber;
	   private int orderSize;
	      
	   public Customer()
	   {
	       this.orderSize = (int)(1+20*Math.random());      
	   }
	   
	   protected Integer getOrderSize()
	   {
	       return this.orderSize;
	   }
	  
	   protected void partialFill()
	   {
	       this.orderSize -= 3;
	   }
	  
	   public int getCustomerNumber()
	   {
	       return this.customerNumber;
	   }
	  

	   public void setCustomerNumber(int customerNumber)
	   {
	       this.customerNumber = customerNumber;
	   }
	   
	   @Override
		public int compareTo(Customer customer) {
	        return getOrderSize().compareTo(customer.getOrderSize());
	    }
}
