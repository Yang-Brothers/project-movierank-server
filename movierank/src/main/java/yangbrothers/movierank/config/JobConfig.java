package yangbrothers.movierank.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import yangbrothers.movierank.entity.Movie;
import yangbrothers.movierank.job.CustomItemWriter;
import yangbrothers.movierank.job.CustomStepListener;
import yangbrothers.movierank.service.MovieApiService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
@Slf4j
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final Partitioner partitioner;
    private final CustomStepListener stepExecutionListener;
    private final CustomItemWriter customItemWriter;
    private final EntityManager entityManager;
    private final MovieApiService movieApiService;

    @Value("${api.secret}")
    private String key;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .start(masterStep())
                .build();
    }

    @Bean
    public Step masterStep() {
        return stepBuilderFactory.get("masterStep")
                .partitioner("slaveStep", partitioner)
                .step(slaveStep())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step slaveStep() {
        return stepBuilderFactory.get("slaveStep")
                .<Movie, Movie>chunk(500)
                .reader(itemReader(0, 0, 0, 0))
                .writer(customItemWriter)
                .listener(stepExecutionListener)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<? extends Movie> itemReader(
            @Value("#{stepExecutionContext['firstPage']}") long firstPage,
            @Value("#{stepExecutionContext['lastPage']}") long lastPage,
            @Value("#{stepExecutionContext['firstIndex']}") long firstIndex,
            @Value("#{stepExecutionContext['lastIndex']}") long lastIndex) {
        ArrayList<Movie> list = new ArrayList<>();

        for (long i = firstPage; i <= lastPage; i++) {
            long startTime = System.currentTimeMillis();
            List<Movie> movies = movieApiService.movieList(String.valueOf(i), null, "100");
            long endTime = System.currentTimeMillis();
            log.info("Thread: {}, runningTime: {}", Thread.currentThread().getName(), endTime - startTime);
            list.addAll(movies);
        }

        indexing(firstIndex, list);
        deleteQuery(firstIndex, lastIndex);

        return new ListItemReader<>(list);
    }

    private void indexing(long firstIndex, ArrayList<Movie> list) {
        long i = firstIndex + 1;
        for (Movie movie : list) {
            movie.setId(i++);
        }
    }

    private void deleteQuery(long firstIndex, long lastIndex) {
        Query query = entityManager.createQuery("DELETE FROM Movie m where m.id > :firstIndex and m.id < :lastIndex");
        int deleteCount = query.setParameter("firstIndex", firstIndex).setParameter("lastIndex", lastIndex).executeUpdate();
        log.info("deleteCount: {}", deleteCount);
    }

    @Bean(name = "myTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(1);
        threadPoolExecutor.setMaxPoolSize(1);
        threadPoolExecutor.setQueueCapacity(100);
        threadPoolExecutor.setThreadNamePrefix("yhw-movieRank-");

        return threadPoolExecutor;
    }
}