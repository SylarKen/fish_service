package com.domor.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 异步返回结果封装
 * @author liyy
 * @since 2018-07-26 11:50:23
 */
@Data
public class Result implements Serializable {

	// 成功
	private static final int OK = 1;
	// 异常
	private static final int ERROR = 2;
	// session超时
	private static final int TIMEOUT = 3;
	// 无权限
	private static final int NOAUTH = 4;

	private int status;
	private Object data;
	private String message;
	private String exception;

	public static Result success(){
		Result result = new Result();
		result.status = OK;
		return result;
	}

	public static Result success(Object data){
		Result result = new Result();
		result.status = OK;
		result.data = data;
		return result;
	}

	public static Result error(){
		Result result = new Result();
		result.status = ERROR;
		return result;
	}

	public static Result error(String message){
		Result result = new Result();
		result.status = ERROR;
		result.message = message;
		return result;
	}

	public static Result error(String message, String exception){
		Result result = new Result();
		result.status = ERROR;
		result.message = message;
		result.exception = exception;
		return result;
	}

	public static Result noAuth(){
		Result result = new Result();
		result.status = NOAUTH;
		return result;
	}

	public static Result timeOut(){
		Result result = new Result();
		result.status = TIMEOUT;
		return result;
	}


}
