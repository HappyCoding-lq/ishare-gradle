package top.dddpeter.ishare.tool.sequence.service;

import top.dddpeter.ishare.tool.sequence.dto.LinkDTO;

import java.util.HashMap;


public interface LinkService {

    /**
     * 添加短链接
     * @param link
     * @return
     */
    Integer addLink(LinkDTO link);

    /**
     * 修改短链接
     * @param id
     * @param description
     * @param surl
     * @return
     */
    Integer updateLink(int id,String description,String surl);

    /**
     * 查询短链是不是重复
     * @return
     */
    LinkDTO getLinkByKey(String key);


    /**
     * 编辑查看页面
     * @param id
     * @return
     */
    LinkDTO getLinkById(int id);


    /**
     * 删除链接
     * @param id
     * @return
     */
    Integer deleteLink(int id);


    /**
     * 查询所有
     * @param description
     * @param createUser
     * @param page
     * @param size
     * @return
     */
    HashMap<String, Object> getAllLink(String surl, String description, String createUser, int page, int size);

    /**
     * @description 保存短链信息并返回其对象信息
     */
    LinkDTO saveLinkDTO(LinkDTO linkDTO);

}
