package top.dddpeter.ishare.common.dto;

import io.swagger.annotations.ApiModelProperty;
import top.dddpeter.ishare.common.enums.ResponseCodeEnum;
import top.dddpeter.ishare.common.utils.AbstractReturnCode;

import java.io.Serializable;

/**
 * 通用响应结果DTO
 *
 * @param
 * @author 徐
 * @date 2019-12-24
 */
public class ResultDTO implements Serializable {

    private static final long serialVersionUID = 5065009887439496906L;

    @ApiModelProperty("响应是否成功")
    private boolean success;
    @ApiModelProperty("响应消息")
    private String message;
    @ApiModelProperty("响应编码")
    private String code;
    @ApiModelProperty("响应内容")
    private Object data;


    public static ResultDTO success() {
        return new ResultDTO(true, ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage());
    }

    public static ResultDTO success(String massage) {
        return new ResultDTO(true, ResponseCodeEnum.SUCCESS.getCode(), massage);
    }

    public static ResultDTO success(Object data) {
        return new ResultDTO(true, ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage(), data);
    }

    public static ResultDTO success(String code, String message) {
        return new ResultDTO(true, code, message);
    }

    public static ResultDTO success(String code, String message, Object data) {
        return new ResultDTO(true, code, message, data);
    }


    public static ResultDTO failure() {
        return new ResultDTO(false, ResponseCodeEnum.ERROR.getCode(), ResponseCodeEnum.ERROR.getMessage());
    }

    public static ResultDTO failure(String message) {
        return new ResultDTO(false, ResponseCodeEnum.ERROR.getCode(), message);
    }

    public static ResultDTO failure(String code, String message) {
        return new ResultDTO(false, code, message);
    }

    public static ResultDTO failure(AbstractReturnCode returnCode) {
        ResultDTO result = new ResultDTO();
        result.setCode(returnCode.getCode());
        result.setMessage(returnCode.getDesc());
        return result;
    }

    public static ResultDTO failure(AbstractReturnCode returnCode, String message) {
        ResultDTO result = new ResultDTO();
        result.setCode(returnCode.getCode());
        result.setMessage(message);
        return result;
    }

    public ResultDTO() {
    }

    public ResultDTO(boolean success, String code, String message) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public ResultDTO(boolean success, String code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDTO [success=" + success + ", message=" + message + ", code=" + code + ", data=" + data + "]";
    }

}
