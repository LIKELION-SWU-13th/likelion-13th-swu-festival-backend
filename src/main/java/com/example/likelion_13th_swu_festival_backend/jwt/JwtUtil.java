package com.example.likelion_13th_swu_festival_backend.jwt;

import com.example.likelion_13th_swu_festival_backend.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${JWT_SECRET}")
    private String SECRET_KEY;

    //private final long ACCESS_EXP_TIME = 1000L * 60 * 60;  // 1시간
    private final long ACCESS_EXP_TIME = 1000L * 60 * 2;  // 2분
    //private final long REFRESH_EXP_TIME = 1000L * 60 * 60 * 24;  // 24시간

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getStudentNum())
                .claim("name", user.getName())
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        // 현재 시간 기준으로 '다음 날 오전 9시' 고정
//        java.util.Calendar calendar = java.util.Calendar.getInstance();
//        calendar.set(java.util.Calendar.HOUR_OF_DAY, 9);
//        calendar.set(java.util.Calendar.MINUTE, 0);
//        calendar.set(java.util.Calendar.SECOND, 0);
//        calendar.set(java.util.Calendar.MILLISECOND, 0);
//
//        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
//            calendar.add(java.util.Calendar.DATE, 1);
//        }
//
//        Date expireAt = calendar.getTime();

        Date expireAt = new Date(System.currentTimeMillis() + 1000L * 60 * 3); // 3분

        return Jwts.builder()
                .setSubject(user.getStudentNum())
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(expireAt)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }



    public TokenStatus validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String type = claims.get("type", String.class);
            if (!"access".equals(type)) {
                return TokenStatus.INVALID;
            }

            return TokenStatus.AUTHENTICATED;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException e) {
            return TokenStatus.INVALID;
        }
    }


    public TokenStatus validateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String type = claims.get("type", String.class);
            if (!"refresh".equals(type)) {
                return TokenStatus.INVALID;
            }

            return TokenStatus.AUTHENTICATED;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException e) {
            return TokenStatus.INVALID;
        }
    }


    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractUsernameFromRefresh(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
