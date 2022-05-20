package top.dddpeter.ishare.gateway.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.redis.util.RedisUtils;
import top.dddpeter.ishare.common.utils.StringUtils;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 请求通用处理工具
 */
@Component
@Slf4j
public class RequestCommonUtil {

    @Resource
    private RedisUtils redisUtils;

    public String checkAuth(ServerHttpRequest request){
        String error = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        String token = request.getHeaders().getFirst(Constants.TOKEN);
        // token为空
        if (StringUtils.isEmpty(token)) {
            throw new IShareException(error);
        }
        String userStr = redisUtils.get(Constants.ACCESS_TOKEN + token);
        if (StringUtils.isEmpty(userStr)) {
            throw new IShareException(error);
        }
        JSONObject jo = parseObject(userStr);
        String userId = jo.getString(Constants.USER_ID);
        // 查询token信息
        if (StringUtils.isEmpty(userId)) {
            throw new IShareException(error);
        }
        return token;
    }

    public String getLocalHost(){
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        }catch (UnknownHostException ex){
            log.error(ex.getMessage(), ex);
        }
        assert inetAddress != null;
        return inetAddress.getHostAddress();
    }

}
