package com.bjsxt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("重置交易密码")
public class UnsetPayPassword {

    @ApiModelProperty("新的交易密码")
    @NotBlank
    private String payPassword;  // 交易密码

    @ApiModelProperty("手机验证码")
    @NotBlank
    private String validateCode; // 手机验证码
}
