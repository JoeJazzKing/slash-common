package com.heycine.slash.common.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heycine.slash.common.business.entity.OssUploadRecordEntity;
import com.heycine.slash.common.business.repository.OssUploadRecordRepository;
import com.heycine.slash.common.file.config.OSSFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;


/**
 * @author zzj
 */
@Service
public class FileUploadService {

	@Autowired
	private OssUploadRecordRepository ossUploadRecordRepository;

	@Resource
	private OSSFactory ossFactory;

	/**
	 * 上传文件
	 *
	 * @param file
	 * @return 返回上传的URL地址
	 * @throws IOException
	 */
	public String upload(MultipartFile file) throws IOException {
		//上传文件
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		String url = ossFactory.build().uploadSuffix(file.getBytes(), extension);

		//保存文件信息
		OssUploadRecordEntity ossEntity = new OssUploadRecordEntity();
		ossEntity.setUrl(url);
		ossUploadRecordRepository.save(ossEntity);

		return url;
	}

	/**
	 * 分页记录
	 *
	 * @param params
	 * @return
	 */
	public Page<OssUploadRecordEntity> page(Map<String, Object> params) {
		IPage<OssUploadRecordEntity> page = null;

		return null;
	}

}
