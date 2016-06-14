package com.example.expressscanningsystem.tools;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 
 * @ClassName: SharPerferencesUtili
 * @Description: SharPerferencesUtili �������� ����ģʽ������
 * @author pengmengyang
 * @date 2015-7-15 ����9:56:26
 */
public class SharPerferencesUtili {
	// 1.˽�л���Ա����
	private static SharPerferencesUtili sharPerferencesUtili;// ˽�л��������
	private SharedPreferences sharedPreferences;// ˽�л���������
	private SharedPreferences.Editor editor;// ˽�л��༭����

	/**
	 * 2.˽�л����췽�� �������� Context context Ҫ�ò������ù��ⲿ���ʵľ�̬���� �Լ���ʼ����Ա����
	 * 
	 * @param context
	 */
	@SuppressWarnings("static-access")
	private SharPerferencesUtili(Context context) {
		// ��һ������ �ļ��� �ڶ������� Ȩ��
		sharedPreferences = context.getSharedPreferences("info",
				context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	// 3. ���ⲿ�ṩһ�����ʵķ���
	public static SharPerferencesUtili getPerferencesUtili(Context context) {
		if (sharPerferencesUtili == null) {
			sharPerferencesUtili = new SharPerferencesUtili(context);
		}
		return sharPerferencesUtili;
	}

	/**
	 * 
	 * @Title: setName
	 * @Description: �޸�����
	 * @param @param name �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	public void setName(String name) {
		editor.putString("username", name);
		editor.commit();
	}

	/**
	 * 
	 * @Title: getName
	 * @Description: ��ȡ����
	 * @param @return �趨�ļ�
	 * @return String ��������
	 * @throws
	 */
	public String getName() {
		return sharedPreferences.getString("username", "");
	}

	/**
	 * 
	 * @Title: setPassword
	 * @Description: ��������
	 * @param @param password �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	public void setPassword(String password) {
		editor.putString("password", password);
		editor.commit();
	}

	/**
	 * 
	 * @Title: getPassword
	 * @Description: ��ȡ����
	 * @param @return �趨�ļ�
	 * @return String ��������
	 * @throws
	 */
	public String getPassword() {
		return sharedPreferences.getString("password", "");
	}

	/**
	 * 
	 * @Title: counter
	 * @Description: ͳ�Ʒ��ʴ���
	 * @param @param record
	 * @param @return �趨�ļ�
	 * @return int ��������
	 * @throws
	 */
	public int counter(int record) {
		record = sharedPreferences.getInt("record", 0);
		record++;
		editor.putInt("record", record);
		editor.commit();
		return record;
	}

	/**
	 * 
	 * @Title: remove
	 * @Description:���
	 * @param �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	public void remove() {
		editor.remove("username");
		editor.remove("password");
		editor.commit();

	}

	/**
	 * 
	 * @Title: save
	 * @Description:�洢��ס�����״̬
	 * @param �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	public void save(Boolean isChecked) {
		editor.putBoolean("isChecked", isChecked);
	}

	/**
	 * 
	 * @Title: saveCB
	 * @Description:�Ƿ��ס������
	 * @param @return �趨�ļ�
	 * @return boolean ��������
	 * @throws
	 */
	public boolean savePassword() {
		return sharedPreferences.getBoolean("isChecked", false);
	}
}
