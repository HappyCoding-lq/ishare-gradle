package top.dddpeter.ishare.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * Json转换工具
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/7 5:15 下午
 */
public class JsonTrans {

    private JsonTrans() {}

    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    public static String objectToJson(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.IgnoreErrorGetter,SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String objectToJson(Object obj, SerializerFeature... features) {
        return JSON.toJSONString(obj, features);
    }

}
