
public class Simulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BurritoBrothersStore.getStore().enterCustomer();
		Server.freeServer();
		BurritoBrothersStore.getStore().enterCustomer();

	}

}
