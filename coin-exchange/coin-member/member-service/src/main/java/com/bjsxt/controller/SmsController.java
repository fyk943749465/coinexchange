package com.bjsxt.controller;

import com.bjsxt.domain.Sms;
import com.bjsxt.model.R;
import com.bjsxt.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
@Api(tags = "sms 短信发送平台")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/sendTo")
    @ApiOperation(value = "发送短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sms", value = "smsjson数据")
    })
    public R sendSms(@RequestBody @Validated Sms sms) {
        boolean isOk = smsService.sendSms(sms);
        if (isOk) {
            return R.ok();
        }
        return R.fail("发送失败");
    }
}
