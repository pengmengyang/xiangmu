package com.example.expressscanningsystem.tools;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 
 * @ClassName: SharPerferencesUtili
 * @Description: SharPerferencesUtili 保存数据 单例模式工具类
 * @author pengmengyang
 * @date 2015-7-15 上午9:56:26
 */
public class SharPerferencesUtili {
	// 1.私有化成员变量
	private static SharPerferencesUtili sharPerferencesUtili;// 私有化本类对象
	private SharedPreferences sharedPreferences;// 私有化保存数据
	private SharedPreferences.Editor editor;// 私有化编辑对象

	/**
	 * 2.私有化构造方法 并传参数 Context context 要用参数调用供外部访问的静态方法 以及初始化成员变量
	 * 
	 * @param context
	 */
	@SuppressWarnings("static-access")
	private SharPerferencesUtili(Context context) {
		// 第一个参数 文件名 第二个参数 权限
		sharedPreferences = context.getSharedPreferences("info",
				context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	// 3. 对外部提供一个访问的方法
	public static SharPerferencesUtili getPerferencesUtili(Context context) {
		if (sharPerferencesUtili == null) {
			sharPerferencesUtili = new SharPerferencesUtili(context);
		}
		return sharPerferencesUtili;
	}

	/**
	 * 
	 * @Title: setName
	 * @Description: 修改名字
	 * @param @param name 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setName(String name) {
		editor.putString("username", name);
		editor.commit();
	}

	/**
	 * 
	 * @Title: getName
	 * @Description: 获取名字
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getName() {
		return sharedPreferences.getString("username", "");
	}

	/**
	 * 
	 * @Title: setPassword
	 * @Description: 设置密码
	 * @param @param password 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setPassword(String password) {
		editor.putString("password", password);
		editor.commit();
	}

	/**
	 * 
	 * @Title: getPassword
	 * @Description: 获取密码
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getPassword() {
		return sharedPreferences.getString("password", "");
	}

	/**
	 * 
	 * @Title: counter
	 * @Description: 统计访问次数
	 * @param @param record
	 * @param @return 设定文件
	 * @return int 返回类型
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
	 * @Description:清空
	 * @param 设定文件
	 * @return void 返回类型
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
	 * @Description:存储记住密码的状态
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void save(Boolean isChecked) {
		editor.putBoolean("isChecked", isChecked);
	}

	/**
	 * 
	 * @Title: saveCB
	 * @Description:是否记住了密码
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean savePassword() {
		return sharedPreferences.getBoolean("isChecked", false);
	}
}
