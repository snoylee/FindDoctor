package com.guangyi.forDoctor.activity;

import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.personCenter.UserLoginActivity;

import android.os.Bundle;
import android.os.Handler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences.Editor;
public class WelcomeActivity extends BasicActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		createShortcut();
		setContentView(R.layout.activity_welcome);
		SysApplication.getInstance().addActivity(this);
		Handler handler=new Handler();
		handler.postDelayed(new MyRunnable(), 2000);
	}
	
	class MyRunnable implements Runnable
	{

		@Override
		public void run() {
			 
			 boolean isLogin=getSharedPreferences("doctor", MODE_PRIVATE).getBoolean("isLogin", false);
			 if(isLogin)
			 {
				 
				 startActivity(new Intent(getApplication(),MainActivity.class));
			 }
			 else
			 {
			 startActivity(new Intent(getApplication(),UserLoginActivity.class));
			 }
//			 AlarmManagerClass.getInstance().startPollingService(getApplicationContext(), AlarmManagerClass.repeatTime, ReceiveMsgService.class, ReceiveMsgService.ACTION);
//			   startService(new Intent(getApplicationContext(),DefendService.class));
//			   WelcomeActivity.this.finish();
			 
			
		}
		
	}
	
	/**
	 * 创建桌面快捷方式
	 */
	private void createShortcut() {

		SharedPreferences setting = getSharedPreferences("findeDoctor.shutCut",
				0);

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
		}
	}

}
