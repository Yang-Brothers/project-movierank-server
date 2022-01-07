package yangbrothers.movierank.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import yangbrothers.movierank.dto.MovieApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.ex.MovieNmRequiredEx;
import yangbrothers.movierank.service.MovieService;

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
            @ApiImplicitParam(name = "movieNm", required = false, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "영화 조회 성공")
    })
    @ApiOperation(value = "영화 조회를 지원하는 메소드")
    public MovieApiDTO movieList(@ModelAttribute PageRequestDTO pageRequestDTO) {
        return movieService.movieList(pageRequestDTO);
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
    public MovieApiDTO movieSearch(@ModelAttribute PageRequestDTO pageRequestDTO) {
        if (!StringUtils.hasText(pageRequestDTO.getMovieNm())) {
            throw new MovieNmRequiredEx("영화 이름을 입력해 주세요.");
        }

        return movieService.movieSearch(pageRequestDTO);
    }


}