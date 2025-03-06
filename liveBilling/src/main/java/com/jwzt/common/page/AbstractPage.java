package com.jwzt.common.page;

/**
 * 分页接口
 */
public interface AbstractPage {
	/**
	 * 总记录数
	 * 
	 * @return
	 */
	public int getTotalCount();

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPage();

	/**
	 * 每页记录数
	 * 
	 * @return
	 */
	public int getPageSize();

	/**
	 * 当前页号
	 * 
	 * @return
	 */
	public int getPageNo();

	/**
	 * 是否第一页
	 * 
	 * @return
	 */
	public boolean isFirstPage();

	/**
	 * 是否最后一页
	 * 
	 * @return
	 */
	public boolean isLastPage();

	/**
	 * 获取下页的页号
	 * 
	 * @return
	 */
	public int getNextPage();

	/**
	 * 获取上页的页号
	 * 
	 * @return
	 */
	public int getPrePage();
}
