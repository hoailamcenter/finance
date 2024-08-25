package com.finance.form.service;

import com.finance.validation.ServiceKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class CreateServiceForm {
    @NotBlank(message = "name cannot be null")
    @ApiModelProperty(name = "name")
    private String name;
    @ServiceKind
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "description")
    private String description;
    @NotNull(message = "money cannot be null")
    @ApiModelProperty(name = "money")
    private String money;
    @NotNull(message = "start date cannot be null")
    @ApiModelProperty(name = "startDate")
    private Date startDate;
    @NotNull(message = "period kind  cannot be null")
    @ApiModelProperty(name = "periodKind")
    private Integer periodKind;
    @ApiModelProperty(name = "expirationDate")
    private Date expirationDate;
    @ApiModelProperty(name = "categoryId")
    private Long categoryId;
    @ApiModelProperty(name = "serviceGroupId")
    private Long serviceGroupId;
}
