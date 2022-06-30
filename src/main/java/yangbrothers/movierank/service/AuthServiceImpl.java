package yangbrothers.movierank.service;

import com.mchange.util.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.dto.request.LoginDTO;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.dto.response.SignUpResponseDTO;
import yangbrothers.movierank.dto.response.TokenDTO;
import yangbrothers.movierank.entity.Member;
import yangbrothers.movierank.ex.SignUpEx;
import yangbrothers.movierank.jwt.JwtFilter;
import yangbrothers.movierank.jwt.TokenProvider;
import yangbrothers.movierank.repo.UserRepo;
import yangbrothers.movierank.util.ApiUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final StringRedisTemplate redisTemplate;

    @Override
    @Transactional
    public ResponseEntity<SignUpResponseDTO> signUp(SignUpDTO signUpDTO) throws AlreadyExistsException {
        if (userRepo.findUserByNickName(signUpDTO.getNickName()).orElse(null) != null) {
            throw new AlreadyExistsException("이미 존재하는 아이디입니다.");
        }
        if (!signUpDTO.getPassword().equals(signUpDTO.getPasswordConfirm())) {
            throw new SignUpEx("비밀번호가 일치하지 않습니다.", signUpDTO);
        }

        userRepo.save(new Member(signUpDTO, passwordEncoder));

        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO(signUpDTO.getUsername(), signUpDTO.getNickName());
        ApiUtil.makeSuccessResult(signUpResponseDTO, ApiUtil.SUCCESS_CREATED);

        return new ResponseEntity<>(signUpResponseDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TokenDTO> login(LoginDTO loginDTO) {
        Authentication authentication = null;

        try {
            authentication = getAuthentication(loginDTO);
        } catch (BadCredentialsException ex) {
            log.info("[AuthService][login][BadCredentialsException]username: {}", loginDTO.getNickName());
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        TokenDTO tokenDTO = new TokenDTO(loginDTO.getNickName(), "Bearer " + jwt);
        ApiUtil.makeSuccessResult(tokenDTO, ApiUtil.SUCCESS_OK);

        return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CommonResult> logout(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        Date expirationDate = tokenProvider.getExpirationDate(token);
        redisTemplate.opsForValue().set(token, "logout", expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        CommonResult successResult = ApiUtil.getSuccessResult(ApiUtil.SUCCESS_OK);

        return new ResponseEntity<>(successResult, HttpStatus.OK);
    }

    private Authentication getAuthentication(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getNickName(), loginDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}