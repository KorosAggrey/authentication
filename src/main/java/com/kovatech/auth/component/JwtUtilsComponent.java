package com.kovatech.auth.component;

import com.kovatech.auth.config.MsConfigProperties;
import com.kovatech.auth.core.enums.WsProcessLogger;
import com.kovatech.auth.core.logging.WsLogManager;
import com.kovatech.auth.datalayer.entities.User;
import com.kovatech.auth.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtilsComponent {
    private final MsConfigProperties config;

    public JwtUtilsComponent(MsConfigProperties config) {
        this.config = config;
    }

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(config.getStaticIv().getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", Role.ROLE_USER);
        claims.put("id",user.getPublicId());
        claims.put("email", user.getEmail());
        return doGenerateToken(claims, user);
    }

    private String doGenerateToken(Map<String, Object> claims, User user) {
        Long expirationTimeLong = Long.parseLong(config.getJwtExpirationTime()); //in second
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

        return Jwts.builder()
                .setId(user.getPublicId())
                .setClaims(claims)
                .setSubject(user.getFirstName())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
