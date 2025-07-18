package finalmission.common.ui;

import finalmission.entity.Customer;
import finalmission.common.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private final long EXPIRATION_TIME = 60 * 60 * 1000;
    private final String SECRET_KEY = "pa4jLq6Kjb7vGZCDrA72YiD6UZNBtCkB=";


    public String createToken(Customer customer) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(customer.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Long getSubjectFromToken(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("토큰이 만료되었습니다");
        } catch (JwtException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다");
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("토큰에서 사용자 ID를 해석할 수 없습니다");
        }
    }
}

