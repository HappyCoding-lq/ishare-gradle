package top.dddpeter.ishare.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.dddpeter.ishare.common.core.dao.BaseMapper;
import top.dddpeter.ishare.system.domain.MsgChannel;

/**
 * 消息通道mapper
 */
@Mapper
public interface MsgChannelMapper extends BaseMapper<MsgChannel> {
    //
}
