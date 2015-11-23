package pipeline.core;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pipeline")
public class Pipeline {

	private List<Program> programs = new LinkedList<Program>();
	private List<Param> params = new LinkedList<Param>();

	public Pipeline() {
	}

	public Pipeline(final List<Program> programs, final List<Param> params) {
		this.programs = programs;
		this.params = params;
	}

	@XmlElementWrapper(name = "programs")
	@XmlElement(name = "program")
	public List<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(final List<Program> programs) {
		this.programs = programs;
	}

	@XmlElementWrapper(name = "params")
	@XmlElement(name = "param")
	public List<Param> getParams() {
		return params;
	}

	public void setParams(final List<Param> params) {
		this.params = params;
	}
}
