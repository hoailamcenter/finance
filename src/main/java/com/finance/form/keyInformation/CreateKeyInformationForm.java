package com.finance.form.keyInformation;

import com.finance.validation.KeyInformationKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class CreateKeyInformationForm {
    @NotBlank(message = "name cannot be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;
    @NotNull(message = "kind cannot be null")
    @KeyInformationKind
    @ApiModelProperty(name = "kind", required = true)
    private Integer kind;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "additionalInformation")
    private String additionalInformation;
    @ApiModelProperty(name = "keyInformationGroupId")
    private Long keyInformationGroupId;
    @ApiModelProperty(name = "organizationId")
    private Long organizationId;
}
