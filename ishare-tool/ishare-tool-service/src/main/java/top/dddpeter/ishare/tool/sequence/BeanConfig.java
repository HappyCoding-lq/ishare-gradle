package top.dddpeter.ishare.tool.sequence;

import com.xuanner.seq.range.impl.db.DbSeqRangeMgr;
import com.xuanner.seq.sequence.RangeSequence;
import com.xuanner.seq.sequence.impl.DefaultRangeSequence;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.dddpeter.ishare.tool.sequence.dao.GlobalSequenceDao;
import top.dddpeter.ishare.tool.sequence.dto.GlobalSequenceDTO;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@MapperScan(basePackages = {"top.dddpeter.ishare.tool.sequence.dao"})
@Slf4j
public class BeanConfig {
    @Resource
    DataSource dataSource;
    @Resource
    GlobalSequenceDao globalSequenceDao;

    @Bean(initMethod = "init")
    public DbSeqRangeMgr dbSeqRangeMgr(){
        DbSeqRangeMgr dbSeqRangeMgr = new DbSeqRangeMgr();
        dbSeqRangeMgr.setDataSource(dataSource);
        dbSeqRangeMgr.setTableName("global");
        dbSeqRangeMgr.setRetryTimes(100);
        dbSeqRangeMgr.setStep(10);
        return dbSeqRangeMgr;
    }
    @Bean
    public Map<String,RangeSequence> globalSequenceMap(@Autowired DbSeqRangeMgr dbSeqRangeMgr){
        List<GlobalSequenceDTO> xSequenceLDMaxNoPojos = globalSequenceDao.findAllSequence();
        Map<String,RangeSequence> globalSequenceMap = new HashMap<>(xSequenceLDMaxNoPojos.size());
        for(GlobalSequenceDTO xSequenceLDMaxNoPojo:xSequenceLDMaxNoPojos){
            DefaultRangeSequence rangeSequence = new DefaultRangeSequence();
            rangeSequence.setSeqRangeMgr(dbSeqRangeMgr);
            rangeSequence.setName(xSequenceLDMaxNoPojo.getName());
            log.debug("init sequence:{},current:{}",xSequenceLDMaxNoPojo.getName(),xSequenceLDMaxNoPojo.getValue());
            globalSequenceMap.put(xSequenceLDMaxNoPojo.getName(),rangeSequence);
        }
        return  globalSequenceMap;
    }

}
