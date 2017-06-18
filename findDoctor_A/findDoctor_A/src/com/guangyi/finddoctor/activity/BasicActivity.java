package com.guangyi.finddoctor.activity;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.custview.CustomProgressDialog;
import com.guangyi.finddoctor.model.Banner;
import com.umeng.analytics.MobclickAgent;

public class BasicActivity extends Activity {
	private CustomProgressDialog progressDialog;
	private SharedPreferences mfSharedPreferences;
	private Toast toast;
	
	// private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// 启动activity时不自动弹出软键盘
				getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				isNetworkConnected();
				 
	}

	
	 protected  boolean isNetworkConnected(){  
	        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);  
	        NetworkInfo info = cm.getActiveNetworkInfo();  
	        if (info != null && info.isConnected())
	        {}
	        else
	        {
	        	showToast("网络连接失败，请检查网络连接!");
	        }
	        return info != null && info.isConnected();
	    } 
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	protected void getFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
	}

	protected void getNotitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题

	}

	protected void openActivity(Class<?> pClass, int args) {
		Intent intent = new Intent();
		intent.putExtra("ckeckId", args);
		intent.setClass(this, pClass);
		startActivity(intent);
	}

	protected void openActivity(Class<?> pClass) {
		Intent intent = new Intent();
		intent.setClass(this, pClass);
		startActivity(intent);
	}

	protected void openActivityforResult(Class<?> pClass) {
		Intent intent = new Intent();
		intent.setClass(this, pClass);
		startActivityForResult(intent, 0);
	}

	protected void showToast(String pInfo) {
		
		
		
	    if (toast == null) {
	    	toast=Toast.makeText(this, pInfo, Toast.LENGTH_LONG);
//	    	LayoutInflater inflater = getLayoutInflater();
//	    	   View layout = inflater.inflate(R.layout.custom,
//	    	     (ViewGroup) findViewById(R.id.llToast));
//	    	   ImageView image = (ImageView) layout
//	    	     .findViewById(R.id.tvImageToast);
//	    	   image.setImageResource(R.drawable.icon);
//	    	   TextView title = (TextView) layout.findViewById(R.id.tvTitleToast);
//	    	   title.setText("Attention");
//	    	   TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
//	    	   text.setText("完全自定义Toast");
//	    	   toast = new Toast(getApplicationContext());
//	    	   toast.setGravity(Gravity.RIGHT | Gravity.TOP, 12, 40);
//	    	   toast.setDuration(Toast.LENGTH_LONG);
//	    	   toast.setView(layout);
        } else {
            toast.setText(pInfo);
        }
        toast.show();
    }
	
	protected void cancleToast()
	{
		 if (toast !=null)
		 {
			 toast.cancel();
		 }
	}
	

	protected void initProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new CustomProgressDialog(this,R.style.dialog_progress);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(true);
		}
			progressDialog.show();
	}

	protected void canclePregressDialog() {
		if (progressDialog != null) {
			progressDialog.cancel();
		} 
	}

	protected void initGPS() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// 判断GPS模块是否开启，如果没有则开启
		if (!locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			// 转到手机设置界面，用户设置GPS
			// Intent intent=new Intent(Settings.ACTION_SECURITY_SETTINGS);
			// startActivityForResult(intent,0); //设置完成后返回到原来的界面
			new AlertDialog.Builder(this).setMessage("如需获取更精确的位置服务，请您打开GPS设置")
					.setNegativeButton("取消", null)
					.setPositiveButton("设置", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
					}).show();
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
		if(mfSharedPreferences==null)
		{
			mfSharedPreferences=getSharedPreferences("personCenter",
					Context.MODE_PRIVATE);
		}
		return mfSharedPreferences;
		
	}
	
	protected Editor getDataSfEditor()
	{
		return getDataSf().edit();
		
	}
	
	
//	  protected void saveObject(List<Map<String,Object>> list) {  
//	        SharedPreferences mSharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);  
//	        try {  
//	            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//	            ObjectOutputStream oos = new ObjectOutputStream(baos);  
//	            oos.writeObject(list);  
//	  
//	            String personBase64 = new String(Base64.encode(baos.toByteArray(),Base64.DEFAULT));  
//	            SharedPreferences.Editor editor = mSharedPreferences.edit();  
//	            editor.putString("picListObject", personBase64);  
//	            editor.commit();  
//	        } catch (IOException e) {  
//	            e.printStackTrace();  
//	        }  
//	    } 

	@Override
	protected void onStop() {
		canclePregressDialog();
//		cancleToast();
		super.onStop();
	}

}
