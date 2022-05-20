package top.dddpeter.ishare.gateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.common.utils.JsonTrans;
import top.dddpeter.ishare.common.utils.NonceStrUtil;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.common.utils.sql.SqlUtil;
import top.dddpeter.ishare.gateway.constant.CommonConstants;
import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;
import top.dddpeter.ishare.gateway.domain.GatewayRouteObject;
import top.dddpeter.ishare.gateway.domain.GatewayRouteObjectArg;
import top.dddpeter.ishare.gateway.domain.MyRouteDefinition;
import top.dddpeter.ishare.gateway.dto.GenerateApiInfoDTO;
import top.dddpeter.ishare.gateway.dynamic.DynamicRoutePublisherAware;
import top.dddpeter.ishare.gateway.enums.RouteTypeEnum;
import top.dddpeter.ishare.gateway.mapper.ClientInfoMapper;
import top.dddpeter.ishare.gateway.mapper.GatewayRouteDefineMapper;
import top.dddpeter.ishare.gateway.mapper.GatewayRouteObjectArgMapper;
import top.dddpeter.ishare.gateway.mapper.GatewayRouteObjectMapper;
import top.dddpeter.ishare.gateway.service.GatewayRouteService;
import top.dddpeter.ishare.gateway.util.GatewayRoutePublishUtil;
import top.dddpeter.ishare.gateway.vo.ClientInfoVO;
import top.dddpeter.ishare.gateway.vo.GenerateApiInfoVO;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 网关动态路由管理service层实现
 */
@Service
@Slf4j
public class GatewayRouteServiceImpl implements GatewayRouteService {


    @Resource
    private List<MyRouteDefinition> myRouteDefinitionList;

    @Resource
    private GatewayRouteDefineMapper gatewayRouteDefineMapper;

    @Resource
    private GatewayRouteObjectMapper gatewayRouteObjectMapper;

    @Resource
    private GatewayRouteObjectArgMapper gatewayRouteObjectArgMapper;

    @Resource
    private DynamicRoutePublisherAware aware;

    @Resource
    private GatewayRoutePublishUtil gatewayRoutePublishUtil;

    @Resource
    private ClientInfoMapper  clientInfoMapper;

    private static final String ROUTE_NOT_EXIST_ERROR = "路由信息不存在";

    @Override
    public void refreshAll() {
        if(myRouteDefinitionList == null){
            return;
        }
        log.info("*****************动态路由刷新(api调用方式)开始执行*****************");
        if(!myRouteDefinitionList.isEmpty()){
            aware.clearRoutes(myRouteDefinitionList.stream().map(MyRouteDefinition::getRouteDefinition).collect(Collectors.toList()));
            myRouteDefinitionList.clear();
        }
        myRouteDefinitionList.addAll(aware.routeDefinitionPublish(gatewayRouteDefineMapper));
        aware.publish();
        log.debug("route define list: {}", myRouteDefinitionList);
        log.info("*****************动态路由刷新(api调用方式)结束执行*****************");
    }

    @Override
    public List<GatewayRouteDefine> routeList(GatewayRouteDefine gatewayRouteDefine){
        List<GatewayRouteDefine> list = gatewayRouteDefineMapper.selectByExample(this.routeListQueryExample(gatewayRouteDefine));
        list.forEach(GatewayRouteDefine::setDefaultFieldsValue);
        return list;
    }

    @Override
    public int routeListCount(GatewayRouteDefine gatewayRouteDefine){
        return gatewayRouteDefineMapper.selectCountByExample(this.routeListQueryExample(gatewayRouteDefine));
    }

    private Example routeListQueryExample(GatewayRouteDefine gatewayRouteDefine){
        Example example = new Example(GatewayRouteDefine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("routeType", gatewayRouteDefine.getRouteType())
                .andEqualTo("routeUri", gatewayRouteDefine.getRouteUri())
                .andEqualTo("trustedIpsEnable", gatewayRouteDefine.getTrustedIpsEnable())
                .andEqualTo("isEnable", gatewayRouteDefine.getIsEnable())
                .andEqualTo("needCheckAuth", gatewayRouteDefine.getNeedCheckAuth())
                .andEqualTo("isLogAble", gatewayRouteDefine.getIsLogAble())
                .andEqualTo("pageManageOperateAble", gatewayRouteDefine.getPageManageOperateAble());
        if(StringUtils.isNotEmpty(gatewayRouteDefine.getApiTransCode())){
            criteria.andLike("apiTransCode", '%' + gatewayRouteDefine.getApiTransCode() + '%');
        }
        if(StringUtils.isNotEmpty(gatewayRouteDefine.getPredicates())){
            criteria.andLike("predicates", '%' + gatewayRouteDefine.getPredicates() + '%');
        }
        if(StringUtils.isNotEmpty(gatewayRouteDefine.getFilters())){
            criteria.andLike("filters", '%' + gatewayRouteDefine.getFilters() + '%');
        }
        Example.Criteria criteria1 = example.createCriteria();
        if(StringUtils.isNotEmpty(gatewayRouteDefine.getTrustedIps())){
            String[] queryIps = gatewayRouteDefine.getTrustedIps().split(",");
            Arrays.stream(queryIps).forEach(ip->criteria1.orLike("trustedIps", '%' + ip + '%'));
        }
        Example.Criteria criteria2 = example.createCriteria();
        if(StringUtils.isNotEmpty(gatewayRouteDefine.getAuthClients())){
            String[] queryClients = gatewayRouteDefine.getAuthClients().split(",");
            Arrays.stream(queryClients).forEach(client->criteria2.orLike("authClients", '%' + client + '%'));
        }
        example.and(criteria1);
        example.and(criteria2);
        return example;
    }

    @Override
    public void enableRoute(String routeId){
        GatewayRouteDefine define = gatewayRouteDefineMapper.selectByPrimaryKey(routeId);
        if(define == null){
            throw new IShareException(ROUTE_NOT_EXIST_ERROR);
        }
        GatewayRouteDefine updateInfo = new GatewayRouteDefine();
        updateInfo.setRouteId(routeId);
        updateInfo.setIsEnable("YES".equals(define.getIsEnable())?"NO":"YES");
        updateInfo.setUpdateTime(DateUtils.getNowDate());
        gatewayRouteDefineMapper.updateByPrimaryKeySelective(updateInfo);
    }

    @Override
    public String addRoute(GatewayRouteDefine gatewayRouteDefine){
        gatewayRouteDefine.setRouteId(UUID.randomUUID().toString());
        gatewayRouteDefine.setCreateTime(DateUtils.getNowDate());
        gatewayRoutePublishUtil.publishCheckDefine(gatewayRouteDefine);
        gatewayRouteDefineMapper.insertSelective(gatewayRouteDefine);
        return gatewayRouteDefine.getRouteId();
    }

    @Override
    public void updateRoute(GatewayRouteDefine gatewayRouteDefine){
        gatewayRoutePublishUtil.publishCheckDefine(gatewayRouteDefine);
        gatewayRouteDefine.setUpdateTime(DateUtils.getNowDate());
        gatewayRouteDefineMapper.updateByPrimaryKeySelective(gatewayRouteDefine);
    }

    @Override
    public void deleteRoute(String routeId){
        gatewayRouteDefineMapper.deleteByIds(SqlUtil.toInParamByJoinStr(routeId));
    }

    @Override
    public void predicateValidCheck(String predicateStr){
        gatewayRoutePublishUtil.checkPredicateStr(predicateStr);
    }

    @Override
    public void filterValidCheck(String filterStr){
        gatewayRoutePublishUtil.checkFilterStr(filterStr);
    }

    @Override
    public List<GatewayRouteObject> routeObjectListQuery(String objectType){
        Example queryObjectExample = new Example(GatewayRouteObject.class);
        Example.Criteria objectCriteria = queryObjectExample.createCriteria();
        objectCriteria.andEqualTo("objectType", objectType);
        queryObjectExample.setOrderByClause("sort_num");
        List<GatewayRouteObject> objectList = gatewayRouteObjectMapper.selectByExample(queryObjectExample);
        Example queryArgExample = new Example(GatewayRouteObjectArg.class);
        Example.Criteria argCriteria = queryArgExample.createCriteria();
        argCriteria.andIn("objectName", objectList.stream().map(GatewayRouteObject::getObjectName).collect(Collectors.toList()));
        queryArgExample.setOrderByClause("sort_num");
        List<GatewayRouteObjectArg> argList = gatewayRouteObjectArgMapper.selectByExample(queryArgExample);
        for (GatewayRouteObject object : objectList) {
            object.setObjectArgsDefine(new ArrayList<>());
            for (GatewayRouteObjectArg arg : argList) {
                if (object.getObjectName().equalsIgnoreCase(arg.getObjectName())) {
                    object.getObjectArgsDefine().add(arg);
                }
            }
        }
        return objectList;
    }

    @Override
    public void reloadRoute(String routeId){
        List<String> idList = Arrays.asList(routeId.split(","));
        List<GatewayRouteDefine> targetDefineList = gatewayRouteDefineMapper.selectByIds(SqlUtil.toInParamByJoinStr(routeId));
        List<MyRouteDefinition> queriedDefineList = myRouteDefinitionList.stream()
                .filter(myRouteDefinition -> idList.contains(myRouteDefinition.getGatewayRouteDefine().getRouteId()))
                .collect(Collectors.toList());
        if(!queriedDefineList.isEmpty()){
            queriedDefineList.forEach(one->{
                aware.deleteRoute(one.getGatewayRouteDefine().getRouteId());
                myRouteDefinitionList.remove(one);
                aware.publish();
            });
        }
        targetDefineList.forEach(one->{
            if (one != null && "YES".equals(one.getIsEnable())) {
                MyRouteDefinition myRouteDefinition = aware.packRouteDefinition(one);
                if (myRouteDefinition != null) {
                    aware.addRoute(myRouteDefinition.getRouteDefinition());
                    myRouteDefinitionList.add(myRouteDefinition);
                }
                aware.publish();
            }
        });
    }

    @Override
    public void enableAndReloadRoute(String routeId) {
        this.enableRoute(routeId);
        this.reloadRoute(routeId);
    }

    @Override
    public String addAndReloadRoute(GatewayRouteDefine gatewayRouteDefine) {
        String routeId = this.addRoute(gatewayRouteDefine);
        this.reloadRoute(routeId);
        return routeId;
    }

    @Override
    public void updateAndReloadRoute(GatewayRouteDefine gatewayRouteDefine) {
        this.updateRoute(gatewayRouteDefine);
        this.reloadRoute(gatewayRouteDefine.getRouteId());
    }

    @Override
    public void deleteAndReloadRoute(String routeId) {
        this.deleteRoute(routeId);
        this.reloadRoute(routeId);
    }

    @Override
    public List<Map<String, String>> getApiPathList() {
        GatewayRouteDefine query = new GatewayRouteDefine();
        query.setRouteType(RouteTypeEnum.OPEN_API.getCode());
        List<GatewayRouteDefine> routeDefineList = gatewayRouteDefineMapper.select(query);
        return routeDefineList.stream().map(gatewayRouteDefine -> {
            String predicates = gatewayRouteDefine.getPredicates();
            List<PredicateDefinition> definitionList = JsonTrans.fromJsonArray(predicates, PredicateDefinition.class);
            Map<String, String> args = Objects.requireNonNull(definitionList.stream()
                    .filter(predicateDefinition -> "Path".equals(predicateDefinition.getName()))
                    .findAny()
                    .orElse(null)).getArgs();
            Map<String, String> map = new HashMap<>();
            map.put("apiTransCode", gatewayRouteDefine.getApiTransCode());
            map.put("routeDesc", gatewayRouteDefine.getRouteDesc());
            if(args.get("pattern")!=null){
                map.put("path", args.get("pattern"));
            }else{
                map.put("path", args.get("_genkey_0"));
            }
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, String>> getPathInfoList(String serviceId) {
        GatewayRouteDefine query = new GatewayRouteDefine();
        query.setRouteType(RouteTypeEnum.OPEN_API.getCode());
        if(StringUtils.isNotEmpty(serviceId)){
            query.setRouteUri(CommonConstants.SERVICE_URI_PREFIX + serviceId);
        }
        List<GatewayRouteDefine> routeDefineList = gatewayRouteDefineMapper.select(query);
        return routeDefineList.stream().map(gatewayRouteDefine -> {
            Map<String, String> map = new HashMap<>();
            map.put("apiTransCode", gatewayRouteDefine.getApiTransCode());
            map.put("routeDesc", gatewayRouteDefine.getRouteDesc());

            String filters = gatewayRouteDefine.getFilters();
            List<FilterDefinition> definitionList = JsonTrans.fromJsonArray(filters, FilterDefinition.class);
            Map<String, String> args = Objects.requireNonNull(definitionList.stream()
                    .filter(filterDefinition -> "RewritePath".equals(filterDefinition.getName()))
                    .findAny()
                    .orElse(null)).getArgs();
            map.put("apiPath", args.get("regexp"));
            map.put("servicePath", args.get("replacement"));
            return map;
        }).sorted(Comparator.comparing(map -> map.get("apiPath"))).collect(Collectors.toList());
    }

    @Override
    public List<ClientInfoVO> getClientList() {
        List<ClientInfoVO> list = clientInfoMapper.getAllClient();
        ClientInfoVO clientInfoVO = new ClientInfoVO();
        clientInfoVO.setAppId(CommonConstants.DEFAULT_CLIENT_APP_ID);
        clientInfoVO.setAppName(CommonConstants.DEFAULT_CLIENT_APP_NAME);
        if(list.isEmpty()){
            list.add(clientInfoVO);
        }else {
            list.add(0, clientInfoVO);
        }
        return list;
    }

    @Override
    public GenerateApiInfoVO generateApiInfo(GenerateApiInfoDTO dto) {
        String apiPath = "/service/" + dto.getServicePath().substring(1).replace('/', '-');
        int rowCount = gatewayRouteDefineMapper.countApiPath(apiPath);
        GenerateApiInfoVO returnVO = new GenerateApiInfoVO();
        if(rowCount < 1){
            returnVO.setApiPath(apiPath);
            returnVO.setApiTransCode((dto.getServiceId() + '-' + dto.getServicePath().substring(1).replace('/', '-')).toUpperCase());
            return returnVO;
        }else{
            dto.setServicePath(dto.getServicePath() + '/' + NonceStrUtil.getLettersNonceStr(3));
            GenerateApiInfoVO vo = this.generateApiInfo(dto);
            vo.setTips("使用了随机串生成，避免了重复");
            return vo;
        }
    }

}
