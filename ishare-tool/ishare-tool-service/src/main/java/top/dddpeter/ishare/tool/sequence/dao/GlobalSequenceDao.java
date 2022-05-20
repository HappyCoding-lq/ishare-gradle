package top.dddpeter.ishare.tool.sequence.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.dddpeter.ishare.tool.sequence.dto.GlobalSequenceDTO;

import java.util.List;

@Mapper
public interface GlobalSequenceDao {
    @Select("select name,value from x_sequence_global")
    List<GlobalSequenceDTO> findAllSequence();
}
