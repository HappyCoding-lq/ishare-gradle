package top.dddpeter.ishare.tool.sequence.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.tool.sequence.dto.LinkDTO;
import top.dddpeter.ishare.tool.sequence.service.LinkService;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping("/link")
public class LinkController {
     @Resource
     private LinkService linkService;

     /**
      * 编辑修改
      * @param link
      * @param id
      * @return
      */
     @RequestMapping("/addOrUpdate")
     public ResultDTO addOrUpdateLink(@RequestBody LinkDTO link, @RequestParam(required = false,defaultValue = "0") int id){
          //修改
          if(id>0 & StringUtils.isNotNull(link)){
               Integer updateLink = linkService.updateLink(id, link.getDescription(), link.getLurl());
               if(updateLink>0){
                    return ResultDTO.success("200","修改成功");
               }

               return ResultDTO.failure("500","服务器异常");
          }
          Integer addLink = linkService.addLink(link);
          if(addLink>0){
               return ResultDTO.success("200","新增成功");
          }
          return ResultDTO.failure("500","服务器异常");
     }

     /**
      * 编辑 查看具体信息
      * @param id
      * @return
      */
     @RequestMapping("/seeLink")
     public LinkDTO  getLinkById(int id){
          return linkService.getLinkById(id);
     }

     /**
      * 删除链接
      * @param id
      * @return
      */
     @RequestMapping("/deleteLink")
     public ResultDTO deleteLink (int id){
          if(linkService.deleteLink(id)>0){
               return ResultDTO.success("200","删除成功");
          }
          return ResultDTO.failure("500","服务器异常");
     }


     /**
      * 模糊查询
      * @param lurl
      * @param description
      * @param createUser
      * @param pageSize
      * @param pageNum
      * @return
      */
     @RequestMapping("/all")
     public ResultDTO getAllLink(@RequestParam(defaultValue = "",required = false) String lurl,
                                     @RequestParam(defaultValue = "",required = false) String description,
                                     @RequestParam(defaultValue = "",required = false)String createUser,
                                     int pageNum,
                                     int pageSize){

          HashMap<String, Object> allLink = linkService.getAllLink(lurl, description, createUser, pageNum, pageSize);
          allLink.put("pageNum",pageNum);
          return ResultDTO.success("200","ok",allLink);

     }


     /**
      * 短链接-长链接跳转
      * @return
      */
     @RequestMapping("/jump/{key}")
     public ModelAndView redirect(@PathVariable String key){
          LinkDTO linkBySurl = linkService.getLinkByKey(key);
          return new ModelAndView("redirect:"+linkBySurl.getLurl());
     }

     @RequestMapping("/common/saveLink")
     public ResultDTO saveLinkDTO(@RequestBody LinkDTO link) {
          return ResultDTO.success(this.linkService.saveLinkDTO(link));
     }








}
