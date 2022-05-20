package top.dddpeter.ishare.tool.feign.domain;


import lombok.Data;

import java.io.Serializable;

/**
 * @description 短链接实体对象
 */
@Data
public class CommonLink implements Serializable {

    private int id;

    private String  description;

    private String lurl;

    private String surl;

    private String gmtCreate;

    private String createUser;

    private String updateTime;

    private String hashKey;

    private Integer length;

}
