package yangbrothers.movierank.controller;

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
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @PostMapping("/register/{username}")
    public CommonResult registerBookMark(@PathVariable String username, @RequestBody BookMarkApiDTO.BookMarkDTO bookMarkDTO) {
        bookMarkService.registerBookMark(username, bookMarkDTO);
        CommonResult commonResult = new CommonResult();
        ApiUtils.makeSuccessResult(commonResult, ApiUtils.BOOKMARK_REGISTER_SUCCESS);

        return commonResult;
    }

    @GetMapping("/list/{username}")
    public BookMarkApiDTO bookMarkList(@PathVariable String username, @RequestBody PageRequestDTO pageRequestDTO) {

        return bookMarkService.bookMarkList(username, pageRequestDTO);
    }
}