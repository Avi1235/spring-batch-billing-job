package example.billingjob;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.jdbc.Sql;

@SpringBatchTest
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class BillingJobApplicationTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@Autowired
	@Qualifier("importUserJob")
	private Job importUserJob;

	@BeforeEach
	public void setUp() {
		this.jobRepositoryTestUtils.removeJobExecutions();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testBillingJobExecution(CapturedOutput output) throws Exception {
		// given
		JobParameters jobParameters = this.jobLauncherTestUtils.getUniqueJobParametersBuilder()
				.addString("input.file", "/some/input/file")
				.toJobParameters();

		// when
		//consumes primary
		JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

		// then
		Assertions.assertTrue(output.getOut().contains("processing billing information from file /some/input/file"));
		Assertions.assertEquals(ExitStatus.COMPLETED.addExitDescription("Successfully processed billing information"), jobExecution.getExitStatus());
	}

	@Test
	@Sql("/schema-test.sql")
	void testPersonJobExecution(CapturedOutput output) throws Exception {
		this.jobLauncherTestUtils.setJob(importUserJob);

		// when
		System.out.println(this.jobLauncherTestUtils.getJob().getName());
		JobExecution jobExecution = this.jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertTrue(output.getOut().contains("Converting (Person[firstName=Jill, lastName=Doe]) into (Person[firstName=JILL, lastName=DOE])"));
		Assertions.assertEquals(ExitStatus.COMPLETED.addExitDescription("Successfully processed person transformation"), jobExecution.getExitStatus());
	}

}
