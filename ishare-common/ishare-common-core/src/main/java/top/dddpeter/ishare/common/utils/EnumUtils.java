package top.dddpeter.ishare.common.utils;

import top.dddpeter.ishare.common.exception.IShareException;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: EnumUtils
 * @Description: 枚举工具类
 * @Author: 徐
 * @Date: 2019/12/30 11:46 上午
 * @Version: 1.0
 */
public class EnumUtils {

    private EnumUtils(){}

    private static final String DEFAULT_CODEMETHOD_KEYWORD = "getCode";

    private static final String DEFAULT_VALUEMETHOD_KEYWORD = "getValue";

    /**
     * 枚举转Map
     *
     * @param em 枚举类
     * @return Map – key为枚举的code，value为枚举的描述
     */
    public static <E extends Enum<E>> Map<Object, String> enumToMap(Class<E> em) {
        return enumToMap(em, DEFAULT_CODEMETHOD_KEYWORD, DEFAULT_VALUEMETHOD_KEYWORD);
    }

    /**
     * 枚举转Map
     *
     * @param em 枚举类
     * @param codeMethod 枚举code获取的方法名
     * @param valueMethod 枚举value获取的方法名
     * @return Map – key为枚举的code，value为枚举的描述
     */
    public static <E extends Enum<E>> Map<Object, String> enumToMap(Class<E> em, String codeMethod, String valueMethod) {
        Map<Object, String> enummap = new HashMap<>();
        if (!em.isEnum()) {
            return enummap;
        }
        Enum[] enums = em.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return enummap;
        }
        if (StringUtils.isEmpty(codeMethod)) {
            codeMethod = DEFAULT_CODEMETHOD_KEYWORD;
        }
        if (StringUtils.isEmpty(valueMethod)) {
            valueMethod = DEFAULT_VALUEMETHOD_KEYWORD;
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            Enum tobj = enums[i];
            try {
                //获取code值
                Object resultValue = getMethodValue(codeMethod, tobj);
                if ("".equals(resultValue)) {
                    continue;
                }
                //获取描述值
                Object resultDes = getMethodValue(valueMethod, tobj);
                //如果描述不存在获取属性值
                if ("".equals(resultDes)) {
                    resultDes = tobj;
                }
                enummap.put(resultValue, resultDes + "");
            } catch (Exception e) {
                throw new IShareException("枚举转换异常");
            }
        }
        return enummap;
    }

    /**
     * 根据反射，通过方法名称获取方法值，忽略大小写的
     *
     * @param methodName
     * @param obj
     * @param args
     * @return return value
     */
    private static <T> Object getMethodValue(String methodName, T obj,
                                             Object... args) {
        Object resut = "";
        try {
            /********************************* start *****************************************/
            //获取方法数组，这里只要共有的方法
            Method[] methods = obj.getClass().getMethods();
            if (methods.length <= 0) {
                return resut;
            }
            Method method = null;
            for (int i = 0, len = methods.length; i < len; i++) {
                //忽略大小写取方法
                if (methods[i].getName().equalsIgnoreCase(methodName)) {
                    //如果存在，则取出正确的方法名称
                    method = methods[i];
                    break;
                }
            }
            /*************************** end ***********************************************/
            if (method == null) {
                return resut;
            }
            //方法执行
            resut = method.invoke(obj, args);
            if (resut == null) {
                resut = "";
            }
            return resut;
        } catch (Exception e) {
            throw new IShareException("EnumUtils操作异常");
        }
    }

    public static <E extends Enum<E>> boolean containsCode(Class<E> emClass, Serializable code) {
        return containsCode(emClass, DEFAULT_CODEMETHOD_KEYWORD, code);
    }

    public static <E extends Enum<E>> boolean containsCode(Class<E> emClass, String methodName, Serializable code) {
        Enum[] enums = emClass.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return false;
        }
        if(StringUtils.isNull(code) || StringUtils.isEmpty(code.toString())){
            return false;
        }
        try {
            Method method = emClass.getDeclaredMethod(methodName);
            for (Enum em : enums) {
                Object resultValue = method.invoke(em);
                if(resultValue == null || StringUtils.isEmpty(resultValue.toString())){
                    return false;
                }
                if (code.toString().equals(resultValue.toString())) {
                    return true;
                }
            }
            return false;
        }catch (NoSuchMethodException ex){
            throw new IShareException("枚举code查询传入方法名错误！");
        }catch (InvocationTargetException | IllegalAccessException ex){
            throw new IShareException("枚举code查询时异常");
        }
    }
}
