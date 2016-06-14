package com.example.expressscanningsystem.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 版本工具类
 * 
 * @author Administrator
 * 
 */
public class VersionUtil {

	private Context context;
	private PackageInfo packageInfo;
	private static VersionUtil versionUtil;
	
	private VersionUtil(Context context) {
		this.context=context;
	}
	
	public static synchronized VersionUtil getVersionUtil(Context context){
		if(versionUtil==null){
			versionUtil=new VersionUtil(context);
		}
		return versionUtil;
	}
	
	/**获取当前程序版本*/
	public String getVersionName(){
		PackageManager packageManager=context.getPackageManager();
		try {
			 packageInfo=packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packageInfo.versionName;
	}
	
}
