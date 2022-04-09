package com.news.nms.config;


/*
 * 腾讯云对象存储（COS）配置文件
 */
public class CosConfig {
    public static final String SECRET_ID = System.getenv("SecretId");
    public static final String SECRET_KEY = System.getenv("SecretKey");
    public static final String REGION = System.getenv("Region");
    public static final String BUCKET_NAME = System.getenv("BucketName");
}
