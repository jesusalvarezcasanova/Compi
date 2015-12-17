package pipeline.validation;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class DOMparsing {

	public static void validateXMLSchema(final String xmlPath,
			final String xsdPath) throws SAXException, IOException {
		final SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		final Schema schema = factory.newSchema(new File(xsdPath));
		final Validator validator = schema.newValidator();
		validator.setErrorHandler(new SimpleErrorHandler());
		validator.validate(new StreamSource(new File(xmlPath)));
	}
}