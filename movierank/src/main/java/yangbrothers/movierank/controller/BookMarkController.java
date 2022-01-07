package yangbrothers.movierank.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import yangbrothers.movierank.api.ApiUtils;
import yangbrothers.movierank.dto.BookMarkApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.service.BookMarkService;

@RestController
@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
@Api(tags = {"북마크 기능을 제공하는 Controller"})
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @PostMapping("/register/{username}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "bookMarkDTO", required = true, dataTypeClass = BookMarkApiDTO.BookMarkDTO.class, paramType = "body")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "북마크 등록 성공")
    })
    @ApiOperation(value = "북마크 등록을 지원하는 메소드")
    public CommonResult bookMarkRegister(@PathVariable String username, @RequestBody BookMarkApiDTO.BookMarkDTO bookMarkDTO) {
        bookMarkService.bookMarkRegister(username, bookMarkDTO);
        CommonResult commonResult = new CommonResult();
        ApiUtils.makeSuccessResult(commonResult, ApiUtils.SUCCESS_CREATED);

        return commonResult;
    }

    @GetMapping("/list/{username}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "pageRequestDTO", required = true, dataTypeClass = PageRequestDTO.class, paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "북마크 조회 성공")
    })
    @ApiOperation(value = "북마크 조회를 지원하는 메소드")
    public BookMarkApiDTO bookMarkList(@PathVariable String username, @ModelAttribute PageRequestDTO pageRequestDTO) {

        return bookMarkService.bookMarkList(username, pageRequestDTO);
    }
}