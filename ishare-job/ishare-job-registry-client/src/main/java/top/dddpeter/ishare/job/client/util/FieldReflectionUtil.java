package top.dddpeter.ishare.job.client.util;//package top.dddpeter.ishare.job.client.util;
//
//
///**
// * data type parse
// *
// * @author hqins 2019-12-10
// */
//public final class FieldReflectionUtil {
//
//	private FieldReflectionUtil(){}
//
//	public static Byte parseByte(String value) {
//		try {
//			value = value.replaceAll("　", "");
//			return Byte.valueOf(value);
//		} catch(NumberFormatException e) {
//			throw new RuntimeException("parseByte but input illegal input=" + value, e);
//		}
//	}
//
//	public static Boolean parseBoolean(String value) {
//		value = value.replaceAll("　", "");
//		if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
//			return Boolean.TRUE;
//		} else if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
//			return Boolean.FALSE;
//		} else {
//			throw new RuntimeException("parseBoolean but input illegal input=" + value);
//		}
//	}
//
//	public static Integer parseInt(String value) {
//		try {
//			value = value.replaceAll("　", "");
//			return Integer.valueOf(value);
//		} catch(NumberFormatException e) {
//			throw new RuntimeException("parseInt but input illegal input=" + value, e);
//		}
//	}
//
//	public static Short parseShort(String value) {
//		try {
//			value = value.replaceAll("　", "");
//			return Short.valueOf(value);
//		} catch(NumberFormatException e) {
//			throw new RuntimeException("parseShort but input illegal input=" + value, e);
//		}
//	}
//
//	public static Long parseLong(String value) {
//		try {
//			value = value.replaceAll("　", "");
//			return Long.valueOf(value);
//		} catch(NumberFormatException e) {
//			throw new RuntimeException("parseLong but input illegal input=" + value, e);
//		}
//	}
//
//	public static Float parseFloat(String value) {
//		try {
//			value = value.replaceAll("　", "");
//			return Float.valueOf(value);
//		} catch(NumberFormatException e) {
//			throw new RuntimeException("parseFloat but input illegal input=" + value, e);
//		}
//	}
//
//	public static Double parseDouble(String value) {
//		try {
//			value = value.replaceAll("　", "");
//			return Double.valueOf(value);
//		} catch(NumberFormatException e) {
//			throw new RuntimeException("parseDouble but input illegal input=" + value, e);
//		}
//	}
//
//    /**
//     * 校验基础参数
//     *
//     * @param fieldType
//     * @return
//     */
//	public static boolean validBaseType(Class<?> fieldType){
//        if (String.class.equals(fieldType)) {
//            return true;
//        } else if (Byte.class.equals(fieldType) || Byte.TYPE.equals(fieldType)) {
//            return true;
//        } else if (Character.class.equals(fieldType) || Character.TYPE.equals(fieldType)) {
//            return true;
//        } else if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
//            return true;
//        } else if (Short.class.equals(fieldType) || Short.TYPE.equals(fieldType)) {
//            return true;
//        } else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
//            return true;
//        } else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
//            return true;
//        } else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
//            return true;
//        } else if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
//            return true;
//        }
//        return false;
//    }
//
//
//	/**
//	 * 参数解析 （支持：Byte、Character、Boolean、String、Short、Integer、Long、Float、Double、Date）
//	 *
//	 * @param fieldType
//	 * @param value
//	 * @return
//	 */
//	public static Object parseValue(Class<?> fieldType, String value) {
//		// Class<?> fieldType = field.getType();	// Field field
//
//		if(value==null || value.trim().length()==0)
//			return null;
//		value = value.trim();
//
//		if (String.class.equals(fieldType)) {
//			return value;
//		} else if (Byte.class.equals(fieldType) || Byte.TYPE.equals(fieldType)) {
//			return parseByte(value);
//		} else if (Character.class.equals(fieldType) || Character.TYPE.equals(fieldType)) {
//			 return value.toCharArray()[0];
//		} else if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
//			return parseBoolean(value);
//		} else if (Short.class.equals(fieldType) || Short.TYPE.equals(fieldType)) {
//			 return parseShort(value);
//		} else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
//			return parseInt(value);
//		} else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
//			return parseLong(value);
//		} else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
//			return parseFloat(value);
//		} else if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
//			return parseDouble(value);
//		} else {
//			throw new RuntimeException("illeagal conf data type, type=" + fieldType);
//		}
//	}
//
//}