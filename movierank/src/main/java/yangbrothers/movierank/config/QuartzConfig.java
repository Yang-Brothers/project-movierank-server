package yangbrothers.movierank.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yangbrothers.movierank.Quartz.BatchScheduledJob;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(BatchScheduledJob.class)
                .storeDurably()
                .withIdentity("BatchJob")
                .build();
    }

    @Bean
    public Trigger jobTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity("Trigger")
                .forJob(jobDetail())
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();
    }
}