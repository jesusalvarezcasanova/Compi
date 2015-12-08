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

	public void setId(final String id) {
		this.id = id.replaceAll(" ", "");
	}

	@XmlAttribute
	public String getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(final String dependsOn) {
		this.dependsOn = dependsOn.replaceAll(" ", "");
	}

	@XmlAttribute
	public String getFileLog() {
		return fileLog;
	}

	public void setFileLog(final String fileLog) {
		this.fileLog = fileLog;
	}

	@XmlAttribute
	public String getFileErrorLog() {
		return fileErrorLog;
	}

	public void setFileErrorLog(final String fileErrorLog) {
		this.fileErrorLog = fileErrorLog;
	}

	@XmlElement
	public Foreach getForeach() {
		return foreach;
	}

	public void setForeach(final Foreach foreach) {
		this.foreach = foreach;
	}

	@XmlElement
	public String getExec() {
		return exec;
	}

	public void setExec(final String exec) {
		this.exec = exec.trim();
		this.toExecute = this.exec;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(final boolean isRunning) {
		this.isRunning = isRunning;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(final boolean isFinished) {
		this.isFinished = isFinished;
	}

	public List<String> getExecStrings() {
		return execStrings;
	}

	public void setExecStrings(final List<String> execStrings) {
		this.execStrings = execStrings;
	}

	public String getToExecute() {
		return toExecute;
	}

	public void setToExecute(final String toExecute) {
		this.toExecute = toExecute;
	}

	public boolean isAborted() {
		return isAborted;
	}

	public void setAborted(final boolean isAborted) {
		this.isAborted = isAborted;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Id: " + this.getId() + "\n");
		if (this.getDependsOn() != null) {
			sb.append("DependsOn: " + this.getDependsOn() + "\n");
		}
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
		for (final String s : this.getExecStrings()) {
			sb.append("Exec strings: " + s + "\n");
		}
		sb.append("To execute: " + this.getToExecute() + "\n");
		return sb.toString();
	}
}
