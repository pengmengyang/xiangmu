package com.example.expressscanningsystem.tools;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.expressscanningsystem.bean.VerSionBean;

/**
 * 自动卸载，安装，打开APK
 * @author Administrator
 *
 */
@SuppressLint("ShowToast") public class AutoInstall {

    private static Context mContext;  
    private static VerSionBean  versionInfo;
	public static final int UPDATA_CLIENT=0; //对话框通知用户升级程序   
	public static final int GET_UNDATAINFO_ERROR=1; //服务器超时   
	public static final int DOWN_ERROR=2;//下载apk失败
	
	@SuppressWarnings("static-access")
	public AutoInstall(Context context) {
		this.mContext=context;
	}
	
	@SuppressWarnings("static-access")
	public AutoInstall(Context context,VerSionBean  versionInfo) {
		this.mContext=context;
		this.versionInfo=versionInfo;
	}
  
    /** 
     * 外部传进来的url以便定位需要安装的APK 
     *  
     * @param url 
     */  
  
    /** 
     * 安装 
     *  
     * @param context 
     *            接收外部传进来的context 
     */  
	
    public static  void install(File file) {  
        // 核心是下面几句代码  
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
        mContext.startActivity(intent);  
    } 
    
    /**卸载*/
    public static void uninstall(String packageName){
    	Uri packageURI = Uri.parse(packageName);     
    	Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);     
    	mContext.startActivity(uninstallIntent); 
    }
	
    /**打开APK*/
    public static  void openAPK(File file){
          Intent intent = new Intent();  
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
          intent.setAction(android.content.Intent.ACTION_VIEW);  
          intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");  
          mContext.startActivity(intent);  
    }
    
	public static Handler handler = new Handler(){  
	      
	    @Override  
	    public void handleMessage(Message msg) {  
	        // TODO Auto-generated method stub  
	        super.handleMessage(msg);  
	        switch (msg.what) {  
	        case UPDATA_CLIENT:  
	            //对话框通知用户升级程序   
	        	showUpdataDialog();  
	            break;  
	        case GET_UNDATAINFO_ERROR:  
	            //服务器超时   
	            Toast.makeText(mContext, "获取服务器更新信息失败", 1).show();  
	            break;    
	        case DOWN_ERROR:  
	            //下载apk失败  
	            Toast.makeText(mContext, "下载新版本失败", 1).show();  
	            break;    
	        }  
	    }  
	}; 
	
	/* 
	 *  
	 * 弹出对话框通知用户更新程序  
	 *  
	 * 弹出对话框的步骤： 
	 *  1.创建alertDialog的builder.   
	 *  2.要给builder设置属性, 对话框的内容,样式,按钮 
	 *  3.通过builder 创建一个对话框 
	 *  4.对话框show()出来   
	 */  
	protected static void showUpdataDialog() {  
	    AlertDialog.Builder builer = new Builder(mContext) ;   
	    builer.setTitle("版本升级");  
	    builer.setMessage(versionInfo.getDescription());  
	    //当点确定按钮时从服务器上下载 新的apk 然后安装   
	    builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	    public void onClick(DialogInterface dialog, int which) {  
	            downLoadApk();  
	        }     
	    });  
	    //当点取消按钮时进行登录  
	    builer.setNegativeButton("取消",new DialogInterface.OnClickListener() {  
	        public void onClick(DialogInterface dialog, int which) {  
	            // TODO Auto-generated method stub  
	        }  
	    });  
	    AlertDialog dialog = builer.create();  
	    dialog.show();  
	}  
	
	/* 
	 * 从服务器中下载APK 
	 */  
	protected static void downLoadApk() {  
	    final ProgressDialog pd;    //进度条对话框  
	    pd = new  ProgressDialog(mContext);  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("正在下载更新");  
	    pd.show();  
	    new Thread(){  
	        @Override  
	        public void run() {  
	            try {  
	                File file=DownLoadManager.getFileFromServer(versionInfo.getUrl(), pd);  
	                AutoInstall.uninstall("package:com.example.updateversion");
                    AutoInstall.install(file);  
                    AutoInstall.openAPK(file);
	                pd.dismiss(); //结束掉进度条对话框  
	            } catch (Exception e) {  
	                Message msg = new Message();  
	                msg.what = DOWN_ERROR;  
	                handler.sendMessage(msg);  
	                e.printStackTrace();  
	            }  
	        }}.start();  
	}  
}
