
public class Customer {

	   private int customerNumber;
	   private int orderSize;
	      
	   public Customer()
	   {
	       this.orderSize = (int)(1+20*Math.random());      
	   }
	   
	   protected int getOrderSize()
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

}
