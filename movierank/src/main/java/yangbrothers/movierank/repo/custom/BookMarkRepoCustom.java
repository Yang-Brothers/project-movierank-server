package yangbrothers.movierank.repo.custom;

import yangbrothers.movierank.dto.request.PageRequestDTO;
import yangbrothers.movierank.dto.response.BookMarkApiDTO;
import yangbrothers.movierank.dto.request.MovieSearchDTO;

import java.util.List;

public interface BookMarkRepoCustom {

    List<BookMarkApiDTO.BookMarkDTO> bookMarkList(Long userId, PageRequestDTO pageRequestDTO);
}
