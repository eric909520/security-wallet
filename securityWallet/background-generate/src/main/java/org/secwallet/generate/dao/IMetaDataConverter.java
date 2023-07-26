package org.secwallet.generate.dao;


import org.secwallet.generate.model.*;

import java.util.Map;


/**
 * Metadata Converter Interface
 */
public interface IMetaDataConverter {

    /**
     * Convert map to Table
     * 
     * @param map
     * @return
     */
    Table convertMap2Table(Map<String, String> map);

    /**
     * Convert map to Column
     * 
     * @param map
     * @return
     */
    Column convertMap2Column(Map<String, String> map);

    /**
     * Convert map to PrimaryKey
     * 
     * @param map
     * @return
     */
    PrimaryKey convertMap2PrimaryKey(Map<String, String> map);

    /**
     * Convert map to ForeignKey
     * 
     * @param map
     * @return
     */
    ForeignKey convertMap2ForeignKey(Map<String, String> map);

    /**
     * Convert map to Index
     * 
     * @param map
     * @return
     */
    Index convertMap2Index(Map<String, String> map);

    /**
     * Convert map to Triger
     * 
     * @param map
     * @return
     */
    Trigger convertMap2Trigger(Map<String, String> map);
}
