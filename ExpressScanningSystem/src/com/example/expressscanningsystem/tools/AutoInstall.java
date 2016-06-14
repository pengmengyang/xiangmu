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
 * �Զ�ж�أ���װ����APK
 * @author Administrator
 *
 */
@SuppressLint("ShowToast") public class AutoInstall {

    private static Context mContext;  
    private static VerSionBean  versionInfo;
	public static final int UPDATA_CLIENT=0; //�Ի���֪ͨ�û���������   
	public static final int GET_UNDATAINFO_ERROR=1; //��������ʱ   
	public static final int DOWN_ERROR=2;//����apkʧ��
	
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
     * �ⲿ��������url�Ա㶨λ��Ҫ��װ��APK 
     *  
     * @param url 
     */  
  
    /** 
     * ��װ 
     *  
     * @param context 
     *            �����ⲿ��������context 
     */  
	
    public static  void install(File file) {  
        // ���������漸�����  
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
        mContext.startActivity(intent);  
    } 
    
    /**ж��*/
    public static void uninstall(String packageName){
    	Uri packageURI = Uri.parse(packageName);     
    	Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);     
    	mContext.startActivity(uninstallIntent); 
    }
	
    /**��APK*/
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
	            //�Ի���֪ͨ�û���������   
	        	showUpdataDialog();  
	            break;  
	        case GET_UNDATAINFO_ERROR:  
	            //��������ʱ   
	            Toast.makeText(mContext, "��ȡ������������Ϣʧ��", 1).show();  
	            break;    
	        case DOWN_ERROR:  
	            //����apkʧ��  
	            Toast.makeText(mContext, "�����°汾ʧ��", 1).show();  
	            break;    
	        }  
	    }  
	}; 
	
	/* 
	 *  
	 * �����Ի���֪ͨ�û����³���  
	 *  
	 * �����Ի���Ĳ��裺 
	 *  1.����alertDialog��builder.   
	 *  2.Ҫ��builder��������, �Ի��������,��ʽ,��ť 
	 *  3.ͨ��builder ����һ���Ի��� 
	 *  4.�Ի���show()����   
	 */  
	protected static void showUpdataDialog() {  
	    AlertDialog.Builder builer = new Builder(mContext) ;   
	    builer.setTitle("�汾����");  
	    builer.setMessage(versionInfo.getDescription());  
	    //����ȷ����ťʱ�ӷ����������� �µ�apk Ȼ��װ   
	    builer.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
	    public void onClick(DialogInterface dialog, int which) {  
	            downLoadApk();  
	        }     
	    });  
	    //����ȡ����ťʱ���е�¼  
	    builer.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {  
	        public void onClick(DialogInterface dialog, int which) {  
	            // TODO Auto-generated method stub  
	        }  
	    });  
	    AlertDialog dialog = builer.create();  
	    dialog.show();  
	}  
	
	/* 
	 * �ӷ�����������APK 
	 */  
	protected static void downLoadApk() {  
	    final ProgressDialog pd;    //�������Ի���  
	    pd = new  ProgressDialog(mContext);  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("�������ظ���");  
	    pd.show();  
	    new Thread(){  
	        @Override  
	        public void run() {  
	            try {  
	                File file=DownLoadManager.getFileFromServer(versionInfo.getUrl(), pd);  
	                AutoInstall.uninstall("package:com.example.updateversion");
                    AutoInstall.install(file);  
                    AutoInstall.openAPK(file);
	                pd.dismiss(); //�������������Ի���  
	            } catch (Exception e) {  
	                Message msg = new Message();  
	                msg.what = DOWN_ERROR;  
	                handler.sendMessage(msg);  
	                e.printStackTrace();  
	            }  
	        }}.start();  
	}  
}
