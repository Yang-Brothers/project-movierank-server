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
        int firstPage = 1;
        int lastPage = 25;
        int count = 0;

        while (lastPage <= 100) {
            int firstIndex = (firstPage - 1) * 100;
            int lastIndex = firstIndex + 2501;
            ExecutionContext executionContext = new ExecutionContext();
            result.put("partition " + count++, executionContext);

            executionContext.putInt("firstPage", firstPage);
            executionContext.putInt("lastPage", lastPage);
            executionContext.putInt("firstIndex", firstIndex);
            executionContext.putInt("lastIndex", lastIndex);

            firstPage += 25;
            lastPage += 25;
        }

        return result;
    }
}