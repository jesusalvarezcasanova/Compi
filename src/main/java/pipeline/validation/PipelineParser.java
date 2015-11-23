package pipeline.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pipeline.core.Program;

public class PipelineParser {

	public void solveExec(final List<Program> programs) {
		for (final Program program : programs) {
			cleanExec(program);
		}
	}

	public void cleanExec(final Program program) {
		final Pattern p = Pattern.compile("\\{(.*?)\\}");
		final Matcher m = p.matcher(program.getExec());
		while (m.find()) {
			program.getExecStrings().add(m.group(1));
		}
	}
}
