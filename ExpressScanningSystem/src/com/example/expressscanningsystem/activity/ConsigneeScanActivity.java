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
 * �ռ�ɨ��
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "HandlerLeak", "InflateParams", "ClickableViewAccessibility" }) public class ConsigneeScanActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//����
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private String tag="CONSIGNEESCANACTIVITY";
	private Button consignee_man_spinner;
	private ShowEmployeeSpinnerAdapter showEmployeeSpinnerAdapter;
	private ProgressDialog progressDialog;
	private ShowWaybillNumberListviewAdaper showWaybillNumberListviewAdaper;
	private ListView show_sing_in_waybill_message_lv;
	private Button add_waybill_btn;//�ֶ�����˵�
	private EditText show_waybill_numbers_et;//�˵��������
	private TextView count_tv;//����
	private Button scan_input_btn;//ɨ������
	private String consigneeman;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private Button consignee_scan_submit_btn;//�ύ
	private ProgressDialog progressDialog2;
	private TextView consignee_scan_show_name;//��ʾ�ռ�Ա����
	private ListView pop_window_lv;
	private String BelongNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consignee_scan_activity_layout);
		
		initView();
		/**����õ�Ĭ���ռ�Ա����*/
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
		title_text_tv.setText("�ռ�ɨ��");
		
		add_waybill_btn.setOnClickListener(this);
		scan_input_btn.setOnClickListener(this);
		consignee_scan_submit_btn.setOnClickListener(this);
		consignee_man_spinner.setOnClickListener(this);
		
		progressDialog=ProgressDialog.show(ConsigneeScanActivity.this, "��ܰ��ʾ", "�������ڼ�����,���Ժ�");
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.title_back_rl://�������
			finish();
			break;
		case R.id.add_waybill_btn://������
			getData();
			break;
		case R.id.scan_input_btn://���ɨ��
			scanInputWaybliiNumber();
			break;
		case R.id.consignee_man_spinner://�����ѯ
			progressDialog=ProgressDialog.show(ConsigneeScanActivity.this, "��ܰ��ʾ", "�������ڼ�����,���Ժ�");
			/**����õ�����Ա��*/
			requestGetAllEmployee(v);
			break;
		case R.id.consignee_scan_submit_btn://����ύ
			progressDialog2=ProgressDialog.show(ConsigneeScanActivity.this,"��ܰ��ʾ","���������ύ��,���Ժ�");
			submitDataToSQL();
			break;
			

		default:
			break;
		}
		
	}
	
	/**�ύ���ݵ����ݿ�*/
	private void submitDataToSQL() {
		
		List<ScanBean> list=showWaybillNumberListviewAdaper.list2;
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				ScanBean scanBean=list.get(i);
				requestSubmitData(scanBean,list);
			}
		}else {
			progressDialog2.dismiss();
			show("û�������ύ");
		}
		
	}
	
	/**�����ύ����*/
	public void requestSubmitData(ScanBean scanBean,final List<ScanBean> list){
		HashMap<String, String> map =new HashMap<String, String>();
		map.put("billNo", scanBean.getBillNo());
		map.put("ScanType", "�ռ�");
		map.put("ScanMan",MainActivity.dianhua);
		map.put("Deliver", scanBean.getScanMan());
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.ADDALLSCAN,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog2.dismiss();
				if(response.equals("ɨ��ɹ�")){
					showWaybillNumberListviewAdaper.removeAll(list);
					count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
				}else if (response.equals("ɨ��ʧ��")) {
					show("�ύʧ��");
				}else if (response.equals("���˵��Ѿ�ɨ��")) {
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
			show("�������˵���ɨ���˵�");
		}
		count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		show_waybill_numbers_et.setText("");
		
		
	}
	
	/**ɨ�������˵���*/
	public void scanInputWaybliiNumber(){
		
		Intent intent = new Intent();
		intent.setClass(ConsigneeScanActivity.this,MipcaActivityCapture.class);
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
			ScanBean scanBean=new ScanBean();
			scanBean.setBillNo(scanResult);
			consigneeman=consignee_scan_show_name.getText().toString();
			scanBean.setScanMan(consigneeman);
			showWaybillNumberListviewAdaper.addItem(scanBean);
			count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		}
	}
	
	/**����õ�����Ա��*/
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
		
		// ������
		new AlertDialog.Builder(this).setTitle("��ܰ��ʾ").setMessage("�Ƿ�Ҫɾ���������ݣ�")
		    			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		    			public void onClick(DialogInterface dialog, int which) {
		    				showWaybillNumberListviewAdaper.remove(position);
		    				count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		    			}})
		    			.setNegativeButton("ȡ��",null)
		    			.show();
		
	}
	
	/**����õ�Ĭ���ռ�Ա����*/
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
	        // һ���Զ���Ĳ��֣���Ϊ��ʾ������
	       
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
	                // �����������true�Ļ���touch�¼���������
	                // ���غ� PopupWindow��onTouchEvent�������ã���������ⲿ�����޷�dismiss
	            }
	        });

	        // ���������PopupWindow�ı����������ǵ���ⲿ������Back�����޷�dismiss����
	        // �Ҿ���������API��һ��bug
	        popupWindow.setBackgroundDrawable(getResources().getDrawable(
	                R.color.background_change_checked));

	        // ���úò���֮����show
	        popupWindow.showAsDropDown(view);

	    }
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).cancelRequestQueue(tag);
	}
	
}
