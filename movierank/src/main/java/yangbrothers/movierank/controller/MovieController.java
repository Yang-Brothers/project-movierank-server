package yangbrothers.movierank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yangbrothers.movierank.dto.MovieApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.service.MovieService;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/list")
    public MovieApiDTO movies(@RequestBody PageRequestDTO pageRequestDTO) {
        return movieService.movieList(pageRequestDTO);
    }

}