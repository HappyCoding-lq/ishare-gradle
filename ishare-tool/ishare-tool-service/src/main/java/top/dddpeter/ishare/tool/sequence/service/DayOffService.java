package top.dddpeter.ishare.tool.sequence.service;

import org.springframework.web.bind.annotation.RequestParam;
import top.dddpeter.ishare.tool.sequence.dto.DayOffDTO;

/**
 * 休假日-调休工作日服务
 * @author: wangmaolin
 * @date: 2020/6/5 10:04
 */
public interface DayOffService {

    DayOffDTO selectByDate(@RequestParam("date") String date);
}
