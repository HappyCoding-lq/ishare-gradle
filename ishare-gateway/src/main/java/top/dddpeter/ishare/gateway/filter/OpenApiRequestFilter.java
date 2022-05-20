package top.dddpeter.ishare.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.dddpeter.ishare.common.dto.OpenApiCommonRequestDTO;
import top.dddpeter.ishare.common.enums.ResponseCodeEnum;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.JsonTrans;
import top.dddpeter.ishare.common.utils.spring.SpringUtils;
import top.dddpeter.ishare.gateway.constant.CommonConstants;
import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;
import top.dddpeter.ishare.gateway.domain.MyRouteDefinition;
import top.dddpeter.ishare.gateway.filter.util.FilterResolveUtil;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对外API接口处理
 *
 * @author : huangshuanbao
 */
@Slf4j
public class OpenApiRequestFilter extends AbstractGatewayFilterFactory<OpenApiRequestFilter.Config> {

    public OpenApiRequestFilter() {
        super(OpenApiRequestFilter.Config.class);
    }

    @Override
    public String name() {
        return "OpenApiRequest";
    }

    @Override
    public GatewayFilter apply(Config config) {
        OpenApiRequestGatewayFilter openApiRequestGatewayFilter = new OpenApiRequestGatewayFilter();
        Integer order = config.getOrder();
        if (order == null) {
            return openApiRequestGatewayFilter;
        }
        return new OrderedGatewayFilter(openApiRequestGatewayFilter, order);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("order");
    }

    public static class OpenApiRequestGatewayFilter implements GatewayFilter {

        private static final String FAILED_REQUEST_MAEESAGE = "failed request";

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            log.debug("***********OpenApiRequestGatewayFilter*************");
            ServerHttpRequest request = exchange.getRequest();
            String url = request.getURI().getPath();
            if (url.contains(CommonConstants.SWAGGER_DOCS_PATH)) {
                return chain.filter(exchange);
            }
            // 请求方式
            HttpMethod method = request.getMethod();
            if (HttpMethod.POST != method) {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.BAD_REQUEST, ResponseCodeEnum.PARAM_WRONG,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, "仅支持POST请求");
            }
            // 获取content-type
            HttpHeaders headers = request.getHeaders();
            MediaType mediaType = headers.getContentType();
            log.debug("mediaType:{}", mediaType);
            if(mediaType == null){
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.BAD_REQUEST, ResponseCodeEnum.PARAM_WRONG,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, FAILED_REQUEST_MAEESAGE);
            }
            if (MediaType.APPLICATION_JSON_UTF8.compareTo(mediaType) != 0) {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.BAD_REQUEST, ResponseCodeEnum.PARAM_WRONG,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, "仅支持" + MediaType.APPLICATION_JSON_UTF8_VALUE + "请求");
            }
            return DataBufferUtils.join(request.getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String requestBody = new String(bytes, StandardCharsets.UTF_8);
                        try {
                            checkStr(JsonTrans.fromJson(requestBody, Map.class), url);
                        } catch (IShareException ex) {
                            log.error("Bad api request", ex);
                            return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.BAD_REQUEST, ResponseCodeEnum.PARAM_WRONG,
                                    MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, ex.getMessage());
                        } catch (Exception ex) {
                            log.error("Failed api request", ex);
                            return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.BAD_REQUEST, ResponseCodeEnum.PARAM_WRONG,
                                    MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, FAILED_REQUEST_MAEESAGE);
                        }
                        DataBufferUtils.release(dataBuffer);
                        return FilterResolveUtil.afterReadBody(exchange, chain, bytes, null);
                    });
        }

        private void checkStr(Map body, String url) {
            OpenApiCommonRequestDTO dto = JsonTrans.fromJson(JsonTrans.objectToJson(body), OpenApiCommonRequestDTO.class);
            String rightTransCode = findApiTransCode(url);
            dto.checkSelf(rightTransCode);
        }

        private String findApiTransCode(String url){
            List<MyRouteDefinition> myRouteDefinitionList = SpringUtils.getBean("myRouteDefinitionList");
            List<GatewayRouteDefine> gatewayRouteDefineList = myRouteDefinitionList.stream().map(MyRouteDefinition::getGatewayRouteDefine).collect(Collectors.toList());
            List<String> list = gatewayRouteDefineList.stream().filter(gatewayRouteDefine -> gatewayRouteDefine.getPredicates().contains(url)).map(GatewayRouteDefine::getApiTransCode).collect(Collectors.toList());
            return list.get(0);
        }

    }

    @Data
    static class Config {
        private Integer order;
    }

}