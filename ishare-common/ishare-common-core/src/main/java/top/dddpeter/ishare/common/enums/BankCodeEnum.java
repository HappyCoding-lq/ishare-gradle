package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 银行代码
 *
 * @author wangmaolin
 * @date 2020/2/4 14:05
 */
public enum BankCodeEnum {

    ICBC("0101","102",  "中国工商银行"),
    ABC("0102", "103","中国农业银行"),
    BOC("0103", "104", "中国银行"),
    CCB("0104", "105", "中国建设银行"),
    COMM("0108", "301", "交通银行"),
    CITIC("0109", "302", "中信银行"),
    CEB("0110", "303", "中国光大银行"),
    HXB("0111", "304", "华夏银行"),
    CMBC("0112", "305", "中国民生银行"),
    GDB("0113", "306", "广东发展银行"),
    SPAB("0198", "307", "平安银行"),
    CMB("0115", "308", "招商银行"),
    CIB("0116", "309", "兴业银行"),
    SPDB("0117", "310", "上海浦东发展银行"),
    PSBC("0128", "403", "中国邮政储蓄银行"),
    CBB("0313", "313" , "城市商业银行"),
    RC("0314", "314" , "农村商业银行"),
    HFB("0315", "315" , "恒丰银行"),
    CZB("0316", "316" , "浙商银行"),
    RCB("0317", "317" , "农村合作银行"),
    BHB("0318", "318" , "渤海银行"),
    HSB("0319", "319" , "徽商银行"),
    XB("0320", "320" , "村镇银行股份有限公司"),
    CTB("0321", "321", "重庆三峡银行股份有限公司"),
    WZB("0323", "323" , "武汉众邦银行"),
    CCBC("0401", "401" , "城市信用社"),
    XXB("0402", "402" , "农村信用联社"),
    CY("0905", "905" , "中国银联股份有限公司"),
    PBOC("0001", "1", "中国人民银行"),
    CG("0011", "11" , "国家金库"),
    CDB("0201", "201" , "国家开发银行"),
    EIB("0202", "202" , "中国进出口银行"),
    ADB("0203", "203" , "中国农业发展银行"),
    DRC("0332", "50016" , "东莞农村商业银行"),
    NYBANK("0513", "20036" , "广东南粤银行"),
    LTD("0199", "20127" , "珠海华润银行"),
    SD("0263", "314" , "广东顺德农村商业银行股份有限公司"),
    GZNS("0251", "50012" , "广州农村商业银行股份有限公司"),
    GDNCX("0252", "402" , "广东省农村信用社联合社"),
    ZHNS("0271", "314" , "珠海农村商业银行股份有限公司"),
    GDNHNS("0264", "314" , "广东南海农村商业银行股份有限公司"),
    ZSNS("0195", "20127" , "中山农村商业银行"),
    NBB("0197", "7002" , "宁波银行"),
    BOB("0194", "7007" , "北京银行"),
    GDGMNS("0265", "314" , "广东高明农村商业银行股份有限公司"),
    ZHNSGS("0333", "314" , "中山农村商业银行股份有限公司"),
    GDGYNS("0287", "314" , "广东高要农村商业银行股份有限公司");







    private final String code;
    private final String zjxtCode;
    private final String value;

    BankCodeEnum(String code, String zjxtCode, String value){
        this.code = code;
        this.zjxtCode = zjxtCode;
        this.value = value;
    }

    public String getCode()
    {
        return code;
    }

    public String getZJXTCode()
    {
        return zjxtCode;
    }

    public String getValue()
    {
        return value;
    }

    public static BankCodeEnum getEnumByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(BankCodeEnum em : BankCodeEnum.values()){
            if(em.getCode().equals(code)){
                return  em;
            }
        }
        return null;
    }

    public static String getValueByCode(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(BankCodeEnum em : BankCodeEnum.values()){
            if(em.getCode().equals(code)){
                return  em.getValue();
            }
        }
        return null;
    }

    public static String getZJXTCodeByCode(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(BankCodeEnum em : BankCodeEnum.values()){
            if(em.getCode().equals(code)){
                return  em.getZJXTCode();
            }
        }
        return null;
    }

    public static String getValuesBySplitCodes(String splitCodes) {
        if(StringUtils.isEmpty(splitCodes)){
            return null;
        }
        String[] codeArray = splitCodes.split(",");
        StringBuilder returnStr = new StringBuilder("");
        for(String code : codeArray){
            try{
                String value = getValueByCode(code);
                if(value==null){
                    return null;
                }
                returnStr.append(value).append(",");
            }catch(NumberFormatException ex){
                return null;
            }
        }
        returnStr.delete(returnStr.length()-1, returnStr.length());
        return returnStr.toString();
    }

    public static Map<Object, String> getCodeValueMap (){
        Map map = new LinkedHashMap();
        for(BankCodeEnum em : BankCodeEnum.values()){
            map.put(em.code, em.value);
        }
        return map;
    }

}
