package pipeline.tests;

import org.junit.Test;
import org.xml.sax.SAXException;

import pipeline.core.CompiApp;
import pipeline.validation.DOMparsing;

public class PipelineTests {

	@Test(expected = SAXException.class)
	public void testXSDSaxException() throws Exception {
		DOMparsing
				.validateXMLSchema(
						ClassLoader
								.getSystemResource(
										"pipelineParsingException.xml")
								.getFile(),
						ClassLoader.getSystemResource("xsd/pipeline.xsd")
								.getFile());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParamsException() throws Exception {
		String[] args = {
				ClassLoader.getSystemResource("pipelineParamsException.xml")
						.getFile(),
				ClassLoader.getSystemResource("params.xml").getFile(), "10" };
		CompiApp compi = new CompiApp(args);
		compi.run();
	}
}
