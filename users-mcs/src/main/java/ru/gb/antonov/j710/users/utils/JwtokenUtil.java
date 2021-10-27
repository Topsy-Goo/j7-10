package ru.gb.antonov.j710.users.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.gb.antonov.j710.monolith.Factory.JWT_PAYLOAD_ROLES;

@Component
public class JwtokenUtil
{
    @Value("${jwt.secret}") //< ссылка на проперти-файл
    private String secret;  //< обычно выносится в к-л конфиг (см. yaml)

    @Value("${jwt.lifetime}")
    private Integer lifetime;   //< обычно выносится в к-л конфиг (см. yaml)

    public String generateJWToken (UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails
                                .getAuthorities()
                                .stream()
                                .map (GrantedAuthority::getAuthority)
                                .collect (Collectors.toList());

        claims.put (JWT_PAYLOAD_ROLES, roles);
        Date dateIssued = new Date();
        Date dateExpired = new Date (dateIssued.getTime() + lifetime);
        return Jwts.builder()
                   .setClaims (claims)
                   .setSubject (userDetails.getUsername())
                   .setIssuedAt (dateIssued)
                   .setExpiration (dateExpired)
                   .signWith (SignatureAlgorithm.HS256, secret)
                   .compact();
    }

/** Сейчас используется для получения инф-ции о юзере для его л/к. */
    public String getLoginFromToken (String jwt)
    {   return getClaimFromToken(jwt, Claims::getSubject);
    }

/** Сейчас используется для получения инф-ции о юзере для его л/к. */
    private <T> T getClaimFromToken (String jwt, Function<Claims, T> claimsResolver)
    {
        Claims claims = getAllClaimsFromToken (jwt);
        return claimsResolver.apply (claims);
    }

/** Сейчас используется для получения инф-ции о юзере для его л/к. */
    private Claims getAllClaimsFromToken(String jwt)
    {
        return Jwts.parser()
                   .setSigningKey (secret)
                   .parseClaimsJws (jwt)
                   .getBody();
    }

    public List<String> getRoles (String jwt)
    {   return getClaimFromToken (jwt, claims -> claims.get (JWT_PAYLOAD_ROLES, List.class));
    }
}
