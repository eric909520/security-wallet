package org.secwallet.core.util.bean;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.LongConverter;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class BeanUtils {


    /**
     * BeanUtil type converter
     */
    public static ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();

    private static BeanUtilsBean beanUtilsBean=new BeanUtilsBean(convertUtilsBean,new PropertyUtilsBean());

    static {
        convertUtilsBean.register(new DateConverter(), Date.class);
        convertUtilsBean.register(new LongConverter(null),Long.class);
    }

    /**
     * Can be used to judge whether Map,Collection,String,Array,Long,Integer,Short are empty
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj){
        if (obj == null) {
            return true;
        }
        if (obj instanceof String){
            if (((String) obj).trim().length() == 0){
                return true;
            }
        }
        else if (obj instanceof Collection){
            if (((Collection) obj).isEmpty()){
                return true;
            }
        }
        else if (obj.getClass().isArray()){
            if (((Object[]) obj).length == 0){
                return true;
            }
        }
        else if (obj instanceof Map){
            if (((Map) obj).isEmpty()){
                return true;
            }
        }
        else if (obj instanceof Long){
            Long lEmpty=0L;
            if(obj==null || lEmpty.equals(obj)){
                return true;
            }
        }
        else if (obj instanceof Short){
            Short sEmpty=0;
            if(obj==null || sEmpty.equals(obj)){
                return true;
            }
        }
        else if (obj instanceof Integer){
            Integer sEmpty=0;
            if(obj==null || sEmpty.equals(obj)){
                return true;
            }
        }
        return false;
    }


}
