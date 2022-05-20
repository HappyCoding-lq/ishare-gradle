package top.dddpeter.ishare.common.encryption.algorithm;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.apache.commons.lang3.StringUtils;
import top.dddpeter.ishare.common.encryption.domain.AESEncryptedDomain;
import top.dddpeter.ishare.common.encryption.enums.EncryptTypeEnum;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;
import top.dddpeter.ishare.common.encryption.utils.DefaultAESUtil;
import top.dddpeter.ishare.common.encryption.utils.DefaultSignUtil;


/**
 * AES对称加解密接口定义实现
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:20 下午
 */
public class DefaultAesEncryptAlgorithm implements EncryptAlgorithm {

	@Override
	public EncryptTypeEnum getEncryptType() {
		return EncryptTypeEnum.AES;
	}

	@Override
	public void checkContent(String content) {
		try {
			if(StringUtils.isBlank(content)){
				throw new EncryptedException("request content is not legal");
			}
			AESEncryptedDomain domain = JSON.parseObject(content, AESEncryptedDomain.class);
			if(domain==null || !domain.checkEncrypt()){
				throw new EncryptedException("request content is not legal");
			}
		}catch (JSONException ex){
			throw new EncryptedException("json error");
		}
	}

	@Override
	public String getICodeFromEncryptStr(String content) {
		AESEncryptedDomain domain = JSON.parseObject(content, AESEncryptedDomain.class);
		return domain.getIcode();
	}

	@Override
	public String encrypt(String content, String icode, String... key) {
		if(key.length!=2){
			throw new EncryptedException("encrypt key numbers error");
		}
		String aesKey = key[0];
		String aesSalt = key[1];
		if(StringUtils.isBlank(aesKey) || StringUtils.isBlank(aesSalt)){
			throw new EncryptedException("encrypt key is blank");
		}
		try {
			AESEncryptedDomain domain = DefaultAESUtil.buildAesEncryptBody(content, icode, aesKey, aesSalt);
			DefaultSignUtil.sha256Sign(domain);
			return JSON.toJSONString(domain);
		}catch (Exception ex){
			throw new EncryptedException(ex.getMessage());
		}
	}

	@Override
	public String decrypt(String encryptStr, String... key) {
		if(key.length!=2){
			throw new EncryptedException("decrypt key numbers error");
		}
		String aesKey = key[0];
		String aesSalt = key[1];
		if(StringUtils.isBlank(aesKey) || StringUtils.isBlank(aesSalt)){
			throw new EncryptedException("decrypt key is blank");
		}
		try {
			AESEncryptedDomain domain = JSON.parseObject(encryptStr, AESEncryptedDomain.class);
			DefaultSignUtil.verifySha256Sign(domain);
			return DefaultAESUtil.decryptAesEncryptBody(domain, aesKey, aesSalt);
		}catch (Exception ex){
			throw new EncryptedException(ex.getMessage());
		}
	}

}
