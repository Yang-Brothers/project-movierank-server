package yangbrothers.movierank.job;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RangePartitioner implements Partitioner {

    @Value("${batch.job.firstPage}")
    private int firstPage;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        HashMap<String, ExecutionContext> result = new HashMap<>();
        int count = 1;

        while (count < 5) {
            int lastPage = firstPage + 4;
            int firstIndex = firstPage * 500;
            int lastIndex = firstIndex + 2499;

            ExecutionContext executionContext = new ExecutionContext();
            result.put("partition " + count++, executionContext);

            executionContext.putInt("firstPage", firstPage);
            executionContext.putInt("lastPage", lastPage);
            executionContext.putInt("firstIndex", firstIndex);
            executionContext.putInt("lastIndex", lastIndex);

            firstPage += 5;
        }

        return result;
    }
}