package top.dddpeter.ishare.common.encryption.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 渠道方RSA公钥信息
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/22 9:16 下午
 */
@Data
public class ChannelRsaKeyDomain implements Serializable {

    private static final long serialVersionUID = -7553116617671191402L;

    /** 渠道码 */
    private String icode;

    /** 公钥串 */
    private String publicKey;

    public boolean checkData(){
        return StringUtils.isNotBlank(icode) && StringUtils.isNotBlank(publicKey);
    }

}
