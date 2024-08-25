package com.finance.form.service;

import com.finance.validation.ServiceKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class UpdateServiceForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(name = "id")
    private Long id;
    @NotBlank(message = "name cannot be null")
    @ApiModelProperty(name = "name")
    private String name;
    @ServiceKind
    @NotNull(message = "kind cannot be null")
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "money")
    private String money;
    @ApiModelProperty(name = "startDate")
    private Date startDate;
    @ApiModelProperty(name = "periodKind")
    private Integer periodKind;
    @ApiModelProperty(name = "expirationDate")
    private Date expirationDate;
    @ApiModelProperty(name = "categoryId")
    private Long categoryId;
    @ApiModelProperty(name = "serviceGroupId")
    private Long serviceGroupId;
}
