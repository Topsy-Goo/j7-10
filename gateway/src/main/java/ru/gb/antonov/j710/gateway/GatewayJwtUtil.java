package ru.gb.antonov.j710.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/** Этот бин будет участвовать в фильтрации всех сообщений, проходящих через ворота. */
@Component
public class GatewayJwtUtil
{
    @Value("${jwt.secret}") private String secret;

    public Claims getAllClaimsFromJWToken (String jwt)
    {
    //(Во время парсинга происходит проверка корректности токена. Если токен окажется поддельным, то будет брошено исключение. Проверка срока годности токена при этом не происходит.)
        return Jwts.parser()
                   .setSigningKey (secret)  //< нужет для проверки подлинности токена
                   .parseClaimsJws (jwt)
                   .getBody();
    }

    public boolean isJwtValid (String jwt)
    {   return (jwt != null) && getAllClaimsFromJWToken (jwt).getExpiration().after (new Date());
    }
}
