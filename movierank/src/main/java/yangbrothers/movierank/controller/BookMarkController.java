package yangbrothers.movierank.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yangbrothers.movierank.dto.BookMarkApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.service.BookMarkService;

@RestController
@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
@Api(tags = {"북마크 기능을 제공하는 Controller"})
@Validated
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @PostMapping("/register/{username}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "bookMarkDTO", required = true, dataTypeClass = BookMarkApiDTO.BookMarkDTO.class, paramType = "body")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "북마크 등록 성공"),
            @ApiResponse(code = 400, message = "북마크 등록 실패")
    })
    @ApiOperation(value = "북마크 등록을 지원하는 메소드")
    public ResponseEntity<CommonResult> bookMarkRegister(@PathVariable("username") String username, @RequestBody BookMarkApiDTO.BookMarkDTO bookMarkDTO) {

        return bookMarkService.register(username, bookMarkDTO);
    }

    @GetMapping("/list/{username}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "len", required = true, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "북마크 조회 성공"),
            @ApiResponse(code = 200, message = "북마크 조회 실패")
    })
    @ApiOperation(value = "북마크 조회를 지원하는 메소드")
    public ResponseEntity<BookMarkApiDTO> bookMarkList(@PathVariable("username") String username, @ModelAttribute PageRequestDTO pageRequestDTO) {

        return bookMarkService.list(username, pageRequestDTO);
    }

    @DeleteMapping("/delete/{bookMarkId}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "bookMarkId", required = true, dataType = "Long", paramType = "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "북마크 삭제 성공"),
            @ApiResponse(code = 200, message = "북마크 삭제 실패")
    })
    @ApiOperation(value = "북마크 삭제를 지원하는 메소드")
    public ResponseEntity<CommonResult> bookMarkDelete(@PathVariable("bookMarkId") Long bookMarkId) {

        return bookMarkService.delete(bookMarkId);
    }
}