package top.dddpeter.ishare.common.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

/**
 * 针对mongo Numberlong类型数据json反序列化解析
 *
 * @author : huangshuanbao
 */
public class MongoLongConverter implements ObjectDeserializer {


    @Override
    public Long deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String longStr = parser.parseObject().getString("$numberLong");
        if (longStr!=null && !longStr.isEmpty()){
            return Long.parseLong(longStr);
        }
        return 0L;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
