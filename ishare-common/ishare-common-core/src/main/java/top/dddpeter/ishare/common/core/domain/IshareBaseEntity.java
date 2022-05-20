package top.dddpeter.ishare.common.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.dddpeter.ishare.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 *
 * @author ishare
 */
@Data
public class IshareBaseEntity implements Serializable {

    private static final long serialVersionUID = 2910888146955061510L;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建者编码
     */
    @Excel(name = "创建者编码")
    private String creatorId;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新者编码
     */
    @Excel(name = "更新者编码")
    private String updaterId;

    /**
     * 是否删除
     */
    @Excel(name = "是否删除")
    private String isDeleted;
}
