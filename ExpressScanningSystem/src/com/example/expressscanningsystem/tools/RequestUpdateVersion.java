package com.example.expressscanningsystem.tools;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.example.expressscanningsystem.bean.VerSionBean;
import com.example.expressscanningsystem.bean.VersionResultBean;
import com.google.gson.Gson;

public class RequestUpdateVersion {
	
	private static String currentVersionName;
	private static VerSionBean verSionBean;

	/**请求更新版本*/
	public static void requestUpdateVersion(final Context context,String url,String tag,HashMap<String, String> map){
		/**获取当前版本*/
		currentVersionName=VersionUtil.getVersionUtil(context).getVersionName();
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(context).PostRequestString(url, new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				Gson gson=new Gson();
				VersionResultBean versionInfoResult=gson.fromJson(response, VersionResultBean.class);
				verSionBean=versionInfoResult.getVerSionBean();
				new AutoInstall(context, verSionBean);
				if(!currentVersionName.equals(verSionBean.getVersion())){
					Message msg = new Message();  
	                msg.what = AutoInstall.UPDATA_CLIENT;  
	                AutoInstall.handler.sendMessage(msg);  
				}else {
//					Toast.makeText(getApplicationContext(), "已是最新版本,不需要更新", 1).show();  
				}
			}
			
			@Override
			public void onsErrorResponse(VolleyError error) {
				new AutoInstall(context);
				Message msg = new Message();  
	            msg.what = AutoInstall.GET_UNDATAINFO_ERROR;  
	            AutoInstall.handler.sendMessage(msg);  
			}
			
			@Override
			public void onjResponse(JSONObject obj) {
				// TODO Auto-generated method stub
				
			}
		}, map, tag);
	}
	
}
