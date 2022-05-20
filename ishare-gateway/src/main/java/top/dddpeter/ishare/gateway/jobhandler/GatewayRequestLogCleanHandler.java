package top.dddpeter.ishare.gateway.jobhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.JsonTrans;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.gateway.domain.GatewayRequestLog;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.handler.IJobHandler;
import top.dddpeter.ishare.job.handler.annotation.ClassJobHandler;
import top.dddpeter.ishare.job.log.JobLogger;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Map;

/**
 * 网关请求日志定期清除任务调度任务
 *
 * @author : huangshuanbao
 */
@Slf4j
@ClassJobHandler(value = "gateway_request_log_clean_handler")
@Component
public class GatewayRequestLogCleanHandler extends IJobHandler {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public ReturnT<String> execute(String paramStr) throws Exception {
        JobLogger.log("======>开始执行网关请求日志定期清除任务调度任务, 任务参数：{}<======", paramStr);
        log.info("======>开始执行网关请求日志定期清除任务调度任务, 任务参数：{}<======", paramStr);
        String days = null;
        try{
            Map paramMap = JsonTrans.fromJson(paramStr, Map.class);
            days = (String) paramMap.get("days");
        }catch (Exception ex){
            //
        }
        try {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, StringUtils.isNotEmpty(days)?Integer.parseInt(days) * -1 : -7);
            Query query = new Query();
            query.addCriteria(Criteria.where("requestTime").lte(c.getTime()));
            mongoTemplate.remove(query, GatewayRequestLog.class);
            log.info("======>网关请求日志定期清除任务调度任务执行完毕<======");
            JobLogger.log("======>网关请求日志定期清除任务调度任务执行完毕<======");
            return SUCCESS;
        } catch (Exception e) {
            log.error("网关请求日志定期清除任务调度任务执行异常:{}", e.getMessage());
            JobLogger.log(new IShareException("网关请求日志定期清除任务调度任务执行异常:" + e.getMessage()));
            return FAIL;
        }
    }

}
