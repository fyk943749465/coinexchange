package com.bjsxt.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.bjsxt.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Api(tags= "文件上传")
public class FileController {

    @Autowired
    private OSS ossClient;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${oss.bucket.name:mall-feng}")
    private String bucketName;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endPoint;

    @Value("${oss.callback.url:http://coinoss.free.idcfengye.com}")
    private String ossCallbackUrl; //公网能访问到的地址，默认调用的是9000端口

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

    @GetMapping("/image/pre/upload")
    public R<Map<String, String>> preUploadPolicy() {
        String dir = DateUtil.today().replaceAll("-", "/") + "/";
        Map<String, String> policy = getPolicy(30L, 3 * 1024 * 1024, dir);
        return R.ok(policy);
    }

    private Map<String, String> getPolicy(long expireTime, int maxFileSize, String dir) {
        try {
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小是5GB,及CONTENT_LENGTH_RANGE = 5 * 1024 * 1024
            PolicyConditions policyConditions = new PolicyConditions();
            // 设置上传文件的最大体积
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxFileSize);
            // 设置上传文件到哪个文件夹
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions); // 设置policy票据的有效时间
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            // 返回值
            Map<String, String> respMap = new LinkedHashMap<>();
            respMap.put("accessid", accessId); // 设置我们之前的accessKey
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", "https://" + bucketName +"."+ endPoint); // 设置 上传的Host https://bulkname.endpoint
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

            JSONObject jasonCallback = new JSONObject();

            jasonCallback.put("callbackUrl", ossCallbackUrl); // 当我们的前端把文件上传到oss 服务器成功后
            // ,oss 服务器会想回调的callbackUrl(公网) 发一个post请求来告诉后端服务器用户上传文件的情况
            jasonCallback.put("callbackBody",
                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);
            return respMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
