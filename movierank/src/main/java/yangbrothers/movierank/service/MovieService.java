package yangbrothers.movierank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import yangbrothers.movierank.api.ApiUtils;
import yangbrothers.movierank.dto.MovieApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.entity.Movie;
import yangbrothers.movierank.repo.MovieRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepo movieRepo;
    private final MovieApiService movieApiService;

    public MovieApiDTO movieList(PageRequestDTO pageRequestDTO) {
        if (StringUtils.hasText(pageRequestDTO.getMovieNm())) {
            List<Movie> movies = movieApiService.movieList(String.valueOf(pageRequestDTO.getPage() + 1), pageRequestDTO.getMovieNm());
            MovieApiDTO movieApiDTO = createMovieApiDTO(movies, pageRequestDTO);
            ApiUtils.makeSuccessResult(movieApiDTO, ApiUtils.MOVIE_LIST_SUCCESS);

            return movieApiDTO;
        }

        List<MovieApiDTO.MovieDTO> movieDTOList = movieRepo.movieList(pageRequestDTO);
        MovieApiDTO movieApiDTO = new MovieApiDTO();
        movieApiDTO.setMovieDTOList(movieDTOList);
        movieApiDTO.setPage(pageRequestDTO.getPage() + 1);
        ApiUtils.makeSuccessResult(movieApiDTO, ApiUtils.MOVIE_LIST_SUCCESS);

        return movieApiDTO;
    }

    private MovieApiDTO createMovieApiDTO(List<Movie> movies, PageRequestDTO pageRequestDTO) {
        MovieApiDTO movieApiDTO = new MovieApiDTO();
        movieApiDTO.setMovieDTOList(movies.stream().map(movie -> new MovieApiDTO.MovieDTO(movie)).collect(Collectors.toList()));
        movieApiDTO.setPage(pageRequestDTO.getPage() + 1);

        return movieApiDTO;
    }
}