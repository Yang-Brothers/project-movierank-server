package yangbrothers.movierank.service;

import org.springframework.security.core.Authentication;
import yangbrothers.movierank.jwt.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class SpyTokenProvider implements TokenProvider {
    @Override
    public String createToken(Authentication authentication) {
        return null;
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
