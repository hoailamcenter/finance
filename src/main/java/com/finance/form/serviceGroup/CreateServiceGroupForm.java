package com.finance.form.serviceGroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateServiceGroupForm {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
}
