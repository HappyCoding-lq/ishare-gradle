
package top.dddpeter.ishare.common.encryption.enums;

/**
 * 加密方式
 * 
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/20 11:53 上午
 */
public enum EncryptTypeEnum {

    RSA("RSA", "RSA非对称加密"),
    AES("AES", "AES对称加密");

    private String code;
    private String desc;

    private EncryptTypeEnum(String code, String desc) {
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
