package top.dddpeter.ishare.tool.sequence.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class LinkDTO implements Serializable {

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
