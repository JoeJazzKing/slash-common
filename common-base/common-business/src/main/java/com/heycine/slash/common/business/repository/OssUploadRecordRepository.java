package com.heycine.slash.common.business.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heycine.slash.common.business.entity.ExampleEntity;
import com.heycine.slash.common.business.entity.OssUploadRecordEntity;
import com.heycine.slash.common.business.mapper.ExampleMapper;
import com.heycine.slash.common.business.mapper.OssUploadRecordMapper;
import org.springframework.stereotype.Service;

/**
 * 任务表
 *
 * @author zhiji.zhou
 * @date 2022/1/11
 */
@Service
public class OssUploadRecordRepository extends ServiceImpl<OssUploadRecordMapper, OssUploadRecordEntity> {

}
