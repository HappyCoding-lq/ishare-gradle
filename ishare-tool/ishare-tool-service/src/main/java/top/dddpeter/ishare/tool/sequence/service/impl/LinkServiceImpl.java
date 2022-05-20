package top.dddpeter.ishare.tool.sequence.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.tool.sequence.dao.LinkDao;
import top.dddpeter.ishare.tool.sequence.dto.LinkDTO;
import top.dddpeter.ishare.tool.sequence.service.LinkService;
import top.dddpeter.ishare.tool.utils.ShortId;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static top.dddpeter.ishare.common.utils.ServletUtils.getRequest;


@Service
public class LinkServiceImpl implements LinkService {

    @Resource
    private LinkDao linkDao;

    @Value("${nps.domain}")
    private String npsDomain;

    @Override
    public Integer addLink (LinkDTO link) {
        //根据长链接拼接生成  固定域名 + shareId随机生成
        String hashKey = ShortId.generate().substring(0,3);
            //需要进行一次查询短链是不是已经存在了
            while (true){
                LinkDTO linkDTO = this.getLinkByKey(hashKey);
                if(StringUtils.isNotNull(linkDTO)){
                    hashKey=ShortId.generate().substring(0,3);
                }else{
                    break;
                }
            }
            String surl = npsDomain + "/s/"+hashKey;
            link.setHashKey(hashKey);
            link.setSurl(surl);
            link.setGmtCreate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            link.setCreateUser(getRequest().getHeader(Constants.CURRENT_USERNAME));


        //添加的时候需要进行判断一下数据库是不是已经存在该短链接了
        return linkDao.addLink(link);
    }

    @Override
    public Integer updateLink (int id, String description, String lurl) {
        String updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return linkDao.updateLink(id, description, lurl,updateTime);
    }

    @Override
    public LinkDTO getLinkByKey (String key) {
        return linkDao.getLinkByKey(key);
    }

    @Override
    public Integer deleteLink (int id) {
        return linkDao.deleteLink(id);
    }

    @Override
    public LinkDTO getLinkById (int id) {
        return linkDao.getLinkById(id);
    }


    @Override
    public HashMap<String, Object> getAllLink (String surl, String description, String createUser, int page, int size) {
        page = (page-1)*size;
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",linkDao.selectTotal());
        map.put("rows",linkDao.getAllLink(surl, description, createUser, page, size));
        return map;
    }

    @Override
    public LinkDTO saveLinkDTO(LinkDTO link) {
        //根据长链接拼接生成  固定域名 + shareId随机生成
        String hashKey = ShortId.generate().substring(0,link.getLength());
        //需要进行一次查询短链是不是已经存在了
        while (true){
            LinkDTO linkDTO = this.getLinkByKey(hashKey);
            if(StringUtils.isNotNull(linkDTO)){
                hashKey=ShortId.generate().substring(0,link.getLength());
            }else{
                break;
            }
        }
        String surl = npsDomain + "/s/"+hashKey;
        link.setHashKey(hashKey);
        link.setSurl(surl);
        link.setGmtCreate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        link.setCreateUser(getRequest().getHeader(Constants.CURRENT_USERNAME));

        //添加的时候需要进行判断一下数据库是不是已经存在该短链接了
        linkDao.addLink(link);
        return link;
    }
}
