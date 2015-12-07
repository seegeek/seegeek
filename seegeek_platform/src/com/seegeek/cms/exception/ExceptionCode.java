package com.seegeek.cms.exception;

/**
 * 异常编码接口
 * 
 * 异常编码规则 1)编码长度为6位； 2)第一二位用来表示异常发生在哪个层级或系统中（如：1－http/socket tcpip
 * 、2－流媒体层的业务、3－平台 4、客户端测的） 3)第三四位表示异常的业务类型 鉴权 4)其余位数用来表示顺序号。
 * 
 * @author Administrator
 * @version 1.0
 */
public interface ExceptionCode {
	/**
	 * 连接超时
	 */
	public static int CONNECTION_TIMEOUT = 101001;
	/**
	 * 用户名或者密码错误
	 */
	public static int USER_OR_PWD_ERROR = 301001;
	/**
	 * token 过期
	 */
	public static int TOKEN_TIMEOUT = 301002;
	/**
	 * token 认证
	 */
	public static int TOKEN_VERIFY = 301003;

}