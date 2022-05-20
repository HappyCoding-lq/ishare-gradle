package top.dddpeter.ishare.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.service.ComponetsService;

import javax.annotation.Resource;


@Api(value = "/componets")
@RestController
@RequestMapping("componets")
public class ComponetsController extends BaseController {
    @Resource
    private ComponetsService componetsService;

    /**
     * 查询url
     * @param componetName
     * @param type
     * @return
     */
    @ApiOperation(value = "通过组件名称和类型来获取访问的url",response = ResultDTO.class, consumes="application/json",produces = "application/json")
    @GetMapping("url/{componetName}/{type}")
    public ResultDTO getComponentsUrl(@ApiParam(value = "组件名称",required = true) @PathVariable("componetName") String componetName,
                                      @ApiParam(value = "组件类型",required = true) @PathVariable("type") String type){
       return result(componetsService.selectComponetUrls(componetName, type));
    }

    /**
     * 查询url
     * @return
     */
    @ApiOperation(value = "获取所有组件的url",response = ResultDTO.class, consumes="application/json",produces = "application/json")
    @GetMapping("all")
    public ResultDTO getAllComponentsUrl(){
        return result(componetsService.selectAllComponetUrls());
    }
}
