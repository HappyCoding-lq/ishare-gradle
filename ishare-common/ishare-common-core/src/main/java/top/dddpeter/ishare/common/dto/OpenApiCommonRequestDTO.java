package top.dddpeter.ishare.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.dddpeter.ishare.common.annotation.DateStringValid;
import top.dddpeter.ishare.common.annotation.EnumValid;
import top.dddpeter.ishare.common.enums.ApiTransCodeEnum;
import top.dddpeter.ishare.common.enums.ApiTransSourceEnum;
import top.dddpeter.ishare.common.enums.DatePatternEnum;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.common.utils.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.text.ParseException;

import static top.dddpeter.ishare.common.utils.DateUtils.parseDate;

/**
 * @author hqins
 */
@Data
public class OpenApiCommonRequestDTO implements Serializable {

    private static final long serialVersionUID = -7313012682790189808L;

    /** 交易系统来源 */
    @ApiModelProperty(value = "交易系统来源",required = true)
    @EnumValid(message = "The transSource is wrong", target = ApiTransSourceEnum.class)
    private String transSource;

    /** 交易编码 */
    @ApiModelProperty(value = "交易编码",required = true)
    @EnumValid(message = "The transCode is wrong", target = ApiTransCodeEnum.class)
    private String transCode;

    /** 交易时间 */
    @ApiModelProperty(value = "交易时间",required = true)
    @DateStringValid(message = "The transTime is wrong", datePattern = DatePatternEnum.yyyy_MM_dd_HH_mm_ss)
    private String transTime;

    /** 交易流水号 */
    @ApiModelProperty(value = "交易流水号",required = true)
    @NotBlank(message = "The transNo is wrong")
    private String transNo;

    public void checkSelf(String rightTransCode){
        if(StringUtils.isEmpty(this.transSource) || ApiTransSourceEnum.getEnumByCode(this.transSource)==null){
            throw new IShareException("The transSource is wrong");
        }
        if(StringUtils.isEmpty(this.transCode) || !this.transCode.equals(rightTransCode)){
            throw new IShareException("The transCode is wrong");
        }
        if(StringUtils.isEmpty(this.transTime)){
            throw new IShareException("The transTime is wrong");
        }
        try {
            parseDate(this.transTime, DateUtils.yyyy_MM_dd_HH_mm_ss);
        } catch (ParseException e) {
            throw new IShareException("The transTime is wrong");
        }
        if(StringUtils.isEmpty(this.transNo)){
            throw new IShareException("The transNo is wrong");
        }
    }

    public static void pack(OpenApiCommonRequestDTO dto, String transSource, String transCode) {
        dto.setTransSource(transSource);
        dto.setTransCode(transCode);
        dto.setTransTime(DateUtils.getTime());
        dto.setTransNo(DateUtils.dateTimeNow(DateUtils.yyyyMMddHHmmssSSS));
    }

}
