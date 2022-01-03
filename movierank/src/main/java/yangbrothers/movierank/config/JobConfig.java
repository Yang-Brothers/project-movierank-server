package yangbrothers.movierank.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import yangbrothers.movierank.entity.Movie;
import yangbrothers.movierank.service.MovieApiService;

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
    private final ItemStreamWriter itemStreamWriter;

    @Value("${api.key}")
    private String key;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
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
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(1);
        threadPoolExecutor.setMaxPoolSize(1);
        threadPoolExecutor.setThreadNamePrefix("yhw-movieRank-");

        return threadPoolExecutor;
    }

    @Bean
    public Step slaveStep() {
        return stepBuilderFactory.get("slaveStep")
                .<Movie, Movie>chunk(100)
                .reader(itemReader(0, 0))
                .writer(itemStreamWriter)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<? extends Movie> itemReader(
            @Value("#{stepExecutionContext['firstPage']}") int firstPage,
            @Value("#{stepExecutionContext['lastPage']}") int lastPage) {
        ArrayList<Movie> list = new ArrayList<>();
        MovieApiService movieApiService = new MovieApiService(key, "100");

        for (int i = firstPage; i <= lastPage; i++) {
            List<Movie> movies = movieApiService.movieList(String.valueOf(i));
            long startTime = System.currentTimeMillis();
            list.addAll(movies);
            long endTime = System.currentTimeMillis();
            log.info("Thread: {}, runningTime: {}", Thread.currentThread().getName(), endTime - startTime);
        }

        return new ListItemReader<>(list);
    }
}