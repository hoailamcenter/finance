package com.finance.form.keyInformationGroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateKeyInformationGroupForm {
    @NotBlank(message = "name cannot be null")
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
}
