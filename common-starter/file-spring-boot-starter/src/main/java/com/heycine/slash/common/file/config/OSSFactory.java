
package com.heycine.slash.common.file.config;

import com.heycine.slash.common.file.cloud.*;
import com.heycine.slash.common.file.enums.CloudTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 文件上传Factory
 *
 * @author zzj
 */
@Component
public final class OSSFactory {

	@Resource
	private CloudStorageConfig config;

	public AbstractCloudStorageService build() {
		//获取云存储配置信息
		Integer type = config.getType();

		if (type.equals(CloudTypeEnum.QINIU.getCode())) {
			return new QiniuCloudStorageService(config);
		} else if (type.equals(CloudTypeEnum.ALIYUN.getCode())) {
			return new AliyunCloudStorageService(config);
		} else if (type.equals(CloudTypeEnum.QCLOUD.getCode())) {
			return new QcloudCloudStorageService(config);
		} else if (type.equals(CloudTypeEnum.FASTDFS.getCode())) {
			return new FastDFSCloudStorageService(config);
		} else if (type.equals(CloudTypeEnum.LOCAL.getCode())) {
			return new LocalCloudStorageService(config);
		} else if (type.equals(CloudTypeEnum.MINIO.getCode())) {
			return new MinioCloudStorageService(config);
		}
		return null;
	}

}