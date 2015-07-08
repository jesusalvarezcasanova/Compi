package pipeline.core;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import pipeline.validation.DOMparsing;



public class Main{
	
	public static void main(String args[]){
		
		//comprobamos si el fichero XML valida con el fichero XSD
		System.out.println(args[0]);

		try {
			Document doc = DOMparsing.loadAndValidateWithExternalXSD(args[0],args[1]);
//			System.out.println(doc.getDocumentElement());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.err.println("Error en la validacion del fichero XML");
			e.printStackTrace();
		}
		
		
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
