package top.dddpeter.ishare.system.service.impl;

import org.springframework.stereotype.Service;
import top.dddpeter.ishare.common.core.text.Convert;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.system.domain.ClientInfo;
import top.dddpeter.ishare.system.mapper.ClientInfoMapper;
import top.dddpeter.ishare.system.service.IClientInfoService;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * clientInfoService业务层处理
 * 
 * @author ishare
 * @date 2019-12-03
 */
@Service
public class ClientInfoServiceImpl implements IClientInfoService 
{
    @Resource
    private ClientInfoMapper clientInfoMapper;

    /**
     * 查询clientInfo
     * 
     * @param id clientInfoID
     * @return clientInfo
     */
    @Override
    public ClientInfo selectClientInfoById(Long id)
    {
        return clientInfoMapper.selectClientInfoById(id);
    }

    /**
     * 查询clientInfo列表
     * 
     * @param clientInfo clientInfo
     * @return clientInfo
     */
    @Override
    public List<ClientInfo> selectClientInfoList(ClientInfo clientInfo)
    {
        return clientInfoMapper.selectClientInfoList(clientInfo);
    }

    /**
     * 新增clientInfo
     * 
     * @param clientInfo clientInfo
     * @return 结果
     */
    @Override
    public int insertClientInfo(ClientInfo clientInfo)
    {
        clientInfo.setCreateTime(DateUtils.getNowDate());
        return clientInfoMapper.insertClientInfo(clientInfo);
    }

    /**
     * 修改clientInfo
     * 
     * @param clientInfo clientInfo
     * @return 结果
     */
    @Override
    public int updateClientInfo(ClientInfo clientInfo)
    {
        clientInfo.setUpdateTime(DateUtils.getNowDate());
        return clientInfoMapper.updateClientInfo(clientInfo);
    }

    /**
     * 删除clientInfo对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteClientInfoByIds(String ids)
    {
        return clientInfoMapper.deleteClientInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除clientInfo信息
     * 
     * @param id clientInfoID
     * @return 结果
     */
    @Override
    public int deleteClientInfoById(Long id)
    {
        return clientInfoMapper.deleteClientInfoById(id);
    }


    @Override
    public String clientSecretGenerate ( ) {
        return UUID.randomUUID().toString().replaceAll("-","").substring(0,20);
    }
}
