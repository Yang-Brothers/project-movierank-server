package yangbrothers.movierank.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import yangbrothers.movierank.entity.Movie;
import yangbrothers.movierank.repo.MovieRepo;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomItemWriter implements ItemStreamWriter<Movie> {

    private final MovieRepo movieRepo;

    @Override
    public void write(List<? extends Movie> items) throws Exception {
        log.info("Thread: {}", Thread.currentThread().getName());
        movieRepo.saveAll(items);
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }
}