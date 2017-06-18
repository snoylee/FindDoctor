package com.guangyi.forDoctor.activity;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.guangyi.forDoctor.custview.CustomProgressDialog;
import com.guangyi.forDoctor.model.Banner;

public class BasicActivity extends Activity {
	private CustomProgressDialog progressDialog;
	private SharedPreferences sharePreferences;
//private ProgressBar progressBar;


@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
}

protected void getFullScreen() {
	requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
}

protected void getNotitle() {
	requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题
	
}


protected void openActivity(Class<?> pClass,int args) {
	Intent intent=new Intent();
	intent.putExtra("flag", args);
	intent.setClass(this, pClass);
	startActivity(intent);
}


protected void openActivity(Class<?> pClass) {
	Intent intent=new Intent();
	intent.setClass(this, pClass);
	startActivity(intent);
}

protected void openActivityforResult(Class<?> pClass) {
	Intent intent=new Intent();
	intent.setClass(this, pClass);
	startActivityForResult(intent, 0);
}

protected void showToast(String pInfo)
{
	Toast.makeText(this, pInfo, Toast.LENGTH_SHORT).show();
}

protected SharedPreferences getCustomSharedPreference()
{
	if(sharePreferences==null)
	{
	sharePreferences=getSharedPreferences("doctor", MODE_PRIVATE);
	return sharePreferences;
	}
	else
	{
		return sharePreferences;
	}
}

protected void initProgressDialog() {
	if (progressDialog == null) {
		progressDialog = new CustomProgressDialog(this, R.style.dialog_progress);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	} else {
		progressDialog.show();

	}
}

protected void canclePregressDialog() {
	if (progressDialog != null) {
		progressDialog.cancel();
	}
}

protected List<Banner>  getObjectInfo() {
	List<Banner>   list = new ArrayList<Banner>();
	try {
		SharedPreferences mSharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);
		String personBase64 = mSharedPreferences.getString("picListObject", "");
		if(personBase64.equals(""))
		{
			Banner banner=new Banner();
			for (int i = 0; i < 2; i++) {
				banner=new Banner();
				banner.setId("-1");
				banner.setPicture("");
				banner.setTitle("");
				list.add(banner);
			}
		}
		else
		{
			byte[] base64Bytes = Base64.decode(personBase64.getBytes(),Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			list = (List<Banner>) ois.readObject();
		}
		
		
		return list;
	} catch (Exception e) {
		
	}
	finally
	{
		return list;
	}
	
}


protected SharedPreferences getDataSf()
{
	if(sharePreferences==null)
	{
		sharePreferences=getSharedPreferences("doctor", MODE_PRIVATE);
	}
	return sharePreferences;
	
}

protected Editor getDataSfEditor()
{
	return getDataSf().edit();
	
}


@Override
protected void onStop() {
	canclePregressDialog();
	super.onStop();
}


}


