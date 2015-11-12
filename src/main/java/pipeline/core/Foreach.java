package pipeline.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "foreach")
public class Foreach {

	private String element;
	private String source;
	private String as;

	@XmlAttribute
	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	@XmlAttribute
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@XmlAttribute
	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}
}