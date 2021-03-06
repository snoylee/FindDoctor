package com.guangyi.finddoctor.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.Utils;
/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:欢迎页面
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:中国移动有限公司东莞分公司
 * </p>
 * 
 * @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
 * @version：1.0
 * @since：2013-9-23
 */
public class WelcomeActivity extends BasicActivity {
	boolean  isCanopen=false ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 SharedPreferences   mfSharedPreferences=getSharedPreferences("personCenter",
					Context.MODE_PRIVATE);
		 Handler handler = new Handler();
//		int holdVersion =mfSharedPreferences.getInt("holdVersion", -1);
//		if(holdVersion==1)
//		{
//			Editor editor=mfSharedPreferences.edit();
//			editor.putInt("holdVersion", 0);
//			editor.commit();
		    ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);  
	        am.killBackgroundProcesses("package:com.guangyi.finddoctor.main");
//	        finish();
//	        isCanopen=true;
//		}
//		else
//		{
		handler.postDelayed(new MyRunnable(), 2000);
		setContentView(R.layout.activity_welcome);
//		}
		createShortcut();
	}
	@Override
	protected void onDestroy() {
		if(isCanopen)
		{
		openActivity(WelcomeActivity.class);
		}
		super.onDestroy();
	}
	class MyRunnable implements Runnable {
		@Override
		public void run() {
			
			 ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);  
		        am.killBackgroundProcesses("package:com.guangyi.finddoctor.main");
			finish();
			
//			AlarmManagerClass.getInstance().startPollingService(getApplicationContext(), AlarmManagerClass.repeatTime, ReceiveMsgService.class, ReceiveMsgService.ACTION);
//			   startService(new Intent(getApplicationContext(),DefendService.class));
			startActivity(new Intent(getApplication(), MainActivity.class));
//			   startActivity(new Intent(getApplication(), PushDemoActivity.class));
		}
	}
	/**
	 * 创建桌面快捷方式
	 */
	
	private void createShortcut() {

		SharedPreferences setting = getSharedPreferences("findeDoctor.shutCut",
				0);
		
//		SharedPreferences	mSharedPreferences = getSharedPreferences("personCenter",
//				Context.MODE_PRIVATE);
//	     Editor mEditor = mSharedPreferences.edit();

		// 判断是否第一次启动应用程序（默认为true）
		boolean firstStart = setting.getBoolean("FIRST_START", true);

		// 第一次启动时创建桌面快捷方式
		if (firstStart) {
			Intent shortcut = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			// 快捷方式的名称
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "找医生");
			// 不允许重复创建
			shortcut.putExtra("duplicate", false);
			// 指定快捷方式的启动对象
			ComponentName comp = new ComponentName(this.getPackageName(), "."
					+ this.getLocalClassName());
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
					Intent.ACTION_MAIN).setComponent(comp));
			// 快捷方式的图标
			ShortcutIconResource iconRes = Intent.ShortcutIconResource
					.fromContext(this, R.drawable.icon);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
			// 发出广播
			sendBroadcast(shortcut);
			// 将第一次启动的标识设置为false
			Editor editor = setting.edit();
			editor.putBoolean("FIRST_START", false);
			// 提交设置
			editor.commit();
	
//		mEditor.putBoolean("isFirstStart", true);
		}
		
//		else
//		{
//			mEditor.putBoolean("isFirstStart", false);
//		}
	}
	//注册广播
//	private void registerReceiver()
//	{
//		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK); 
//		ServiceBroadcastReceiver receiver = new ServiceBroadcastReceiver(); 
//		getApplicationContext().registerReceiver(receiver, filter); 
//	}

}
