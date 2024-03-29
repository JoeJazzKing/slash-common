package com.heycine.slash.common.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heycine.slash.common.business.entity.ExampleEntity;
import com.heycine.slash.common.business.entity.TestOrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务表
 *
 * @author zhiji.zhou
 * @date 2022/1/11
 */
@Mapper
public interface TestOrderMapper extends BaseMapper<TestOrderEntity> {

}
