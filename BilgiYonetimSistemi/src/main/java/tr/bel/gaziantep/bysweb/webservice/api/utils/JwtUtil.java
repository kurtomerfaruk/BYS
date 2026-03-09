package tr.bel.gaziantep.bysweb.webservice.api.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 18.02.2026 09:34
 */
public class JwtUtil {
    private static final String SECRET = "g81slBVGmPXu2OovEgKQKiDhKNjaXjIsqL0glsUqk/k=";

    public static String generateToken(String appKey) {
        return Jwts.builder()
                .setSubject(appKey)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(
                        Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public static String validate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
