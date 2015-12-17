package pipeline.interfaces;

import pipeline.core.Program;

public interface ProgramExecutionHandler {
	public void programStarted(Program p);
	public void programFinished(Program p);
	public void programAborted(Program p, Exception e);
}
