package top.dddpeter.ishare.tool.sequence.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.dddpeter.ishare.tool.sequence.dao.DayOffDao;
import top.dddpeter.ishare.tool.sequence.dto.DayOffDTO;
import top.dddpeter.ishare.tool.sequence.service.DayOffService;

import javax.annotation.Resource;

/**
 * 休假日-调休工作日
 * @author: wangmaolin
 * @date: 2020/6/5 10:12
 */
@Service
@Slf4j
@RestController
public class DayOffServiceImpl implements DayOffService {
    @Resource
    private DayOffDao dayOffDao;

    @Override
    public DayOffDTO selectByDate(@RequestParam("date") String date) {
        log.info("DayOffServiceImpl.selectByDate:date={}",date);
        try{
            return dayOffDao.selectByDate(date);
        } catch (Exception e){
            log.error("DayOffServiceImpl.selectByDate程序异常：",e);
            return null;
        }
    }
}
