
package top.dddpeter.ishare.common.encryption.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;


public class SignUtil {

    private SignUtil() {}

    public static String requestSign(SortedMap<String, String> paramsMap, String privateKey) {
        String str = null;
        try {
            str = map2str(paramsMap);
            return RSAUtil.rsa256Sign(str, privateKey);
        } catch (Exception ex) {
            throw new EncryptedException("SignUtil requestSign error,params:{" + JSON.toJSONString(paramsMap) + "},str:{" + str + "} ");
        }
    }

    public static void responseCheckSign(SortedMap<String, String> paramsMap, String sign, String publicKey) {
        String str = map2str(paramsMap);
        boolean signFlag;
        try {
            signFlag = RSAUtil.rsa256CheckContent(str, sign, publicKey);
        }catch (EncryptedException ex){
            throw new EncryptedException("验签失败");
        }
        if(!signFlag){
            throw new EncryptedException("验签不通过");
        }
    }

    public static String sha256Sign(SortedMap<String, String> paramsMap) {
        try {
            String str = map2str(paramsMap);
            return StringUtils.lowerCase(DigestUtil.sha256Sign(str));
        }catch (EncryptedException ex){
            throw new EncryptedException("SignUtil sha256Sign error");
        }
    }

    public static boolean verifySha256Sign(SortedMap<String, String> paramsMap, String sign) {
        boolean signFlag;
        try {
            String zqSign = sha256Sign(paramsMap);
            signFlag = zqSign.equals(sign);
            return signFlag;
        }catch (EncryptedException ex){
            throw new EncryptedException("SignUtil verifySha256Sign error");
        }
    }

    public static String map2str(SortedMap<String, String> map) {
        List<String> values = new ArrayList();
        Iterator var2 = map.keySet().iterator();
        while (var2.hasNext()) {
            String key = (String) var2.next();
            String value = key + "=" + map.get(key);
            values.add(value);
        }
        return StringUtils.join(values, "&");
    }

}
