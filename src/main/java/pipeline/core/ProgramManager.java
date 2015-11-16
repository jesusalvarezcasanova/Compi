package pipeline.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProgramManager {

	// Directed Acyclic Graph
	private Map<String, Program> DAG = new ConcurrentHashMap<String, Program>();
	private List<String> programsLeft = new CopyOnWriteArrayList<String>();
	private List<Program> runnablePrograms = new CopyOnWriteArrayList<Program>();
	private boolean firstExecution;

	public ProgramManager(final Pipeline pipeline) {
		for (Program p : pipeline.getPrograms()) {
			this.DAG.put(p.getId(), p);
			this.programsLeft.add(p.getId());
		}
		this.firstExecution = true;
	}

	public Map<String, Program> getDAG() {
		return DAG;
	}

	public List<String> getProgramsLeft() {
		return programsLeft;
	}

	public List<Program> getRunnablePrograms() {
		this.runnablePrograms.clear();
		if (this.firstExecution) {
			this.firstExecution = false;
			for (Map.Entry<String, Program> entry : DAG.entrySet()) {
				// si no tiene dependencias se puede ejecutar
				if (entry.getValue().getDependsOn() == null) {
					this.runnablePrograms.add(entry.getValue());
					this.programsLeft.remove(entry.getKey());
				}
			}
		} else {
			for (String programId : this.programsLeft) {
				Program p = DAG.get(programId);
				if (checkProgramDependencies(p)) {
					this.runnablePrograms.add(p);
					this.programsLeft.remove(p.getId());
				}
			}
		}
		return runnablePrograms;
	}

	// recorre las dependencias del programa y comprueba si estas tienen el
	// atributo isFinished a true
	private boolean checkProgramDependencies(Program program) {
		int count = 0;
		String[] dependsArray = program.getDependsOn().split(",");
		for (String s : dependsArray) {
			Program p = DAG.get(s);
			if (p.isFinished()) {
				count++;
			}
		}
		if (count == dependsArray.length) {
			return true;
		} else {
			return false;
		}
	}

	// comprueba si los ids que hay en dependsOn existen
	public void checkDependsOnIds() {
		for (String programs : this.programsLeft) {
			Program program = DAG.get(programs);
			if (program.getDependsOn() != null) {
				String[] dependsArray = program.getDependsOn().split(",");
				for (String s : dependsArray) {
					if (!DAG.containsKey(s)) {
						throw new IllegalArgumentException(
								"El/los IDs contenidos en el atributo dependsOn del programa " + program.getId()
										+ " no son correctos");
					}
				}
			}
		}
	}

}
