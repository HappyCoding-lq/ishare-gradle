package top.dddpeter.ishare.system.domain;

import lombok.Data;

import java.util.List;

/**
 * @auther Yoko
 * @date 2020/12/25
 */
@Data
public class EmailInfoDTO {

    private String templateNo;
    private List<String> toEmailList;
    private String businessTypeMsg;
    private String customerName;
}
