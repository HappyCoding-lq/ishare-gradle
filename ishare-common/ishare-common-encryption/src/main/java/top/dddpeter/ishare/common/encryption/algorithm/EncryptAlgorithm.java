package top.dddpeter.ishare.common.encryption.algorithm;

import top.dddpeter.ishare.common.encryption.enums.EncryptTypeEnum;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;

/**
 * 加解密接口定义
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:14 下午
 */
public interface EncryptAlgorithm {

	/**
	 * 加密类型
	 *
	 * @return : EncryptTypeEnum – RSA方式/AES方式
	 */
	EncryptTypeEnum getEncryptType();

	/**
	 * 校验密文
	 *
	 * @param content 密文
	 * @throws EncryptedException 校验失败或不通过时抛出
	 */
	void checkContent(String content) throws EncryptedException;

	/**
	 * 获取渠道码
	 *
	 * @param content 密文
	 * @return : String – 渠道码
	 * @throws EncryptedException 异常时抛出
	 */
	String getICodeFromEncryptStr(String content) throws EncryptedException;

	/**
	 * 加密
	 *
	 * @param content 明文
	 * @param icode 渠道码
	 * @param key 密钥
	 * @return : String – 密文
	 * @throws EncryptedException 加密异常时抛出
	 */
	String encrypt(String content, String icode, String... key) throws EncryptedException;

	/**
	 * 解密
	 *
	 * @param encryptStr 密文
	 * @param key 密钥
	 * @return : String – 明文
	 * @throws EncryptedException 解密异常时抛出
	 */
	String decrypt(String encryptStr, String... key) throws EncryptedException;

}
