package top.dddpeter.ishare.gateway.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import top.dddpeter.ishare.common.core.page.PageDomain;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.common.utils.JsonTrans;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.gateway.domain.GatewayRequestLog;
import top.dddpeter.ishare.gateway.dto.GatewayRequestLogListQueryDTO;
import top.dddpeter.ishare.gateway.enums.RouteTypeEnum;
import top.dddpeter.ishare.gateway.service.RequestLogService;
import top.dddpeter.ishare.gateway.vo.GatewayRequestLogListQueryVO;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * 网关请求日志处理service层实现
 */
@Service
public class RequestLogServiceImpl implements RequestLogService {

    private static final String[] REGEX_ESCAPE_CHARACTER = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<GatewayRequestLogListQueryVO> list(GatewayRequestLogListQueryDTO queryDTO, PageDomain pageDomain) {
        return mongoTemplate.find(this.packListQuery(queryDTO, pageDomain), GatewayRequestLogListQueryVO.class, "gateway_request_log");
    }

    @Override
    public int listCount(GatewayRequestLogListQueryDTO queryDTO) {

        return (int)mongoTemplate.count(this.packListQuery(queryDTO, new PageDomain()), GatewayRequestLogListQueryVO.class, "gateway_request_log");
    }

    @Override
    public GatewayRequestLog detail(String logId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Fields.UNDERSCORE_ID).is(logId));
        return mongoTemplate.findOne(query, GatewayRequestLog.class);
    }

    @Override
    public void delete(String logIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Fields.UNDERSCORE_ID).in((Object[]) logIds.split(",")));
        mongoTemplate.remove(query, GatewayRequestLog.class);
    }

    @Override
    public void clean(String routeType, Integer days) {
        if(days<=0 && StringUtils.isEmpty(routeType)){
            mongoTemplate.dropCollection(GatewayRequestLog.class);
        } else {
            Query query = new Query();
            if(StringUtils.isNotEmpty(routeType)){
                query.addCriteria(Criteria.where("routeType").is(routeType));
            }
            // 保留days天日志（包含当天）
            if(days>0){
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(DateUtils.parseDate(DateUtils.dateToString("yyyy-MM-dd 00:00:00"), DateUtils.yyyy_MM_dd_HH_mm_ss));
                } catch (ParseException e) {
                    throw new IShareException(e.getMessage());
                }
                c.add(Calendar.DAY_OF_YEAR, (days-1) * -1);
                query.addCriteria(Criteria.where("requestTime").lte(c.getTime()));
            }
            mongoTemplate.remove(query, GatewayRequestLog.class);
        }
    }

    private Query packListQuery(GatewayRequestLogListQueryDTO queryDTO, PageDomain pageDomain) {
        Query query = new Query();
        if(StringUtils.isNotEmpty(queryDTO.getRouteType())){
            query.addCriteria(Criteria.where("routeType").is(queryDTO.getRouteType()));
        }
        if(StringUtils.isNotEmpty(queryDTO.getServiceId())){
            query.addCriteria(Criteria.where("serviceId").is(queryDTO.getServiceId()));
        }
        if(StringUtils.isNotEmpty(queryDTO.getAppId())){
            query.addCriteria(Criteria.where("appId").is(queryDTO.getAppId()));
        }
        if(StringUtils.isNotEmpty(queryDTO.getLoginName())){
            query.addCriteria(Criteria.where("loginName").is(queryDTO.getLoginName()));
        }
        if(StringUtils.isNotEmpty(queryDTO.getPath())){
            Criteria criteria = Criteria.where("path");
            if(RouteTypeEnum.OPEN_API.getCode().equals(queryDTO.getRouteType())){
                criteria.is(queryDTO.getPath());
            }else{
                String path = queryDTO.getPath();
                if(path.contains("/**")){
                    path = queryDTO.getPath().replace("/**", "(/.+)+");
                }else if(path.contains("/*")){
                    path = queryDTO.getPath().replace("/*", "/.+");
                }else if(path.contains("{") && path.contains("}")){
                    path = queryDTO.getPath().replaceAll("\\{.+\\}", ".+");
                }
                criteria.regex(path);
            }
            query.addCriteria(criteria);
        }
        if(StringUtils.isNotNull(queryDTO.getRequestTimeBegin()) && StringUtils.isNotNull(queryDTO.getRequestTimeEnd())){
            query.addCriteria(Criteria.where("requestTime").gte(queryDTO.getRequestTimeBegin()).lte(queryDTO.getRequestTimeEnd()));
        }
        if(StringUtils.isNotEmpty(queryDTO.getRequestInfo())){
            queryDTO.setRequestInfo(this.escapeCharacterForRegex(queryDTO.getRequestInfo()));
            query.addCriteria(Criteria.where("requestInfo").regex(queryDTO.getRequestInfo()));
        }
        if(StringUtils.isNotEmpty(queryDTO.getResponseInfo())){
            queryDTO.setResponseInfo(this.escapeCharacterForRegex(queryDTO.getResponseInfo()));
            query.addCriteria(Criteria.where("responseInfo").regex(queryDTO.getResponseInfo()));
        }
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
            LinkedHashMap<String, Integer> sortMap = JsonTrans.fromJson(pageDomain.getOrderBy(), LinkedHashMap.class);
            for (Map.Entry<String, Integer> item : sortMap.entrySet()) {
                query.with(new Sort(item.getValue()==1?Sort.Direction.ASC:Sort.Direction.DESC, item.getKey()));
            }
        }
        if (pageDomain.getPageNum()!=null && pageDomain.getPageSize()!=null) {
            int limit = pageDomain.getPageSize();
            int skip = (pageDomain.getPageNum() - 1) * pageDomain.getPageSize();
            query.skip(skip);
            query.limit(limit);
        }
        return query;
    }

    private String escapeCharacterForRegex(String sourceStr) {
        final String[] finalStrArray = {sourceStr};
        Arrays.stream(REGEX_ESCAPE_CHARACTER).forEach(key->{
            if (finalStrArray[0].contains(key)) {
                finalStrArray[0] = finalStrArray[0].replace(key, "\\" + key);
            }
        });
        return finalStrArray[0];
    }

}
