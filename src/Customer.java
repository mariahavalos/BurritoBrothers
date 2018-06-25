
public class Customer {

	   private int customerId;
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
	  
	   public int getCustomerId()
	   {
	       return this.customerId;
	   }
	  

	   public void setCustomerId(int customerId)
	   {
	       this.customerId = customerId;
	   }

}
