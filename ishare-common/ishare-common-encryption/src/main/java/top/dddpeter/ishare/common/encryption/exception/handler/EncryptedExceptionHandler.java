package top.dddpeter.ishare.common.encryption.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;


/**
 * 加解密异常类捕获处理器
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/20 11:05 上午
 */
@RestControllerAdvice
@Slf4j
public class EncryptedExceptionHandler {
    @ExceptionHandler(value = EncryptedException.class)
    public String handleEncryptedException(EncryptedException ex) {
        return ex.getMessage();
    }
}