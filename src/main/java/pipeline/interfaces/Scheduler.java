package pipeline.interfaces;

import pipeline.core.Program;

public interface Scheduler {

	void programFinished(Program p);

	void programAborted(Program p);

	void programAborted(Program p, Throwable t);

	void programAborted(Program p, Exception e);

}
