package com.finance.form.transactionGroup;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateTransactionGroupForm {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
}
