package com.example.expressscanningsystem;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.example.expressscanningsystem.activity.LoginSucceesActivity;
import com.example.expressscanningsystem.config.Config;
import com.example.expressscanningsystem.tools.InterfaceVolleyRequest;
import com.example.expressscanningsystem.tools.RequestErrorInfo;
import com.example.expressscanningsystem.tools.RequestUpdateVersion;
import com.example.expressscanningsystem.tools.VolleyRequestUtil;

public class MainActivity extends BaseActivity implements OnClickListener{
	
	private Button login_btn;
	private EditText input_phone_et;//����绰
	private EditText input_password_et;//��������
	private String tag="MAINACTIVITY";
	private String Phone;
	private String password;
	private ProgressDialog progressDialog;
	private CheckBox member_password_cb;//��ס����
	private CheckBox auto_login_cb;//�Զ���¼
	private SharedPreferences sharedPreferences;
	public static String dianhua;
	public static String mima;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		requestUpdateVersion();//������°汾
		initView();
		initMemberAuto();
	}
	public void requestUpdateVersion(){
		
		String url=Config.Net+"/UpdateVersion";
		RequestUpdateVersion.requestUpdateVersion(MainActivity.this, url, tag, null);
		
	}


	private void initView() {
		
		login_btn=(Button) findViewById(R.id.login_btn);
		input_phone_et=(EditText) findViewById(R.id.input_phone_et);
		input_password_et=(EditText) findViewById(R.id.input_password_et);
		member_password_cb=(CheckBox) findViewById(R.id.member_password_cb);
		auto_login_cb=(CheckBox) findViewById(R.id.auto_login_cb);
		
		
		login_btn.setOnClickListener(this);
	}
	

	private void initMemberAuto() {
		sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE); 
		  //�жϼ�ס�����ѡ���״̬  
	      if(sharedPreferences.getBoolean("ISCHECK", false))  
	        {  
	          //����Ĭ���Ǽ�¼����״̬  
	    	  member_password_cb.setChecked(true);  
	    	  input_phone_et.setText(sharedPreferences.getString("USER_NAME", ""));  
	    	  input_password_et.setText(sharedPreferences.getString("PASSWORD", ""));  
	          //�ж��Զ���½��ѡ��״̬  
	          if(sharedPreferences.getBoolean("AUTO_ISCHECK", false))  
	          {  
	                 //����Ĭ�����Զ���¼״̬  
	        	  auto_login_cb.setChecked(true);  
	                //��ת����  
//	                  myToast("�Զ���¼�ɹ�");
	        	  openActivity(LoginSucceesActivity.class);
	        	  dianhua=Phone;
	        	  mima=password;
	          }  
	        }  
	      
	      //������ס�����ѡ��ť�¼�  
	      member_password_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	                if (member_password_cb.isChecked()) {  
	                      show("��ס������ѡ��");
	                    sharedPreferences.edit().putBoolean("ISCHECK", true).commit();  
	                      
	                }else {  
	                	 show("��ס����û��ѡ��");
	                    sharedPreferences.edit().putBoolean("ISCHECK", false).commit();  
	                }  
	  
	            }  
	        });  
	          
	        //�����Զ���¼��ѡ���¼�  
	      auto_login_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	                if (auto_login_cb.isChecked()) {  
	                	show("�Զ���¼��ѡ��");
	                	sharedPreferences.edit().putBoolean("AUTO_ISCHECK", true).commit();  
	  
	                } else {  
	                	show("�Զ���¼û��ѡ��");
	                	sharedPreferences.edit().putBoolean("AUTO_ISCHECK", false).commit();  
	                }  
	            }  
	        });  
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.login_btn://�����¼
			getData();
			break;

		default:
			break;
		}
		
	}
	public void getData(){
		Phone=input_phone_et.getText().toString().trim();
		password=input_password_et.getText().toString().trim();
		if(null != Phone &&Phone.length() > 0){
			if(null != password && password.length() > 0){
				progressDialog=ProgressDialog.show(MainActivity.this,"��ܰ��ʾ","���ڵ�½��,���Ժ�");
				requestLoginUserIsExist();//�ж��û��Ƿ����
			}else {
				show("���벻��Ϊ��");
			}
		}else {
			show("�绰����Ϊ��");
		}
	}
	//�ж��û��Ƿ����
	public void requestLoginUserIsExist(){
		
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("Phone", Phone);
		map.put("password", password);
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.LOGINUSERISEXIST, new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				if(response.equals("��¼�ɹ�")){
					 if(member_password_cb.isChecked())  
	                    {  
	                     //��ס�û��������롢  
	                      Editor editor = sharedPreferences.edit();  
	                      editor.putString("USER_NAME", Phone);  
	                      editor.putString("PASSWORD",password);  
	                      editor.commit();  
	                    }  
					openActivity(LoginSucceesActivity.class);
					 dianhua=Phone;
		        	  mima=password;
				}else if (response.equals("�����û������������")) {
					show(response);
				}
			}
			
			@Override
			public void onsErrorResponse(VolleyError error) {
				progressDialog.dismiss();
				show(RequestErrorInfo.getErrorMessage(error));
			}
			
			@Override
			public void onjResponse(JSONObject obj) {
				// TODO Auto-generated method stub
				
			}
		}, map, tag);
		
	}
	
	/** �����˳�������ʾ�� */
	private void showTips() {

		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("��ܰ��ʾ").setMessage("�Ƿ��˳�����")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}

				}).setNegativeButton("ȡ��",

				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create(); // �����Ի���
		alertDialog.show(); // ��ʾ�Ի���
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showTips();
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).cancelRequestQueue(tag);
	}
	
}
