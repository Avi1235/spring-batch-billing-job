package example.billingjob.job;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.JobRepository;

import java.util.UUID;

public class BillingJob implements Job {
    private JobRepository jobRepository;

    public BillingJob(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

//    public String getName() {
//        return UUID.randomUUID() + " BillingJob";
//    }
    public String getName() {
        return "BillingJob";
    }


    @Override
    public void execute(JobExecution execution) {

        try {
            JobParameters jobParameters = execution.getJobParameters();
            String inputFile = jobParameters.getString("input.file");
            System.out.println("processing billing information from file " + inputFile);
            execution.setStatus(BatchStatus.COMPLETED);
            execution.setExitStatus(ExitStatus.COMPLETED);
            execution.setExitStatus(ExitStatus.COMPLETED.addExitDescription("Successfully processed billing information"));
        } catch (Exception e) {
            execution.addFailureException(e);
            execution.setStatus(BatchStatus.COMPLETED);
            execution.setExitStatus(ExitStatus.FAILED.addExitDescription(e.getMessage()));
        } finally {
            this.jobRepository.update(execution);
        }
    }
}