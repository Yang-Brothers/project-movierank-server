package yangbrothers.movierank.repo.custom;

import yangbrothers.movierank.dto.request.PageRequestDTO;
import yangbrothers.movierank.dto.response.MovieApiDTO;
import yangbrothers.movierank.dto.request.MovieSearchDTO;

import java.util.List;

public interface MovieRepoCustom {

    List<MovieApiDTO.MovieDTO> movieList(PageRequestDTO pageRequestDTO);

    List<MovieApiDTO.MovieDTO> movieSearch(MovieSearchDTO movieSearchDTO);
}
