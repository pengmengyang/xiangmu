package com.example.expressscanningsystem.tools;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

/**
 * ����ڶ��η�װ�ӿڻص�
 * @author Administrator
 *
 */
public abstract class InterfaceVolleyRequest {
	
	/**����String�ɹ�*/
	public abstract void onsResponse(String response);
	
	/**����JSONObject�ɹ�*/
	public abstract void onjResponse(JSONObject obj);

	/**����ʧ��*/
	public abstract void onsErrorResponse(VolleyError error);
	
	@SuppressWarnings("unused")
	private Context context;//������
	private Listener<String> listener;//����ɹ�����
	private ErrorListener errorListener;//����ʧ�ܼ���
	private Listener<JSONObject> mJsonListener;//json��������ɹ�
	
	/**�޲ι��췽��*/
	public InterfaceVolleyRequest(){
		
	}
	
	
	/**���췽����ʼ��*/
	public InterfaceVolleyRequest(Context context,Listener<String> listener,ErrorListener errorListener){
		
		this.context=context;
		this.listener=listener;
		this.errorListener=errorListener;
		
	}
	
	/**�õ�String����ɹ��Ľ��*/
	public Listener<String> loadingListener(){
		
		listener=new Listener<String>() {

			@Override
			public void onResponse(String response) {
				
				/**����ɹ�*/
				onsResponse(response);
				
			}
		};
	
		return listener;
		
	}
	
	/**�õ�JSONObject����ɹ��Ľ��*/
	public Listener<JSONObject> JsonObjectlistener(){
		
		mJsonListener=new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				
				/**����ɹ�*/
				onjResponse(response);
				
			}
		};
		
		return mJsonListener;
		
	}
	
	/**�õ�����ʧ�ܵĽ��*/
	public ErrorListener errorListener(){
		
		errorListener=new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				/**����ʧ��*/
				onsErrorResponse(error);
				
			}
		};
		
		return errorListener; 
		
	}
}
