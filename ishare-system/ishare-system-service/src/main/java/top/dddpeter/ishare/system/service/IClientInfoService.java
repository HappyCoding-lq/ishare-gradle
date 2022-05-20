package top.dddpeter.ishare.system.service;

import top.dddpeter.ishare.system.domain.ClientInfo;

import java.util.List;

/**
 * clientInfoService接口
 * 
 * @author ishare
 * @date 2019-12-03
 */
public interface IClientInfoService 
{
    /**
     * 查询clientInfo
     * 
     * @param id clientInfoID
     * @return clientInfo
     */
    public ClientInfo selectClientInfoById(Long id);

    /**
     * 查询clientInfo列表
     * 
     * @param clientInfo clientInfo
     * @return clientInfo集合
     */
    public List<ClientInfo> selectClientInfoList(ClientInfo clientInfo);

    /**
     * 新增clientInfo
     * 
     * @param clientInfo clientInfo
     * @return 结果
     */
    public int insertClientInfo(ClientInfo clientInfo);

    /**
     * 修改clientInfo
     * 
     * @param clientInfo clientInfo
     * @return 结果
     */
    public int updateClientInfo(ClientInfo clientInfo);

    /**
     * 批量删除clientInfo
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteClientInfoByIds(String ids);

    /**
     * 删除clientInfo信息
     * 
     * @param id clientInfoID
     * @return 结果
     */
    public int deleteClientInfoById(Long id);


    /**
     * secret 生成
     * @return
     */
    public String  clientSecretGenerate();
}
