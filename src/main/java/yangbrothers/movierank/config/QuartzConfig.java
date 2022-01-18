package yangbrothers.movierank.config;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yangbrothers.movierank.Quartz.BatchScheduledJob;

@Configuration
public class QuartzConfig {

    @Value("${quartz.time}")
    private String time;

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
                .withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
    }
}