package pipeline.core;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pipeline.validation.DOMparsing;



public class Main{

	public static void main(String args[]){

		//comprobamos si el fichero XML valida con el fichero XSD
		URL xsdPath = Thread.currentThread().getContextClassLoader().getResource("xsd/pipeline.xsd");

		if(DOMparsing.validateXMLSchema(args[0], xsdPath.getPath())){
			try{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(args[0]);
				NodeList nl = doc.getElementsByTagName("exec");
				int num = nl.getLength();
				for(int i=0; i<num;i++){
					Element node = (Element) nl.item(i);
					System.out.println(node.getTextContent().trim());
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
