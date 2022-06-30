package yangbrothers.movierank.controller;

import com.mchange.util.AlreadyExistsException;
import org.springframework.http.ResponseEntity;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.dto.request.LoginDTO;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.dto.response.SignUpResponseDTO;
import yangbrothers.movierank.dto.response.TokenDTO;
import yangbrothers.movierank.entity.Member;
import yangbrothers.movierank.ex.SignUpEx;
import yangbrothers.movierank.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class SpyAuthService implements AuthService {

    public SignUpDTO signUp_argument;
    public ResponseEntity<SignUpResponseDTO> signUp_returnValue;
    public Optional<Member> userRepo_findUserByNickName = Optional.empty();
    public LoginDTO login_argument;
    public ResponseEntity<TokenDTO> login_returnValue;

    @Override
    public ResponseEntity<SignUpResponseDTO> signUp(SignUpDTO signUpDTO) throws AlreadyExistsException {
        if (userRepo_findUserByNickName.orElse(null) != null) {
            userRepo_findUserByNickName = Optional.empty();
            throw new AlreadyExistsException("이미 존재하는 아이디입니다.");
        }
        if (!signUpDTO.getPassword().equals(signUpDTO.getPasswordConfirm())) {
            throw new SignUpEx("비밀번호가 일치하지 않습니다.", signUpDTO);
        }

        this.signUp_argument = signUpDTO;
        return signUp_returnValue;
    }

    @Override
    public ResponseEntity<TokenDTO> login(LoginDTO loginDTO) {
        this.login_argument = loginDTO;
        return login_returnValue;
    }

    @Override
    public ResponseEntity<CommonResult> logout(HttpServletRequest request) {
        return null;
    }
}
