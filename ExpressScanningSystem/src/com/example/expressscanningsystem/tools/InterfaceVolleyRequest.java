package com.example.expressscanningsystem.tools;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

/**
 * 请求第二次封装接口回调
 * @author Administrator
 *
 */
public abstract class InterfaceVolleyRequest {
	
	/**请求String成功*/
	public abstract void onsResponse(String response);
	
	/**请求JSONObject成功*/
	public abstract void onjResponse(JSONObject obj);

	/**请求失败*/
	public abstract void onsErrorResponse(VolleyError error);
	
	@SuppressWarnings("unused")
	private Context context;//上下文
	private Listener<String> listener;//请求成功监听
	private ErrorListener errorListener;//请求失败监听
	private Listener<JSONObject> mJsonListener;//json数据请求成功
	
	/**无参构造方法*/
	public InterfaceVolleyRequest(){
		
	}
	
	
	/**构造方法初始化*/
	public InterfaceVolleyRequest(Context context,Listener<String> listener,ErrorListener errorListener){
		
		this.context=context;
		this.listener=listener;
		this.errorListener=errorListener;
		
	}
	
	/**得到String请求成功的结果*/
	public Listener<String> loadingListener(){
		
		listener=new Listener<String>() {

			@Override
			public void onResponse(String response) {
				
				/**请求成功*/
				onsResponse(response);
				
			}
		};
	
		return listener;
		
	}
	
	/**得到JSONObject请求成功的结果*/
	public Listener<JSONObject> JsonObjectlistener(){
		
		mJsonListener=new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				
				/**请求成功*/
				onjResponse(response);
				
			}
		};
		
		return mJsonListener;
		
	}
	
	/**得到请求失败的结果*/
	public ErrorListener errorListener(){
		
		errorListener=new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				/**请求失败*/
				onsErrorResponse(error);
				
			}
		};
		
		return errorListener; 
		
	}
}
