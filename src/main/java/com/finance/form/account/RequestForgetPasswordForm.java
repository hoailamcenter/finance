package com.finance.form.account;

import com.finance.validation.EmailConstraint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class RequestForgetPasswordForm {
    @EmailConstraint
    @NotBlank(message = "Email cannot be null.")
    @ApiModelProperty(name = "email", required = true)
    private String email;
}
