//package com.bjsxt.controller;
//
//import com.bjsxt.model.R;
//import com.bjsxt.model.WebLog;
//import com.bjsxt.service.TestService;
//import com.ctc.wstx.util.StringUtil;
//import io.swagger.annotations.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//
//@RestController
//@Api(tags = "CoinCommon里面测试的接口")
//public class TestController {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private TestService testService;
//
//    @GetMapping("/common/test")
//    @ApiOperation(value = "测试方法", authorizations = {@Authorization("Authorization")})
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "param", value = "参数1", dataType = "String", paramType = "query", example = "paramValue"),
//            @ApiImplicitParam(name = "param1", value = "参数2", dataType = "String", paramType = "query", example = "paramValue")
//    })
//    public R<String> testMethod(String param, String param1) {
//
//        return R.ok("ok");
//    }
//
//    @GetMapping("/jackson/test")
//    @ApiOperation(value = "date测试方法", authorizations = {@Authorization("Authorization")})
//    public R<Date> testJackson() {
//        return R.ok(new Date());
//    }
//
//    @GetMapping("/redis/test")
//    @ApiOperation(value = "redis测试方法", authorizations = {@Authorization("Authorization")})
//    public R<String> testRedis() {
//        WebLog webLog = new WebLog();
//        webLog.setResult("ok");
//        webLog.setMethod("com.bjsxt.controller.TestController.testRedis");
//        webLog.setUsername("110001111");
//        redisTemplate.opsForValue().set("com.bjsxt.Weblog", webLog);
//        return R.ok("ok");
//    }
//
//    @GetMapping("/jetcache/test")
//    @ApiOperation(value = "jetcache测试方法", authorizations = {@Authorization("Authorization")})
//    public R<String> testJetcache(String username) {
//        WebLog webLog = testService.getUsername(username);
//        System.out.println(webLog.hashCode());
//        WebLog webLog1 = new WebLog();
//        System.out.println(webLog1.hashCode());
//        return R.ok("ok");
//    }
//
//}
