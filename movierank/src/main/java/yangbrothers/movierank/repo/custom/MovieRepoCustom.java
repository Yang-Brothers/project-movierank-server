package yangbrothers.movierank.repo.custom;

import yangbrothers.movierank.dto.MovieApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;

import java.util.List;

public interface MovieRepoCustom {

    List<MovieApiDTO.MovieDTO> movieList(PageRequestDTO pageRequestDTO);
}
