package com.bjsxt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "注册的表单数据")
public class RegisterParam extends GeetestForm{

    @ApiModelProperty(value = "国家代码")
    private String countryCode;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("邀请码")
    private String invitionCode;
    @ApiModelProperty("密码")
    @NotBlank
    private String password;
    @ApiModelProperty("手机号码")
    private String mobile;
    @ApiModelProperty("验证码--现在被放弃使用了")
    private String validateCode;
}
