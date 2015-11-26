package pipeline.tests;

import java.net.URL;

import org.junit.Test;

import pipeline.core.App;
import pipeline.core.CompiApp;

public class PipelineTests {

	@Test
	public void test1() {
		String[] args = { "test1_pipeline.xml", "test1_params.xml", "10" };
		CompiApp compi = new CompiApp(args);
		Thread testThread = new Thread(compi);
		testThread.start();
	}

}
