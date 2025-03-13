package com.hsbc.demo.transaction.api.base;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Pagination Response
 * @author miles.zeng
 * @since  
 */
@Getter
@Setter
public class PageResult<T> {

   	/**
	 * Page number, starting from 1
	 */
	private int page;
	/**
	 * Number of results per page
	 */
	private int pageSize;
	/**
	 * Total number of pages
	 */
	private int totalPages;
	/**
	 * Total count
	 */
	private int total;

	/**
	 * Results
	 */
	private List<T> results;

}
