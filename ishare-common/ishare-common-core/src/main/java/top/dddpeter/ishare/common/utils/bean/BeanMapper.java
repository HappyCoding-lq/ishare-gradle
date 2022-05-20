package top.dddpeter.ishare.common.utils.bean;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;

import java.util.Collection;
import java.util.List;

/**
 * 封装并持有Dozer单例，深度转换Bean<->Bean的Mapper，实现： 1. 转换对象的类型 2. 转换Collection中对象的类型 3. 将对象A的值拷贝到对象B中
 */
public class BeanMapper {

    /**
     * 持有Dozer单例
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 转换对象的类型
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    /**
     * 转换Collection中对象的类型
     */
    @SuppressWarnings("rawtypes")
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 将对象A的值拷贝到对象B中
     */
    public static <T> T copy(Object source, T destinationObject) {
        dozer.map(source, destinationObject);
        return destinationObject;
    }



    public static String decodeUnicode(String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
}
