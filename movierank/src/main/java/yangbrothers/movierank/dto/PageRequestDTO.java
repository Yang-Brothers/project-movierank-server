package yangbrothers.movierank.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PageRequestDTO {
    int page = 0;
    int size = 10;
    private String movieNm = "";

    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }
}