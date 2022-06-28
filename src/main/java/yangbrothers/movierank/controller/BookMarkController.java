package yangbrothers.movierank.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.dto.request.PageRequestDTO;
import yangbrothers.movierank.dto.response.BookMarkApiDTO;
import yangbrothers.movierank.dto.response.BookMarkApiDTO.BookMarkDTO;
import yangbrothers.movierank.service.BookMarkService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
@Api(tags = {"북마크 기능을 제공하는 Controller"})
@Validated
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @PostMapping("/{username}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "bookMarkDTO", required = true, dataTypeClass = BookMarkDTO.class, paramType = "body")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "북마크 등록 성공", response = CommonResult.class),
            @ApiResponse(code = 400, message = "북마크 등록 실패", response = CommonResult.class)
    })
    @ApiOperation(value = "북마크 등록을 지원하는 메소드")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommonResult> register(@PathVariable String username, @RequestBody BookMarkDTO bookMarkDTO) {

        return bookMarkService.register(username, bookMarkDTO);
    }

    @GetMapping("/list")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "len", required = true, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "북마크 조회 성공", response = BookMarkApiDTO.class),
            @ApiResponse(code = 400, message = "북마크 조회 실패", response = CommonResult.class)
    })
    @ApiOperation(value = "북마크 조회를 지원하는 메소드")
    public ResponseEntity<BookMarkApiDTO> list(@RequestParam @NotBlank String username, @ModelAttribute PageRequestDTO pageRequestDTO) {

        return bookMarkService.list(username, pageRequestDTO);
    }

    @DeleteMapping
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "bookmarkid", required = true, dataType = "Long", paramType = "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "북마크 삭제 성공", response = CommonResult.class),
            @ApiResponse(code = 400, message = "북마크 삭제 실패", response = CommonResult.class)
    })
    @ApiOperation(value = "북마크 삭제를 지원하는 메소드")
    public ResponseEntity<CommonResult> delete(@RequestParam("bookmarkid") @NotNull Long bookMarkId) {

        return bookMarkService.delete(bookMarkId);
    }
}