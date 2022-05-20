package top.dddpeter.ishare.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.enums.ResponseCodeEnum;
import top.dddpeter.ishare.common.utils.IpUtils;
import top.dddpeter.ishare.gateway.constant.CommonConstants;
import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;
import top.dddpeter.ishare.gateway.filter.util.FilterResolveUtil;
import top.dddpeter.ishare.gateway.mapper.ClientInfoMapper;
import top.dddpeter.ishare.gateway.util.GatewayRouteDefineUtil;
import top.dddpeter.ishare.gateway.vo.ClientInfoVO;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 网关鉴权
 */
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    /**
     * swagger排除自行添加跳过授权检查
     */
    private static final String[] WHITE_LIST = {CommonConstants.SWAGGER_DOCS_PATH};

    private int order;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate ops;

    @Resource
    private ClientInfoMapper clientInfoMapper;

    @Resource
    private GatewayRouteDefineUtil gatewayRouteDefineUtil;

    public AuthFilter(int order) {
        this.order = order;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("***********AuthFilter执行*************");
        return this.auth(exchange, chain);
    }

    private Mono<Void> auth(ServerWebExchange exchange, GatewayFilterChain chain) {

        String url = exchange.getRequest().getURI().getPath();
        // 跳过不需要验证的路径
        for (String urlPattern : WHITE_LIST) {
            if (url.contains(urlPattern)) {
                log.debug("********************请求路径{}, 跳过授权检查************************", url);
                return chain.filter(exchange);
            }
        }

        // 获取当前请求匹配的路由定义信息
        GatewayRouteDefine gatewayRouteDefine = gatewayRouteDefineUtil.getMatchGatewayRouteDefine(exchange);
        if(gatewayRouteDefine != null && "NO".equals(gatewayRouteDefine.getNeedCheckAuth())){
            log.debug("********************请求路径{}, 跳过授权检查************************", url);
            // 跳过不需要验证的路径
            return chain.filter(exchange);
        }

        // ip白名单检查
        if(gatewayRouteDefine != null && "YES".equalsIgnoreCase(gatewayRouteDefine.getTrustedIpsEnable())){
            // 获取请求ip
            String requestIp = IpUtils.getIpAddr(exchange.getRequest());
            log.debug("请求ip:{}", requestIp);
            String trustedIps = gatewayRouteDefine.getTrustedIps();
            if(StringUtils.isEmpty(trustedIps)){
                log.debug("服务请求url {} 缺失ip白名单配置", url);
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, ResponseCodeEnum.ERROR,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, String.format("service path %s lose trustedIps config", url));
            }
            if (!PatternMatchUtils.simpleMatch(trustedIps.split(","), requestIp)) {
                log.debug("服务请求url {} ip白名单检查不通过，请求的ip为:{}，授信ip为:{}", url, requestIp, trustedIps);
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.FORBIDDEN, ResponseCodeEnum.FORBIDDEN,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, String.format("service path %s trustedIps check no pass", url));
            }
        }

        // 客户端限制以及验证检查
        String token = exchange.getRequest().getHeaders().getFirst(Constants.TOKEN);
        String appId = exchange.getRequest().getHeaders().getFirst(Constants.APP_ID);
        String appSecret = exchange.getRequest().getHeaders().getFirst(Constants.APP_SECRET);

        if (gatewayRouteDefine!=null && gatewayRouteDefine.getAuthClients()!=null) {
            List<String> authClientArray = Arrays.asList(gatewayRouteDefine.getAuthClients().split(","));
            if (!authClientArray.contains("token") && StringUtils.isNotBlank(token)) {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.UNAUTHORIZED, ResponseCodeEnum.UNAUTHORIZED,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, "illegal request");
            }

            if (StringUtils.isBlank(token)
                    && StringUtils.isNotBlank(appId)
                    && StringUtils.isNotBlank(appSecret)
                    && !authClientArray.contains(appId)) {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.UNAUTHORIZED, ResponseCodeEnum.UNAUTHORIZED,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, "illegal request");
            }
        }

        if (StringUtils.isBlank(token)) {
            if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(appSecret)) {
                ClientInfoVO info = clientInfoMapper.getClientById(appId);
                if (info != null && info.getAppSecret() != null && info.getAppSecret().equals(appSecret)) {
                    ServerHttpRequest mutableReq = exchange.getRequest().mutate()
                            .headers(httpHeaders -> httpHeaders.add(Constants.APP_ID, appId))
                            .headers(httpHeaders -> httpHeaders.add(Constants.APP_SECRET, appSecret))
                            .build();
                    ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
                    return chain.filter(mutableExchange);
                } else {
                    return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.UNAUTHORIZED, ResponseCodeEnum.UNAUTHORIZED,
                            MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, "client secret and id are not correct,please check");
                }

            } else {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.UNAUTHORIZED, ResponseCodeEnum.UNAUTHORIZED,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, "client secret and id are not null");
            }
        } else {
            token = exchange.getRequest().getHeaders().getFirst(Constants.TOKEN);
            // token为空
            if (StringUtils.isBlank(token)) {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.UNAUTHORIZED, ResponseCodeEnum.UNAUTHORIZED,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, "token can't null or empty string");
            }
            String userStr = ops.opsForValue().get(Constants.ACCESS_TOKEN + token);
            if (StringUtils.isBlank(userStr)) {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.UNAUTHORIZED, ResponseCodeEnum.UNAUTHORIZED,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, "token verify error");
            }
            JSONObject jo = parseObject(userStr);
            String userId = jo.getString(Constants.USER_ID);
            // 查询token信息
            if (StringUtils.isBlank(userId)) {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.UNAUTHORIZED, ResponseCodeEnum.UNAUTHORIZED,
                        MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, "token verify error");
            }
            // 设置userId到request里，后续根据userId，获取用户信息
            ServerHttpRequest mutableReq = exchange.getRequest().mutate()
                    .headers(httpHeaders -> httpHeaders.add(Constants.CURRENT_ID, userId))
                    .headers(httpHeaders -> httpHeaders.add(Constants.CURRENT_USERNAME, jo.getString(Constants.LOGIN_NAME)))
                    .build();
            ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
            return chain.filter(mutableExchange);
        }
    }

    @Override
    public int getOrder() {
        return order;
    }
}