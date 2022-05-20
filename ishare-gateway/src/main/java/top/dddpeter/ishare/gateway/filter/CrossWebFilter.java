package top.dddpeter.ishare.gateway.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import top.dddpeter.ishare.gateway.domain.GatewayCrossOriginConfig;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 跨域处理filter
 *
 * @author : huangshuanbao
 */
public class CrossWebFilter implements WebFilter {

    private static final String MAX_AGE = "18000L";

    @Resource(name="myCrossOriginMap")
    private Map<String, List<GatewayCrossOriginConfig>> myCrossOriginMap;


    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        if(!CorsUtils.isCorsRequest(request)){
            return chain.filter(exchange);
        }
         // 获取请求路径
        String path = request.getURI().getPath();
        // 获取请求域
        String origin = request.getHeaders().getFirst(HttpHeaders.ORIGIN);

        List<GatewayCrossOriginConfig> origins = myCrossOriginMap.get(path);
        if(origins!=null && !origins.isEmpty() && origins.stream().map(GatewayCrossOriginConfig::getAllowOrigin).anyMatch(allowOrigin-> allowOrigin.equals(origin))) {
            HttpHeaders headers = response.getHeaders();
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            // 允许的请求方法
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, CorsConfiguration.ALL);
            // 是否允许后续请求携带认证信息（cookies）,该值只能是true,否则不返回
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            // 允许的请求头字段
            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, CorsConfiguration.ALL);
            // 允许的请求头字段
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, CorsConfiguration.ALL);
            // 用来指定本次预检请求的有效期
            headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
        }

        if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return chain.filter(exchange);
    }
}
