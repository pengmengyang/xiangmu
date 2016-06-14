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
 * 签收录入
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat") public class SignInActivity extends BaseActivity implements OnClickListener{
	
	private RelativeLayout title_back_rl;//返回
	private  RelativeLayout title_add_rl;
	private  TextView title_text_tv;
	private Button sign_in_scan_btn;//扫描
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private EditText sign_in_input_billway_nuber_et;
	private ImageButton take_photo_ib;//拍照
	public final static int CAMERA_RESULT = 8888;
	private ImageView show_photo_iv;//显示照片
	private Button sign_in_submit_btn;//提交
	private ProgressDialog progressDialog;
	private EditText sign_in_input_name_et;//输入姓名
	private String tag="SignInActivity";
//	private String mPhotoPath;
//	private File mPhotoFile;
//	private static final int SCALE = 5;//照片缩小比例
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
		title_text_tv.setText("签收录入");
		
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
		case R.id.title_back_rl://点击返回
			finish();
			break;
		case R.id.sign_in_scan_btn://点击扫描
			scanInputWaybliiNumber();
			break;
		case R.id.take_photo_ib://点击拍照
			imageName=getPhotoFileName();
			getImageFromCamera();
			break;
		case R.id.sign_in_submit_btn://点击提交
			progressDialog=ProgressDialog.show(SignInActivity.this,"温馨提示","数据正在提交中,请稍后");
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
				show("请输入签收人姓名");
			}
		}else {
			progressDialog.dismiss();
			show("你先输入或扫描运单号");
		}
	}
	
	/**提交数据到数据库*/
	private void submitDataToSQL(final String billway,String name) {
		ur=Config.Net+"/ImageServlet";
		
		HashMap<String, String> map =new HashMap<String, String>();
		map.put("billNo", billway);
		map.put("ScanType", "签收");
		map.put("ScanMan",MainActivity.dianhua);
		map.put("SignInMan", name);
		if(newBitmap!=null){
			/**提交到数据库之前上传图片*/
			map.put("Photo", "ftp://ftpuser:ftp%40)!%5Euser2016@121.201.61.40/"+billway+".jpg");
			flag=true;
		}
		
		VolleyRequestUtil.getNewInstanceVolleyRequestUtil(getApplicationContext()).PostRequestString(Config.ADDALLSCAN,new InterfaceVolleyRequest() {
			
			@Override
			public void onsResponse(String response) {
				progressDialog.dismiss();
				if(response.equals("扫描成功")){
					sign_in_input_billway_nuber_et.setText("");
					sign_in_input_name_et.setText("");
					show_photo_iv.setImageResource(0);
//					show("提交成功");
					if(flag){
						UploadUtil uploadUtil=new UploadUtil(SignInActivity.this);
						uploadUtil.uploadFile(ur, SDCardUtils.getSDCardPath() + "file/" + imageName, billway+".jpg");
					}
				}else if (response.equals("扫描失败")) {
					show("提交失败");
				}else if (response.equals("该运单已经扫描")) {
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
	/**扫描输入运单号*/
	public void scanInputWaybliiNumber(){
		
		Intent intent = new Intent();
		intent.setClass(SignInActivity.this,MipcaActivityCapture.class);
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
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SCANNIN_GREQUEST_CODE && null != data){    //扫描按钮触发的
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			sign_in_input_billway_nuber_et.setText(scanResult);
		}else if (requestCode == CAMERA_RESULT ) {
			if(resultCode== Activity.RESULT_OK){
//				Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
				 //将保存在本地的图片取出并缩小后显示在界面上  
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");  
//                newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);  
                newBitmap =YaSuoImage.comp(bitmap);
               SaveImage.saveImage(ImageByrry.Bitmap2Bytes(newBitmap), imageName);
                //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常  
                bitmap.recycle();  
				show_photo_iv.setImageBitmap(newBitmap);
			}
		}
	}
	/** 从照相机获取照片 */
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
			//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换  
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  
			startActivityForResult(intent, CAMERA_RESULT);
		} catch (Exception e) {
		}
	}
	/**
	 * 用时间戳生成照片名称
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
