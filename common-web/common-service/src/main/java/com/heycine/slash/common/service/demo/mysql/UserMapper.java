package com.heycine.slash.common.service.demo.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-30 12:54:32
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {


}