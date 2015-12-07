package com.seegeek.cms.exception;

/**
 * 自定义异常类
 * @author Administrator
 * @version 1.0
 * @created 24-七月-2012 14:23:10
 */
public class AppException extends Exception {

	/**
	 * 异常码
	 */
	private int exceptionCode;
	/**
	 * 异常描述信息
	 */
	private String easyMessage;
	

	public AppException(int code) {
		super();
		this.exceptionCode = code;
	}

	public AppException(int code, String message) {
		super(message);
		this.exceptionCode = code;
		this.easyMessage = message;
	}

	public int getExceptionCode() {
		return this.exceptionCode;
	}

	public String getExceptionMessage() {
		return this.easyMessage;
	}

}