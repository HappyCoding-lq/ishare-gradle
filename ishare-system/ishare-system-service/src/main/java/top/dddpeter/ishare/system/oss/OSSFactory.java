package top.dddpeter.ishare.system.oss;

import com.alibaba.fastjson.JSON;
import top.dddpeter.ishare.common.utils.spring.SpringUtils;
import top.dddpeter.ishare.system.oss.CloudConstant.CloudService;
import top.dddpeter.ishare.system.service.ISysConfigService;

/**
 * 文件上传Factory
 */
public final class OSSFactory
{
    private static ISysConfigService sysConfigService;
    static
    {
        OSSFactory.sysConfigService = (ISysConfigService) SpringUtils.getBean(ISysConfigService.class);
    }

    public static AbstractCloudStorageService build()
    {
        String jsonconfig = sysConfigService.selectConfigByKey(CloudConstant.CLOUD_STORAGE_CONFIG_KEY);
        // 获取云存储配置信息
        CloudStorageConfig config = JSON.parseObject(jsonconfig, CloudStorageConfig.class);
        if (config.getType() == CloudService.QINIU.getValue())
        {
            return new QiniuAbstractCloudStorageService(config);
        }
        else if (config.getType() == CloudService.ALIYUN.getValue())
        {
            return new AliyunAbstractCloudStorageService(config);
        }
        else if (config.getType() == CloudService.QCLOUD.getValue())
        {
            return new QcloudAbstractCloudStorageService(config);
        }
        return null;
    }
}
