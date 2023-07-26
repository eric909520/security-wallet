package org.secwallet.generate.model;

import lombok.Data;

@Data
public class ForeignKey {

    private String fkName;
    
    private String pkTableName;
    
    private String pkColumnName;
    
    private String fkTableName;
    
    private String fkColumnName;


}
