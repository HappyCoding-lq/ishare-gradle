package top.dddpeter.ishare.tool.sequence.service.impl;

import com.xuanner.seq.range.impl.db.DbSeqRangeMgr;
import com.xuanner.seq.sequence.RangeSequence;
import com.xuanner.seq.sequence.impl.DefaultRangeSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.dddpeter.ishare.tool.sequence.service.MaxNoPubFunService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lijinde
 */
@Service
@Slf4j
public class MaxNoPubFunServiceImpl implements MaxNoPubFunService {

    @Resource
    Map<String, RangeSequence> globalSequenceMap;
    @Resource
    DbSeqRangeMgr dbSeqRangeMgr;




    @Override
    public DefaultRangeSequence createNewRangeSequence(String sequenceName) {
        DefaultRangeSequence rangeSequence = new DefaultRangeSequence();
        rangeSequence.setSeqRangeMgr(dbSeqRangeMgr);
        rangeSequence.setName(sequenceName);
        globalSequenceMap.put(sequenceName, rangeSequence);
        return rangeSequence;
    }






    /**
     * 功能：产生制定长度的流水号，一个号码类型一个流水
     *
     * @param cNoType   String 流水号的类型
     * @param cNoLength int 流水号的长度
     * @return String 返回产生的流水号码
     */
    @Override
    public String createMaxNo(String cNoType, int cNoLength) {

        if ((cNoType == null) || (cNoType.trim().length() <= 0)
                || (cNoLength <= 0)) {
            log.debug("NoType长度错误或NoLength错误");

            return null;
        }
        RangeSequence sequence = globalSequenceMap.get(cNoType);
        if (sequence == null) {
            sequence = this.createNewRangeSequence(cNoType);
        }
        long no = sequence.nextValue();
        String originSeq = String.valueOf(no);
        log.info("originSeq==" + originSeq);
        return String.format(String.format("%%0%dd", cNoLength), no);
    }
}
