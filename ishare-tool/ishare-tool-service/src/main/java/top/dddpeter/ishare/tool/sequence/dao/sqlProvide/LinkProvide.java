package top.dddpeter.ishare.tool.sequence.dao.sqlProvide;


import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;

public class LinkProvide {

//    select * from link where surl like '%${surl}%' and description like '%${description}%'  and create_user like '%${createUser}%' limit #{page},#{size}

    /**
     *
     * @param surl
     * @param description
     * @param createUser
     * @param page
     * @param size
     * @return
     */
    public String getAllLink(@Param("surl") String surl,
                                     @Param("description") String description,
                                     @Param("createUser") String createUser,
                                     @Param("page") int page,
                                     @Param("size") int size){

        StringBuffer sql = new StringBuffer();
        sql.append("select * from link where 1=1");
            if (StringUtils.isNotBlank(surl)) {
                sql.append(" and surl like '%"+surl+"%'");
            }
            if (StringUtils.isNotBlank(description)) {
                sql.append(" and description like '%"+description+"%'");
            }
            if (StringUtils.isNotBlank(createUser)) {
                sql.append(" and create_user like '%"+createUser+"%'");
            }
            sql.append(" order by gmt_create desc ");
            sql.append(" limit "+page+","+size);

            return sql.toString();

    }

}
