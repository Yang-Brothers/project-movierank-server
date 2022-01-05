package yangbrothers.movierank.repo.custom;

import yangbrothers.movierank.dto.BookMarkApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;

import java.util.List;

public interface BookMarkRepoCustom {

    List<BookMarkApiDTO.BookMarkDTO> bookMarkList(Long userId, PageRequestDTO pageRequestDTO);
}
