package pipeline.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pipeline.core.Program;

public class PipelineParser {

	public void solveExec(List<Program> programs) {
		for (Program program : programs) {
			cleanExec(program);
		}
	}

	public void cleanExec(Program program) {
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher m = p.matcher(program.getExec());
		while (m.find()) {
			program.getExecStrings().add(m.group(1));
		}
	}
}
