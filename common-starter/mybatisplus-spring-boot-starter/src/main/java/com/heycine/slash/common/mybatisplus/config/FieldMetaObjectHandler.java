package com.heycine.slash.common.mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 公共字段，自动填充值
 *
 * @author zhiji.zhou
 */
@Component
public class FieldMetaObjectHandler implements MetaObjectHandler {
	/**
	 * 创建时间
	 */
	private final static String CREATE_DATE = "createTime";
	/**
	 * 修改时间
	 */
	private final static String UPDATE_DATE = "updateTime";

	@Override
	public void insertFill(MetaObject metaObject) {
		Date date = new Date();

		// 创建时间
		this.strictInsertFill(metaObject, CREATE_DATE, Date.class, date);

		// 更新时间
		this.strictInsertFill(metaObject, UPDATE_DATE, Date.class, date);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Date date = new Date();

		// 更新时间
		this.strictUpdateFill(metaObject, UPDATE_DATE, Date.class, date);
	}

}
