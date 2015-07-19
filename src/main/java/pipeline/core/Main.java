package pipeline.core;

import java.io.File;
import java.net.URL;

import pipeline.validation.DOMparsing;



public class Main{

	public static void main(String args[]){

		//comprobamos si el fichero XML valida con el fichero XSD
		URL xsdPath = Thread.currentThread().getContextClassLoader().getResource("xsd/pipeline.xsd");

		boolean doc = DOMparsing.validateXMLSchema(args[0], xsdPath.getPath());

		System.out.println(doc);


		//		int threadNumber = Integer.parseInt(args[0]);
		//		ExecutorService eS = Executors.newFixedThreadPool(threadNumber);
		//		Resolver paramResolver = new Resolver();
		//		synchronized (this) {
		//			
		//		}
		//		
		//		Future<?> f = eS.submit();
		//		try {
		//			f.get();
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (ExecutionException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}

}
