
package top.dddpeter.ishare.common.encryption.enums;

/**
 * 加解密枚举
 * 
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/20 11:53 上午
 */
public enum EncryptionEnum {

    ICODE("icode", "渠道码"),
    DATA("data", "传输的数据(原始数据经过AES加密)"),
    KEY("key", "AES的密钥(随机产生)"),
    SALT("salt", "AES的初始化向量(随机产生)"),
    SIGN("sign", "签名数据");

    private String code;
    private String desc;

    private EncryptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

}
