package pipeline.core;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import pipeline.validation.DOMparsing;
import pipeline.validation.Resolver;



public class Main{

	public static void main(String args[]){

		URL xsdPath = Thread.currentThread().getContextClassLoader().getResource("xsd/pipeline.xsd");
		Resolver resolver = new Resolver(args[0]);

		if(DOMparsing.validateXMLSchema(args[0], xsdPath.getPath())){//comprobamos si el fichero XML valida con el fichero XSD
			try{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(args[0]);
				//extraemos el contenido de las etiquetas <exec> del fichero XML
				resolver.solveExecXML(doc.getElementsByTagName("exec"));
				for(String s: resolver.getExecStrings()){
					System.out.println(s);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}

			//		Resolver paramResolver = new Resolver();

			//		int threadNumber = Integer.parseInt(args[0]);
			//		ExecutorService eS = Executors.newFixedThreadPool(threadNumber);
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
}
