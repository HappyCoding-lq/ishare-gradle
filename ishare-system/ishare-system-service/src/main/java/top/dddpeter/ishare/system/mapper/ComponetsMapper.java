package top.dddpeter.ishare.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.dddpeter.ishare.system.domain.ComponentDTO;

import java.util.List;

public interface ComponetsMapper {
    @Select("select componet_name as componetName,type,url from componets where componet_name=#{componet_name} and type=#{type}")
    List<ComponentDTO> getComponetUrls(@Param("componet_name") String compnents, @Param("type") String type);
    @Select("select componet_name as componetName,type,url from componets ")
    List<ComponentDTO> getAllComponetUrls();
}
