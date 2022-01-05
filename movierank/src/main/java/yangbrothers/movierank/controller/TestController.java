package yangbrothers.movierank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import yangbrothers.movierank.entity.Movie;
import yangbrothers.movierank.repo.MovieRepo;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MovieRepo movieRepo;

    @GetMapping("/test")
    public Movie test() {
        Optional<Movie> movieByIndex = movieRepo.findMovieByIndex(200);
        Movie movie = movieByIndex.orElse(null);
        return movie;
    }
}
