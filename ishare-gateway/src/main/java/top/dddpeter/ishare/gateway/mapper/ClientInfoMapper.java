package top.dddpeter.ishare.gateway.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.dddpeter.ishare.gateway.vo.ClientInfoVO;

import java.util.List;

@Mapper
public interface ClientInfoMapper {
    @Select("select  appId,AES_DECRYPT(UNHEX(appSecret), appId) as appSecret,appName,description from client_info order by appId")
    List<ClientInfoVO> getAllClient();

    @Select("select appId,AES_DECRYPT(UNHEX(appSecret), appId) as appSecret,appName,description from client_info where appId=#{appId}")
   ClientInfoVO getClientById(@Param("appId")String appId);
}
