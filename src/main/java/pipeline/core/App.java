package pipeline.core;

public class App {

	public static void main(final String args[]){
		final CompiApp compi = new CompiApp(args);
		final Thread compiThread = new Thread(compi);
		compiThread.start();
	}
}
