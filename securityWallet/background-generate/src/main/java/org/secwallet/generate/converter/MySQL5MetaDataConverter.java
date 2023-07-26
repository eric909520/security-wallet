package org.secwallet.generate.converter;



import org.secwallet.generate.dao.IMetaDataConverter;
import org.secwallet.generate.model.Index;
import org.secwallet.generate.model.Table;

import java.util.Map;



/**
 * metadata converter
 */
public class MySQL5MetaDataConverter extends CommonMetaDataConverter {

    private static IMetaDataConverter instance = new MySQL5MetaDataConverter();
    
    public static final IMetaDataConverter getInstance() {
        return instance;
    }
    
    @Override
    public Table convertMap2Table(Map<String, String> map) {
        Table table = super.convertMap2Table(map);
        
        /**
         * The value stored in the table_comment field of the information_schema.TABLES table of MySQL5 is:
         * If there are comments: "Table comments; InnoDB free: 4096 kB"
         * If there is no comment, "InnoDB free: 4096 kB" stores engine information like this
         * Therefore, string interception is required
         */
//        String desc = table.getDescription();
//        int endIndex = desc.lastIndexOf(";");
//        if (endIndex != -1) {
//            desc = desc.substring(0, endIndex);
//        }
//        table.setDescription(desc);
        return table;
    }

    @Override
    public Index convertMap2Index(Map<String, String> map) {
        Index index = super.convertMap2Index(map);
        // MySql5 only has non_unique fields, no IS_UNIQUE. 0 means not empty
        index.setUnique("0".equals(getValue(map, "NON_UNIQUE")));
        return index;
    }
}
