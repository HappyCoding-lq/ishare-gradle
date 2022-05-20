package top.dddpeter.ishare.system.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * 通用邮件发送请求DTO
 */
@Data
public class CommonSendEmailDTO {

    @NotBlank(message = "邮件模板号不能为空")
    private String templateNo;

    @NotEmpty(message = "收件邮箱不能为空")
    private List<String> emailList;

    @NotEmpty(message = "邮件内容不能为空")
    private Map<String, String> param;

}
