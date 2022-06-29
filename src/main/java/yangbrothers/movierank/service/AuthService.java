package yangbrothers.movierank.service;

import com.mchange.util.AlreadyExistsException;
import org.springframework.http.ResponseEntity;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.dto.request.LoginDTO;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.dto.response.SignUpResponseDTO;
import yangbrothers.movierank.dto.response.TokenDTO;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    ResponseEntity<SignUpResponseDTO> signUp(SignUpDTO signUpDTO) throws AlreadyExistsException;

    ResponseEntity<TokenDTO> login(LoginDTO loginDTO);

    ResponseEntity<CommonResult> logout(HttpServletRequest request);
}