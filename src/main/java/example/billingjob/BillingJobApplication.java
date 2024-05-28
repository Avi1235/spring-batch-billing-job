package example.billingjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class BillingJobApplication {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Autowired
	@Qualifier("importUserJob")
	private Job job2;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BillingJobApplication.class, args);
		SpringApplication.exit(context, () -> 0);
	}

	@Bean
	public CommandLineRunner run() {
		return (args) -> {
			System.out.println(job.getName());
			JobParameters jobParameters = new JobParametersBuilder()
					.addString("random UUID", UUID.randomUUID().toString()) // Customize job parameters if needed
					.toJobParameters();

			JobExecution execution = jobLauncher.run(job, jobParameters);

			System.out.println("Job Exit Status : " + execution.getStatus());

			JobExecution personJobExecution = jobLauncher.run(job2, jobParameters);

			System.out.println("Job Exit Status : " + personJobExecution.getStatus());
		};
	}
}
