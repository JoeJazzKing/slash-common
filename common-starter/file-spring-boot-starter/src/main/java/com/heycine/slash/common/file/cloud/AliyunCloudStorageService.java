package com.heycine.slash.common.file.cloud;

import com.aliyun.oss.OSSClient;
import com.heycine.slash.common.file.config.CloudStorageConfig;
import com.heycine.slash.common.file.exception.FileUploadException;
import com.heycine.slash.common.file.enums.ErrorCodeEnum;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 阿里云存储
 *
 * @author zzj
 */
public class AliyunCloudStorageService extends AbstractCloudStorageService {

    public AliyunCloudStorageService(CloudStorageConfig config){

        this.config = config;
    }

    @Override
    public String upload(byte[] data, String path) {

        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        OSSClient client = new OSSClient(
                config.getAliyunEndPoint(),
                config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret()
        );
        try {
            client.putObject(config.getAliyunBucketName(), path, inputStream);
            client.shutdown();
        } catch (Exception e){
            throw new FileUploadException(ErrorCodeEnum.OSS_UPLOAD_FILE_ERROR);
        }

        return config.getAliyunDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {

        return upload(data, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {

        return upload(inputStream, getPath(config.getAliyunPrefix(), suffix));
    }

}