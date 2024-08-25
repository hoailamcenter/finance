package com.finance.form.notificationGroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateNotificationGroupForm {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
}
