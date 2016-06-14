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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
 * ����ɨ��
 * 
 * @author Administrator
 * 
 */
public class ArriveScanActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//����
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private String tag="ARRIVESCANACTIVITY";
	private Spinner previous_station_spinner;//��һվ
	private ShowNetSpinnerAdapter showNetSpinnerAdapter;
	private ProgressDialog progressDialog;
	private EditText arrive_billway_scan_et;//�˵�ɨ��
	private Button arrive_add_btn;//���
	private Button arrive_scan_btn;//ɨ��
	private ListView arrive_show_sing_in_waybill_message_lv;
	private TextView arrive_count_tv;//ͳ��
	private String consigneeman;
	private ShowWaybillNumberListviewAdaper showWaybillNumberListviewAdaper;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private ProgressDialog progressDialog2;
	private Button arrive_scan_submit_btn;//�ύ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.arrive_scan_activity_layout);
		
		initView();
		/**����õ���������*/
		requestGetNetData();
	}

	private void initView() {
		
		title_back_rl=(RelativeLayout) findViewById(R.id.title_back_rl);
		title_add_rl=(RelativeLayout) findViewById(R.id.title_add_rl);
		title_text_tv=(TextView) findViewById(R.id.title_text_tv);
		arrive_scan_submit_btn=(Button) findViewById(R.id.arrive_scan_submit_btn);
		
		arrive_scan_submit_btn.setOnClickListener(this);
		
		title_back_rl.setOnClickListener(this);
		title_add_rl.setVisibility(View.GONE);
		title_text_tv.setText("����ɨ��");
		
		
		progressDialog=ProgressDialog.show(ArriveScanActivity.this,"��ܰ��ʾ","�������ڼ�����,���Ժ�");
		
		arrive_billway_scan_et=(EditText) findViewById(R.id.arrive_billway_scan_et);
		arrive_add_btn=(Button) findViewById(R.id.arrive_add_btn);
		arrive_scan_btn=(Button) findViewById(R.id.arrive_scan_btn);
		arrive_show_sing_in_waybill_message_lv=(ListView) findViewById(R.id.arrive_show_sing_in_waybill_message_lv);
		arrive_count_tv=(TextView) findViewById(R.id.arrive_count_tv);
		
		arrive_add_btn.setOnClickListener(this);
		arrive_scan_btn.setOnClickListener(this);
		monitorListview(arrive_show_sing_in_waybill_message_lv);
		showWaybillNumberListviewAdaper=new ShowWaybillNumberListviewAdaper(getApplicationContext());
		arrive_show_sing_in_waybill_message_lv.setAdapter(showWaybillNumberListviewAdaper);
	}
	
	/**����õ���������*/
	public void requestGetNetData(){
		
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
		
		previous_station_spinner=(Spinner) findViewById(R.id.previous_station_spinner);
		showNetSpinnerAdapter=new ShowNetSpinnerAdapter(ArriveScanActivity.this, list);
		previous_station_spinner.setAdapter(showNetSpinnerAdapter);
		
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.title_back_rl://�������
			finish();
			break;
		case R.id.arrive_add_btn://������
			getData();
			break;
		case R.id.arrive_scan_btn://���ɨ��
			scanInputWaybliiNumber();
			break;
		case R.id.arrive_scan_submit_btn://����ύ
			progressDialog2=ProgressDialog.show(ArriveScanActivity.this,"��ܰ��ʾ","���������ύ��,���Ժ�");
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
		map.put("ScanType", "����");
		map.put("ScanMan",MainActivity.dianhua);
		map.put("PointNet", scanBean.getScanMan());
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.ADDALLSCAN,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog2.dismiss();
				if(response.equals("ɨ��ɹ�")){
					showWaybillNumberListviewAdaper.removeAll(list);
					arrive_count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
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
		
		String waybillnumber=arrive_billway_scan_et.getText().toString();
		if(null !=waybillnumber && waybillnumber.length() > 0 ){
			NetBean netBean=(NetBean) previous_station_spinner.getSelectedItem();
			consigneeman=netBean.getNetName();
			ScanBean scanBean=new ScanBean();
			scanBean.setBillNo(waybillnumber);
			scanBean.setScanMan(consigneeman);
			showWaybillNumberListviewAdaper.addItem(scanBean);
		}else {
			show("�������˵���ɨ���˵�");
		}
		arrive_count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		arrive_billway_scan_et.setText("");
		
		
	}
	
	/**ɨ�������˵���*/
	public void scanInputWaybliiNumber(){
		
		Intent intent = new Intent();
		intent.setClass(ArriveScanActivity.this,MipcaActivityCapture.class);
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
			NetBean netBean= (NetBean) previous_station_spinner.getSelectedItem();
			consigneeman=netBean.getNetName();
			scanBean.setScanMan(consigneeman);
			showWaybillNumberListviewAdaper.addItem(scanBean);
			arrive_count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		}
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
		    				arrive_count_tv.setText(showWaybillNumberListviewAdaper.getCount()+"");
		    			}})
		    			.setNegativeButton("ȡ��",null)
		    			.show();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).cancelRequestQueue(tag);
	}
	
}
