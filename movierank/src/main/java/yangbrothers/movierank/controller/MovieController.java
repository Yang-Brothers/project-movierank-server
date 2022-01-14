package yangbrothers.movierank.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yangbrothers.movierank.dto.MovieApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.service.MovieService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
@Api(tags = {"영화 조회 기능을 제공하는 Controller"})
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/list")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "len", required = true, dataType = "int", paramType = "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "영화 조회 성공")
    })
    @ApiOperation(value = "영화 조회를 지원하는 메소드")
    public ResponseEntity<MovieApiDTO> movieList(@Valid @ModelAttribute PageRequestDTO pageRequestDTO) {

        return movieService.list(pageRequestDTO);
    }


    @GetMapping("/search")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "len", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "movieNm", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "영화 검색 성공"),
            @ApiResponse(code = 400, message = "영화 검색 실패"),
            @ApiResponse(code = 404, message = "해당되는 제목의 영화 부재")
    })
    @ApiOperation(value = "영화 검색를 지원하는 메소드")
    public ResponseEntity<MovieApiDTO> movieSearch(@Valid @ModelAttribute PageRequestDTO pageRequestDTO) {

        return movieService.search(pageRequestDTO);
    }
}