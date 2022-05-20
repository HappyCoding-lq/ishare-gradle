package top.dddpeter.ishare.system.service.impl;

import org.springframework.stereotype.Service;
import top.dddpeter.ishare.system.domain.ComponentDTO;
import top.dddpeter.ishare.system.mapper.ComponetsMapper;
import top.dddpeter.ishare.system.service.ComponetsService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ComponetsServiceImpl implements ComponetsService {
    @Resource
    private ComponetsMapper componetsMapper;

    @Override
    public List<ComponentDTO> selectComponetUrls(String componetName, String type) {
        return componetsMapper.getComponetUrls(componetName, type);
    }

    @Override
    public List<ComponentDTO> selectAllComponetUrls() {
        return componetsMapper.getAllComponetUrls();
    }
}
