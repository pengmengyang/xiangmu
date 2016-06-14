package com.example.expressscanningsystem.activity;

import com.example.expressscanningsystem.BaseActivity;
import com.example.expressscanningsystem.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 登录成功主界面
 * 
 * @author Administrator
 * 
 */
public class LoginSucceesActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout title_back_rl;// 返回
	private RelativeLayout title_add_rl;// 修改密码
	private LinearLayout consignee_scan_ll;// 收件扫描
	private LinearLayout send_scan_ll;// 发件扫描
	private LinearLayout arrive_scan_ll;// 到件扫描
	private LinearLayout pai_scan_ll;// 派件扫描
	private LinearLayout deal_consignee_bill_ll;// 交收件单
	private LinearLayout deal_pai_bill_ll;// 交派件单
	private LinearLayout sign_in_ll;// 签收录入
	private LinearLayout abnormal_register_ll;// 异常登记
	private LinearLayout tracking_select_ll;// 跟踪查询

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_succees_activity_layout);

		initView();
	}

	private void initView() {

		title_back_rl = (RelativeLayout) findViewById(R.id.title_back_rl);
		title_add_rl = (RelativeLayout) findViewById(R.id.title_add_rl);
		consignee_scan_ll = (LinearLayout) findViewById(R.id.consignee_scan_ll);
		send_scan_ll = (LinearLayout) findViewById(R.id.send_scan_ll);
		arrive_scan_ll = (LinearLayout) findViewById(R.id.arrive_scan_ll);
		pai_scan_ll = (LinearLayout) findViewById(R.id.pai_scan_ll);
		deal_consignee_bill_ll = (LinearLayout) findViewById(R.id.deal_consignee_bill_ll);
		deal_pai_bill_ll = (LinearLayout) findViewById(R.id.deal_pai_bill_ll);
		sign_in_ll = (LinearLayout) findViewById(R.id.sign_in_ll);
		abnormal_register_ll = (LinearLayout) findViewById(R.id.abnormal_register_ll);
		tracking_select_ll = (LinearLayout) findViewById(R.id.tracking_select_ll);

		title_back_rl.setOnClickListener(this);
		title_add_rl.setOnClickListener(this);
		consignee_scan_ll.setOnClickListener(this);
		send_scan_ll.setOnClickListener(this);
		arrive_scan_ll.setOnClickListener(this);
		pai_scan_ll.setOnClickListener(this);
		deal_consignee_bill_ll.setOnClickListener(this);
		deal_pai_bill_ll.setOnClickListener(this);
		sign_in_ll.setOnClickListener(this);
		abnormal_register_ll.setOnClickListener(this);
		tracking_select_ll.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_rl:// 点击返回
			finish();
			break;
		case R.id.title_add_rl:// 点击修改密码
			openActivity(UpdatePasswordActivity.class);
			break;
		case R.id.consignee_scan_ll:// 点击收件扫描
			openActivity(ConsigneeScanActivity.class);
			break;
		case R.id.send_scan_ll:// 点击发件扫描
			openActivity(SendScanActivity.class);
			break;
		case R.id.arrive_scan_ll:// 点击到件扫描
			openActivity(ArriveScanActivity.class);
			break;
		case R.id.pai_scan_ll:// 点击派件扫描
			openActivity(PaiScanActivity.class);
			break;
		case R.id.deal_consignee_bill_ll:// 点击交收件单
			openActivity(Deal_consignee_bill_Activity.class);
			break;
		case R.id.deal_pai_bill_ll:// 点击交派件单
			openActivity(Deal_pai_bill_Activity.class);
			break;
		case R.id.sign_in_ll:// 点击签收录入
			openActivity(SignInActivity.class);
			break;
		case R.id.abnormal_register_ll:// 点击异常登记
			openActivity(AbnormalRegisterActivity.class);
			break;
		case R.id.tracking_select_ll:// 点击跟踪查询
			openActivity(TrackingSelectActivity.class);
			break;

		default:
			break;
		}
	}

}
