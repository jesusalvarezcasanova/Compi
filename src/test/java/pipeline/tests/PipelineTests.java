package pipeline.tests;

import java.net.URL;

import org.junit.Test;

import pipeline.core.App;
import pipeline.core.CompiApp;

public class PipelineTests {

	@Test
	public void test1() {
		final URL xmlPipeline = Thread.currentThread().getContextClassLoader()
				.getResource("test1_pipeline.xml");
		final URL xmlParams = Thread.currentThread().getContextClassLoader()
				.getResource("test1_params.xml");
		String[] args = { xmlPipeline.toString(), xmlParams.toString(), "10" };
		CompiApp compi = new CompiApp(args);
		Thread testThread = new Thread(compi);
		testThread.start();
	}

}
