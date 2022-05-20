package top.dddpeter.ishare.system.service;

import top.dddpeter.ishare.system.domain.ComponentDTO;

import java.util.List;

public interface ComponetsService {

    /**
     * 根据componetName和type查询对应的url
     * @param componetName
     * @param type
     * @return
     */
    List<ComponentDTO> selectComponetUrls(String componetName, String type);

    List<ComponentDTO> selectAllComponetUrls();
}
