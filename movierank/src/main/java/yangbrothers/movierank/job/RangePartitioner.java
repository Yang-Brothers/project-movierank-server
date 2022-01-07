package yangbrothers.movierank.job;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RangePartitioner implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        HashMap<String, ExecutionContext> result = new HashMap<>();
        long firstPage = 1;
        long lastPage = 25;
        int count = 0;

        while (lastPage <= 100) {
            long firstIndex = (firstPage - 1) * 100;
            long lastIndex = firstIndex + 2501;
            ExecutionContext executionContext = new ExecutionContext();
            result.put("partition " + count++, executionContext);

            executionContext.putLong("firstPage", firstPage);
            executionContext.putLong("lastPage", lastPage);
            executionContext.putLong("firstIndex", firstIndex);
            executionContext.putLong("lastIndex", lastIndex);

            firstPage += 25;
            lastPage += 25;
        }

        return result;
    }
}