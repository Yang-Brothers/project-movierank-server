package yangbrothers.movierank.job;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yangbrothers.movierank.quartz.BatchScheduledJob;

@SpringBatchTest
@SpringBootTest
class JobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    Job job;

    @Autowired
    BatchScheduledJob batchScheduledJob;


    @Test
    @DisplayName("JobIntegrationTest1")
    public void jobIntegrationTest1() throws Exception {
        jobLauncherTestUtils.launchJob();
    }

    @Test
    @DisplayName("JobIntegrationTest2")
    public void jobIntegrationTest2() throws Exception {
        jobLauncherTestUtils.launchJob();
    }

}