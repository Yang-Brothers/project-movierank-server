package yangbrothers.movierank.service;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.dto.response.BookMarkApiDTO;
import yangbrothers.movierank.entity.BookMark;
import yangbrothers.movierank.entity.User;
import yangbrothers.movierank.repo.BookMarkRepo;
import yangbrothers.movierank.repo.UserRepo;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookMarkServiceTest {

    @Mock
    UserRepo userRepo;

    @Mock
    BookMarkRepo bookMarkRepo;

    @Spy
    DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

    @InjectMocks
    BookMarkService bookMarkService;


    @Test
    @DisplayName("registerFailTest")
    public void registerFailTest() throws Exception {
        given(userRepo.findUserByUsername(any(String.class))).willThrow(UsernameNotFoundException.class);
        assertThatThrownBy(() -> bookMarkService.register("test", any())).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("registerTest")
    public void registerTest() throws Exception {
        User user = new User(new SignUpDTO("test", "test", "test"), new BCryptPasswordEncoder());
        given(userRepo.findUserByUsername(anyString())).willReturn(Optional.ofNullable(user));
        given(bookMarkRepo.save(any(BookMark.class))).willReturn(any(BookMark.class));

        ResponseEntity<CommonResult> response = bookMarkService.register("test", new BookMarkApiDTO.BookMarkDTO("test", "test",
                "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isInstanceOf(CommonResult.class);
        assertThat(response.getBody().getValidationMsg()).isNullOrEmpty();
    }

}