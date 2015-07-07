package pipeline.interfaces;

import pipeline.core.Program;

public interface Scheduler {
	void programFinished(Program p);
	void programAborter(Program p, Throwable t);
}
