package yangbrothers.movierank.jwt;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public interface TokenProvider {

    String createToken(Authentication authentication);

    Authentication getAuthentication(String token);

    boolean validateToken(String token);

    Date getExpirationDate(String token);

    String resolveToken(HttpServletRequest request);
}