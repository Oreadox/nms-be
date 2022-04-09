package com.news.nms.controller;

import com.news.nms.config.CosConfig;
import com.news.nms.config.PermissionConfig;
import com.news.nms.model.response.BaseResponse;
import com.news.nms.model.response.UrlResponse;
import com.news.nms.model.response.data.UrlData;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.region.Region;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.Date;


@RestController
@RequestMapping("/file")
public class FileController {
    private COSCredentials cred = new BasicCOSCredentials(CosConfig.SECRET_ID, CosConfig.SECRET_KEY);
    private Region region = new Region(CosConfig.REGION);
    private String bucketName = CosConfig.BUCKET_NAME;
    private COSClient cosClient = new COSClient(cred, new ClientConfig(region));


    @GetMapping("/presigned_url/{filename}")
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    public ResponseEntity<?> getPresignedUrl (@PathVariable String filename) {
        Date expirationTime = new Date(System.currentTimeMillis() + 3 * 60 * 1000);
        try{
            URL url = cosClient.generatePresignedUrl(bucketName, filename, expirationTime, HttpMethodName.PUT);
            return new ResponseEntity<>(
                    UrlResponse.builder().status(1).message("成功").data(new UrlData(url.toString())).build()
                    , HttpStatus.OK);
        } catch (CosClientException e) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("获取失败").build()
                    , HttpStatus.OK);
        }
    }

    @GetMapping("/url/{filename}")
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    public ResponseEntity<?> getFileUrl (@PathVariable String filename) {
        URL url = cosClient.getObjectUrl(bucketName, filename);
        return new ResponseEntity<>(
                UrlResponse.builder().status(1).message("成功").data(new UrlData(url.toString())).build()
                , HttpStatus.OK);
    }
}
