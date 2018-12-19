package net.batch.batchloader.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

//generic stereotype annotation for any Spring-managed component
@Component
//Triggers spring batch to include and configure the feature of the class as spring beans. Class provide strategy how beans are to be used.
//Once you have an @EnableBatchProcessing class in your configuration you will have an instance of StepScope and JobScope so your beans inside steps can have @Scope("step") and @Scope("job") respectively. 
@EnableBatchProcessing
public class BatchConfiguration implements BatchConfigurer {

	//Responsible for persisting metadata about batch jobs in memory/db
	private JobRepository jobRepository;
	//Provided getters/setters to retrieve the metadata from JobRepository
	private JobExplorer jobExplorer;
	//Runs jobs with given parameters
	private JobLauncher jobLauncher;

	//autowire beans to support above 3 attributes
	@Autowired
	@Qualifier( value = "batchTransactionManager")
	private PlatformTransactionManager batchTransactionManager;
	
	@Autowired
	@Qualifier( value = "batchDataSource")
	private DataSource batchDataSource;
	
	@Override
	public JobExplorer getJobExplorer() throws Exception {
		// TODO Auto-generated method stub
		return this.jobExplorer;
	}

	@Override
	public JobLauncher getJobLauncher() throws Exception {
		// TODO Auto-generated method stub
		return this.jobLauncher;
	}

	@Override
	public JobRepository getJobRepository() throws Exception {
		// TODO Auto-generated method stub
		return this.jobRepository;
	}

	@Override
	public PlatformTransactionManager getTransactionManager() throws Exception {
		// TODO Auto-generated method stub
		return this.batchTransactionManager;
	}


	/**
	 * 	Define strategy how above beans are defined for the batch 
	 *	executes the job on demand
	 * @return
	 * @throws Exception
	 */
	protected JobLauncher createJobLauncher () throws Exception{
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		//ensure that job repository dependencies have been set
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}
	
	/**
	 * Create repository with dataSource and transaction manager.
	 * @return
	 * @throws Exception
	 */
	protected JobRepository createjobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(this.batchDataSource);
		factory.setTransactionManager(getTransactionManager());
		factory.afterPropertiesSet();
		return factory.getObject();		
	}
	
	/**
	 * This method handles the actual bean configuration and is called after dependency injection
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void afterPropertiesSet() throws Exception{
		this.jobRepository=createjobRepository();
		JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
		jobExplorerFactoryBean.setDataSource(this.batchDataSource);
		jobExplorerFactoryBean.afterPropertiesSet();
		this.jobExplorer = jobExplorerFactoryBean.getObject();
		this.jobLauncher = createJobLauncher();
		
	}
	
	
}
