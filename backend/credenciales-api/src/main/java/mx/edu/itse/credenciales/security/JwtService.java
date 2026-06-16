package mx.edu.itse.credenciales.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key =
            Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generarToken(
        String username,
        String rol
) {

    return Jwts.builder()
            .setSubject(username)
            .claim("rol", rol)
            .setIssuedAt(new Date())
            .setExpiration(
                    new Date(
                            System.currentTimeMillis()
                                    + 86400000
                    )
            )
            .signWith(key)
            .compact();
}

    public String extraerUsername(String token) {

        Claims claims =
                Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token)
                        .getBody();

        return claims.getSubject();
    }

    public boolean validarToken(String token) {

        try {

            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public String extraerRol(
        String token
) {

    Claims claims =
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    return claims.get("rol", String.class);
}
}