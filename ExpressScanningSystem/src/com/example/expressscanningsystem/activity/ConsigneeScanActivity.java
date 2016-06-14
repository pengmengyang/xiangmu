package com.example.expressscanningsystem.activity;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.expressscanningsystem.BaseActivity;
import com.example.expressscanningsystem.MainActivity;
import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.adapter.ShowEmployeeSpinnerAdapter;
import com.example.expressscanningsystem.adapter.ShowWaybillNumberListviewAdaper;
import com.example.expressscanningsystem.bean.EmployeeBean;
import com.example.expressscanningsystem.bean.EmployeeBeanResult;
import com.example.expressscanningsystem.bean.ScanBean;
import com.example.expressscanningsystem.config.Config;
import com.example.expressscanningsystem.scan.MipcaActivityCapture;
import com.example.expressscanningsystem.tools.InterfaceVolleyRequest;
import com.example.expressscanningsystem.tools.RequestErrorInfo;
import com.example.expressscanningsystem.tools.VolleyRequestUtil;
import com.google.gson.Gson;

/**
 * 收件扫描
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "HandlerLeak", "InflateParams", "ClickableViewAccessibility" }) public class ConsigneeScanActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//返回
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private String tag="CONSIGNEESCANACTIVITY";
	private Button consignee_man_spinner;
	private ShowEmployeeSpinnerAdapter showEmployeeSpinnerAdapter;
	private ProgressDialog progressDialog;
	private ShowWaybillNumberListviewAdaper showWaybillNumberListviewAdaper;
	private ListView show_sing_in_waybill_message_lv;
	private Button add_waybill_btn;//手动添加运单
	private EditText show_waybill_numbers_et;//运单号输入框
	private TextView count_tv;//计数
	private Button scan_input_btn;//扫描输入
	private String consigneeman;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private Button consignee_scan_submit_btn;//提交
	private ProgressDialog progressDialog2;
	private TextView consignee_scan_show_name;//显示收件员名称
	private ListView pop_window_lv;
	private String BelongNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consignee_scan_activity_layout);
		
		initView();
		/**请求得到默认收件员名称*/
		requestGetTheDefaultEmployee();
		
	}

	private void initView() {
		
		title_back_rl=(RelativeLayout) findViewById(R.id.title_back_rl);
		title_add_rl=(RelativeLayout) findViewById(R.id.title_add_rl);
		title_text_tv=(TextView) findViewById(R.id.title_text_tv);
		add_waybill_btn=(Button) findViewById(R.id.add_waybill_btn);
		show_waybill_numbers_et=(EditText) findViewById(R.id.show_waybill_numbers_et);
		count_tv=(TextView) findViewById(R.id.count_tv);
		scan_input_btn=(Button) findViewById(R.id.scan_input_btn);
		consignee_scan_submit_btn=(Button) findViewById(R.id.consignee_scan_submit_btn);
		consignee_scan_show_name=(TextView) findViewById(R.id.consignee_scan_show_name);
		consignee_man_spinner=(Button) findViewById(R.id.consignee_man_spinner);
		
		
		show_sing_in_waybill_message_lv=(ListView) findViewById(R.id.show_sing_in_waybill_message_lv);
		showWaybillNumberListviewAdaper=new ShowWaybillNumberListviewAdaper(getApplicationContext());
		show_sing_in_waybill_message_lv.setAdapter(showWaybillNumberListviewAdaper);
		monitorListview(show_sing_in_waybill_message_lv);
		
		
		title_back_rl.setOnClickListener(this);
		title_add_rl.setVisibility(View.GONE);
		title_text_tv.setText("收件扫描");
		
		add_waybill_btn.setOnClickListener(this);
		scan_input_btn.setOnClickListener(this);
		consignee_scan_submit_btn.setOnClickListener(this);
		consignee_man_spinner.setOnClickListener(this);
		
		progressDialog=ProgressDialog.show(ConsigneeScanActivity.this, "温馨提示", "数据正在加载中,请稍后");
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.title_back_rl://点击返回
			finish();
			break;
		case R.id.add_waybill_btn://点击添加
			getData();
			break;
		case R.id.scan_input_btn://点击扫描
			scanInputWaybliiNumber();
			break;
		case R.id.consignee_man_spinner://点击查询
			progressDialog=ProgressDialog.show(ConsigneeScanActivity.this, "温馨提示", "数据正在加载中,请稍后");
			/**请求得到所有员工*/
			requestGetAllEmployee(v);
			break;
		case R.id.consignee_scan_submit_btn://点击提交
			progressDialog2=ProgressDialog.show(ConsigneeScanActivity.this,"温馨提示","数据正在提交中,请稍后");
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
		map.put("ScanType", "收件");
		map.put("ScanMan",MainActivity.dianhua);
		map.put("Deliver", scanBean.getScanMan());
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.ADDALLSCAN,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog2.dismiss();
				if(response.equals("扫描成功")){
					showWaybillNumberListviewAdaper.removeAll(list);
					count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
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
		
		String waybillnumber=show_waybill_numbers_et.getText().toString();
		if(null !=waybillnumber && waybillnumber.length() > 0 ){
			consigneeman=consignee_scan_show_name.getText().toString();
			ScanBean scanBean=new ScanBean();
			scanBean.setBillNo(waybillnumber);
			scanBean.setScanMan(consigneeman);
			showWaybillNumberListviewAdaper.addItem(scanBean);
		}else {
			show("请输入运单号扫描运单");
		}
		count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		show_waybill_numbers_et.setText("");
		
		
	}
	
	/**扫描输入运单号*/
	public void scanInputWaybliiNumber(){
		
		Intent intent = new Intent();
		intent.setClass(ConsigneeScanActivity.this,MipcaActivityCapture.class);
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
			consigneeman=consignee_scan_show_name.getText().toString();
			scanBean.setScanMan(consigneeman);
			showWaybillNumberListviewAdaper.addItem(scanBean);
			count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		}
	}
	
	/**请求得到所有员工*/
	public void requestGetAllEmployee(final View view){
		
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("BelongNet", BelongNet);
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.SELECTALLEMPLOYEE, new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				Gson gson=new Gson();
				EmployeeBeanResult employeeBeanResult=gson.fromJson(response, EmployeeBeanResult.class);
				List<EmployeeBean> list=employeeBeanResult.getList();
				showPopupWindow(view,list);
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
		    				count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		    			}})
		    			.setNegativeButton("取消",null)
		    			.show();
		
	}
	
	/**请求得到默认收件员名称*/
	public void requestGetTheDefaultEmployee(){
		
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("Phone",MainActivity.dianhua);
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.SelectNameAndNetByPhone,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				Gson gson=new Gson();
				EmployeeBeanResult employeeBeanResult=gson.fromJson(response, EmployeeBeanResult.class);
				List<EmployeeBean> list=employeeBeanResult.getList();
				for(EmployeeBean employeeBean:list){
					consignee_scan_show_name.setText(employeeBean.getEP_Name());
					BelongNet=employeeBean.getBelongNet();
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
	
	  private void showPopupWindow(View view,List<EmployeeBean> list) {

		  View contentView = LayoutInflater.from(this).inflate(R.layout.pop_window, null);
		  final PopupWindow popupWindow = new PopupWindow(contentView,
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
	        // 一个自定义的布局，作为显示的内容
	       
	        pop_window_lv=(ListView) contentView.findViewById(R.id.pop_window_lv);
	        showEmployeeSpinnerAdapter=new ShowEmployeeSpinnerAdapter(getApplicationContext(), list);
	        pop_window_lv.setAdapter(showEmployeeSpinnerAdapter);
	        
	        pop_window_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					EmployeeBean employeeBean=showEmployeeSpinnerAdapter.getItem(position);
					String selectItem=employeeBean.getEP_Name();
					consignee_scan_show_name.setText(selectItem);
					popupWindow.dismiss();
				}
			});

	      

	        popupWindow.setTouchable(true);

	        popupWindow.setTouchInterceptor(new OnTouchListener() {

	            @Override
	            public boolean onTouch(View v, MotionEvent event) {


	                return false;
	                // 这里如果返回true的话，touch事件将被拦截
	                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
	            }
	        });

	        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
	        // 我觉得这里是API的一个bug
	        popupWindow.setBackgroundDrawable(getResources().getDrawable(
	                R.color.background_change_checked));

	        // 设置好参数之后再show
	        popupWindow.showAsDropDown(view);

	    }
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).cancelRequestQueue(tag);
	}
	
}
