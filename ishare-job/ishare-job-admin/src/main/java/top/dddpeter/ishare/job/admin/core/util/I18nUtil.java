package top.dddpeter.ishare.job.admin.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import top.dddpeter.ishare.job.admin.core.conf.JobAdminConfig;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * i18n util
 *
 * @author hqins 2019-12-10
 */
public class I18nUtil {
    private static Logger logger = LoggerFactory.getLogger(I18nUtil.class);

    private static Properties prop = null;
    public static Properties loadI18nProp(){
        if (prop != null) {
            return prop;
        }
        try {
            // build i18n prop
            String i18n = JobAdminConfig.getAdminConfig().getI18n();
            i18n = (i18n!=null && i18n.trim().length()>0)?("_"+i18n):i18n;
            String i18nFile = MessageFormat.format("i18n/message{0}.properties", i18n);

            // load prop
            Resource resource = new ClassPathResource(i18nFile);
            EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
            prop = PropertiesLoaderUtils.loadProperties(encodedResource);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return prop;
    }

    /**
     * get val of i18n key
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return loadI18nProp().getProperty(key);
    }

    /**
     * get mult val of i18n mult key, as json
     *
     * @param keys
     * @return
     */
    public static String getMultString(String... keys) {
        Map<String, String> map = new HashMap<String, String>();

        Properties prop = loadI18nProp();
        if (keys!=null && keys.length>0) {
            for (String key: keys) {
                map.put(key, prop.getProperty(key));
            }
        } else {
            for (String key: prop.stringPropertyNames()) {
                map.put(key, prop.getProperty(key));
            }
        }

        String json = JacksonUtil.writeValueAsString(map);
        return json;
    }

}