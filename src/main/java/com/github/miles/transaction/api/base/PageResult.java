package com.github.miles.transaction.api.base;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页响应
 * @author miles.zeng
 * @since  
 */
@Getter
@Setter
public class PageResult<T> {

   	/**
	 * 页码，从1开始
	 */
	private int page;
	/**
	 * 每页结果数
	 */
	private int pageSize;
	/**
	 * 总页数
	 */
	private int totalPages;
	/**
	 * 总数
	 */
	private int total;

	/**
	 * 结果
	 */
	private List<T> results;

}
