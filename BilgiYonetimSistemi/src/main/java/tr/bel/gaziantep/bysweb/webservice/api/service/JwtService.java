package tr.bel.gaziantep.bysweb.webservice.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 18.02.2026 14:06
 */
public class JwtService {

    private static final String SECRET = "VeryVerySecretKeyForJwtSigningMustBeLongEnough123!";
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 saat

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String generateToken(Integer apiUserId, String appKey) {

        return Jwts.builder()
                .setSubject(appKey)
                .claim("apiUserId", apiUserId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
