package yangbrothers.movierank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import yangbrothers.movierank.dto.MovieApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.ex.MovieNmNotFoundEx;
import yangbrothers.movierank.repo.MovieRepo;
import yangbrothers.movierank.util.ApiUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepo movieRepo;

    public ResponseEntity<MovieApiDTO> list(PageRequestDTO pageRequestDTO) {

        return new ResponseEntity<>(getMovieApiDTO(movieRepo.movieList(pageRequestDTO)), HttpStatus.OK);
    }

    public ResponseEntity<MovieApiDTO> search(PageRequestDTO pageRequestDTO) {
        List<MovieApiDTO.MovieDTO> movieList = movieRepo.movieSearch(pageRequestDTO);

        if (movieList.isEmpty()) {
            throw new MovieNmNotFoundEx("해당되는 제목의 영화가 없습니다.");
        }

        return new ResponseEntity<>(getMovieApiDTO(movieList), HttpStatus.OK);
    }

    private MovieApiDTO getMovieApiDTO(List<MovieApiDTO.MovieDTO> movieList) {
        MovieApiDTO movieApiDTO = new MovieApiDTO();
        movieApiDTO.getData().put("movieList", movieList);
        ApiUtil.makeSuccessResult(movieApiDTO, ApiUtil.SUCCESS_OK);
        return movieApiDTO;
    }
}