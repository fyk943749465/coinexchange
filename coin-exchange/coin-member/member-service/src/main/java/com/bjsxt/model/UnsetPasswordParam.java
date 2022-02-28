package com.bjsxt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "重置密码表单")
public class UnsetPasswordParam extends GeetestForm{

    @ApiModelProperty(value = "国家代码")
    private String countryCode;

    @ApiModelProperty(value = "手机号码")
    @NotBlank
    private String mobile;

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "验证码")
    @NotBlank
    private String validateCode;
}
