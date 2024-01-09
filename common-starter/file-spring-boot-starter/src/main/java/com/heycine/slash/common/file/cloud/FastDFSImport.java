/**
 * Copyright (c) 2018 yiplus All rights reserved.
 *
 * http://mwcare.cn/#/
 *
 * 版权所有，侵权必究！
 */

package com.heycine.slash.common.file.cloud;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * 导入FastDFS-Client组件
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastDFSImport {

}