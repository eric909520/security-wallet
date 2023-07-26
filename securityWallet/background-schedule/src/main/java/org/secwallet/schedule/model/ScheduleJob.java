package org.secwallet.schedule.model;


import org.secwallet.core.model.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "Scheduled Task Model")
@Data
public class ScheduleJob extends BaseModel {

    private String id;
    private String taskDescription;
    private String taskName;
    private String taskGroup;
    private String triggerName;
    private String triggerGroup;
    private String triggerCronExpression;
    private String executeClassName;
    private String executeMethodName;
    private String targetTable;
    private Integer status;

}
