package top.dddpeter.ishare.gateway.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.dddpeter.ishare.common.oss.config.OssProxyProperties;
import top.dddpeter.ishare.common.oss.constant.OssConstants;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
public class OssProxyFilter implements GlobalFilter, Ordered {
    private final String[] OSS_URI_LIST = {OssConstants.OSS_PROXY_PATH};
    private OssProxyProperties ossProxyProperties;
    public OssProxyFilter(OssProxyProperties ossProxyProperties){
        this.ossProxyProperties = ossProxyProperties;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        ServerWebExchange configedExchange = exchange;
        // 跳过不需要验证的路径
        for (String urlPattern : OSS_URI_LIST) {
            if (url.contains(urlPattern)) {
                log.debug("********************请求路径{},Oss转发************************", url);
                String ip = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                        .map(address -> address.getAddress().getHostAddress())
                        .orElse("unknown");
                Consumer<HttpHeaders> httpHeaders = httpHeader -> {
                    httpHeader.set("Host", ossProxyProperties.getHost());
                    httpHeader.set("X-Real-IP",ip);
                };
                ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
                configedExchange = exchange.mutate().request(serverHttpRequest).build();
            }
        }
        return chain.filter(configedExchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
