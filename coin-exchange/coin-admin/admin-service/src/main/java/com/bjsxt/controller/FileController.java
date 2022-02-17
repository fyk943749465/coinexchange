package com.bjsxt.controller;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.bjsxt.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Api(tags= "文件上传")
public class FileController {

    @Autowired
    private OSS ossClient;

    @Value("${oss.bucket.name:mall-feng}")
    private String bucketName;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endPoint;

    @ApiOperation(value = "文件上传")
    @PostMapping("/image/AliYunImgUpload")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "你要上传的文件")
    })
    public R<String> fileUpload(@RequestParam("file")MultipartFile file) throws IOException {

        String fileName = DateUtil.today().replaceAll("-", "/") + "/" + file.getOriginalFilename();
        ossClient.putObject(bucketName, fileName, file.getInputStream());
        return R.ok("https://" + bucketName + "." + endPoint + "/" + fileName);
    }
}
