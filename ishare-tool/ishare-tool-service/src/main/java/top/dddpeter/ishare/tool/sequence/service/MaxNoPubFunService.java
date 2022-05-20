package top.dddpeter.ishare.tool.sequence.service;

import com.xuanner.seq.sequence.impl.DefaultRangeSequence;

/**
 * @author lichao
 */
public interface MaxNoPubFunService {
    DefaultRangeSequence createNewRangeSequence(String sequenceName);
    String createMaxNo(String noType, int noLength);
}
