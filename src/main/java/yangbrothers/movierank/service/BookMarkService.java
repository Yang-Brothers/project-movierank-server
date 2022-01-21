package yangbrothers.movierank.service;

import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.dto.request.PageRequestDTO;
import yangbrothers.movierank.dto.response.BookMarkApiDTO;
import yangbrothers.movierank.entity.BookMark;
import yangbrothers.movierank.entity.User;
import yangbrothers.movierank.repo.BookMarkRepo;
import yangbrothers.movierank.repo.UserRepo;
import yangbrothers.movierank.util.ApiUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final UserRepo userRepo;
    private final BookMarkRepo bookMarkRepo;
    private final DozerBeanMapper dozerBeanMapper;

    public ResponseEntity<CommonResult> register(String username, BookMarkApiDTO.BookMarkDTO bookMarkDTO) {
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 이름은 갖는 사용자가 없습니다."));

        BookMark bookMark = dozerBeanMapper.map(bookMarkDTO, BookMark.class);
        bookMark.createUser(user);

        bookMarkRepo.save(bookMark);
        CommonResult commonResult = new CommonResult();
        ApiUtil.makeSuccessResult(commonResult, ApiUtil.SUCCESS_CREATED);

        return new ResponseEntity<>(commonResult, HttpStatus.CREATED);
    }

    public ResponseEntity<BookMarkApiDTO> list(String username, PageRequestDTO pageRequestDTO) {
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 이름은 갖는 사용자가 없습니다."));

        List<BookMarkApiDTO.BookMarkDTO> bookMarkList = bookMarkRepo.bookMarkList(user.getUserId(), pageRequestDTO);
        BookMarkApiDTO bookMarkApiDTO = new BookMarkApiDTO();
        bookMarkApiDTO.getData().put("bookMarkList", bookMarkList);
        ApiUtil.makeSuccessResult(bookMarkApiDTO, ApiUtil.SUCCESS_OK);

        return new ResponseEntity<>(bookMarkApiDTO, HttpStatus.OK);
    }

    public ResponseEntity<CommonResult> delete(Long bookMarkId) {
        bookMarkRepo.deleteById(bookMarkId);
        CommonResult successResult = ApiUtil.getSuccessResult(ApiUtil.SUCCESS_OK);

        return new ResponseEntity<>(successResult, HttpStatus.OK);
    }
}