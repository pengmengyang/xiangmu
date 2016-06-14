package com.example.expressscanningsystem.activity;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.expressscanningsystem.BaseActivity;
import com.example.expressscanningsystem.MainActivity;
import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.config.Config;
import com.example.expressscanningsystem.tools.InterfaceVolleyRequest;
import com.example.expressscanningsystem.tools.RequestErrorInfo;
import com.example.expressscanningsystem.tools.VolleyRequestUtil;

/**
 * �޸�����
 * 
 * @author Administrator
 * 
 */
public class UpdatePasswordActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//����
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private EditText enter_the_original_password;//����ԭʼ����
	private EditText enter_new_password;//����������
	private EditText retype_new_password;//�ٴ�����������
	private Button update_password_submit_btn;//�ύ
	public String tag="UPDATEPASSWORDACTIVITY";
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update_password_activity_layout);
		
		initView();
	}

	private void initView() {
		
		title_back_rl=(RelativeLayout) findViewById(R.id.title_back_rl);
		title_add_rl=(RelativeLayout) findViewById(R.id.title_add_rl);
		title_text_tv=(TextView) findViewById(R.id.title_text_tv);
		
		title_back_rl.setOnClickListener(this);
		title_add_rl.setVisibility(View.GONE);
		title_text_tv.setText("�޸�����");
		
		enter_the_original_password=(EditText) findViewById(R.id.enter_the_original_password);
		enter_new_password=(EditText) findViewById(R.id.enter_new_password);
		retype_new_password=(EditText) findViewById(R.id.retype_new_password);
		update_password_submit_btn=(Button) findViewById(R.id.update_password_submit_btn);
		
		update_password_submit_btn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.title_back_rl://�������
			finish();
			break;
		case R.id.update_password_submit_btn://����ύ
			getData();
			break;

		default:
			break;
		}
		
	}
	private void getData() {
		String originalPassword=enter_the_original_password.getText().toString().trim();
		String newPassword=enter_new_password.getText().toString().trim();
		String retypeNewPassword=retype_new_password.getText().toString().trim();
		progressDialog=ProgressDialog.show(UpdatePasswordActivity.this, "��ܰ��ʾ", "���������޸���,���Ժ�");
		if(null != originalPassword && originalPassword.length() > 0){
			if(null != newPassword && newPassword.length() > 0){
				if(null != retypeNewPassword && retypeNewPassword.length() > 0){
					if(newPassword.equals(retypeNewPassword)){
						if(originalPassword.equals(MainActivity.mima)){
							requestUpdatePassword(newPassword);//�����޸�����
						}else {
							progressDialog.dismiss();
							show("ԭʼ�����������");
						}
					}else {
						progressDialog.dismiss();
						show("�������������벻һ��");
					}
				}else {
					progressDialog.dismiss();
					show("���ٴ�����������");
				}
			}else {
				progressDialog.dismiss();
				show("�����벻��Ϊ��");
			}
		}else {
			progressDialog.dismiss();
			show("ԭʼ���벻��Ϊ��");
		}
	}

	//�����޸�����
	private void requestUpdatePassword(String password) {
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("password", password);
		map.put("Phone", MainActivity.dianhua);
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.UPDATEPASSWORD,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				if(response.equals("�޸ĳɹ�")){
					show("�޸ĳɹ�,�������������µ�¼");
					openActivity(MainActivity.class);
					finish();
					enter_the_original_password.setText("");
					enter_new_password.setText("");
					retype_new_password.setText("");
				}else if (response.equals("�޸�ʧ��")) {
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

	/**
	 * ����հ״�ʹ���������ʧ�ķ���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).cancelRequestQueue(tag);
	}
	
}
