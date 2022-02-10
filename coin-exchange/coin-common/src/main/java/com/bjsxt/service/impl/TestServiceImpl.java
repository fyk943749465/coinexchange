//package com.bjsxt.service.impl;
//
//import com.alicp.jetcache.anno.CacheType;
//import com.alicp.jetcache.anno.Cached;
//import com.bjsxt.model.WebLog;
//import com.bjsxt.service.TestService;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TestServiceImpl implements TestService {
//
//    @Override
//    @Cached(name="com.bjsxt.service.impl.TestServiceImpl:", key="#name", cacheType = CacheType.BOTH)
//    public WebLog getUsername(String name) {
//        WebLog webLog = new WebLog();
//        webLog.setUsername(name);
//        webLog.setResult("ok");
//        return webLog;
//    }
//}
