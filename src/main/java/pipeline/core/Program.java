package pipeline.core;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "program")
public class Program {

	private String id;
	private String dependsOn;
	private Foreach foreach;
	private String exec;
	private List<String> execStrings = new LinkedList<String>();
	private String toExecute;
	private String fileLog;
	private String fileErrorLog;
	private boolean isRunning = false;
	private boolean isFinished = false;
	private boolean isAborted = false;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id.replaceAll(" ", "");
	}

	@XmlAttribute
	public String getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn.replaceAll(" ", "");
	}

	@XmlAttribute
	public String getFileLog() {
		return fileLog;
	}

	public void setFileLog(String fileLog) {
		this.fileLog = fileLog;
	}

	@XmlAttribute
	public String getFileErrorLog() {
		return fileErrorLog;
	}

	public void setFileErrorLog(String fileErrorLog) {
		this.fileErrorLog = fileErrorLog;
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
		this.exec = exec.trim();
		this.toExecute = this.exec;
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

	public List<String> getExecStrings() {
		return execStrings;
	}

	public void setExecStrings(List<String> execStrings) {
		this.execStrings = execStrings;
	}

	public String getToExecute() {
		return toExecute;
	}

	public void setToExecute(String toExecute) {
		this.toExecute = toExecute;
	}

	public boolean isAborted() {
		return isAborted;
	}

	public void setAborted(boolean isAborted) {
		this.isAborted = isAborted;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: " + this.getId() + "\n");
		sb.append("DependsOn: " + this.getDependsOn() + "\n");
		if (this.fileLog != null) {
			sb.append("File log : " + this.fileLog + "\n");
		}
		if (this.fileErrorLog != null) {
			sb.append("File errorlog : " + this.fileErrorLog + "\n");
		}
		if (this.getForeach() != null) {
			sb.append("Foreach element: " + this.getForeach().getElement() + "\n");
			sb.append("Foreach source: " + this.getForeach().getSource() + "\n");
			sb.append("Foreach as: " + this.getForeach().getAs() + "\n");
		} else {
			sb.append("Foreach: " + this.getForeach() + "\n");
		}
		sb.append("Exec: " + this.getExec().trim() + "\n");
		for (String s : this.getExecStrings()) {
			sb.append("Exec strings: " + s + "\n");
		}
		sb.append("To execute: " + this.getToExecute() + "\n");
		return sb.toString();
	}
}
