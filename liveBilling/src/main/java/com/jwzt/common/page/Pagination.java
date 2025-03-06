package com.jwzt.common.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表分页。包含list属性。
 */
@SuppressWarnings("serial")
public class Pagination extends SimplePage implements java.io.Serializable, AbstractPage {

	public Pagination() {
	}

	/**
	 * 构造器
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页几条数据
	 * @param totalCount
	 *            总共几条数据
	 */
	public Pagination(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	/**
	 * 构造器
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页几条数据
	 * @param totalCount
	 *            总共几条数据
	 * @param list
	 *            分页内容
	 */
	public Pagination(int pageNo, int pageSize, int totalCount, List<?> list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}

	/**
	 * 第一条数据位置
	 * 
	 * @return
	 */
	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * 当前页的数据
	 */
	private List<?> list;

	/**
	 * 获得分页内容
	 * 
	 * @return
	 */
	public List<?> getList() {
		return list;
	}

	/**
	 * 设置分页内容
	 * 
	 * @param list
	 */
	public void setList(List<?> list) {
		this.list = list;
	}

	/**
	 * 当前显示的页码。如：1 ... 4 5 6 其中...为-1
	 * 
	 * @return
	 */
	public List<Integer> getShowPages() {
		List<Integer> showPages = new ArrayList<Integer>();
		Integer totalPage = getTotalPage();
		if (pageNo - 4 > 1) {
			if (pageNo < totalPage - 2) {
				showPages.add(1);
				showPages.add(-1);
				showPages.add(pageNo - 2);
				showPages.add(pageNo - 1);
			} else if (pageNo - 6 > 1) {
				showPages.add(1);
				showPages.add(-1);
				for (int i = pageNo - 4; i <= pageNo - 1; i++) {
					showPages.add(i);
				}
			} else {
				for (int i = 1; i <= pageNo - 1; i++) {
					showPages.add(i);
				}
			}
		} else if (pageNo == 1) {
		} else {
			for (int i = 1; i <= pageNo - 1; i++) {
				showPages.add(i);
			}
		}
		showPages.add(pageNo);
		if (pageNo + 4 < totalPage) {
			if (pageNo > 2) {
				for (int i = pageNo + 1; i <= pageNo + 2; i++) {
					showPages.add(i);
				}
				showPages.add(-1);
				showPages.add(totalPage);
			} else if (pageNo + 6 < totalPage) {
				for (int i = pageNo + 1; i <= pageNo + 4; i++) {
					showPages.add(i);
				}
				showPages.add(-1);
				showPages.add(totalPage);
			} else {
				for (int i = pageNo + 1; i <= totalPage; i++) {
					showPages.add(i);
				}
			}
		} else if (pageNo == totalPage) {
		} else {
			for (int i = pageNo + 1; i <= totalPage; i++) {
				showPages.add(i);
			}
		}
		return showPages;
	}
}
