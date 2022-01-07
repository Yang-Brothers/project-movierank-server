package yangbrothers.movierank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yangbrothers.movierank.api.ApiUtils;
import yangbrothers.movierank.dto.MovieApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.ex.MovieNmNotFoundEx;
import yangbrothers.movierank.repo.MovieRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepo movieRepo;

    public MovieApiDTO movieList(PageRequestDTO pageRequestDTO) {
        List<MovieApiDTO.MovieDTO> movieList = movieRepo.movieList(pageRequestDTO);

        return getMovieApiDTO(movieList);
    }

    public MovieApiDTO movieSearch(PageRequestDTO pageRequestDTO) {
        List<MovieApiDTO.MovieDTO> movieList = movieRepo.movieSearch(pageRequestDTO);

        if (movieList.isEmpty()) {
            throw new MovieNmNotFoundEx("해당되는 제목의 영화가 없습니다.");
        }

        return getMovieApiDTO(movieList);
    }

    private MovieApiDTO getMovieApiDTO(List<MovieApiDTO.MovieDTO> movieList) {
        MovieApiDTO movieApiDTO = new MovieApiDTO();
        movieApiDTO.getData().put("movieList", movieList);
        ApiUtils.makeSuccessResult(movieApiDTO, ApiUtils.SUCCESS_OK);
        return movieApiDTO;
    }
}