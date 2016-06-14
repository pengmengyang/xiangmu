package com.example.expressscanningsystem.tools;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
/**
 * 错误输出
 */
public class RequestErrorInfo {
	public static String getErrorMessage(VolleyError error) {
		String errorMessage = "";
		if (error instanceof AuthFailureError) { // 验证失败
			errorMessage = "验证失败";
		} 
		if (error instanceof NetworkError) { // 网络错误
			errorMessage = "网络错误";
		}
		if (error instanceof TimeoutError) { // 网络连接超时
			errorMessage = "网络连接超时";
		}
		if (error instanceof NoConnectionError) { // 无法连接服务器
			errorMessage = "无法连接服务器";
		}
		if (error instanceof ServerError) { // 服务器响应错误
			errorMessage = "服务器响应错误";
		}
		if (error instanceof ParseError) { // 无法解析服务器的返回值
			errorMessage = "无法解析服务器的返回值";
		}
		if (error instanceof VolleyError) { // 接口返回的错误
//			errorMessage = "服务器繁忙,稍后再试";
			errorMessage = "网络繁忙,稍后再试";
		}
		return errorMessage;
	}
	
}
