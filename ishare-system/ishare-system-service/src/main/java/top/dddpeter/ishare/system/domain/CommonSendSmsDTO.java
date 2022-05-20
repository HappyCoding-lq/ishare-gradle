package top.dddpeter.ishare.system.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * 通用短信发送请求DTO
 */
@Data
public class CommonSendSmsDTO {

    @NotBlank(message = "短信模板号不能为空")
    private String templateNo;

    @NotEmpty(message = "手机号不能为空")
    private List<String> phonesList;

    @NotEmpty(message = "短信内容不能为空")
    private Map<String, String> param;

}
