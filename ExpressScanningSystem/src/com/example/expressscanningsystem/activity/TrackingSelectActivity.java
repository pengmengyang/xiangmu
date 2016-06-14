package com.example.expressscanningsystem.activity;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.expressscanningsystem.BaseActivity;
import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.adapter.TrackingSelectAdapter;
import com.example.expressscanningsystem.bean.TrackTheQueryBean;
import com.example.expressscanningsystem.bean.TrackTheQueryBeanResult;
import com.example.expressscanningsystem.config.Config;
import com.example.expressscanningsystem.tools.InterfaceVolleyRequest;
import com.example.expressscanningsystem.tools.RequestErrorInfo;
import com.example.expressscanningsystem.tools.VolleyRequestUtil;
import com.google.gson.Gson;

/**
 * 跟踪查询
 * 
 * @author Administrator
 * 
 */
public class TrackingSelectActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//返回
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private EditText tracking_select_show_waybill_numbers_et;//运单号
	private Button tracking_select_select_btn;//查询
	private ListView tracking_select_lv;
	private String tag="TRACKINGSELECTACTIVITY";
	private ProgressDialog progressDialog;
	private TrackingSelectAdapter trackingSelectAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tracking_select_activity_layout);
		
		initView();
	}

	private void initView() {
		
		title_back_rl=(RelativeLayout) findViewById(R.id.title_back_rl);
		title_add_rl=(RelativeLayout) findViewById(R.id.title_add_rl);
		title_text_tv=(TextView) findViewById(R.id.title_text_tv);
		
		title_back_rl.setOnClickListener(this);
		title_add_rl.setVisibility(View.GONE);
		title_text_tv.setText("跟踪查询");
		
		tracking_select_show_waybill_numbers_et=(EditText) findViewById(R.id.tracking_select_show_waybill_numbers_et);
		tracking_select_select_btn=(Button) findViewById(R.id.tracking_select_select_btn);
		tracking_select_lv=(ListView) findViewById(R.id.tracking_select_lv);
		
		tracking_select_select_btn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.title_back_rl://点击返回
			finish();
			break;
		case R.id.tracking_select_select_btn://点击查询
			getData();
			break;

		default:
			break;
		}
		
	}

	private void getData() {
		
		String billway=tracking_select_show_waybill_numbers_et.getText().toString().trim();
		progressDialog=ProgressDialog.show(TrackingSelectActivity.this,"温馨提示","正在查询,请稍后");
		if(null != billway && billway.length() > 0){
			requestGetTrackSelectData(billway);
		}else {
			progressDialog.dismiss();
			show("请输入运单号");
		}
		
	}
	
	public void requestGetTrackSelectData(String billway){
		
		HashMap<String,String> map =new HashMap<String, String>();
		map.put("FBillNo", billway);
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.TRACKTHEQUERY,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				Gson gson=new Gson();
				TrackTheQueryBeanResult theQueryBeanResult=gson.fromJson(response,TrackTheQueryBeanResult.class);
				List<TrackTheQueryBean> list=theQueryBeanResult.getList();
				if(null != list && list.size() > 0){
					trackingSelectAdapter=new TrackingSelectAdapter(TrackingSelectActivity.this, list);
					tracking_select_lv.setAdapter(trackingSelectAdapter);
				}else {
					show("对不起,没有该运单");
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
		
	};
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).cancelRequestQueue(tag);
	}
}
