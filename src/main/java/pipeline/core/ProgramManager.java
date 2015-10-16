package pipeline.core;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProgramManager {

	//Directed Acyclic Graph
	private Map<String, Program> DAG = new ConcurrentHashMap<String, Program>();
	private List<String> programsLeft = new CopyOnWriteArrayList<String>();
	private List<Program> runnablePrograms = new CopyOnWriteArrayList<Program>();
	private boolean firstExecution;

	public ProgramManager(final Pipeline pipeline) {
		for(Program p: pipeline.getPrograms()){
			this.DAG.put(p.getId(),p);
			programsLeft.add(p.getId());
		}
		this.firstExecution=true;
	}

	public Map<String, Program> getDAG() {
		return DAG;
	}

	public void setDAG(Map<String, Program> dAG) {
		DAG = dAG;
	}

	public List<String> getProgramsLeft() {
		return programsLeft;
	}

	public List<Program> getRunnablePrograms() {
		this.runnablePrograms.clear();
		if(this.firstExecution){
			this.firstExecution=false;
			for (Map.Entry<String, Program> entry : DAG.entrySet()) {
				if(entry.getValue().getDependsOn()==null){
					this.runnablePrograms.add(entry.getValue());
					this.programsLeft.remove(entry.getKey());
				}
			}
		}
		else {
			for(String programId : this.programsLeft){
				Program p = DAG.get(programId);
				if(checkDependsOn(p)){
					this.runnablePrograms.add(p);
					this.programsLeft.remove(p.getId());
				}
			}
		}
		return runnablePrograms;
	}

	//recorre la lista dependsOn y comprueba si los programas tienen el atributo isFinished a true
	private boolean checkDependsOn(Program program) {
		int count = 0;
		String[] dependsArray = program.getDependsOn().split(",");
		for(String s: dependsArray){
			Program p = DAG.get(s);
			if(p.isFinished()){
				count++;
			}
		}
		if(count==dependsArray.length){
			return true;
		} else {
			return false;
		}
	}

}
