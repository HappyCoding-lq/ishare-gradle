package top.dddpeter.ishare.tool.sequence.dto;

import lombok.*;
import top.dddpeter.ishare.common.core.domain.IshareBaseEntity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Date;

/**
 * 休假日-调休工作日表
 *
 * @author: wangmaolin
 * @date: 2020/6/5 9:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "day_off", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class DayOffDTO extends IshareBaseEntity implements Serializable {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 日期
     */
    private Date dayOff;

    /**
     * 日期说明
     */
    private String remark;

    /**
     * 日期类型：0-调休工作日，1-休假日
     */
    private Integer type;
}
