package top.dddpeter.ishare.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.exception.UnauthorizedException;
import top.dddpeter.ishare.common.exception.ValidateCodeException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 异常处理器
 *
 * @author zmr
 * @author lucas
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    public ResultDTO handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ResultDTO.failure("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * Content-Type不支持
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResultDTO handleException(HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ResultDTO.failure("不支持' " + e.getContentType() + "'请求");
    }

    /**
     * Content-Type不支持
     */
    @ExceptionHandler({UnsupportedMediaTypeStatusException.class})
    public ResultDTO handleException(UnsupportedMediaTypeStatusException e) {
        log.error(e.getMessage(), e);
        return ResultDTO.failure("不支持' " + e.getContentType() + "'请求");
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResultDTO handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex.contains(JsonProcessingException.class)) {
            return ResultDTO.failure("json解析错误");
        } else if (ex.getMessage().contains("request body is missing")) {
            return ResultDTO.failure("请求内容不能为空");
        } else if (ex.getCause() != null) {
            return ResultDTO.failure(ex.getCause().getMessage());
        } else {
            return ResultDTO.failure(ex.getMessage());
        }
    }

    /**
     * 请求参数为空异常处理
     *
     * @param ex MissingServletRequestParameterException
     * @return : ResultDTO
     * @author : huangshuanbao
     * @date : 2020/2/12 6:38 下午
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultDTO handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorMsg = ex.getParameterName() + "不能为空";
        log.error("[MissingServletRequestParameterException]接口参数检查不通过，错误信息：{}", errorMsg);
        return ResultDTO.failure(errorMsg);
    }

    /**
     * MethodArgumentNotValidException
     *
     * @param ex MethodArgumentNotValidException
     * @return : ResultDTO
     * @author : huangshuanbao
     * @date : 2020/2/12 6:38 下午
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();
        String errorMsg = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
        log.error("[MethodArgumentNotValidException]接口参数检查不通过，错误信息：{}", errorMsg);
        return ResultDTO.failure(errorMsg);
    }

    /**
     * ConstraintViolationException
     *
     * @param ex ConstraintViolationException
     * @return : ResultDTO
     * @author : huangshuanbao
     * @date : 2020/2/12 6:38 下午
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResultDTO handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        log.error("[ConstraintViolationException]接口参数检查不通过，错误信息：{}", message);
        return ResultDTO.failure(message);
    }

    @ExceptionHandler(value = WebExchangeBindException.class)
    public ResultDTO handleWebExchangeBindException(WebExchangeBindException ex) {

        BindingResult result = ex.getBindingResult();
        String errorMsg = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
        log.error("[WebExchangeBindException]接口参数检查不通过，错误信息：{}", errorMsg);
        return ResultDTO.failure(errorMsg);
    }

    @ExceptionHandler(value = ServerWebInputException.class)
    public ResultDTO handleServerWebInputException(ServerWebInputException ex) {
        String parameterName = ex.getMethodParameter().getParameterName();
        String errorMsg = parameterName + "不能为空";
        log.error("[ServerWebInputException]接口参数检查不通过，错误信息：{}", errorMsg);
        return ResultDTO.failure(errorMsg);
    }

    /**
     * BindException
     *
     * @param ex ConstraintViolationException
     * @return : ResultDTO
     * @author : huangshuanbao
     * @date : 2020/2/12 6:38 下午
     */
    @ExceptionHandler(value = BindException.class)
    public ResultDTO handleBindException(BindException ex) {
        BindingResult result = ex.getBindingResult();
        String errorMsg = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
        log.error("[BindException]接口参数检查不通过，错误信息：{}", errorMsg);
        return ResultDTO.failure(errorMsg);
    }

    /**
     * 捕获并处理未授权异常
     *
     * @param e 授权异常
     * @return 统一封装的结果类, 含有代码code和提示信息msg
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResultDTO handle401(UnauthorizedException e) {
        return ResultDTO.failure("401", e.getMessage());
    }

    // 验证码错误
    @ExceptionHandler(ValidateCodeException.class)
    public ResultDTO handleCaptcha(ValidateCodeException e) {
        return ResultDTO.failure(e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResultDTO handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return ResultDTO.failure("数据库中已存在该记录");
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(IShareException.class)
    public ResultDTO handleWindException(IShareException e) {
        log.error("业务处理异常，错误信息：{}", e.getMessage());
        return ResultDTO.failure(e.getErrorCode(), e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResultDTO notFount(RuntimeException e) {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        log.error("运行时异常:", e);
        return ResultDTO.failure("运行时异常:" + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultDTO handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResultDTO.failure("服务器错误，请联系管理员");
    }

}