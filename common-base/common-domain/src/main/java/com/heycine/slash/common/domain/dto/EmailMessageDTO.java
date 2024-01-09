package com.heycine.slash.common.domain.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/8/30 下午5:38
 */
@Data
@ApiModel("EmailMessageDTO")
public class EmailMessageDTO {

	@ApiModelProperty(value = "对方的邮箱地址，可以是单个，也可以是多个")
	private String[] tos;

	@ApiModelProperty(value = "标题")
	private String subject;

	@ApiModelProperty(value = "邮件正文，可以是文本，也可以是HTML内容")
	private String content;

	@ApiModelProperty(value = "是否为HTML，如果是，那参数3识别为HTML内容")
	private Boolean isHtml;

	@ApiModelProperty(value = "可选附件，可以为多个或没有，将File对象加在最后一个可变参数中即可")
	private File[] files;

	@ApiModelProperty(value = "网络文件地址集合")
	private List<String> urls;

	/**
	 * 将网络地址，转换为本地文件
	 *
	 * @return java.io.File[]
	 * @author zhiji.zhou
	 * @date 2022.09.05 17:41:24
	 */
	public File[] getFiles() {
		if (CollectionUtil.isNotEmpty(this.urls)) {
			List<File> fileList = new ArrayList<>();
			for (String url : urls) {
				// 网络URL，转换为临时本地文件
				File tempFile = null;
				try {
					tempFile = File.createTempFile(FileNameUtil.getPrefix(url) + "_", "." + FileNameUtil.getSuffix(url));
					FileUtils.copyURLToFile(new URL(url), tempFile);
					fileList.add(tempFile);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (null != tempFile) {
						tempFile.deleteOnExit();
					}
				}
			}
			files = ArrayUtil.toArray(fileList, File.class);
		}
		return files;
	}

	/**
	 * 获取DataSource的集合
	 *
	 * @return javax.activation.DataSource[]
	 * @author zhiji.zhou
	 * @date 2022.09.05 18:51:20
	 */
	public DataSource[] getDataSources() {
		List<DataSource> dataSources = new ArrayList<>();

		File[] files = this.getFiles();
		for (File file : files) {
			dataSources.add(new FileDataSource(file));
		}

		return ArrayUtil.toArray(dataSources, DataSource.class);
	}

	public static void main(String[] args) {
		EmailMessageDTO emailMessageDTO = new EmailMessageDTO();
		ArrayList<String> urls = new ArrayList<>();
		urls.add("http://argus.epsilon.langjtech.com/static/img/argus-login.f1de3039.png");
		emailMessageDTO.setUrls(urls);

		File[] files = emailMessageDTO.getFiles();
		System.out.println(files);
		for (File file : files) {
			System.out.println(file.getPath());
		}
	}

}
