package com.example.expressscanningsystem.activity;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.VolleyError;
import com.example.expressscanningsystem.BaseActivity;
import com.example.expressscanningsystem.MainActivity;
import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.adapter.ShowNetSpinnerAdapter;
import com.example.expressscanningsystem.adapter.ShowWaybillNumberListviewAdaper;
import com.example.expressscanningsystem.bean.NetBean;
import com.example.expressscanningsystem.bean.NetBeanResult;
import com.example.expressscanningsystem.bean.ScanBean;
import com.example.expressscanningsystem.config.Config;
import com.example.expressscanningsystem.scan.MipcaActivityCapture;
import com.example.expressscanningsystem.tools.InterfaceVolleyRequest;
import com.example.expressscanningsystem.tools.RequestErrorInfo;
import com.example.expressscanningsystem.tools.VolleyRequestUtil;
import com.google.gson.Gson;

/**
 * 发件扫描
 * 
 * @author Administrator
 * 
 */
public class SendScanActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//返回
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private String tag="SENDSCANACTIVITY";
	private Spinner next_station_spinner;//下一站网点
	private ShowNetSpinnerAdapter showNetSpinnerAdapter;
	private ProgressDialog progressDialog;
	private EditText send_input_waybill_numbers_et;//输入运单号
	private Button send_add_btn;//添加
	private Button send_scan_btn;//扫描
	private ListView send_show_sing_in_waybill_message_lv;
	private TextView send_count_tv;//统计票数
	private String consigneeman;
	private ShowWaybillNumberListviewAdaper showWaybillNumberListviewAdaper;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private Button send_scan_submit_btn;//提交
	private ProgressDialog progressDialog2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.send_scan_activity_layout);
		
		initView();
		requestGetAllNet();//请求得到所有网点
		
	}

	private void initView() {
		
		title_back_rl=(RelativeLayout) findViewById(R.id.title_back_rl);
		title_add_rl=(RelativeLayout) findViewById(R.id.title_add_rl);
		title_text_tv=(TextView) findViewById(R.id.title_text_tv);
		
		send_input_waybill_numbers_et=(EditText) findViewById(R.id.send_input_waybill_numbers_et);
		send_add_btn=(Button) findViewById(R.id.send_add_btn);
		send_scan_btn=(Button) findViewById(R.id.send_scan_btn);
		send_show_sing_in_waybill_message_lv=(ListView) findViewById(R.id.send_show_sing_in_waybill_message_lv);
		send_count_tv=(TextView) findViewById(R.id.send_count_tv);
		showWaybillNumberListviewAdaper=new ShowWaybillNumberListviewAdaper(getApplicationContext());
		send_show_sing_in_waybill_message_lv.setAdapter(showWaybillNumberListviewAdaper);
		monitorListview(send_show_sing_in_waybill_message_lv);
		
		
		send_add_btn.setOnClickListener(this);
		send_scan_btn.setOnClickListener(this);
		
		title_back_rl.setOnClickListener(this);
		title_add_rl.setVisibility(View.GONE);
		title_text_tv.setText("发件扫描");
		
		send_scan_submit_btn=(Button) findViewById(R.id.send_scan_submit_btn);
		
		send_scan_submit_btn.setOnClickListener(this);
		
		
		progressDialog=ProgressDialog.show(SendScanActivity.this, "温馨提示","数据正在加载中,请稍后");
		
	}
	
	/**请求得到所有网点*/
	public void requestGetAllNet(){
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.SELECTALLNETBEAN, new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				Gson gson=new Gson();
				NetBeanResult netBeanResult=gson.fromJson(response,NetBeanResult.class);
				List<NetBean> list=netBeanResult.getList();
				initSpinner(list);
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
		}, null, tag);
		
	}
	
	public void initSpinner(List<NetBean> list){
		
		next_station_spinner=(Spinner) findViewById(R.id.next_station_spinner);
		showNetSpinnerAdapter=new ShowNetSpinnerAdapter(getApplicationContext(), list);
		next_station_spinner.setAdapter(showNetSpinnerAdapter);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.title_back_rl://点击返回
			finish();
			break;
		case R.id.send_add_btn://点击添加
			getData();
			break;
		case R.id.send_scan_btn://点击扫描
			scanInputWaybliiNumber();
			break;
		case R.id.send_scan_submit_btn://点击提交
			progressDialog2=ProgressDialog.show(SendScanActivity.this,"温馨提示","数据正在提交中,请稍后");
			submitDataToSQL();
			break;

		default:
			break;
		}
		
	}
	/**提交数据到数据库*/
	private void submitDataToSQL() {
		
		List<ScanBean> list=showWaybillNumberListviewAdaper.list2;
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				ScanBean scanBean=list.get(i);
				requestSubmitData(scanBean,list);
			}
		}else {
			progressDialog2.dismiss();
			show("没有数据提交");
		}
		
	}
	
	/**请求提交数据*/
	public void requestSubmitData(ScanBean scanBean,final List<ScanBean> list){
		HashMap<String, String> map =new HashMap<String, String>();
		map.put("billNo", scanBean.getBillNo());
		map.put("ScanType", "发件");
		map.put("ScanMan",MainActivity.dianhua);
		map.put("PointNet", scanBean.getScanMan());
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.ADDALLSCAN,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog2.dismiss();
				if(response.equals("扫描成功")){
					showWaybillNumberListviewAdaper.removeAll(list);
					send_count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
				}else if (response.equals("扫描失败")) {
					show("提交失败");
				}else if (response.equals("该运单已经扫描")) {
					show(response);
				}
			}
			
			@Override
			public void onsErrorResponse(VolleyError error) {
				progressDialog2.dismiss();
				show(RequestErrorInfo.getErrorMessage(error));
			}
			
			@Override
			public void onjResponse(JSONObject obj) {
				// TODO Auto-generated method stub
				
			}
		}, map, tag);
	}
	
	public void getData(){
		
		String waybillnumber=send_input_waybill_numbers_et.getText().toString();
		if(null !=waybillnumber && waybillnumber.length() > 0 ){
			NetBean netBean= (NetBean) next_station_spinner.getSelectedItem();
			consigneeman=netBean.getNetName();
			ScanBean scanBean=new ScanBean();
			scanBean.setBillNo(waybillnumber);
			scanBean.setScanMan(consigneeman);
			showWaybillNumberListviewAdaper.addItem(scanBean);
		}else {
			show("请输入运单号扫描运单");
		}
		send_count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		send_input_waybill_numbers_et.setText("");
		
		
	}
	
	/**扫描输入运单号*/
	public void scanInputWaybliiNumber(){
		
		Intent intent = new Intent();
		intent.setClass(SendScanActivity.this,MipcaActivityCapture.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
		
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
	
	public void monitorListview(ListView listView){
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				popupFrame(position);
			}
		});
		
	}
	
	public void popupFrame(final int position){
		
		// 弹出框
		new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否要删除这条数据！")
		    			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		    			public void onClick(DialogInterface dialog, int which) {
		    				showWaybillNumberListviewAdaper.remove(position);
		    				send_count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		    			}})
		    			.setNegativeButton("取消",null)
		    			.show();
		
	}
	
	/**返回扫描结果*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null){
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SCANNIN_GREQUEST_CODE){    //扫描按钮触发的
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			ScanBean scanBean=new ScanBean();
			scanBean.setBillNo(scanResult);
			NetBean  netBean=(NetBean) next_station_spinner.getSelectedItem();
			consigneeman=netBean.getNetName();
			scanBean.setScanMan(consigneeman);
			showWaybillNumberListviewAdaper.addItem(scanBean);
			send_count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).cancelRequestQueue(tag);
	}
	
}
