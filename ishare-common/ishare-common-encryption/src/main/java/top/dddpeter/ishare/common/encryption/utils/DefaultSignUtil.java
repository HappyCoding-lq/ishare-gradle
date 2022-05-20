package top.dddpeter.ishare.common.encryption.utils;

import com.alibaba.fastjson.JSON;
import top.dddpeter.ishare.common.encryption.domain.AESEncryptedDomain;
import top.dddpeter.ishare.common.encryption.domain.RSAEncryptedDomain;
import top.dddpeter.ishare.common.encryption.enums.EncryptionEnum;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;

import java.util.SortedMap;
import java.util.TreeMap;


public class DefaultSignUtil {

    private DefaultSignUtil() {}

    public static void sha256Sign(AESEncryptedDomain domain) {
        try {
            domain.setSign(SignUtil.sha256Sign(convertAESEncryptionDomainToTreeMap(domain)));
        }catch (EncryptedException ex){
            throw new EncryptedException("SignUtil sha256Sign error");
        }
    }

    public static void verifySha256Sign(AESEncryptedDomain domain) {
        boolean signFlag;
        try {
            signFlag = SignUtil.verifySha256Sign(convertAESEncryptionDomainToTreeMap(domain), domain.getSign());
        }catch (EncryptedException ex){
            throw new EncryptedException("验签失败");
        }
        if(!signFlag){
            throw new EncryptedException("验签不通过");
        }
    }

    public static void buildRsaSign(RSAEncryptedDomain encryptedBody, String selfPrivateKey) {
        try {
            String sign = SignUtil.requestSign(JSON.parseObject(JSON.toJSONString(encryptedBody), TreeMap.class), selfPrivateKey);
            encryptedBody.setSign(sign);
        }catch (Exception ex){
            throw new EncryptedException("SignUtil buildSignBody error");
        }
    }

    public static void verifyRsaSign(RSAEncryptedDomain parameterDTO, String selfOrganizationPublicKey) {
        SignUtil.responseCheckSign(convertRSAEncryptionDomainToTreeMap(parameterDTO), parameterDTO.getSign(), selfOrganizationPublicKey);
    }

    private static SortedMap convertRSAEncryptionDomainToTreeMap(RSAEncryptedDomain domain){
        SortedMap map = new TreeMap();
        map.put(EncryptionEnum.KEY.getCode(), domain.getKey());
        map.put(EncryptionEnum.SALT.getCode(), domain.getSalt());
        map.put(EncryptionEnum.DATA.getCode(), domain.getData());
        map.put(EncryptionEnum.ICODE.getCode(), domain.getIcode());
        return map;
    }

    private static SortedMap convertAESEncryptionDomainToTreeMap(AESEncryptedDomain domain){
        SortedMap map = new TreeMap();
        map.put(EncryptionEnum.DATA.getCode(), domain.getData());
        map.put(EncryptionEnum.ICODE.getCode(), domain.getIcode());
        return map;
    }

}
