package yangbrothers.movierank.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.dto.request.DailyBoxOfficeSearchDTO;
import yangbrothers.movierank.dto.request.MovieSearchDTO;
import yangbrothers.movierank.dto.request.PageRequestDTO;
import yangbrothers.movierank.dto.response.DailyBoxOfficeApiDTO;
import yangbrothers.movierank.dto.response.MovieApiDTO;
import yangbrothers.movierank.service.MovieApiService;
import yangbrothers.movierank.service.MovieService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/movie")
@RequiredArgsConstructor
@Api(tags = {"영화 조회 기능을 제공하는 Controller"})
public class MovieController {

    private final MovieService movieService;
    private final MovieApiService movieApiService;

    @GetMapping("/searchmovielist")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "len", required = true, dataType = "int", paramType = "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "영화 조회 성공", response = MovieApiDTO.class)
    })
    @ApiOperation(value = "영화 조회를 지원하는 메소드")
    public ResponseEntity<MovieApiDTO> searchMovieList(@Valid @ModelAttribute PageRequestDTO pageRequestDTO) {

        return movieService.searchMovieList(pageRequestDTO);
    }


    @GetMapping("/searchmovieinfo")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "len", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "movieNm", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "영화 검색 성공", response = MovieApiDTO.class),
            @ApiResponse(code = 400, message = "영화 검색 실패", response = CommonResult.class),
            @ApiResponse(code = 404, message = "해당되는 제목의 영화 부재", response = CommonResult.class)
    })
    @ApiOperation(value = "영화 검색를 지원하는 메소드")
    public ResponseEntity<MovieApiDTO> searchMovieInfo(@Valid @ModelAttribute MovieSearchDTO movieSearchDTO) {

        return movieService.searchMovieInfo(movieSearchDTO);
    }

    @GetMapping("/searchdailyboxofficelist")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "len", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "date", required = true, dataType = "date", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "일별 박스오피스 검색 성공", response = DailyBoxOfficeApiDTO.class),
            @ApiResponse(code = 400, message = "일별 박스오피스 검색 실패", response = CommonResult.class)
    })
    @ApiOperation(value = "일별 박스오피스 검색를 지원하는 메소드")
    public ResponseEntity<CommonResult> searchDailyBoxOfficeList(@Valid @ModelAttribute DailyBoxOfficeSearchDTO dailyBoxOfficeSearchDTO) {

        return movieApiService.searchDailyBoxOfficeList(dailyBoxOfficeSearchDTO);
    }
}