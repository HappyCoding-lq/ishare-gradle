package top.dddpeter.ishare.common.utils;

import java.io.Serializable;

public class AbstractReturnCode implements Serializable {


    private String name;
    private String desc;
    private String code;
    private String service;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 初始化一个对外暴露的ReturnCodeSuper(用于客户端异常处理)
     */
    public AbstractReturnCode(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

    /**
     * 初始化一个不对外暴露的ReturnCodeSuper(仅用于服务端数据分析)
     */
    public AbstractReturnCode(String code) {
        this.desc = null;
        this.code = code;
    }

    /**
     * 初始化一个不对外暴露的ReturnCodeSuper(仅用于服务端数据分析)
     */
    public AbstractReturnCode() {
        this.desc = null;
        this.code = "-1";
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
