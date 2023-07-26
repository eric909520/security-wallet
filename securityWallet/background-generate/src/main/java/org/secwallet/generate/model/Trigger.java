package org.secwallet.generate.model;

import lombok.Data;


@Data
public class Trigger {
    
    private String name;
    
    private String triggerType;
    
    private String eventType;
    
    private String description;
    
    private String definition;
    
}
