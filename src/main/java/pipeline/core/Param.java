package pipeline.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "param")
public class Param {

	private String type;
	private String name;

	public Param() {
	}

	public Param(final String type, final String name) {
		this.type = type;
		this.name = name;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Name: " + this.getName() + "\n");
		sb.append("Type: " + this.getType() + "\n");
		return sb.toString();
	}

}
