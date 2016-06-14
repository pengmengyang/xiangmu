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
 * ��¼�ɹ�������
 * 
 * @author Administrator
 * 
 */
public class LoginSucceesActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout title_back_rl;// ����
	private RelativeLayout title_add_rl;// �޸�����
	private LinearLayout consignee_scan_ll;// �ռ�ɨ��
	private LinearLayout send_scan_ll;// ����ɨ��
	private LinearLayout arrive_scan_ll;// ����ɨ��
	private LinearLayout pai_scan_ll;// �ɼ�ɨ��
	private LinearLayout deal_consignee_bill_ll;// ���ռ���
	private LinearLayout deal_pai_bill_ll;// ���ɼ���
	private LinearLayout sign_in_ll;// ǩ��¼��
	private LinearLayout abnormal_register_ll;// �쳣�Ǽ�
	private LinearLayout tracking_select_ll;// ���ٲ�ѯ

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
		case R.id.title_back_rl:// �������
			finish();
			break;
		case R.id.title_add_rl:// ����޸�����
			openActivity(UpdatePasswordActivity.class);
			break;
		case R.id.consignee_scan_ll:// ����ռ�ɨ��
			openActivity(ConsigneeScanActivity.class);
			break;
		case R.id.send_scan_ll:// �������ɨ��
			openActivity(SendScanActivity.class);
			break;
		case R.id.arrive_scan_ll:// �������ɨ��
			openActivity(ArriveScanActivity.class);
			break;
		case R.id.pai_scan_ll:// ����ɼ�ɨ��
			openActivity(PaiScanActivity.class);
			break;
		case R.id.deal_consignee_bill_ll:// ������ռ���
			openActivity(Deal_consignee_bill_Activity.class);
			break;
		case R.id.deal_pai_bill_ll:// ������ɼ���
			openActivity(Deal_pai_bill_Activity.class);
			break;
		case R.id.sign_in_ll:// ���ǩ��¼��
			openActivity(SignInActivity.class);
			break;
		case R.id.abnormal_register_ll:// ����쳣�Ǽ�
			openActivity(AbnormalRegisterActivity.class);
			break;
		case R.id.tracking_select_ll:// ������ٲ�ѯ
			openActivity(TrackingSelectActivity.class);
			break;

		default:
			break;
		}
	}

}
