package com.example.expressscanningsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends  Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	/**不带参数跳转界面*/
	public void openActivity(Class<?> cls){
		Intent intent=new Intent(getApplicationContext(), cls);
		startActivity(intent);
	}
	
	/**提示*/
	public void show(String message){
		
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		
	}
	
}
