package top.dddpeter.ishare.common.core.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页数据
 * 
 * @author ishare
 */
@Setter
@Getter
@ToString
public class PageDomain
{
    /** 当前记录起始索引 */
    @ApiModelProperty("当前页码")
    private Integer pageNum;

    /** 每页显示记录数 */
    @ApiModelProperty("每页显示记录数")
    private Integer pageSize;

    @ApiModelProperty("排序参数串，包括字段、升序降序类型 比如传值'order_id desc'")
    private String orderBy;
}
