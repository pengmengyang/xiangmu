package com.example.expressscanningsystem.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import android.content.Context;

/**
 * Volley请求 工具类 单例模式
 * 
 * @author Administrator
 * 
 */
public class VolleyRequestUtil {

	/**私有化成员变量*/
	private static VolleyRequestUtil volleyRequestUtil;//本类实例
	private Context context;//上下文

	/**私有化构造方法*/
	private VolleyRequestUtil (Context context){

		this.context=context;

	}

	/**提供公用方法供外部访问*/

	public static synchronized VolleyRequestUtil getNewInstanceVolleyRequestUtil(Context context){

		/**判断是否为空*/
		if(volleyRequestUtil == null ){

			volleyRequestUtil = new VolleyRequestUtil(context);//实例化

		}

		return volleyRequestUtil;

	}

	/**Post带参数请求String*/
	public void PostRequestString(String url,InterfaceVolleyRequest interfaceVolleyRequest,final HashMap<String, String> map,String tag){

		StringRequest stringRequest=new StringRequest(Method.POST, url, interfaceVolleyRequest.loadingListener(), interfaceVolleyRequest.errorListener()){@Override

			protected Map<String, String> getParams() throws AuthFailureError {

			return map;

		}};
		/**给请求做个标记*/
		stringRequest.setTag(tag);
		stringRequest.setShouldCache(true);//控制是否缓存
		/**设置超时时间已经请求时间*/
				stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		/**添加到请求队列*/
		MyRequestQueue.getNewInstanceRequestQueue(context).add(stringRequest);
	}


	/**Post带参请求JsonObjectRequest*/
	public void PostRequestJsonObjectRequest(String url,InterfaceVolleyRequest interfaceVolleyRequest,final HashMap<String, String> map,String tag){

		/**把参数传入*/
		JSONObject jsonRequest = null;
		if(null != map && map.size() > 0){

			jsonRequest=new JSONObject(map);

		}

		JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Method.POST, url, jsonRequest, interfaceVolleyRequest.JsonObjectlistener(), interfaceVolleyRequest.errorListener());
		/**给请求做个标记*/
		jsonObjectRequest.setTag(tag);
		jsonObjectRequest.setShouldCache(true);//控制是否缓存
		/**设置超时时间已经请求时间*/
//				stringRequest.setRetryPolicy(new DefaultRetryPolicy(5, 5, 5.0f));
		/**添加到请求队列*/
		MyRequestQueue.getNewInstanceRequestQueue(context).add(jsonObjectRequest);
	}


	/**Get带参数请求String*/
	public void GetRequestString(String urls,InterfaceVolleyRequest interfaceVolleyRequest,final HashMap<String, String> map,String tag){
		String url=urls;
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(urls).append("&");
		/**判断参数是否为空*/
		if(null != map && map.size() >0){
			/**把参数集成到地址里面去*/
			for (Map.Entry<String, String> entry : map.entrySet()) {  

				/**如果请求参数中有中文，需要进行URLEncoder编码*/  

				try {
					stringBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
					stringBuilder.append("&");                            
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  

			}  

			stringBuilder.deleteCharAt(stringBuilder.length()-1);  

			url = stringBuilder.toString(); 
			//			 Log.d("tag", "------url------"+url);
		}

		StringRequest stringRequest=new StringRequest(Method.GET, url, interfaceVolleyRequest.loadingListener(), interfaceVolleyRequest.errorListener());
		/**给请求做个标记*/
		stringRequest.setTag(tag);
		stringRequest.setShouldCache(true);//控制是否缓存
		/**设置超时时间已经请求时间*/
//				stringRequest.setRetryPolicy(new DefaultRetryPolicy(5, 5, 5.0f));
		/**添加到请求队列*/
		MyRequestQueue.getNewInstanceRequestQueue(context).add(stringRequest);
	}


	/**清空请求队列*/
	public void cancelRequestQueue(String tag){

		if(MyRequestQueue.getNewInstanceRequestQueue(context)!= null){

			/**清空队列*/
			MyRequestQueue.getNewInstanceRequestQueue(context).cancelAll(tag);

		}

	}




}
