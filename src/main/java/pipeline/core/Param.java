package pipeline.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="param")
public class Param {

	private String type;
	private String name;

	public Param() {}

	public Param(String type, String name) {
		this.type=type;
		this.name=name;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + this.getName() + "\n");
		sb.append("Type: " + this.getType() + "\n");
		return sb.toString();
	}

}
