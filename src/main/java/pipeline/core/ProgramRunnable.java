package pipeline.core;

import pipeline.interfaces.Scheduler;
import pipeline.validation.XMLResolver;

public class ProgramRunnable implements Runnable{
	private Program p;
	private Scheduler s;
	
	public ProgramRunnable(Program p, Scheduler s) {
		this.p=p;
		this.s=s;
	}
	
	public void run(){
		//Comprobar si con el XMLResolver se puede resolver el exec del fichero XML
		//sino excepcion
		XMLResolver r = new XMLResolver();
		try{
			s.programFinished(this.p);
		}
		catch (Exception e) {
			s.programAborter(this.p, e);
		}
	}

}
