package yangbrothers.movierank.controller;

import org.springframework.http.ResponseEntity;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.dto.request.PageRequestDTO;
import yangbrothers.movierank.dto.response.BookMarkApiDTO;
import yangbrothers.movierank.service.BookMarkService;

public class SpyBookMarkService implements BookMarkService {
    @Override
    public ResponseEntity<CommonResult> register(String username, BookMarkApiDTO.BookMarkDTO bookMarkDTO) {
        return null;
    }

    @Override
    public ResponseEntity<BookMarkApiDTO> list(String username, PageRequestDTO pageRequestDTO) {
        return null;
    }

    @Override
    public ResponseEntity<CommonResult> delete(Long bookMarkId) {
        return null;
    }
}
