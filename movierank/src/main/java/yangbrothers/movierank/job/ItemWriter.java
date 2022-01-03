package yangbrothers.movierank.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.stereotype.Component;
import yangbrothers.movierank.entity.Movie;
import yangbrothers.movierank.repo.MovieRepo;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemWriter implements ItemStreamWriter<Movie> {

    private final MovieRepo movieRepo;

    @Override
    public void write(List<? extends Movie> items) throws Exception {
        long startTime = System.currentTimeMillis();
        movieRepo.saveAll(items);
        long endTime = System.currentTimeMillis();
        log.info("Thread: {}, runningTime: {}", Thread.currentThread().getName(), endTime - startTime);
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
