package top.dddpeter.ishare.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.enums.ResponseCodeEnum;
import top.dddpeter.ishare.common.exception.ValidateCodeException;
import top.dddpeter.ishare.gateway.filter.util.FilterResolveUtil;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 验证码处理
 * @author ChinaPost-life
 */
@Slf4j
public class ImgCodeFilter extends AbstractGatewayFilterFactory<ImgCodeFilter.Config> {

    private static final String AUTH_URL = "/auth/login";

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    public ImgCodeFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        ImgCodeGatewayFilter imgCodeGatewayFilter = new ImgCodeGatewayFilter();
        Integer order = config.getOrder();
        if(order==null){
            return imgCodeGatewayFilter;
        }else{
            return new OrderedGatewayFilter(imgCodeGatewayFilter, order);
        }
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("order");
    }

    public class ImgCodeGatewayFilter implements GatewayFilter {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            log.debug("***********ImgCodeFilter执行*************");
            ServerHttpRequest request = exchange.getRequest();
            URI uri = request.getURI();
            // 不是登录请求，直接向下执行
            if (!StringUtils.containsIgnoreCase(uri.getPath(), AUTH_URL)) {
                return chain.filter(exchange);
            }
            try {
                String bodyStr = FilterResolveUtil.resolveBodyFromRequest(request);
                JSONObject bodyJson = parseObject(bodyStr);
                String code = (String) bodyJson.get("captcha");
                String randomStr = (String) bodyJson.get("randomStr");
                // 校验验证码
                checkCode(code, randomStr);
            } catch (Exception e) {
                return FilterResolveUtil.setFailedResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR,
                        ResponseCodeEnum.ERROR, MediaType.APPLICATION_JSON_UTF8_VALUE, StandardCharsets.UTF_8, e.getMessage());
            }
            return chain.filter(exchange);
        }

        /**
         * 检查code
         *
         * @params code
         * @params randomStr
         */
        @SneakyThrows
        private void checkCode(String code, String randomStr) {
            if (StringUtils.isBlank(code)) {
                throw new ValidateCodeException("验证码不能为空");
            }
            if (StringUtils.isBlank(randomStr)) {
                throw new ValidateCodeException("验证码不合法");
            }
            String key = Constants.DEFAULT_CODE_KEY + randomStr;
            String saveCode = redisTemplate.opsForValue().get(key);
            redisTemplate.delete(key);
            if (!code.equalsIgnoreCase(saveCode)) {
                throw new ValidateCodeException("验证码不合法");
            }
        }

    }

    @Data
    static class Config {
        private Integer order;
    }
}
