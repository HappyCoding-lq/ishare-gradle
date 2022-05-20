package top.dddpeter.ishare.common.encryption.algorithm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.apache.commons.lang3.StringUtils;
import top.dddpeter.ishare.common.encryption.domain.RSAEncryptedDomain;
import top.dddpeter.ishare.common.encryption.enums.EncryptTypeEnum;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;
import top.dddpeter.ishare.common.encryption.utils.DefaultAESUtil;
import top.dddpeter.ishare.common.encryption.utils.DefaultSignUtil;


/**
 * 默认加解密接口定义实现(RSA非对称加密、加签)
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:20 下午
 */
public class DefaultRsaEncryptAlgorithm implements EncryptAlgorithm {

	@Override
	public EncryptTypeEnum getEncryptType() {
		return EncryptTypeEnum.RSA;
	}

	@Override
	public void checkContent(String content) {
		try {
			if(StringUtils.isBlank(content)){
				throw new EncryptedException("request content is not legal");
			}
			RSAEncryptedDomain domain = JSON.parseObject(content, RSAEncryptedDomain.class);
			if(domain==null || !domain.checkEncrypt()){
				throw new EncryptedException("request content is not legal");
			}
		}catch (JSONException ex){
			throw new EncryptedException("json error");
		}
	}

	@Override
	public String getICodeFromEncryptStr(String content) {
		RSAEncryptedDomain domain = JSON.parseObject(content, RSAEncryptedDomain.class);
		return domain.getIcode();
	}

	@Override
	public String encrypt(String content, String icode, String... key) {
		if(key.length!=2){
			throw new EncryptedException("encrypt key numbers error");
		}
		String publicKey = key[0];
		String privateKey = key[1];
		if(StringUtils.isBlank(publicKey) || StringUtils.isBlank(privateKey)){
			throw new EncryptedException("encrypt key is blank");
		}
		RSAEncryptedDomain domain = DefaultAESUtil.buildRsaEncryptBody(content, icode, publicKey);
		DefaultSignUtil.buildRsaSign(domain, privateKey);
		return JSON.toJSONString(domain);
	}

	@Override
	public String decrypt(String encryptStr, String... key) {
		if(key.length!=2){
			throw new EncryptedException("decrypt key numbers error");
		}
		String publicKey = key[0];
		String privateKey = key[1];
		if(StringUtils.isBlank(publicKey) || StringUtils.isBlank(privateKey)){
			throw new EncryptedException("decrypt key is blank");
		}
		RSAEncryptedDomain domain = JSON.parseObject(encryptStr, RSAEncryptedDomain.class);
		DefaultSignUtil.verifyRsaSign(domain, publicKey);
		return DefaultAESUtil.decryptRsaEncryptBody(domain, privateKey);
	}

}
