package top.dddpeter.ishare.tool.sequence.dao;


import org.apache.ibatis.annotations.*;
import top.dddpeter.ishare.tool.sequence.dao.sqlProvide.LinkProvide;
import top.dddpeter.ishare.tool.sequence.dto.LinkDTO;

import java.util.List;


/**
 * huangwei
 */

@Mapper
public interface LinkDao {


    @Insert("insert into link (description,lurl,surl,gmt_create,create_user,hash_key) values(#{link.description},#{link.lurl},#{link.surl},#{link.gmtCreate},#{link.createUser},#{link.hashKey})")
    Integer addLink(@Param("link") LinkDTO link);


    @Update("update link set description=#{description} , lurl=#{lurl},update_time=#{updateTime} where id = #{id}" )
    Integer updateLink(@Param("id")int id,@Param("description") String description,@Param("lurl")String lurl,@Param("updateTime") String updateTime);


    @Select("select * from link where hash_key = #{key}")
    LinkDTO getLinkByKey(String key);


    /**
     * 编辑查看页面
      */

    @Select("select * from link where id = #{id}")
    LinkDTO getLinkById(int id);


    @Delete("delete  from link where id=#{id}")
    Integer deleteLink(int id);

    @Select("select count(*) from link ")
    Integer selectTotal();

    @SelectProvider(type = LinkProvide.class,method = "getAllLink")
    @Results(id="linkMap",value={
            @Result(id=true,column = "id",property = "id"),
            @Result(column = "description",property = "description"),
            @Result(column = "lurl",property = "lurl"),
            @Result(column = "surl",property = "surl"),
            @Result(column = "gmt_create",property = "gmtCreate"),
            @Result(column = "create_user",property = "createUser"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "hash_key",property = "hashKey"),
    })

    List<LinkDTO>  getAllLink(@Param("surl") String surl,@Param("description") String description, @Param("createUser") String createUser,@Param("page") int page, @Param("size") int size);



}
