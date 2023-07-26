package org.secwallet.generate.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class Procedure {
    
    @ApiModelProperty(value = "name")
    private String name;
    @ApiModelProperty(value = "description")
    private String description;
    @ApiModelProperty(value = "definition")
    private String definition;
    
}
