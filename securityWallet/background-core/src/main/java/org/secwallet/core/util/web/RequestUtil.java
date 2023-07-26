package org.secwallet.core.util.web;



import org.secwallet.core.util.bean.BeanUtils;
import org.secwallet.core.util.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RequestUtil {



    /**
     * Takes an argument of type string. If the obtained value is null, the default string is returned.
     *
     * @param request
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(HttpServletRequest request, String key,
                                   String defaultValue, boolean b) {
        String value = request.getParameter(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        if(b){
            return value.replace("'", "''").trim();
        }else{
            return value.trim();
        }
    }

    /**
     * Takes an argument of type string. If the obtained value is null, an empty string is returned.
     *
     * @param request
     * @param key
     * @return
     */
    public static String getString(HttpServletRequest request, String key) {
        return getString(request, key, "",true);
    }

    /**
     * Takes an argument of type string. If the obtained value is null, an empty string is returned.
     *
     * @param request
     * @param key
     * @return
     */
    public static String getString(HttpServletRequest request, String key,
                                   boolean b) {
        return getString(request, key, "", b);
    }
    /**
     * Takes an argument of type string. If the obtained value is null, an empty string is returned.
     *
     * @param request
     * @param key
     * @return
     */
    public static String getString(HttpServletRequest request, String key,
                                   String defaultValue) {
        return getString(request, key, defaultValue, true);
    }

    /**
     *
     * @param request
     * @param key
     * @return
     */
    public static String getStringAry(HttpServletRequest request, String key){
        String[] aryValue = request.getParameterValues(key);
        if(aryValue==null || aryValue.length==0){
            return "";
        }
        String tmp="";
        for(String v:aryValue){
            if("".equals(tmp)){
                tmp+=v;
            }
            else{
                tmp+="," + v;
            }
        }
        return tmp;
    }
    /**
     * get safe string。
     *
     * @param request
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getSecureString(HttpServletRequest request,
                                         String key, String defaultValue) {
        String value = request.getParameter(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return filterInject(value);

    }

    /**
     * Obtain safe strings to prevent program sql injection and script attacks.
     *
     * @param request
     * @param key
     * @return
     */
    public static String getSecureString(HttpServletRequest request, String key) {
        return getSecureString(request, key, "");
    }

    /**
     * filter script|iframe|\\||;|exec|insert|select|delete|update|count|chr|truncate
     * | char string to prevent SQL injection
     *
     * @param str
     * @return
     */
    public static String filterInject(String str) {
        String injectstr = "\\||;|exec|insert|select|delete|update|count|chr|truncate|char";
        Pattern regex = Pattern.compile(injectstr, Pattern.CANON_EQ
                | Pattern.DOTALL | Pattern.CASE_INSENSITIVE
                | Pattern.UNICODE_CASE);
        Matcher matcher = regex.matcher(str);
        str = matcher.replaceAll("");
        str = str.replace("'", "''");
        str = str.replace("-", "—");
        str = str.replace("(", "（");
        str = str.replace(")", "）");
        str = str.replace("%", "％");

        return str;
    }

    /**
     * Get the specified lowercase value from Request
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static String getLowercaseString(HttpServletRequest request,
                                            String key) {
        return getString(request, key).toLowerCase();
    }

    /**
     * Get int value from request
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static int getInt(HttpServletRequest request, String key) {
        return getInt(request, key, 0);
    }

    /**
     * Get the int value from the request, if there is no value, return the default value
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static int getInt(HttpServletRequest request, String key,
                             int defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return Integer.parseInt(str);

    }

    /**
     * Get long value from Request
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static long getLong(HttpServletRequest request, String key) {
        return getLong(request, key, 0);
    }

    /**
     * get long integer array
     *
     * @param request
     * @param key
     * @return
     */
    public static Long[] getLongAry(HttpServletRequest request, String key) {
        String[] aryKey = request.getParameterValues(key);
        if (BeanUtils.isEmpty(aryKey)) {
            return null;
        }
        Long[] aryLong = new Long[aryKey.length];
        for (int i = 0; i < aryKey.length; i++) {
            aryLong[i] = Long.parseLong(aryKey[i]);
        }
        return aryLong;
    }

    /**
     * Gets an array of long integers from a comma-separated string of long integers
     *
     * @param request
     * @param key
     * @return
     */
    public static Long[] getLongAryByStr(HttpServletRequest request, String key) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        String[] aryId = str.split(",");
        Long[] lAryId = new Long[aryId.length];
        for (int i = 0; i < aryId.length; i++) {
            lAryId[i] = Long.parseLong(aryId[i]);
        }
        return lAryId;
    }

    /**
     * Get a string-shaped array from a comma-separated string
     *
     * @param request
     * @param key
     * @return
     */
    public static String[] getStringAryByStr(HttpServletRequest request,
                                             String key) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        String[] aryId = str.split(",");
        String[] lAryId = new String[aryId.length];
        for (int i = 0; i < aryId.length; i++) {
            lAryId[i] = (aryId[i]);
        }
        return lAryId;
    }

    /**
     * Get integer array by key value
     *
     * @param request
     * @param key
     * @return
     */
    public static Integer[] getIntAry(HttpServletRequest request, String key) {
        String[] aryKey = request.getParameterValues(key);
        if (BeanUtils.isEmpty(aryKey)) {
            return null;
        }
        Integer[] aryInt = new Integer[aryKey.length];
        for (int i = 0; i < aryKey.length; i++) {
            aryInt[i] = Integer.parseInt(aryKey[i]);
        }
        return aryInt;
    }

    public static Float[] getFloatAry(HttpServletRequest request, String key) {
        String[] aryKey = request.getParameterValues(key);
        if (BeanUtils.isEmpty(aryKey)) {
            return null;
        }
        Float[] fAryId = new Float[aryKey.length];
        for (int i = 0; i < aryKey.length; i++) {
            fAryId[i] = Float.parseFloat(aryKey[i]);
        }
        return fAryId;
    }

    /**
     * Get the long value from the Request, if there is no value, return the default value
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static long getLong(HttpServletRequest request, String key,
                               long defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return Long.parseLong(str.trim());
    }


    /**
     * Get the long value from the Request, if there is no value, return the default value
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static Long getLong(HttpServletRequest request, String key,
                               Long defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return Long.parseLong(str.trim());
    }
    /**
     * Get float value from Request
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static float getFloat(HttpServletRequest request, String key) {
        return getFloat(request, key, 0);
    }

    /**
     * Get the float value from Request, if there is no value, return the default value
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static float getFloat(HttpServletRequest request, String key,
                                 float defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return Float.parseFloat(request.getParameter(key));
    }

    /**
     * Get the boolean value from Request, if there is no value, return the default value false, if the value is the number 1, return true
     *
     * @param request
     * @param key
     * @return
     */
    public static boolean getBoolean(HttpServletRequest request, String key) {
        return getBoolean(request, key, false);
    }

    /**
     * Get the boolean value from the Request pair of strings, if there is no value, it will return the default value, if the value is the number 1, it will return true
     *
     * @param request
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(HttpServletRequest request, String key,
                                     boolean defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        if (StringUtils.isNumeric(str)) {
            return (Integer.parseInt(str) == 1 ? true : false);
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * Get the boolean value from Request, if there is no value, return the default value 0
     *
     * @param request
     * @param key
     * @return
     */
    public static Short getShort(HttpServletRequest request, String key) {
        return getShort(request, key, (short) 0);
    }

    /**
     * Get the Short value from the Request pair of strings, if there is no value, return the default value
     *
     * @param request
     * @param key
     * @param defaultValue
     * @return
     */
    public static Short getShort(HttpServletRequest request, String key,
                                 Short defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return Short.parseShort(str);
    }

    /**
     * Get the Date value from the Request, if there is no value, return the default value, if there is a value, return the date in yyyy-MM-dd HH:mm:ss format, or the date in custom format
     *
     * @param request
     * @param key
     * @param style
     * @return
     * @throws ParseException
     */
    public static Date getDate(HttpServletRequest request, String key,
                               String style) throws ParseException {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        if (StringUtils.isEmpty(style)) {
            style = "yyyy-MM-dd HH:mm:ss";
        }
        return (Date) DateUtil.parse(str, style);
    }

    /**
     * Get the Date value from Request, if there is no value, return the default value of null, if there is a value, return the date in yyyy-MM-dd format
     *
     * @param request
     * @param key
     * @return
     * @throws ParseException
     */
    public static Date getDate(HttpServletRequest request, String key)
            throws ParseException {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return (Date) DateUtil.parseDate(str);

    }

    /**
     * Get the Date value from Request, if there is no value, return the default value If there is a value, return the date in yyyy-MM-dd HH:mm:ss format
     *
     * @param request
     * @param key
     * @return
     * @throws ParseException
     */
    public static Date getTimestamp(HttpServletRequest request, String key)
            throws ParseException {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return DateUtil.parse(str, DateUtil.DATE_FORMAT_DATETIME);
    }

    /**
     * Get the current page URL, with parameters if there are parameters
     *
     * @param request
     * @return
     */
    public static String getUrl(HttpServletRequest request) {
        StringBuffer urlThisPage = new StringBuffer();
        urlThisPage.append(request.getRequestURI());
        Enumeration<?> e = request.getParameterNames();
        String para = "";
        String values = "";
        urlThisPage.append("?");
        while (e.hasMoreElements()) {
            para = (String) e.nextElement();
            values = request.getParameter(para);
            urlThisPage.append(para);
            urlThisPage.append("=");
            urlThisPage.append(values);
            urlThisPage.append("&");
        }
        return urlThisPage.substring(0, urlThisPage.length() - 1);
    }


    /**
     * Only process the parameters of the query
     * <pre>
     * Q_parameter name_parameter type
     * </pre>
     * @param request
     * @return
     */
    public static Map<String, Object> getQueryParams(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<?> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement().toString();
            String[] values = request.getParameterValues(key);
            if (values.length > 0 && StringUtils.isNotEmpty(values[0])) {
                if (key.startsWith("Q_")) {
                    String[] aryParaKey = key.split("_");
                    if (aryParaKey.length < 3) {
                        continue;
                    }
                    if (values.length == 1) {
                        String val = values[0].trim();
                        if (StringUtils.isNotEmpty(val)) {
                            map.put(key, values[0].trim());
                        }
                    } else {
                        map.put(key, values);
                    }
                }
            }
        }
        return map;
    }

    /**
     * Put the parameters into the map. <br>
     *
     * <pre>
     * 1. If paging is required, the parameters need to be configured as follows:
     * Q_parameter name_parameter type
     *
     * The types that can be used are as follows:
     *S: string
     * L: long integer
     * N: integer
     * DB: double
     *BD:decimal
     * FT: floating point data
     * SN: short data
     *DL: start time
     * DG: end time
     * SL: string fuzzy query
     * SLL: string left fuzzy query
     * SLR: string right fuzzy query
     * 2. If the parameters do not need paging, you can pass the parameters in the above way.
     *
     * </pre>
     *
     * @param request
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map<String,Object> getQueryMap(HttpServletRequest request) {
        // TYPE
        // param name:Q_PARANAME_TYPE
        // sortField,orderSeq
        Map map = new HashMap();
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement().toString();
            String[] values = request.getParameterValues(key);
            if ("sortField".equals(key) || "orderSeq".equals(key)
                    || "sortColumns".equals(key)) {
                String val = values[0].trim();
                if (StringUtils.isNotEmpty(val)) {
                    map.put(key, values[0].trim());
                }
            }
            if (values.length > 0 && StringUtils.isNotEmpty(values[0])) {
                if (key.startsWith("Q_")) {
                    addParameter(key, values, map);
                } else {
                    if (values.length == 1) {
                        String val = values[0].trim();
                        if (StringUtils.isNotEmpty(val)) {
                            map.put(key, values[0].trim());
                        }
                    } else {
                        map.put(key, values);
                    }
                }
            }
        }
        return map;
    }

    public static void addParameter(String key, String[] values,
                                    Map<String, Object> map) {
        String[] aryParaKey = key.split("_");
        if (aryParaKey.length < 3) {
            return;
        }
        String paraName = key.substring(2, key.lastIndexOf("_"));
        String type = key.substring(key.lastIndexOf("_") + 1);
        if (values.length == 1) {
            String value = values[0].trim();
            if (StringUtils.isNotEmpty(value)) {
                try {
                    if (value.indexOf("_") != -1) {
                        value = value.replaceAll("_", "\\\\_");
                    }
                    Object obj = getObjValue(type, value);
                    map.put(paraName, obj);
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
            }
        } else {
            Object[] aryObj = getObjValue(type, values);
            map.put(paraName, aryObj);
        }
    }
    /**
     * Format to get the true value based on the type passed in
     *
     * </p>
     *
     * @param type
     * @param paramValue
     * @return
     */
    private static Object getObjValue(String type, String paramValue) {
        Object value = null;
        // String exact match
        if ("S".equals(type)) {
            value = paramValue;
        }
        // string fuzzy query
        else if ("SL".equals(type)) {
            value = "%" + paramValue + "%";
        }
        // string right fuzzy query
        else if ("SLR".equals(type)) {
            value = paramValue + "%";
        }
        // string left fuzzy query
        else if ("SLL".equals(type)) {
            value = "%" + paramValue;
        }
        // string uppercase fuzzy query
        else if ("SUPL".equals(type)) {
            value = "%" + paramValue.toUpperCase() + "%";
        }
        // string uppercase right fuzzy query
        else if ("SUPLR".equals(type)) {
            value = paramValue.toUpperCase() + "%";
        }
        // string uppercase left fuzzy query
        else if ("SUPLL".equals(type)) {
            value = "%" + paramValue.toUpperCase() ;
        }
        // string lowercase fuzzy query
        else if ("SLOL".equals(type)) {
            value = "%" + paramValue.toLowerCase() + "%";
        }
        // string lowercase right fuzzy query
        else if ("SLOLR".equals(type)) {
            value = paramValue.toLowerCase() + "%";
        }
        // string lowercase left fuzzy query
        else if ("SLOLL".equals(type)) {
            value = "%" + paramValue.toLowerCase() ;
        }
        // long integer
        else if ("L".equals(type)) {
            value = new Long(paramValue);
        }
        // Integer
        else if ("N".equals(type)) {
            value = new Integer(paramValue);
        } else if ("DB".equals(type)) {
            value = new Double(paramValue);
        }
        // decimal
        else if ("BD".equals(type)) {
            value = new BigDecimal(paramValue);
        }
        // FLOAT
        else if ("FT".equals(type)) {
            value = new Float(paramValue);
        }
        // short
        else if ("SN".equals(type)) {
            value = new Short(paramValue);
        }
        // date begin
        else if ("DL".equals(type)) {
            try {
                value = DateUtil.parse(paramValue, DateUtil.DATE_FORMAT_DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // date end
        else if ("DG".equals(type)) {
            try {
                value = DateUtil.getNextDays(
                        DateUtil.parse(paramValue, DateUtil.DATE_FORMAT_DATE), 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            value = paramValue;
        }
        return value;
    }

    private static Object[] getObjValue(String type, String[] value) {
        Object[] aryObj = new Object[value.length];
        for (int i = 0; i < aryObj.length; i++) {
            String v = "";
            if (value[i] != null) {
                v = value[i].toString();
            }
            aryObj[i] = getObjValue(type, v);
        }
        return aryObj;
    }

    /**
     * Encapsulate the request of the current context in a map
     *
     * @param request
     * @param remainArray
     *            keep as array
     * @param isSecure
     *            Filter unsafe characters
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map getParameterValueMap(HttpServletRequest request,
                                           boolean remainArray, boolean isSecure) {
        Map map = new HashMap();
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement().toString();
            String[] values = request.getParameterValues(key);
            if (values == null) {
                continue;
            }
            if (values.length == 1) {
                String tmpValue = values[0];
                if (tmpValue == null) {
                    continue;
                }
                tmpValue = tmpValue.trim();
                if ("".equals(tmpValue)) {
                    continue;
                }
                if (isSecure) {
                    tmpValue = filterInject(tmpValue);
                }
                if ("".equals(tmpValue)) {
                    continue;
                }
                map.put(key, tmpValue);
            } else {
                String rtn = getByAry(values, isSecure);
                if (rtn.length() > 0) {
                    if (remainArray) {
                        map.put(key, rtn.split(","));
                    } else {
                        map.put(key, rtn);
                    }
                }
            }
        }
        return map;
    }

    /**
     *
     * @param aryTmp
     * @param isSecure
     * @return
     */
    private static String getByAry(String[] aryTmp, boolean isSecure) {
        String rtn = "";
        for (int i = 0; i < aryTmp.length; i++) {
            String str = aryTmp[i].trim();
            if (!"".equals(str)) {
                if (isSecure) {
                    str = filterInject(str);
                    if (!"".equals(str)) {
                        rtn += str + ",";
                    }
                } else {
                    rtn += str + ",";
                }
            }
        }
        if (rtn.length() > 0) {
            rtn = rtn.substring(0, rtn.length() - 1);
        }
        return rtn;
    }

    /**
     * Get parameter value based on parameter name.
     *
     * <pre>
     * 1. Get the array of parameter values according to the parameter name.
     * 2. Use a comma-separated string for the array.
     * </pre>
     *
     * @param request
     * @param paramName
     * @return
     */
    public static String getStringValues(HttpServletRequest request,
                                         String paramName) {
        String[] values = request.getParameterValues(paramName);
        if (BeanUtils.isEmpty(values)) {
            return "";
        }
        String tmp = "";
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {
                tmp += values[i];
            } else {
                tmp += "," + values[i];
            }
        }
        return tmp;
    }

    /**
     * get local。
     *
     * @param request
     * @return
     */
    public static Locale getLocal(HttpServletRequest request) {
        Locale local = request.getLocale();
        if (local == null) {
            local = Locale.CHINA;
        }
        return local;
    }

    /**
     * get the wrong url
     *
     * @param request
     * @return
     */
    public final static String getErrorUrl(HttpServletRequest request) {
        String errorUrl = (String) request
                .getAttribute("javax.servlet.error.request_uri");
        if (errorUrl == null) {
            errorUrl = (String) request
                    .getAttribute("javax.servlet.forward.request_uri");
        }
        if (errorUrl == null) {
            errorUrl = (String) request
                    .getAttribute("javax.servlet.include.request_uri");
        }
        if (errorUrl == null) {
            errorUrl = request.getRequestURL().toString();
        }
        return errorUrl;
    }
    /**
     * Get IP address
     * @param request
     * @return
     */
    public  static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
