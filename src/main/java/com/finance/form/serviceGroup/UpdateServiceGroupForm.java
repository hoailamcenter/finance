package com.finance.form.serviceGroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateServiceGroupForm {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
}
