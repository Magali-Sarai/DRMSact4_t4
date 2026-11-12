package com.informatingconfig.spring.app1.springboot_applications.security;

import com.informatingconfig.spring.app1.springboot_applications.Models.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(Usuario usuario) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("usuarioId", usuario.getId())
                .claim("nombre", usuario.getNombre())
                .claim("rol", usuario.getRol())
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(getSigningKey())
                .compact();
    }

    public String extraerEmail(String token) {
        return obtenerClaims(token).getSubject();
    }

    public boolean esTokenValido(String token) {
        try {
            Claims claims = obtenerClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}