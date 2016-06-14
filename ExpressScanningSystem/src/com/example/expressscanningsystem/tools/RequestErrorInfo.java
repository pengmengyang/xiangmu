package com.example.expressscanningsystem.tools;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
/**
 * �������
 */
public class RequestErrorInfo {
	public static String getErrorMessage(VolleyError error) {
		String errorMessage = "";
		if (error instanceof AuthFailureError) { // ��֤ʧ��
			errorMessage = "��֤ʧ��";
		} 
		if (error instanceof NetworkError) { // �������
			errorMessage = "�������";
		}
		if (error instanceof TimeoutError) { // �������ӳ�ʱ
			errorMessage = "�������ӳ�ʱ";
		}
		if (error instanceof NoConnectionError) { // �޷����ӷ�����
			errorMessage = "�޷����ӷ�����";
		}
		if (error instanceof ServerError) { // ��������Ӧ����
			errorMessage = "��������Ӧ����";
		}
		if (error instanceof ParseError) { // �޷������������ķ���ֵ
			errorMessage = "�޷������������ķ���ֵ";
		}
		if (error instanceof VolleyError) { // �ӿڷ��صĴ���
//			errorMessage = "��������æ,�Ժ�����";
			errorMessage = "���緱æ,�Ժ�����";
		}
		return errorMessage;
	}
	
}
