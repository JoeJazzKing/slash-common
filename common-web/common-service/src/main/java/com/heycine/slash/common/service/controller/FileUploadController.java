package com.heycine.slash.common.service.controller;

import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heycine.slash.common.basic.http.R;
import com.heycine.slash.common.business.entity.OssUploadRecordEntity;
import com.heycine.slash.common.file.config.CloudStorageConfig;
import com.heycine.slash.common.service.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.Map;

/**
 * 文件上传
 *
 * @author zzj
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件上传")
public class FileUploadController {

	@Autowired
	private FileUploadService fileUploadService;

	@PostMapping("/upload")
	@ApiOperation(value = "上传文件")
	public R<String> upload(@RequestPart("file") MultipartFile file) {
		if (file.isEmpty()) {
			return R.fail("上传文件不能为空!");
		}

		String uploadURL;
		try {
			uploadURL = fileUploadService.upload(file);
		} catch (IOException e) {
			e.printStackTrace();
			return R.fail("上传文件发生异常!请稍后重试");
		}

		return R.ok(uploadURL);
	}


	@GetMapping("/page")
	@ApiOperation(value = "分页")
	public R<Page<OssUploadRecordEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

		Page<OssUploadRecordEntity> page = fileUploadService.page(params);

		return R.ok(page);
	}

	@GetMapping("/info")
	@ApiOperation(value = "云存储配置信息")
	public R<CloudStorageConfig> configInfo() {
		CloudStorageConfig bean = SpringUtil.getBean(CloudStorageConfig.class);
		CloudStorageConfig copy = CglibUtil.copy(bean, CloudStorageConfig.class);
		return R.ok(copy);
	}


}