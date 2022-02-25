package com.bjsxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.UserBank;
import com.bjsxt.model.R;
import com.bjsxt.service.UserBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userBanks")
@Api(tags = "会员的银行卡管理")
public class UserBankController {
    @Autowired
    private UserBankService userBankService ;


    @GetMapping
    @ApiOperation(value = "分页查询用户的银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usrId" ,value = "会员的Id") ,
            @ApiImplicitParam(name = "current" ,value = "当前页")  ,
            @ApiImplicitParam(name = "size" ,value = "每页显示的条数")
    })
    @PreAuthorize("hasAuthority('user_bank_query')")
    public R<Page<UserBank>> findByPage(Page<UserBank> page , Long usrId){
        page.addOrder(OrderItem.desc("last_update_time")) ;
        Page<UserBank> userBankPage = userBankService.findByPage(page ,usrId) ;
        return R.ok(userBankPage) ;
    }

    @PostMapping("/status")
    @ApiOperation(value = "更新银行卡状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "银行卡id"),
            @ApiImplicitParam(name = "status", value = "银行卡状态")
    })
    public R updateStatus(Long id, Byte status) {
        UserBank userBank = new UserBank();
        userBank.setId(id);
        userBank.setStatus(status);
        boolean b = userBankService.updateById(userBank);
        if (b) {
            return R.ok();
        }
        return R.fail("更新银行卡状态失败");
    }

    @PatchMapping
    @ApiOperation(value = "更新银行卡信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userBank", value = "银行卡json数据")
    })
    public R update(@RequestBody @Validated UserBank userBank) {
        boolean updateById = userBankService.updateById(userBank);
        if(updateById){
            return R.ok() ;
        }
        return R.fail("银行卡状态修改失败") ;
    }

    /**
     * 查询当前用户的银行卡,银行卡默认只能查到一张，因为如果要让别人转账的时候，别人看到多张银行卡，就会有疑问，
     * 还要再次确认要转到哪一张银行卡里。
     * @return
     */
    @GetMapping("/current")
    @ApiOperation(value = "查询当前用户的银行卡")
    public R<UserBank> getCurrentUserBank() {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserBank userBank = userBankService.getCurrentUserBank(Long.valueOf(idStr));
        return R.ok(userBank);
    }

    /**
     * 如果用户能够提供id，即前端提交过来的数据有银行卡id，则认为是更换银行卡
     * 如果没有提供银行卡id，那么则认为是新增操作
     */
    @PostMapping("/bind")
    @ApiOperation(value = "绑定银行卡")
    public R bindBank(@RequestBody @Validated UserBank userBank) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userBankService.bind(Long.valueOf(idStr), userBank);
        return isOk ? R.ok() : R.fail("绑定银行卡失败");
    }
}