package yangbrothers.movierank.service;

import org.springframework.security.core.Authentication;
import yangbrothers.movierank.jwt.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class SpyTokenProvider implements TokenProvider {

    public String createToken_returnValue;
    public Authentication createToken_argument;

    @Override
    public String createToken(Authentication authentication) {
        this.createToken_argument = authentication;
        return createToken_returnValue;
    }

    @Override
    public Authentication getAuthentication(String token) {
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public Date getExpirationDate(String token) {
        return null;
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        return null;
    }
}
