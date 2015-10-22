package pipeline.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pipeline.core.Program;

public class PipelineParser {

	private String ficheroXML;

	//clase para parsear los parametros ${..} dentro del XML
	public PipelineParser(final String fichero){
		this.ficheroXML = fichero;
	}

	public String getFicheroXML() {
		return ficheroXML;
	}

	public void solveExec(List<Program> programs){
		for(Program program : programs){
			cleanExec(program);
		}
	}

	private void cleanExec(Program program){
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher m = p.matcher(program.getExec());
		while(m.find()){
			program.getParsedExec().add(m.group(1));
		}
	}
}
