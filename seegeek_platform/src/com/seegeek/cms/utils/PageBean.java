package com.seegeek.cms.utils;

import java.util.List;

/**
 * @author 作者 zhaogaofei
 * @email 邮箱 zhaogaofei2012@163.com
 * @date 创建时间 Sep 12, 2015 6:20:18 PM
 */
@SuppressWarnings("unchecked")
public class PageBean<T> {

	// 每页多少行记录
	private int pageSize = 20;
	// 当前第几页
	private int pageNo = 1;
	// 记录总数
	private int totalRows = 0;
	// 开始记录数
	private int startRow;
	// 结束记录数
	private int endRow;
	// 总页数
	private int totalPages;
	// 记录集合
	private List<T> resultList;

	public PageBean(int pageNo, int pageSize, int totalRows, List<T> resultList) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalRows = totalRows;
		this.resultList = resultList;
		int v_endrownum = pageNo * pageSize;
		this.startRow = v_endrownum - pageSize + 1;
	}

	public PageBean() {
	}

	public PageBean(int pageNo) {
		int v_endrownum = pageNo * pageSize;
		this.startRow = v_endrownum - pageSize + 1;
	}

	public PageBean(int startRow,int pageNo, int pageSize) {
		System.out.println("pageNum---"+pageNo);
		System.out.println("pageSize---"+pageSize);
		this.startRow=startRow;
		this.pageNo=pageNo;
		this.pageSize=pageSize;
		this.endRow = startRow + pageSize;
//		this.startRow = endRow - pageSize + 1;
//		if (pageSize != 0)
//			this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public String toString() {
		System.out.println("pageNo:"+pageNo);
		System.out.println("startRow:"+startRow);
		System.out.println("totalRows"+totalRows);
		return super.toString();
	}

	
}
