package com.heycine.slash.common.mybatisplus.basic;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * 基础service
 *
 * @author zzj
 */
public interface IBaseService<T> {

	String PAGE_NO = "pageNo";
	String PAGE_SIZE = "pageSize";
	String ORDER_FIELD = "orderField";
	String ORDER = "order";

	/**
	 * 获取wrapper
	 *
	 * @param paramMap
	 * @return
	 */
	default Wrapper<T> getWrapper(Map<String, Object> paramMap) {

		return new LambdaQueryWrapper<T>();
	}

	/**
	 * 获取分页插件 -按照map
	 *
	 * @param paramMap
	 * @return
	 */
	default IPage getPage(Map<String, Object> paramMap) {

		return new Page(Integer.parseInt(paramMap.get(PAGE_NO).toString()), Integer.parseInt(paramMap.get(PAGE_SIZE).toString()));
	}

	/**
	 * 获取分页插件 -按照参数
	 *
	 * @return
	 */
	default IPage getPage(Integer pageNo, Integer pageSize) {

		return new Page<>(pageNo, pageSize);
	}

}
