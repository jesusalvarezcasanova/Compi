package pipeline.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pipeline.validation.Resolver;



public class Main{
	
	public void main(String args[]){
		int threadNumber = Integer.parseInt(args[0]);
		ExecutorService eS = Executors.newFixedThreadPool(threadNumber);
		Resolver paramResolver = new Resolver();
		
		synchronized (this) {
			
		}
		
		
		Future<?> f = eS.submit();
		try {
			f.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
