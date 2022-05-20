package top.dddpeter.ishare.gateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.common.utils.sql.SqlUtil;
import top.dddpeter.ishare.gateway.domain.GatewayCrossOriginConfig;
import top.dddpeter.ishare.gateway.mapper.GatewayCrossOriginConfigMapper;
import top.dddpeter.ishare.gateway.service.CrossOriginConfigService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 跨域配置service层实现
 */
@Service
@Slf4j
public class CrossOriginConfigServiceImpl implements CrossOriginConfigService {
    
    @Resource
    private GatewayCrossOriginConfigMapper crossOriginConfigMapper;

    @Resource(name="myCrossOriginMap")
    private Map<String, List<GatewayCrossOriginConfig>> myCrossOriginMap;

    @Override
    public List<GatewayCrossOriginConfig> configList(GatewayCrossOriginConfig crossOriginConfig) {
        return crossOriginConfigMapper.selectByExample(this.crossListQueryExample(crossOriginConfig));
    }

    @Override
    public int configListCount(GatewayCrossOriginConfig crossOriginConfig) {
        return crossOriginConfigMapper.selectCountByExample(this.crossListQueryExample(crossOriginConfig));
    }

    private Example crossListQueryExample(GatewayCrossOriginConfig crossOriginConfig){
        Example example = new Example(GatewayCrossOriginConfig.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(crossOriginConfig.getPath())){
            criteria.andLike("path", '%' + crossOriginConfig.getPath() + '%');
        }
        if(StringUtils.isNotEmpty(crossOriginConfig.getAllowOrigin())){
            criteria.andLike("allowOrigin", '%' + crossOriginConfig.getAllowOrigin() + '%');
        }
        if(StringUtils.isNotEmpty(crossOriginConfig.getDescription())){
            criteria.andLike("description", '%' + crossOriginConfig.getDescription() + '%');
        }
        return example;
    }

    @Override
    public void refreshConfig() {
        myCrossOriginMap.clear();
        List<GatewayCrossOriginConfig> configList = crossOriginConfigMapper.selectAll();
        configList.forEach(gatewayCrossOriginConfig->{
            myCrossOriginMap.computeIfAbsent(gatewayCrossOriginConfig.getPath(), k -> new ArrayList<>());
            myCrossOriginMap.get(gatewayCrossOriginConfig.getPath()).add(gatewayCrossOriginConfig);
        });
    }

    @Override
    public Long addConfig(GatewayCrossOriginConfig crossOriginConfig) {
        crossOriginConfig.setCreateTime(DateUtils.getNowDate());
        crossOriginConfigMapper.insertSelective(crossOriginConfig);
        this.reloadConfig(crossOriginConfig.getId()+"");
        return crossOriginConfig.getId();
    }

    @Override
    public void updateConfig(GatewayCrossOriginConfig crossOriginConfig) {
        crossOriginConfig.setUpdateTime(DateUtils.getNowDate());
        crossOriginConfigMapper.updateByPrimaryKeySelective(crossOriginConfig);
        this.reloadConfig(crossOriginConfig.getId()+"");
    }

    @Override
    public void deleteConfig(String ids) {
        crossOriginConfigMapper.deleteByIds(SqlUtil.toInParamByJoinStr(ids));
        this.reloadConfig(ids);
    }

    @Override
    public void reloadConfig(String id){
        List<String> idList = Arrays.asList(id.split(","));
        List<GatewayCrossOriginConfig> queriedList = new ArrayList<>();
        myCrossOriginMap.keySet().forEach(key-> myCrossOriginMap.get(key).forEach(one->{
            if(idList.contains(one.getId()+"")){
                queriedList.add(one);
            }
        }));
        queriedList.forEach(one->{
            myCrossOriginMap.get(one.getPath()).remove(one);
            if(myCrossOriginMap.get(one.getPath()).isEmpty()){
                myCrossOriginMap.remove(one.getPath());
            }
        });
        List<GatewayCrossOriginConfig> targetList = crossOriginConfigMapper.selectByIds(SqlUtil.toInParamByJoinStr(id));
        targetList.forEach(one->{
            List<GatewayCrossOriginConfig> list = myCrossOriginMap.get(one.getPath());
            if(list==null){
                list = new ArrayList<>();
                list.add(one);
                myCrossOriginMap.put(one.getPath(), list);
            }else{
                list.add(one);
            }
        });
    }

    @Override
    public Set<Map<String, String>> pathList(String path) {
        if(StringUtils.isEmpty(path)){
            return crossOriginConfigMapper.selectAll().stream().map(cross-> Collections.singletonMap("path", cross.getPath())).collect(Collectors.toSet());
        }else{
            Example condition = new Example(GatewayCrossOriginConfig.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andLike("path", '%' + path + '%');
            return crossOriginConfigMapper.selectByExample(condition).stream().map(cross-> Collections.singletonMap("path", cross.getPath())).collect(Collectors.toSet());
        }
    }

    @Override
    public List<GatewayCrossOriginConfig> pathCrossList(String path) {
        GatewayCrossOriginConfig condition = new GatewayCrossOriginConfig();
        condition.setPath(path);
        return crossOriginConfigMapper.select(condition);
    }

    @Override
    public String deletePathCross(String path) {
        Example condition = new Example(GatewayCrossOriginConfig.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andIn("path", Arrays.asList(path.split(",")));
        String ids = crossOriginConfigMapper.selectByExample(condition).stream().map(cross->cross.getId()+"").collect(Collectors.joining(","));
        crossOriginConfigMapper.deleteByExample(condition);
        this.reloadConfig(ids);
        return ids;
    }

}