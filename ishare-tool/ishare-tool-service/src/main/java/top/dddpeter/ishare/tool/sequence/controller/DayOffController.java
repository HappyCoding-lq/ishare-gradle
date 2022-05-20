package top.dddpeter.ishare.tool.sequence.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.tool.sequence.dto.DayOffDTO;
import top.dddpeter.ishare.tool.sequence.service.DayOffService;

import javax.annotation.Resource;
import java.util.Date;


/**
 * 休假日-调休工作日控制器
 *
 * @author wangmaolin
 * @date 2020/6/5 10:24
 */
@Api(value = "休假日-调休工作日")
@Slf4j
@RestController
public class DayOffController {
    @Resource
    private DayOffService dayOffService;

    @RequestMapping(value = "/checkDayOff/{date}", method = RequestMethod.GET)
    @ApiOperation(value = "查询日期节假日属性", httpMethod = "GET", response = DayOffDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "date", value = "日期", required = true, dataType = "query")})
    public ResultDTO checkDayOff(@PathVariable String date) {
        log.info("DayOffController.checkDayOff:入参date={}", date);
        DayOffDTO offDTO = new DayOffDTO();
        try {
            Date day = DateUtils.parseDate(date);
            offDTO = dayOffService.selectByDate(date);
            log.info("DayOffController.checkDayOff:调用core返回结果:dayOffResult={}", JSON.toJSONString(offDTO));
            if (offDTO != null) {
                //休假日表中有该日期数据，说明是公休日或调休日
            } else if(day!=null) {
                //表中无数据，按正常周六日计算工作休假
                Integer type = DateUtils.checkDayOff(day) ? 1 : 0;
                String remark = DateUtils.checkDayOff(day) ? "周六日" : "工作日";
                offDTO = new DayOffDTO();
                offDTO.setDayOff(day);
                offDTO.setType(type);
                offDTO.setRemark(remark);
            }else {
                ResultDTO.failure("查询错误  请使用 {" +
                        "            \"yyyy-MM-dd\", \"yyyy-MM-dd HH:mm:ss\", \"yyyy-MM-dd HH:mm\", " +
                        "            \"yyyy/MM/dd\", \"yyyy/MM/dd HH:mm:ss\", \"yyyy/MM/dd HH:mm\", " +
                        "            \"yyyy.MM.dd\", \"yyyy.MM.dd HH:mm:ss\", \"yyyy.MM.dd HH:mm\",  } 类型的数据请求");
            }
            return ResultDTO.success(offDTO);
        } catch (Exception e) {
            log.error("DayOffController.checkDayOff:程序异常,e:", e);
            return ResultDTO.failure("查询异常");
        }
    }
}
