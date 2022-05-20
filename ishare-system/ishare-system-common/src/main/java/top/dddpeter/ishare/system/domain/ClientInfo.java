package top.dddpeter.ishare.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.dddpeter.ishare.common.annotation.Excel;
import top.dddpeter.ishare.common.core.domain.BaseEntity;

/**
 * clientInfo对象 client_info
 * 
 * @author ishare
 * @date 2019-12-03
 */
@Data
public class ClientInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** appId */
    @Excel(name = "appId")
    private String appid;

    /** appSecret */
    @Excel(name = "appSecret")
    private String appsecret;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String appname;

    /** 应用简述 */
    @Excel(name = "应用简述")
    private String description;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setAppid(String appid) 
    {
        this.appid = appid;
    }

    public String getAppid() 
    {
        return appid;
    }
    public void setAppsecret(String appsecret) 
    {
        this.appsecret = appsecret;
    }

    public String getAppsecret() 
    {
        return appsecret;
    }
    public void setAppname(String appname) 
    {
        this.appname = appname;
    }

    public String getAppname() 
    {
        return appname;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("appid", getAppid())
            .append("appsecret", getAppsecret())
            .append("appname", getAppname())
            .append("description", getDescription())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
