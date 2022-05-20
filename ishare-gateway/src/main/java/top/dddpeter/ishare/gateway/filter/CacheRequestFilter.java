package top.dddpeter.ishare.gateway.filter;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author ChinaPost-life
 */
@Slf4j
public class CacheRequestFilter implements GlobalFilter, Ordered {

    private int order;

    public CacheRequestFilter(int order) {
        this.order = order;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("***********CacheRequestGatewayFilter执行*************");
        // GET DELETE 不过滤
        HttpMethod method = exchange.getRequest().getMethod();
        if (method == null || method.matches("GET") || method.matches("DELETE")) {
            return chain.filter(exchange);
        }
        return DataBufferUtils.join(exchange.getRequest().getBody()).map(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            return bytes;
        }).defaultIfEmpty(new byte[0]).flatMap(bytes -> {
            DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                @NonNull
                public Flux<DataBuffer> getBody() {
                    if (bytes.length > 0) {
                        return Flux.just(dataBufferFactory.wrap(bytes));
                    }
                    return Flux.empty();
                }
            };
            return chain.filter(exchange.mutate().request(decorator).build());
        });
    }

    @Override
    public int getOrder() {
        return order;
    }
}