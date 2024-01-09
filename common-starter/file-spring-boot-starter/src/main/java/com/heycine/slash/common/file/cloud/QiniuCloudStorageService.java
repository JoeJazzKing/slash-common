/**
 * Copyright (c) 2018 yiplus All rights reserved.
 *
 * http://mwcare.cn/#/
 *
 * 版权所有，侵权必究！
 */

package com.heycine.slash.common.file.cloud;

import com.heycine.slash.common.file.config.CloudStorageConfig;
import com.heycine.slash.common.file.exception.FileUploadException;
import com.heycine.slash.common.file.enums.ErrorCodeEnum;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云存储
 *
 * @author zzj
 */
public class QiniuCloudStorageService extends AbstractCloudStorageService {

    private UploadManager uploadManager;

    private String token;

    public QiniuCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init() {
        uploadManager = new UploadManager(new Configuration(Region.autoRegion()));
        token = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey()).
                uploadToken(config.getQiniuBucketName());
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new FileUploadException(ErrorCodeEnum.OSS_UPLOAD_FILE_ERROR);
            }
        } catch (Exception e) {
            throw new FileUploadException(ErrorCodeEnum.OSS_UPLOAD_FILE_ERROR);
        }

        return config.getQiniuDomain() + "/" + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new FileUploadException(ErrorCodeEnum.OSS_UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {

        return upload(data, getPath(config.getQiniuPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {

        return upload(inputStream, getPath(config.getQiniuPrefix(), suffix));
    }

}
