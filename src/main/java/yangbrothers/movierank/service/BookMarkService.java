package yangbrothers.movierank.service;

import org.springframework.http.ResponseEntity;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.dto.request.PageRequestDTO;
import yangbrothers.movierank.dto.response.BookMarkApiDTO;

public interface BookMarkService {

    ResponseEntity<CommonResult> register(String username, BookMarkApiDTO.BookMarkDTO bookMarkDTO);

    ResponseEntity<BookMarkApiDTO> list(String username, PageRequestDTO pageRequestDTO);

    ResponseEntity<CommonResult> delete(Long bookMarkId);
}
