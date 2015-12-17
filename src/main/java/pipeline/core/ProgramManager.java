package pipeline.core;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import pipeline.interfaces.ProgramExecutionHandler;

public class ProgramManager implements ProgramExecutionHandler {

	// Directed Acyclic Graph
	private final Map<String, Program> DAG = new ConcurrentHashMap<String, Program>();
	private final List<String> programsLeft = new CopyOnWriteArrayList<String>();
	private final List<Program> runnablePrograms = new CopyOnWriteArrayList<Program>();
	private final Map<String, Set<String>> dependencies = new ConcurrentHashMap<String, Set<String>>();
	private boolean firstExecution;

	public ProgramManager(final Pipeline pipeline) {
		for (final Program p : pipeline.getPrograms()) {
			this.DAG.put(p.getId(), p);
			this.programsLeft.add(p.getId());
			this.dependencies.put(p.getId(), new HashSet<String>());
		}
		this.firstExecution = true;
	}

	public List<Program> getRunnablePrograms() {
		this.runnablePrograms.clear();
		if (this.firstExecution) {
			this.firstExecution = false;
			for (final Map.Entry<String, Program> entry : DAG.entrySet()) {
				// si no tiene dependencias se puede ejecutar
				if (entry.getValue().getDependsOn() == null) {
					this.runnablePrograms.add(entry.getValue());
				}
			}
		} else {
			for (final String programId : this.programsLeft) {
				final Program program = DAG.get(programId);
				if (checkProgramDependencies(program)) {
					this.runnablePrograms.add(program);
				}
			}
		}
		return runnablePrograms;
	}

	// recorre las dependencias del programa y comprueba si estas tienen el
	// atributo isFinished a true
	private boolean checkProgramDependencies(final Program program) {
		int count = 0;
		if (program.getDependsOn() == null)
			return true;
		final String[] dependsArray = program.getDependsOn().split(",");
		for (final String s : dependsArray) {
			final Program programToCheck = DAG.get(s);
			if (programToCheck.isFinished()) {
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
		for (final String programs : this.programsLeft) {
			final Program program = DAG.get(programs);
			if (program.getDependsOn() != null) {
				for (final String dependsOn : program.getDependsOn()
						.split(",")) {
					if (!DAG.containsKey(dependsOn)) {
						throw new IllegalArgumentException(
								"El/los IDs contenidos en el atributo dependsOn del programa "
										+ program.getId()
										+ " no son correctos");
					}
				}
			}
		}
	}

	public void setDependencies() {
		for (final Map.Entry<String, Program> entry : DAG.entrySet()) {
			// si el programa tiene dependencias
			if (entry.getValue().getDependsOn() != null) {
				// añadimos las dependencias a la lista del programa
				// correspondiente
				for (final String dependsOn : entry.getValue().getDependsOn()
						.split(",")) {
					dependencies.get(dependsOn).add(entry.getKey());
				}
			}
		}
		setUpDependencies();
	}

	private void setUpDependencies() {
		for (final Map.Entry<String, Program> entry : DAG.entrySet()) {
			// comprobamos si el programa es dependencia de otro
			for (final Map.Entry<String, Set<String>> entry2 : dependencies
					.entrySet()) {
				if (entry2.getValue().contains(entry.getKey())) {
					entry2.getValue().addAll(dependencies.get(entry.getKey()));
				}
			}
		}
	}

	private Map<String, Program> getDAG() {
		return DAG;
	}

	public List<String> getProgramsLeft() {
		return programsLeft;
	}

	public Map<String, Set<String>> getDependencies() {
		return dependencies;
	}

	public void programFinished(Program p) {
		this.getDAG().get(p.getId()).setFinished(true);
		this.getDAG().get(p.getId()).setRunning(false);

	}

	public void programAborted(Program p, Exception e) {
		this.getDAG().get(p.getId()).setAborted(true);
		this.getDAG().get(p.getId()).setRunning(false);
		// // comprobamos sus dependencias para ver si es necesario abortar la
		// ejecucion del programa o se puede seguir
		for (String programToAbort : this.getDependencies().get(p.getId())) {
			this.getProgramsLeft().remove(programToAbort);
		}
	}

	public void programStarted(Program p) {
		this.getDAG().get(p.getId()).setRunning(true);
		this.getProgramsLeft().remove(p.getId());
	}
}
