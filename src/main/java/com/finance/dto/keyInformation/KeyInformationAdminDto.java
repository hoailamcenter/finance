package com.finance.dto.keyInformation;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.account.AccountDto;
import com.finance.dto.keyInformantionGroup.KeyInformationGroupDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KeyInformationAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "additionalInformation")
    private String additionalInformation;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "keyInformationGroup")
    private KeyInformationGroupDto keyInformationGroup;
}
