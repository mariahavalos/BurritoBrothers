
public class Simulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BurritoBrothersStore.getStore().enterCustomer();
		Server.freeServer();
		System.out.println("ok2");
		BurritoBrothersStore.getStore().enterCustomer();

	}

}
