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
 * 修改密码
 * 
 * @author Administrator
 * 
 */
public class UpdatePasswordActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//返回
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private EditText enter_the_original_password;//输入原始密码
	private EditText enter_new_password;//输入新密码
	private EditText retype_new_password;//再次输入新密码
	private Button update_password_submit_btn;//提交
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
		title_text_tv.setText("修改密码");
		
		enter_the_original_password=(EditText) findViewById(R.id.enter_the_original_password);
		enter_new_password=(EditText) findViewById(R.id.enter_new_password);
		retype_new_password=(EditText) findViewById(R.id.retype_new_password);
		update_password_submit_btn=(Button) findViewById(R.id.update_password_submit_btn);
		
		update_password_submit_btn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.title_back_rl://点击返回
			finish();
			break;
		case R.id.update_password_submit_btn://点击提交
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
		progressDialog=ProgressDialog.show(UpdatePasswordActivity.this, "温馨提示", "密码正在修改中,请稍后");
		if(null != originalPassword && originalPassword.length() > 0){
			if(null != newPassword && newPassword.length() > 0){
				if(null != retypeNewPassword && retypeNewPassword.length() > 0){
					if(newPassword.equals(retypeNewPassword)){
						if(originalPassword.equals(MainActivity.mima)){
							requestUpdatePassword(newPassword);//请求修改密码
						}else {
							progressDialog.dismiss();
							show("原始密码输入错误");
						}
					}else {
						progressDialog.dismiss();
						show("两次输入新密码不一样");
					}
				}else {
					progressDialog.dismiss();
					show("请再次输入新密码");
				}
			}else {
				progressDialog.dismiss();
				show("新密码不能为空");
			}
		}else {
			progressDialog.dismiss();
			show("原始密码不能为空");
		}
	}

	//请求修改密码
	private void requestUpdatePassword(String password) {
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("password", password);
		map.put("Phone", MainActivity.dianhua);
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.UPDATEPASSWORD,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				if(response.equals("修改成功")){
					show("修改成功,请用新密码重新登录");
					openActivity(MainActivity.class);
					finish();
					enter_the_original_password.setText("");
					enter_new_password.setText("");
					retype_new_password.setText("");
				}else if (response.equals("修改失败")) {
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
	 * 点击空白处使得软键盘消失的方法
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
