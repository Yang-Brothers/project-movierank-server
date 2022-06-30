package yangbrothers.movierank.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class SpyAuthenticationManager implements AuthenticationManager {

    public boolean isPasswordNotMath;
    public Authentication authenticate_returnValue;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (isPasswordNotMath) {
            isPasswordNotMath = false;
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return authenticate_returnValue;
    }
}
