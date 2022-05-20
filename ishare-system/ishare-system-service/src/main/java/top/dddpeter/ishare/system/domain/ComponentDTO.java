package top.dddpeter.ishare.system.domain;

import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.ColumnType;

@Data
@ToString
public class ComponentDTO {
    @ColumnType(column = "componet_name")
    private String componetName;
    private String type;
    private String url;
}
