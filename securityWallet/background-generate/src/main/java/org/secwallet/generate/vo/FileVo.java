package org.secwallet.generate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileVo {


    @ApiModelProperty(value = "filePath")
    private String filePath;

    @ApiModelProperty(value = "zipFileName")
    private  String zipFileName;
}
