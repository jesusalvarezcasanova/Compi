package pipeline.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="program")
public class Program {

	private String id;
	private String dependsOn;
	private Foreach foreach;
	private String exec;
	private boolean isRunning=false;
	private boolean isFinished=false;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id.replaceAll(" ","");
	}

	@XmlAttribute
	public String getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn.replaceAll(" ","");
	}

	@XmlElement
	public Foreach getForeach() {
		return foreach;
	}

	public void setForeach(Foreach foreach) {
		this.foreach = foreach;
	}

	@XmlElement
	public String getExec() {
		return exec;
	}

	public void setExec(String exec) {
		this.exec = exec;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: " + this.getId() + "\n");
		sb.append("DependsOn: " + this.getDependsOn() + "\n");
		if(this.getForeach()!=null){
			sb.append("Foreach element: " + this.getForeach().getElement() + "\n");
			sb.append("Foreach source: " + this.getForeach().getSource() + "\n");
			sb.append("Foreach as: " + this.getForeach().getAs() + "\n");
		} else {
			sb.append("Foreach: " + this.getForeach() + "\n");
		}
		sb.append("Exec: " + this.getExec().trim() + "\n");
		return sb.toString();
	}
}
