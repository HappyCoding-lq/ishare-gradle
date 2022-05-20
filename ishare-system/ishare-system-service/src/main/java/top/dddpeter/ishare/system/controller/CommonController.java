package top.dddpeter.ishare.system.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.dddpeter.ishare.common.constant.EnumTypeConstants;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.enums.BankCodeEnum;
import top.dddpeter.ishare.common.enums.IdCardTypeEnum;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.common.utils.EnumUtils;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.common.utils.ToolUtil;
import top.dddpeter.ishare.common.utils.file.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 通用请求处理
 * 
 * @author ishare
 */
@Controller
@RequestMapping("/common")
@Slf4j
public class CommonController {

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.isValidFilename(fileName))
            {
                throw new IShareException(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = DateUtils.dateTimeNow() + fileName.substring(fileName.indexOf('_') + 1);
            String filePath = ToolUtil.getDownloadPath() + fileName;
            response.setCharacterEncoding("utf-8");
            // 下载使用"application/octet-stream"更标准
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete.booleanValue())
            {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            throw new IShareException("下载文件失败");
        }
    }

    /**
     * 证件类型字典
     *
     * @author : huangshuanbao
     * @date : 2020/3/3 12:41 下午
     */
    @GetMapping("/enum")
    @ApiOperation(value = "枚举类型字典查询接口", notes = "枚举类型字典查询接口", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, response = ResultDTO.class)
    @ResponseBody
    public ResultDTO commonGetEnumMap(@RequestParam("enumType") String enumType) {
        switch (enumType) {
            // 证件类型
            case EnumTypeConstants.IDCARD :
                return ResultDTO.success(EnumUtils.enumToMap(IdCardTypeEnum.class, null, null));
            // 银行编码
            case EnumTypeConstants.BANK :
                return ResultDTO.success(EnumUtils.enumToMap(BankCodeEnum.class, null, null));
            default:
                return ResultDTO.failure();
        }
    }

}