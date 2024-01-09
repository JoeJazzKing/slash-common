/**
 * Copyright (c) 2018 yiplus All rights reserved.
 * <p>
 * http://mwcare.cn/#/
 * <p>
 * 版权所有，侵权必究！
 */

package com.heycine.slash.common.file.cloud;

import cn.hutool.core.io.IoUtil;
import com.heycine.slash.common.file.config.CloudStorageConfig;
import com.heycine.slash.common.file.exception.FileUploadException;
import com.heycine.slash.common.file.enums.ErrorCodeEnum;

import java.io.*;

/**
 * 本地上传
 *
 * @author zzj
 */
public class LocalCloudStorageService extends AbstractCloudStorageService {

	public LocalCloudStorageService(CloudStorageConfig config) {

		this.config = config;
	}

	@Override
	public String upload(byte[] data, String path) {

		return upload(new ByteArrayInputStream(data), path);
	}

	@Override
	public String upload(InputStream inputStream, String path) {
		File file = new File(config.getLocalPath() + File.separator + path);
		try {
			IoUtil.copy(inputStream, new FileOutputStream(file));
		} catch (IOException e) {
			throw new FileUploadException(ErrorCodeEnum.OSS_UPLOAD_FILE_ERROR);
		}
		return config.getLocalDomain() + "/" + path;
	}

	@Override
	public String uploadSuffix(byte[] data, String suffix) {

		return upload(data, getPath(config.getLocalPrefix(), suffix));
	}

	@Override
	public String uploadSuffix(InputStream inputStream, String suffix) {

		return upload(inputStream, getPath(config.getLocalPrefix(), suffix));
	}

}
