package com.heycine.slash.common.file.config;

import com.heycine.slash.common.file.group.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 云存储配置信息
 *
 * @author zzj
 */
@ConfigurationProperties(prefix = "cloud-storage")
@Configuration

@Data
@ApiModel(value = "云存储配置信息")
public class CloudStorageConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "类型 1:阿里云 2:腾讯云 3:七牛云 4:MinIO 5:FastDFS 6:本地上传 ")
	@Range(min = 1, max = 6, message = "{oss.type.range}")
	private Integer type;

	/**
	 * 1 阿里云
	 */
	@ApiModelProperty(value = "阿里云绑定的域名")
	@NotBlank(groups = AliyunGroup.class)
	@URL(groups = AliyunGroup.class)
	private String aliyunDomain;
	@ApiModelProperty(value = "阿里云路径前缀")
	private String aliyunPrefix;
	@ApiModelProperty(value = "阿里云BucketName")
	@NotBlank(groups = AliyunGroup.class)
	private String aliyunBucketName;
	@ApiModelProperty(value = "阿里云EndPoint")
	@NotBlank(groups = AliyunGroup.class)
	private String aliyunEndPoint;
	@ApiModelProperty(value = "阿里云AccessKeyId")
	@NotBlank( groups = AliyunGroup.class)
	private String aliyunAccessKeyId;
	@ApiModelProperty(value = "阿里云AccessKeySecret")
	@NotBlank(groups = AliyunGroup.class)
	private String aliyunAccessKeySecret;

	/**
	 * 2 腾讯云
	 */
	@ApiModelProperty(value = "腾讯云绑定的域名")
	@NotBlank(groups = QcloudGroup.class)
	@URL(groups = QcloudGroup.class)
	private String qcloudDomain;
	@ApiModelProperty(value = "腾讯云BucketName")
	@NotBlank(groups = QcloudGroup.class)
	private String qcloudBucketName;
	@ApiModelProperty(value = "腾讯云路径前缀")
	private String qcloudPrefix;
	@ApiModelProperty(value = "腾讯云AppId")
	@NotNull(groups = QcloudGroup.class)
	private Integer qcloudAppId;
	@ApiModelProperty(value = "腾讯云SecretId")
	@NotBlank(groups = QcloudGroup.class)
	private String qcloudSecretId;
	@ApiModelProperty(value = "腾讯云SecretKey")
	@NotBlank(groups = QcloudGroup.class)
	private String qcloudSecretKey;
	@ApiModelProperty(value = "腾讯云COS所属地区")
	@NotBlank(groups = QcloudGroup.class)
	private String qcloudRegion;

	/**
	 * 3 七牛云
	 */
	@ApiModelProperty(value = "七牛绑定的域名")
	@NotBlank(groups = QiniuGroup.class)
	@URL(groups = QiniuGroup.class)
	private String qiniuDomain;
	@ApiModelProperty(value = "七牛路径前缀")
	private String qiniuPrefix;
	@ApiModelProperty(value = "七牛存储空间名")
	@NotBlank(groups = QiniuGroup.class)
	private String qiniuBucketName;
	@ApiModelProperty(value = "七牛ACCESS_KEY")
	@NotBlank(groups = QiniuGroup.class)
	private String qiniuAccessKey;
	@ApiModelProperty(value = "七牛SECRET_KEY")
	@NotBlank(groups = QiniuGroup.class)
	private String qiniuSecretKey;

	/**
	 * 4 Minio
	 */
	@ApiModelProperty(value = "Minio EndPoint")
	@NotBlank(groups = MinioGroup.class)
	private String minioEndPoint;
	@ApiModelProperty(value = "BucketName")
	@NotBlank(groups = MinioGroup.class)
	private String minioBucketName;
	@ApiModelProperty(value = "MinIO上传路径前缀")
	private String minioPrefix;
	@ApiModelProperty(value = "accessKey")
	@NotBlank(groups = MinioGroup.class)
	private String minioAccessKey;
	@ApiModelProperty(value = "secretKey")
	@NotBlank(groups = MinioGroup.class)
	private String minioSecretKey;


	/**
	 * 5 FastDFS
	 */
	@ApiModelProperty(value = "FastDFS绑定的域名")
	@NotBlank(groups = FastDFSGroup.class)
	@URL(groups = FastDFSGroup.class)
	private String fastdfsDomain;

	/**
	 * 6 本地上传
	 */
	@ApiModelProperty(value = "本地上传绑定的域名")
	@NotBlank(groups = LocalGroup.class)
	@URL(groups = LocalGroup.class)
	private String localDomain;
	@ApiModelProperty(value = "本地上传路径前缀")
	private String localPrefix;
	@ApiModelProperty(value = "本地上传存储目录")
	@NotBlank(groups = LocalGroup.class)
	private String localPath;

}