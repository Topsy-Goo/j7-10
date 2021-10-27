package ru.gb.antonov.j710.gateway;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

import static ru.gb.antonov.j710.gateway.GatewayApp.*;

@Component
public class GatewayAuthFilter extends AbstractGatewayFilterFactory<GatewayAuthFilter.Config>
{
    public static final int bearerLength = BEARER_.length();
    @Autowired private GatewayJwtUtil gatewayJwtUtil;

    public static class Config {}
//-----------------------------------------------------------------------------
    public GatewayAuthFilter() { super(Config.class); }
//-----------------------------------------------------------------------------
/** Метод добавляет заголовок во внутернний запрос при маршрутизации внешних запросов во внутренние.<p>
Добавление заголовка возможно только для авторизованных пользователей. Для гостей заголовок не добавляется.<p>
Если JWT просрочен, но метод возвращает ошибку, в результате чего пользователь не может пользоваться
магазином.<p>
Добавляемый заголовок содержит поле {@code "username"}, содержащее логин авторизованного пользователя, и поле
 {@code "roles"}, содержащее строковый массив названий ролей и разрешений, присвоенных пользователю. */
    @Override public GatewayFilter apply (Config config)
    {
        return (srvrWebExchange, filterChain) ->
        {
            ServerHttpRequest srvrHttpRequest = srvrWebExchange.getRequest();
            final String jwt = jwtFromServerHttpRequest (srvrHttpRequest);
            if (jwt != null)
            {
                if (!gatewayJwtUtil.isJwtValid (jwt))
                    return onError (srvrWebExchange, "Некорректный Authorization.token.");

                populateRequestWithHeaders (srvrWebExchange, jwt);
            }
            return filterChain.filter (srvrWebExchange);
        };
    }

/** извлекаем заголовок Authorization из сообщения и достаём из него jwt */
    private static String jwtFromServerHttpRequest (ServerHttpRequest srvrHttpRequest)
    {
        List<String> elements = srvrHttpRequest.getHeaders().getOrEmpty (AUTHORIZATION_HDR_TITLE);
/*      return elements.isEmpty() ? null : elements.get(0).substring (bearerLength);
        Этот упрощённый вариант вызывает искл-е: java.lang.StringIndexOutOfBoundsException: String index out of range: -7
        при выходе юзера из учётки. Поэтому пришлось пойти длинным путём.  */
        String result = null;
        if (!elements.isEmpty())
        {
            String elemnt = elements.get(0);
            if (elemnt.length() > bearerLength)
                result = elemnt.substring (bearerLength);
        }
        return result;
    }

/** создаём для сообщения заголовок, с которым оно будет передаваться между частями приложения */
    private void populateRequestWithHeaders (ServerWebExchange srvrWebExchange, String jwt)
    {
        Claims claims = gatewayJwtUtil.getAllClaimsFromJWToken (jwt);
        srvrWebExchange.getRequest()
                       .mutate()
                       .header (INAPP_HDR_FIELD_LOGIN, claims.getSubject()) //< стд.поле jwt : sub
                       .header (INAPP_HDR_FIELD_ROLES, jwtRolesToStringArray (claims.get (JWT_PAYLOAD_ROLES))) //< для всех нестандартных полей — доступ по их имени
                       .build();
    }

/** В JWT "roles" это List<String> (см.JwtokenUtil.generateJWToken()). */
    public static String[] jwtRolesToStringArray (Object value)
    {
        String[] result = new String[]{};
        if (value instanceof Collection && ((Collection<?>) value).size() > 0)
        {
/*          List<String> roles = ((List<?>) value).stream().map(Object::toString).collect(Collectors.toList());     < правильный путь (без «желтизны»), но долгий */
            Collection<String> roles = (Collection<String>) value; //< быстрый путь
            result = roles.toArray (result);
        }
        return result;
    }

    private static Mono<Void> onError (ServerWebExchange srvrWebExchange, String err)
    {
        System.err.println (err);
        ServerHttpResponse srvrHttpResponse = srvrWebExchange.getResponse();
        srvrHttpResponse.setStatusCode (HttpStatus.UNAUTHORIZED);
        return srvrHttpResponse.setComplete();
    }
}
