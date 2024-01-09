/**
 * Copyright (c) 2018 yiplus All rights reserved.
 *
 * http://mwcare.cn/#/
 *
 * 版权所有，侵权必究！
 */

package com.heycine.slash.common.file.cloud;

import cn.hutool.extra.spring.SpringUtil;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultGenerateStorageClient;
import com.heycine.slash.common.file.config.CloudStorageConfig;
import com.heycine.slash.common.file.exception.FileUploadException;
import com.heycine.slash.common.file.enums.ErrorCodeEnum;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * FastDFS
 *
 * @author zzj
 */
public class FastDFSCloudStorageService extends AbstractCloudStorageService {

    private static DefaultGenerateStorageClient defaultGenerateStorageClient;

    static {
        defaultGenerateStorageClient = SpringUtil.getBean("defaultGenerateStorageClient");
    }

    public FastDFSCloudStorageService(CloudStorageConfig config){

        this.config = config;
    }

    @Override
    public String upload(byte[] data, String path) {

        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String suffix) {
        StorePath storePath;

        try {
            storePath = defaultGenerateStorageClient.uploadFile("group1", inputStream, inputStream.available(), suffix);
        }catch (Exception ex){
            throw new FileUploadException(ErrorCodeEnum.OSS_UPLOAD_FILE_ERROR);
        }

        return config.getFastdfsDomain() + "/" + storePath.getPath();
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {

        return upload(data, suffix);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {

        return upload(inputStream, suffix);
    }

}