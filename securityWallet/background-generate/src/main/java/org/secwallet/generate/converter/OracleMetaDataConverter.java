package org.secwallet.generate.converter;



import org.secwallet.generate.dao.IMetaDataConverter;
import org.secwallet.generate.model.Column;
import org.secwallet.generate.model.Index;

import java.util.Map;


/**
 * metadata converter
 */
public class OracleMetaDataConverter extends CommonMetaDataConverter {

    private static final IMetaDataConverter instance = new OracleMetaDataConverter();
    
    public static IMetaDataConverter getInstance() {
        return instance;
    }
    
    @Override
    public Column convertMap2Column(Map<String, String> map) {
        Column column = super.convertMap2Column(map);
        
        String charLength = getValue(map, "CHAR_LENGTH");
        if (!"0".equals(charLength) && column.getDataType().contains("CHAR")) {
//          String length = getValue(map, "DATA_LENGTH");
//          String.valueOf(Integer.valueOf(charLength).intValue() * 2)
//          if (NumberUtils.toInt(length) == NumberUtils.toInt(charLength) * 2) {
//              charLength += " char"; 
//          }
            column.setLength(charLength);
//      } else {
//          // Oracel，non-numeric precision is null
//          String precision = getValue(map, "DATA_PRECISION");
//          if (StringUtils.isNotEmpty(precision)
//                  && !"0".equals(precision)) {
//              column.setLength(precision);
//          } else {
//              column.setLength(getValue(map, "DATA_LENGTH"));
//          }
        }
        return column;
    }
    
    @Override
    public Index convertMap2Index(Map<String, String> map) {
        Index index = super.convertMap2Index(map);
        // The uniqueness field of Oracle's user_indexes as IS_UNIQUE, if NONUNIQUE is returned, it can be null
        index.setUnique("NONUNIQUE".equals(
                getValue(map, "IS_UNIQUE")) ? false : true);
        return index;
    }
}
