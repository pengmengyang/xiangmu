package com.example.expressscanningsystem.tools;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 请求队列单例模式
 * @author Administrator
 *
 */
public class MyRequestQueue {
	
	/**私有化成员变量*/
	private static RequestQueue requestQueue;//请求队列
	@SuppressWarnings("unused")
	private Context context;//上下文
	
	/**私有化构造方法*/
	private MyRequestQueue(Context context){
		
		this.context=context;
		
	}
	
	/**提供公用方法供外部访问得到请求队列的实例*/
	public static synchronized RequestQueue getNewInstanceRequestQueue(Context context){
		
		/**判断请求队列是否为空*/
		if(requestQueue == null){
			
			requestQueue=Volley.newRequestQueue(context);//实例化请求队列
			
		}
		
		return requestQueue;
		
	}
	
}
