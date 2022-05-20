package top.dddpeter.ishare.gateway.bank.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.dddpeter.ishare.common.core.dao.BaseMapper;
import top.dddpeter.ishare.gateway.bank.po.BankInfo;

/**
 * @author lijinde
 * @create 2022/4/7 15:57
 **/
@Mapper
public interface BankInfoMapper extends BaseMapper<BankInfo> {
}
