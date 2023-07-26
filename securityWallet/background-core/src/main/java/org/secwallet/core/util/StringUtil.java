package org.secwallet.core.util;

import org.springframework.util.StringUtils;

public class StringUtil {

    public static String getString(Object value){
        if (StringUtils.isEmpty(value))
            return "";
        return value.toString();
    }
    public static String getString(Object value,String defValue){
        if (StringUtils.isEmpty(value))
            return defValue;
        return value.toString();
    }

    public static int getInt(Object obj) {
        try {
            if (null == obj || "null".equals(obj) || "".equals(obj))
                return 0;
            else
                return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getLong(Object obj) {
        try {
            if (null == obj || "null".equals(obj) || "".equals(obj))
                return 0;
            else
                return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

}
