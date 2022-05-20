package top.dddpeter.ishare.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.enums.ResponseCodeEnum;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.common.utils.IpUtils;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.gateway.constant.CommonConstants;
import top.dddpeter.ishare.gateway.domain.GatewayRequestLog;
import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;
import top.dddpeter.ishare.gateway.filter.util.FilterResolveUtil;
import top.dddpeter.ishare.gateway.util.GatewayRouteDefineUtil;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 网关请求日志记录filter
 *
 * @author : huangshuanbao
 */
@Slf4j
public class RequestLogGlobalFilter implements GlobalFilter, Ordered {

    private int order;

    @Value("${gateway.requestLog.enable:true}")
    private boolean enable;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private GatewayRouteDefineUtil gatewayRouteDefineUtil;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate ops;




    public RequestLogGlobalFilter(int order) {
        this.order = order;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("***********LogRequestGlobalFilter执行*************enable:{}", enable);
        GatewayRouteDefine gatewayRouteDefine = gatewayRouteDefineUtil.getMatchGatewayRouteDefine(exchange);
        if (!enable
                || exchange.getRequest().getURI().getPath().contains(CommonConstants.SWAGGER_DOCS_PATH)
                || gatewayRouteDefine == null || StringUtils.isEmpty(gatewayRouteDefine.getIsLogAble()) ) {
            return chain.filter(exchange);
        }
        final GatewayRequestLog[] gatewayRequestLog = {null};
        Date requestTime = DateUtils.getNowDate();

        ServerHttpRequest request = exchange.getRequest();
        StringBuilder responseData = new StringBuilder();

        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator response = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] uppedContent = new byte[1];
                        try {
                            dataBuffers.forEach(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                try {
                                    outputStream.write(content);
                                } catch (IOException e) {
                                    log.warn("报文读取出错，但不影响后续业务流程", e);
                                }
                            });
                            uppedContent = outputStream.toByteArray();
                            responseData.append(new String(uppedContent, "UTF-8"));
                        } catch (Exception e) {
                            log.warn("处理报文出错，但不影响后续业务流程", e);
                        } finally {
                            try {
                                outputStream.close();
                            } catch (Exception e) {
                                log.warn("字节流出错，但不影响后续业务流程", e);
                            }
                        }
                        String resStr = responseData.toString();
                        log.debug("请求{}响应：{}", request.getPath(), resStr.length() > 5000 ? resStr.substring(0, 5000) + "###超长截断###" : resStr);
                        endLog(gatewayRequestLog[0], requestTime, resStr);
                        originalResponse.getHeaders().setContentLength(uppedContent.length);
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                // 上面的代码只是为了记录报文信息，实际不对响应做任何修改
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };
        Charset charset = null;
        if(request.getHeaders().getContentType()!=null){
            charset = request.getHeaders().getContentType().getCharset();
        }
        //  对不同的请求方法不同的处理方式
        if (exchange.getRequest().getMethod() == HttpMethod.POST
                || exchange.getRequest().getMethod() == HttpMethod.PATCH
                ||  exchange.getRequest().getMethod() == HttpMethod.DELETE
                || exchange.getRequest().getMethod() == HttpMethod.PUT) {

            log.info("网关接收到{}的{}请求", request.getPath(),exchange.getRequest().getMethod());
            Charset finalCharset = charset;
            return DataBufferUtils.join(request.getBody()).map(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                return bytes;
            }).defaultIfEmpty(new byte[0]).flatMap(bytes -> {
                StringBuilder requestStr = new StringBuilder();
                MultiValueMap<String, String> map = exchange.getRequest().getQueryParams();
                if (!map.isEmpty()) {
                    requestStr.append(map);
                }
                String requestBody = finalCharset == null ? new String(bytes) : new String(bytes, finalCharset);
                if (StringUtils.isNotEmpty(requestStr.toString()) && StringUtils.isNotEmpty(requestBody)) {
                    requestStr.append(":").append(requestBody);
                }
                if (StringUtils.isEmpty(requestStr.toString()) && StringUtils.isNotEmpty(requestBody)) {
                    requestStr.append(requestBody);
                }
                log.debug("网关接收到{}的{}请求，请求数据为：{}", request.getPath(),exchange.getRequest().getMethod(),
                        requestStr.length()>5000?requestStr.substring(0, 5000)+"###超长截断###":requestStr);
                // 新增日志
                gatewayRequestLog[0] = newLog(request, requestStr.toString(), requestTime, gatewayRouteDefine);
                return FilterResolveUtil.afterReadBody(exchange, chain, bytes, response);
            });
        }
        else  {
            MultiValueMap<String, String> map = exchange.getRequest().getQueryParams();
            try {
                // 新增日志
                gatewayRequestLog[0] = newLog(request, map.toString(), requestTime, gatewayRouteDefine);
            } catch (Exception e) {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR,
                        ResponseCodeEnum.ERROR, MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, e.getMessage());
            }
        }
        return chain.filter(exchange.mutate().response(response).build());
    }

    @Override
    public int getOrder() {
        return order;
    }

    private GatewayRequestLog newLog(ServerHttpRequest request, String requestParam, Date requestTime,
                                     GatewayRouteDefine gatewayRouteDefine){
        GatewayRequestLog gatewayRequestLog = new GatewayRequestLog();
        gatewayRequestLog.setPath(request.getPath().value());
        gatewayRequestLog.setUserAgent(request.getHeaders().getFirst("User-Agent"));
        String token = request.getHeaders().getFirst(Constants.TOKEN);
        String appId = request.getHeaders().getFirst(Constants.APP_ID);
        if(StringUtils.isNotEmpty(token)){
            String userStr = ops.opsForValue().get(Constants.ACCESS_TOKEN + token);
            if (StringUtils.isNotEmpty(userStr)) {
                JSONObject jo = parseObject(userStr);
                gatewayRequestLog.setLoginName(jo.getString(Constants.LOGIN_NAME));
            }
            gatewayRequestLog.setAppId(CommonConstants.DEFAULT_CLIENT_APP_ID);
        }else if(StringUtils.isNotEmpty(appId)){
            gatewayRequestLog.setAppId(appId);
        }
        if(StringUtils.isNotEmpty(gatewayRouteDefine.getApiTransCode())){
            gatewayRequestLog.setApiTransCode(gatewayRouteDefine.getApiTransCode());
        }
        gatewayRequestLog.setRouteType(gatewayRouteDefine.getRouteType());
        gatewayRequestLog.setRouteDesc(gatewayRouteDefine.getRouteDesc());
        if (gatewayRouteDefine.getRouteUri()!=null && gatewayRouteDefine.getRouteUri().startsWith(CommonConstants.SERVICE_URI_PREFIX)) {
            gatewayRequestLog.setServiceId(gatewayRouteDefine.getRouteUri().substring(5));
        }
        gatewayRequestLog.setRequestInfo(requestParam);
        gatewayRequestLog.setRequestMethod(request.getMethodValue());
        if(request.getHeaders().getContentType()!=null){
            gatewayRequestLog.setRequestContentType(Objects.requireNonNull(request.getHeaders().getContentType()).toString());
        }
        String ip = IpUtils.getIpAddr(request);
        gatewayRequestLog.setRequestIp(ip);
        gatewayRequestLog.setRequestTime(DateUtils.dateToString(DateUtils.yyyy_MM_dd_HH_mm_ss, requestTime));
        mongoTemplate.insert(gatewayRequestLog);
        return gatewayRequestLog;
    }

    private void endLog(GatewayRequestLog gatewayRequestLog, Date requestTime,String resData){
        Query query = new Query();
        query.addCriteria(Criteria.where(Fields.UNDERSCORE_ID).is(new ObjectId(gatewayRequestLog.getLogId())));
        Date now = DateUtils.getNowDate();
        gatewayRequestLog.setResponseTime(DateUtils.dateToString(DateUtils.yyyy_MM_dd_HH_mm_ss, now));
        Long accessTime = now.getTime()-requestTime.getTime();
        gatewayRequestLog.setAccessTime(accessTime);
        gatewayRequestLog.setResponseInfo(resData);
        mongoTemplate.save(gatewayRequestLog);
    }

}