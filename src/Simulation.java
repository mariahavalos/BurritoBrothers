
public class Simulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server(1);
		BurritoBrothersStore.getStore().enterCustomer();
		server.run();
		BurritoBrothersStore.getStore().enterCustomer();


	}

}
