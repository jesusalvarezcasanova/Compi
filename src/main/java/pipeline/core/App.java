package pipeline.core;

public class App {

	public static void main(final String args[]) {
		final CompiApp compi = new CompiApp(args);
		final Thread compiThread = new Thread(compi);
		compiThread.start();
		try {
			compiThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("------Fin programa------");
	}
	
}
