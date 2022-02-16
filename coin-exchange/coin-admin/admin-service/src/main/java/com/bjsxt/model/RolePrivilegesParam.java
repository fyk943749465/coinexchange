package com.bjsxt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel("接收角色和权限数据")
public class RolePrivilegesParam {

    @ApiModelProperty(value = "角色ID")
    @NotNull
    private Long roleId;

    @ApiModelProperty(value = "角色对应的权限列表")
    private List<Long> privilegeIds = Collections.emptyList();
}
