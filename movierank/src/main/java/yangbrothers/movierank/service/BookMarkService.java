package yangbrothers.movierank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yangbrothers.movierank.api.ApiUtils;
import yangbrothers.movierank.dto.BookMarkApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.entity.BookMark;
import yangbrothers.movierank.entity.User;
import yangbrothers.movierank.repo.BookMarkRepo;
import yangbrothers.movierank.repo.UserRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final UserRepo userRepo;
    private final BookMarkRepo bookMarkRepo;

    public void bookMarkRegister(String username, BookMarkApiDTO.BookMarkDTO bookMarkDTO) {
        User user = userRepo.findUserByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("해당 이름은 갖는 사용자가 없습니다.");
        }
        BookMark bookMark = new BookMark(bookMarkDTO, user);
        bookMarkRepo.save(bookMark);
    }

    public BookMarkApiDTO bookMarkList(String username, PageRequestDTO pageRequestDTO) {
        User user = userRepo.findUserByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("해당 이름은 갖는 사용자가 없습니다.");
        }

        List<BookMarkApiDTO.BookMarkDTO> bookMarkList = bookMarkRepo.bookMarkList(user.getUserId(), pageRequestDTO);
        BookMarkApiDTO bookMarkApiDTO = new BookMarkApiDTO();
        bookMarkApiDTO.getData().add(bookMarkApiDTO);
        ApiUtils.makeSuccessResult(bookMarkApiDTO, ApiUtils.SUCCESS_OK);

        return bookMarkApiDTO;
    }
}