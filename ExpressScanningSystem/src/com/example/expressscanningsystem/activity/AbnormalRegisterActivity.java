package com.example.expressscanningsystem.activity;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.expressscanningsystem.BaseActivity;
import com.example.expressscanningsystem.MainActivity;
import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.config.Config;
import com.example.expressscanningsystem.scan.MipcaActivityCapture;
import com.example.expressscanningsystem.tools.InterfaceVolleyRequest;
import com.example.expressscanningsystem.tools.RequestErrorInfo;
import com.example.expressscanningsystem.tools.VolleyRequestUtil;

/**
 * �쳣�Ǽ�
 * 
 * @author Administrator
 * 
 */
public class AbnormalRegisterActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//����
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private Button abnormal_register_scan_btn;//ɨ��
	private EditText abnormal_register_scan_billway_numbers_et;//ɨ���˵���
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private ProgressDialog progressDialog;
	private Button abnormal_register_submit_btn;//�ύ
	private String tag="AbnormalRegisterActivity";
	private RadioGroup abmormal_register_rg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.abnormal_register_activity_layout);
		
		initView();
	}

	private void initView() {
		
		
		title_back_rl=(RelativeLayout) findViewById(R.id.title_back_rl);
		title_add_rl=(RelativeLayout) findViewById(R.id.title_add_rl);
		title_text_tv=(TextView) findViewById(R.id.title_text_tv);
		abnormal_register_submit_btn=(Button) findViewById(R.id.abnormal_register_submit_btn);
		abmormal_register_rg=(RadioGroup) findViewById(R.id.abmormal_register_rg);
		
		abnormal_register_submit_btn.setOnClickListener(this);
		
		title_back_rl.setOnClickListener(this);
		title_add_rl.setVisibility(View.GONE);
		title_text_tv.setText("�쳣�Ǽ�");
		
		abnormal_register_scan_btn=(Button) findViewById(R.id.abnormal_register_scan_btn);
		abnormal_register_scan_billway_numbers_et=(EditText) findViewById(R.id.abnormal_register_scan_billway_numbers_et);
		
		abnormal_register_scan_btn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.title_back_rl://�������
			finish();
			break;
		case R.id.abnormal_register_scan_btn://���ɨ��
			scanInputWaybliiNumber();
			break;
		case R.id.abnormal_register_submit_btn://����ύ
			progressDialog=ProgressDialog.show(AbnormalRegisterActivity.this,"��ܰ��ʾ","���������ύ��,���Ժ�");
			getData();
			break;

		default:
			break;
		}
		
	}
	
	public void getData(){
		String billway=abnormal_register_scan_billway_numbers_et.getText().toString().trim();
		String checkedData=null;
		if(null != billway && billway.length() > 0){
			 for(int i=0; i<abmormal_register_rg.getChildCount(); i++){  
		            RadioButton r = (RadioButton)abmormal_register_rg.getChildAt(i);  
		            if(r.isChecked()){  
		               checkedData=r.getText().toString();  
		                break;  
		            }  
		        }  
			 submitDataToSQL(billway,checkedData);
		}else {
			progressDialog.dismiss();
			show("���������ɨ���˵���");
		}
	}
	
	/**�ύ���ݵ����ݿ�*/
	private void submitDataToSQL(String billway,String checkedData) {

		HashMap<String, String> map =new HashMap<String, String>();
		map.put("billNo", billway);
		map.put("ScanType", "�����");
		map.put("ScanMan",MainActivity.dianhua);
		map.put("Reason", checkedData);
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.ADDALLSCAN,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				if(response.equals("ɨ��ɹ�")){
					abnormal_register_scan_billway_numbers_et.setText("");
					show("�ύ�ɹ�");
				}else if (response.equals("ɨ��ʧ��")) {
					show("�ύʧ��");
				}else if (response.equals("���˵��Ѿ�ɨ��")) {
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

	/**ɨ�������˵���*/
	public void scanInputWaybliiNumber(){
		
		Intent intent = new Intent();
		intent.setClass(AbnormalRegisterActivity.this,MipcaActivityCapture.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
		
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
	
	/**����ɨ����*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null){
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SCANNIN_GREQUEST_CODE){    //ɨ�谴ť������
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			abnormal_register_scan_billway_numbers_et.setText(scanResult);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).cancelRequestQueue(tag);
	}
	
}
