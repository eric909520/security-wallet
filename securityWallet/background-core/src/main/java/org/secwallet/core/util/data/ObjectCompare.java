package org.secwallet.core.util.data;

import com.alibaba.fastjson.JSON;
import org.secwallet.core.util.data.annotation.FieldName;

import java.lang.reflect.Field;
import java.util.*;

public class ObjectCompare {

    /**
     * Get the modified data item
     * @param oldObject
     * @param newObject
     * @param <T>
     * @return
     */
    public static  <T>  String getDiffData(T oldObject,T newObject)throws IllegalAccessException{
        Map<String, String> mapData = null;
        List<Map<String,String>> list=new ArrayList<>();
        String jsonString="";
        if (oldObject!=null&&newObject!=null){
            Class<?> oldType = oldObject.getClass();
            Class<?> newType = newObject.getClass().getSuperclass();
            List<Field> oldFields=Arrays.asList(oldType.getDeclaredFields());
            List<Field> newFields = Arrays.asList(newType.getDeclaredFields());
            for (Field field : newFields) {
                FieldName fieldName = field.getAnnotation(FieldName.class);
                Field  oldField = oldFields.stream().filter(field1 -> field1.getName().endsWith(field.getName())).findAny().orElse(null);
                String newValue= field.get(newObject).toString();
                String oldValue = "";

                try {
                    oldValue=oldField.get(oldObject).toString();
                }catch (NullPointerException e){
                    oldValue = "";
                }finally {
                    if (fieldName!=null&&!newValue.endsWith(oldValue)){
                        mapData = new HashMap<>();
                        mapData.put("itemName",fieldName.fieldName());
                        mapData.put("newContent",newValue);
                        mapData.put("oldContent",oldValue);
                        list.add(mapData);
                    }
                }

            }
        }
        if (mapData!=null && mapData.size()>0){
            jsonString = JSON.toJSONString(list);
        }
        return jsonString;
    }
}
