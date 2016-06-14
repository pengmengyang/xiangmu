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
 * Volley���� ������ ����ģʽ
 * 
 * @author Administrator
 * 
 */
public class VolleyRequestUtil {

	/**˽�л���Ա����*/
	private static VolleyRequestUtil volleyRequestUtil;//����ʵ��
	private Context context;//������

	/**˽�л����췽��*/
	private VolleyRequestUtil (Context context){

		this.context=context;

	}

	/**�ṩ���÷������ⲿ����*/

	public static synchronized VolleyRequestUtil getNewInstanceVolleyRequestUtil(Context context){

		/**�ж��Ƿ�Ϊ��*/
		if(volleyRequestUtil == null ){

			volleyRequestUtil = new VolleyRequestUtil(context);//ʵ����

		}

		return volleyRequestUtil;

	}

	/**Post����������String*/
	public void PostRequestString(String url,InterfaceVolleyRequest interfaceVolleyRequest,final HashMap<String, String> map,String tag){

		StringRequest stringRequest=new StringRequest(Method.POST, url, interfaceVolleyRequest.loadingListener(), interfaceVolleyRequest.errorListener()){@Override

			protected Map<String, String> getParams() throws AuthFailureError {

			return map;

		}};
		/**�������������*/
		stringRequest.setTag(tag);
		stringRequest.setShouldCache(true);//�����Ƿ񻺴�
		/**���ó�ʱʱ���Ѿ�����ʱ��*/
				stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		/**��ӵ��������*/
		MyRequestQueue.getNewInstanceRequestQueue(context).add(stringRequest);
	}


	/**Post��������JsonObjectRequest*/
	public void PostRequestJsonObjectRequest(String url,InterfaceVolleyRequest interfaceVolleyRequest,final HashMap<String, String> map,String tag){

		/**�Ѳ�������*/
		JSONObject jsonRequest = null;
		if(null != map && map.size() > 0){

			jsonRequest=new JSONObject(map);

		}

		JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Method.POST, url, jsonRequest, interfaceVolleyRequest.JsonObjectlistener(), interfaceVolleyRequest.errorListener());
		/**�������������*/
		jsonObjectRequest.setTag(tag);
		jsonObjectRequest.setShouldCache(true);//�����Ƿ񻺴�
		/**���ó�ʱʱ���Ѿ�����ʱ��*/
//				stringRequest.setRetryPolicy(new DefaultRetryPolicy(5, 5, 5.0f));
		/**��ӵ��������*/
		MyRequestQueue.getNewInstanceRequestQueue(context).add(jsonObjectRequest);
	}


	/**Get����������String*/
	public void GetRequestString(String urls,InterfaceVolleyRequest interfaceVolleyRequest,final HashMap<String, String> map,String tag){
		String url=urls;
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(urls).append("&");
		/**�жϲ����Ƿ�Ϊ��*/
		if(null != map && map.size() >0){
			/**�Ѳ������ɵ���ַ����ȥ*/
			for (Map.Entry<String, String> entry : map.entrySet()) {  

				/**�����������������ģ���Ҫ����URLEncoder����*/  

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
		/**�������������*/
		stringRequest.setTag(tag);
		stringRequest.setShouldCache(true);//�����Ƿ񻺴�
		/**���ó�ʱʱ���Ѿ�����ʱ��*/
//				stringRequest.setRetryPolicy(new DefaultRetryPolicy(5, 5, 5.0f));
		/**��ӵ��������*/
		MyRequestQueue.getNewInstanceRequestQueue(context).add(stringRequest);
	}


	/**����������*/
	public void cancelRequestQueue(String tag){

		if(MyRequestQueue.getNewInstanceRequestQueue(context)!= null){

			/**��ն���*/
			MyRequestQueue.getNewInstanceRequestQueue(context).cancelAll(tag);

		}

	}




}
