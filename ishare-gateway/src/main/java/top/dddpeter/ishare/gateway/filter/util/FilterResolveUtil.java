package top.dddpeter.ishare.gateway.filter.util;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.enums.ResponseCodeEnum;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * 网关过滤器常用工具
 *
 * @author : huangshuanbao
 */
public class FilterResolveUtil {

    private FilterResolveUtil(){}

    public static String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        // 获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }

    public static Mono<Void> setFailedResponse(ServerWebExchange exchange, HttpStatus status, ResponseCodeEnum responseCodeEnum, String contentType, Charset charset, String msg) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        originalResponse.setStatusCode(status);
        originalResponse.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        byte[] response = null;
        response = toJSONString(ResultDTO.failure(responseCodeEnum.getCode(), msg)).getBytes(charset);
        DataBuffer buffer = originalResponse.bufferFactory().wrap(response);
        return originalResponse.writeWith(Flux.just(buffer));
    }

    public static Mono<Void> setSuccessResponse(ServerWebExchange exchange, String contentType, Charset charset, ResultDTO resultDTO) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        originalResponse.setStatusCode(HttpStatus.OK);
        originalResponse.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        byte[] response = toJSONString(resultDTO).getBytes(charset);
        DataBuffer buffer = originalResponse.bufferFactory().wrap(response);
        return originalResponse.writeWith(Flux.just(buffer));
    }

    public static Mono<Void> afterReadBody(ServerWebExchange exchange, GatewayFilterChain chain, byte[] bytes, ServerHttpResponse response){
        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
            DataBuffer buffer = exchange.getResponse().bufferFactory()
                    .wrap(bytes);
            return Mono.just(buffer);
        });
        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return cachedFlux;
            }
        };
        if(response==null){
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        }else{
            return chain.filter(exchange.mutate().request(mutatedRequest).response(response).build());
        }
    }

}
