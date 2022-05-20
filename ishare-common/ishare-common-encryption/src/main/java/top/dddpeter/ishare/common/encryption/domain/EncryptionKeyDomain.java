package top.dddpeter.ishare.common.encryption.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 渠道交互密钥信息
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/22 9:16 下午
 */
@Data
public class EncryptionKeyDomain implements Serializable {

    private static final long serialVersionUID = -94160412891996641L;

    /** 渠道码 */
    private String icode;

    /** 密钥 */
    private String[] keys;

    public void setKeys(String... key) {
        this.keys = key;
    }

    public static boolean check(Map<String, EncryptionKeyDomain> keyDomain, String icode){
        EncryptionKeyDomain domain = keyDomain.get(icode);
        return domain!=null && domain.getKeys()!=null && domain.getKeys().length>0;
    }

}
