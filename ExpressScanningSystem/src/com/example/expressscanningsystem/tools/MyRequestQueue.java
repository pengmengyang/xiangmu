package com.example.expressscanningsystem.tools;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * ������е���ģʽ
 * @author Administrator
 *
 */
public class MyRequestQueue {
	
	/**˽�л���Ա����*/
	private static RequestQueue requestQueue;//�������
	@SuppressWarnings("unused")
	private Context context;//������
	
	/**˽�л����췽��*/
	private MyRequestQueue(Context context){
		
		this.context=context;
		
	}
	
	/**�ṩ���÷������ⲿ���ʵõ�������е�ʵ��*/
	public static synchronized RequestQueue getNewInstanceRequestQueue(Context context){
		
		/**�ж���������Ƿ�Ϊ��*/
		if(requestQueue == null){
			
			requestQueue=Volley.newRequestQueue(context);//ʵ�����������
			
		}
		
		return requestQueue;
		
	}
	
}
