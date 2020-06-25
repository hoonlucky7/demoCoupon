package com.example.demo.common.util;

import com.example.demo.common.ErrorCode;
import com.example.demo.exception.ApiException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private static final String jwtSecret = "cousec";

    public static final int jwtExpirationInMs = 7200000; // 2 hour

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.AUTH_INVALID_CODE);
        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorCode.AUTH_EXPIRED_CODE);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.AUTH_INVALID_CODE);
        }
    }
}