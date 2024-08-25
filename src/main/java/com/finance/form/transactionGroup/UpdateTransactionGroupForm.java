package com.finance.form.transactionGroup;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateTransactionGroupForm {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
}
