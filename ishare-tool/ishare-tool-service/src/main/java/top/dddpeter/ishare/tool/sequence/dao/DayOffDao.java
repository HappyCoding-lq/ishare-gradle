package top.dddpeter.ishare.tool.sequence.dao;

import org.apache.ibatis.annotations.Select;
import top.dddpeter.ishare.common.core.dao.BaseMapper;
import top.dddpeter.ishare.tool.sequence.dto.DayOffDTO;


/**
 * @author lzj10\
 * @create 2020-08-24-宪9:32
 */
public interface DayOffDao extends BaseMapper<DayOffDTO> {
    @Select(" select `id`, `day_off`, `remark`, `type`, `creator_id`, `create_time`, `updater_id`, `update_time`, `is_deleted` from day_off" +
            "    where day_off = #{date}")
    DayOffDTO selectByDate(String date);
}
