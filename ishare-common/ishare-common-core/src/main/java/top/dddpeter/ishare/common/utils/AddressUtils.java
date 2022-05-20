package top.dddpeter.ishare.common.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.common.utils.http.HttpUtils;

import java.util.Map;

/**
 * 获取地址类
 *
 * @author ishare
 */
public class AddressUtils
{
    private static final Logger log    = LoggerFactory.getLogger(AddressUtils.class);

    public static final String  IP_URL = "http://region.zmrit.com";

    public static String getRealAddressByIP(String ip)
    {
        String address = "XX XX";
        // 内网不查询
        if (IpUtils.internalIp(ip))
        {
            return "内网IP";
        }
        String rspStr = HttpUtils.sendPost(IP_URL, "ip=" + ip);
        if (StringUtils.isEmpty(rspStr))
        {
            log.error("获取地理位置异常 {}", ip);
            return address;
        }
        try
        {

            Map map = JSON.parseObject(rspStr, Map.class);
            address = (String)(((Map)map.get("data")).get("address"));
        }
        catch (Exception e)
        {
            log.error("获取地理位置异常 {}", ip);
        }
        return address;
    }

    public static void main(String[] args) {
        System.out.println(getRealAddressByIP("114.246.35.136"));
    }

}
