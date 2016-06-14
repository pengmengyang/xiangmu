package com.example.expressscanningsystem.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.expressscanningsystem.BaseActivity;
import com.example.expressscanningsystem.MainActivity;
import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.config.Config;
import com.example.expressscanningsystem.scan.MipcaActivityCapture;
import com.example.expressscanningsystem.tools.ImageByrry;
import com.example.expressscanningsystem.tools.InterfaceVolleyRequest;
import com.example.expressscanningsystem.tools.RequestErrorInfo;
import com.example.expressscanningsystem.tools.SDCardUtils;
import com.example.expressscanningsystem.tools.SaveImage;
import com.example.expressscanningsystem.tools.UploadUtil;
import com.example.expressscanningsystem.tools.VolleyRequestUtil;
import com.example.expressscanningsystem.tools.YaSuoImage;

/**
 * ǩ��¼��
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat") public class SignInActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//����
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private Button sign_in_scan_btn;//ɨ��
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private EditText sign_in_input_billway_nuber_et;
	private ImageButton take_photo_ib;//����
	public final static int CAMERA_RESULT = 8888;
	private ImageView show_photo_iv;//��ʾ��Ƭ
	private Button sign_in_submit_btn;//�ύ
	private ProgressDialog progressDialog;
	private EditText sign_in_input_name_et;//��������
	private String tag="SignInActivity";
//	private String mPhotoPath;
//	private File mPhotoFile;
//	private static final int SCALE = 5;//��Ƭ��С����
	private String imageName;
	private Bitmap newBitmap;
	private String ur;
	private boolean flag=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sign_in_activity_layout);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		
		initView();
	}

	private void initView() {
		
		title_back_rl=(RelativeLayout) findViewById(R.id.title_back_rl);
		title_add_rl=(RelativeLayout) findViewById(R.id.title_add_rl);
		title_text_tv=(TextView) findViewById(R.id.title_text_tv);
		sign_in_submit_btn=(Button) findViewById(R.id.sign_in_submit_btn);
		sign_in_input_name_et=(EditText) findViewById(R.id.sign_in_input_name_et);
		
		sign_in_submit_btn.setOnClickListener(this);
		
		title_back_rl.setOnClickListener(this);
		title_add_rl.setVisibility(View.GONE);
		title_text_tv.setText("ǩ��¼��");
		
		sign_in_scan_btn=(Button) findViewById(R.id.sign_in_scan_btn);
		sign_in_input_billway_nuber_et=(EditText) findViewById(R.id.sign_in_input_billway_nuber_et);
		take_photo_ib=(ImageButton) findViewById(R.id.take_photo_ib);
		show_photo_iv=(ImageView) findViewById(R.id.show_photo_iv);
		
		sign_in_scan_btn.setOnClickListener(this);
		take_photo_ib.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.title_back_rl://�������
			finish();
			break;
		case R.id.sign_in_scan_btn://���ɨ��
			scanInputWaybliiNumber();
			break;
		case R.id.take_photo_ib://�������
			imageName=getPhotoFileName();
			getImageFromCamera();
			break;
		case R.id.sign_in_submit_btn://����ύ
			progressDialog=ProgressDialog.show(SignInActivity.this,"��ܰ��ʾ","���������ύ��,���Ժ�");
			getData();
			break;

		default:
			break;
		}
		
	}
	public void getData(){
		String billway=sign_in_input_billway_nuber_et.getText().toString().trim();
		String name=sign_in_input_name_et.getText().toString().trim();
		if(null != billway && billway.length() > 0){
			if(null != name && name.length() > 0){
				submitDataToSQL(billway,name);
			}else {
				progressDialog.dismiss();
				show("������ǩ��������");
			}
		}else {
			progressDialog.dismiss();
			show("���������ɨ���˵���");
		}
	}
	
	/**�ύ���ݵ����ݿ�*/
	private void submitDataToSQL(final String billway,String name) {
		ur=Config.Net+"/ImageServlet";
		
		HashMap<String, String> map =new HashMap<String, String>();
		map.put("billNo", billway);
		map.put("ScanType", "ǩ��");
		map.put("ScanMan",MainActivity.dianhua);
		map.put("SignInMan", name);
		if(newBitmap!=null){
			/**�ύ�����ݿ�֮ǰ�ϴ�ͼƬ*/
			map.put("Photo", "ftp://ftpuser:ftp%40)!%5Euser2016@121.201.61.40/"+billway+".jpg");
			flag=true;
		}
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.ADDALLSCAN,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				if(response.equals("ɨ��ɹ�")){
					sign_in_input_billway_nuber_et.setText("");
					sign_in_input_name_et.setText("");
					show_photo_iv.setImageResource(0);
//					show("�ύ�ɹ�");
					if(flag){
						UploadUtil uploadUtil=new UploadUtil(SignInActivity.this);
						uploadUtil.uploadFile(ur, SDCardUtils.getSDCardPath() + "file/" + imageName, billway+".jpg");
					}
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
		intent.setClass(SignInActivity.this,MipcaActivityCapture.class);
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
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SCANNIN_GREQUEST_CODE && null != data){    //ɨ�谴ť������
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			sign_in_input_billway_nuber_et.setText(scanResult);
		}else if (requestCode == CAMERA_RESULT ) {
			if(resultCode== Activity.RESULT_OK){
//				Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
				 //�������ڱ��ص�ͼƬȡ������С����ʾ�ڽ�����  
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");  
//                newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);  
                newBitmap =YaSuoImage.comp(bitmap);
               SaveImage.saveImage(ImageByrry.Bitmap2Bytes(newBitmap), imageName);
                //����Bitmap�ڴ�ռ�ýϴ�������Ҫ�����ڴ棬����ᱨout of memory�쳣  
                bitmap.recycle();  
				show_photo_iv.setImageBitmap(newBitmap);
			}
		}
	}
	/** ���������ȡ��Ƭ */
	protected void getImageFromCamera() {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			mPhotoPath = "mnt/sdcard/DCIM/Camera/" + getPhotoFileName();
//			mPhotoFile = new File(mPhotoPath);
//			if (!mPhotoFile.exists()) {
//				mPhotoFile.createNewFile();
//			}
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
			Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));  
			//ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻  
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  
			startActivityForResult(intent, CAMERA_RESULT);
		} catch (Exception e) {
		}
	}
	/**
	 * ��ʱ���������Ƭ����
	 * 
	 * @return
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
}
