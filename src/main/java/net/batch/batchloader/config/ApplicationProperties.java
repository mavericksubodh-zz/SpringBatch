package net.batch.batchloader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Properties specific to the batch.
 * Properties are configured in application.properties
 * 
 * @author spal
 *
 */
@ConfigurationProperties ( prefix= "application", ignoreUnknownFields=false)
public class ApplicationProperties {

	private final Batch batch = new Batch();

	public Batch getBatch() {
		return batch;
	}

	public static class Batch{
		//TODO add the file path here, else it will throw null pointer exception
		private String inputPath="";
		public String getInputPath() {
			return this.inputPath;
		}
		public void setInputPath( String inputPath) {
			this.inputPath = inputPath;
		}
	}

}
