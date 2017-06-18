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
 * Title: ����ҽԺ��Ӫ֧��ƽ̨-APP���˰�
 * </p>
 * <p>
 * Description:��ӭҳ��
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:�й��ƶ����޹�˾��ݸ�ֹ�˾
 * </p>
 * 
 * @author��<a href=��mailto:jomin5120@sina.com��>jomin5120@sina.com</a>
 * @version��1.0
 * @since��2013-9-23
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
	 * ���������ݷ�ʽ
	 */
	
	private void createShortcut() {

		SharedPreferences setting = getSharedPreferences("findeDoctor.shutCut",
				0);
		
//		SharedPreferences	mSharedPreferences = getSharedPreferences("personCenter",
//				Context.MODE_PRIVATE);
//	     Editor mEditor = mSharedPreferences.edit();

		// �ж��Ƿ��һ������Ӧ�ó���Ĭ��Ϊtrue��
		boolean firstStart = setting.getBoolean("FIRST_START", true);

		// ��һ������ʱ���������ݷ�ʽ
		if (firstStart) {
			Intent shortcut = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			// ��ݷ�ʽ������
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "��ҽ��");
			// �������ظ�����
			shortcut.putExtra("duplicate", false);
			// ָ����ݷ�ʽ����������
			ComponentName comp = new ComponentName(this.getPackageName(), "."
					+ this.getLocalClassName());
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
					Intent.ACTION_MAIN).setComponent(comp));
			// ��ݷ�ʽ��ͼ��
			ShortcutIconResource iconRes = Intent.ShortcutIconResource
					.fromContext(this, R.drawable.icon);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
			// �����㲥
			sendBroadcast(shortcut);
			// ����һ�������ı�ʶ����Ϊfalse
			Editor editor = setting.edit();
			editor.putBoolean("FIRST_START", false);
			// �ύ����
			editor.commit();
	
//		mEditor.putBoolean("isFirstStart", true);
		}
		
//		else
//		{
//			mEditor.putBoolean("isFirstStart", false);
//		}
	}
	//ע��㲥
//	private void registerReceiver()
//	{
//		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK); 
//		ServiceBroadcastReceiver receiver = new ServiceBroadcastReceiver(); 
//		getApplicationContext().registerReceiver(receiver, filter); 
//	}

}