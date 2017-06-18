package com.guangyi.forDoctor.activity;

import java.net.SocketException;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.Utils;
import com.guangyi.forDoctor.adapter.FocusAdapter;
import com.guangyi.forDoctor.application.FindDoctorApplication;
import com.guangyi.forDoctor.checkvision.UpdateManager;
import com.guangyi.forDoctor.custview.FocusGallery;
import com.guangyi.forDoctor.hospitalRegister.HospitalRegisterActivity;
import com.guangyi.forDoctor.model.Banner;
import com.guangyi.forDoctor.onlineConsult.OnLineMainActivity;
import com.guangyi.forDoctor.personCenter.PersonCenter;
import com.guangyi.forDoctor.personCenter.UserLoginActivity;
import com.guangyi.forDoctor.phoneAsk.PhoneAskActivity;
import com.guangyi.forDoctor.utils.StateInfos;

public class MainActivity extends BasicActivity {
	private FocusGallery mGallery;
	private TextView mTitleText;
	private FocusAdapter mFocusAdapter;
	private ImageView mFocusPointImage;
	
	private ImageView iv_hospital_register_pic,iv_online_ask_pic,iv_phone_ask,iv_person_center_pic;
	private long exitTime = 0;
	
	private Handler handler;
//	private int i=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SysApplication.getInstance().addActivity(this);
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, 
				Utils.getMetaValue(MainActivity.this, "api_key"));
		initView();
		initListener();
		
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
//		initBanner();
		
		handler = new Handler() {
			boolean right = true;
			public void handleMessage(Message msg) {
				// mGallery.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,
				// R.anim.push_left_in));
				// mGallery.setSelection((msg.arg1+1)%5);

				if (msg.arg1 == 1) {
					right = false;
				}
				if (msg.arg1 == 0) {
					right = true;
				}
				if (right) {
					mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
				} else {
					mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
				}

				handler.sendMessageDelayed(new Message(), 4000);
				// FrameLayout
			};
		};

		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// 初始化view
	private void initView() {
		iv_hospital_register_pic=(ImageView) findViewById(R.id.iv_hospital_register_pic);
		iv_online_ask_pic=(ImageView) findViewById(R.id.iv_online_ask_pic);
		iv_phone_ask=(ImageView) findViewById(R.id.iv_phone_ask);
		iv_person_center_pic=(ImageView) findViewById(R.id.iv_person_center_pic);
	}



	// 初始化监听器
	private void initListener() {
		iv_hospital_register_pic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(HospitalRegisterActivity.class);
				
			}
		});
		
		iv_online_ask_pic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openActivity(OnLineMainActivity.class);
				
			}
		});
	
		iv_phone_ask.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			openActivity(PhoneAskActivity.class);
			
		}
	});
	
		iv_person_center_pic.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			openActivity(PersonCenter.class);
			
		}
	});
	}
	
	
	private void initBanner() {
		mGallery = (FocusGallery) findViewById(R.id.focusGallery);
		mTitleText = (TextView) findViewById(R.id.titleText);
		mFocusPointImage = (ImageView) findViewById(R.id.focusPointImage);

		final List<Banner> list = getObjectInfo();
		mFocusAdapter = new FocusAdapter(this,
				list);
		mGallery.setAdapter(mFocusAdapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String id = list.get(arg2).getId();
				Log.i("id", id + "id test");
				if (id != "-1") {
					Intent it = new Intent();
					it.putExtra("id", id);
					it.setClass(MainActivity.this, BannerDetail.class);
					startActivity(it);
				}

			}
		});
		 mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
		 @Override
		 public void onItemSelected(AdapterView<?> arg0, View arg1,
		 int position, long arg3) {
		 // i=position;
		 Log.i("onItemSelected", position + "");
		 changeBannerBg(position);
		 handler.removeMessages(0);
		 Message msg = new Message();
		 msg.arg1 = position;
		 handler.sendMessageDelayed(msg, 4000);
		
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> arg0) {
		
		 }
		 });
		 
		 if(list.size()>0)
		 {
		 mTitleText.setText(mFocusAdapter.getItem(0).getTitle());
		 }
			// mContentText
			// .setText(mFocusAdapter.getItem(0).get("content").toString());
		 
		 
		// mGallery.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// handler.removeMessages(0);
		// } else if (event.getAction() == MotionEvent.ACTION_UP) {
		// Message msg = new Message();
		// msg.arg1 = mGallery.getSelectedItemPosition();
		// handler.sendMessageDelayed(msg, 4000);
		//
		//
		// String id=list.get(mGallery.getSelectedItemPosition()).getId();
		// Log.i("id", id+"id test");
		// if(id!="-1")
		// {
		// Intent it=new Intent();
		// it.putExtra("id", id);
		// it.setClass(MainActivity.this, BannerDetail.class);
		// startActivity(it);
		// }
		// }
		// return false;
		// }
		// });

	}
	
	private void checkVersion()
	{
		FindDoctorApplication application=(FindDoctorApplication) getApplication();
		if(application.getIsCheckUpdate()==0)
		{
			SharedPreferences mSharedPreferences = getSharedPreferences("doctor",
					Context.MODE_PRIVATE); 
		UpdateManager manager = new UpdateManager(this);
//		// 检查软件更新
		HashMap<String, String> hashMap=new HashMap<String, String>();
		hashMap.put("versionCode", mSharedPreferences.getInt("versionCode", 0)+"");
		hashMap.put("versionName", mSharedPreferences.getString("versionName", ""));
		hashMap.put("versionUrl", mSharedPreferences.getString("versionUrl", ""));
		hashMap.put("updateLog", mSharedPreferences.getString("updateLog", ""));
		manager.checkUpdate(hashMap);
		}
		
	
	}
	
	private void changeBannerBg(int position) {
		mTitleText.setText(mFocusAdapter.getItem(position).getTitle());
		// mContentText.setText(map.get("content").toString());
		switch (position) {
		case 0:
			mFocusPointImage.setBackgroundResource(R.drawable.focus_point_1);
			break;
		case 1:
			mFocusPointImage.setBackgroundResource(R.drawable.focus_point_2);
			break;
//		case 2:
//			mFocusPointImage.setBackgroundResource(R.drawable.focus_point_3);
//			break;
//		case 3:
//			mFocusPointImage.setBackgroundResource(R.drawable.focus_point_4);
//			break;
		// case 4:
		// mFocusPointImage.setBackgroundResource(R.drawable.focus_point_5);
		// break;
		}
	}
	
	@Override
	protected void onStart() {
		checkVersion();
		initBanner();
		Message msg = new Message();
		msg.what = 0;
		msg.arg1 = 0;
		handler.sendMessageDelayed(msg, 4000);
		try {
			new StateInfos(MainActivity.this).getPicture();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		super.onStart();
	}
	@Override
	protected void onStop() {
		handler.removeMessages(0);
		super.onStop();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序！", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	        	
	        	FindDoctorApplication application=(FindDoctorApplication) this.getApplication();
				application.setIsCheckUpdate(0);
				
		        Intent i = new Intent(Intent.ACTION_MAIN);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.addCategory(Intent.CATEGORY_HOME);
				 startActivity(i);
//				 SysApplication.getInstance().exit();
				ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);  
		        am.killBackgroundProcesses("com.guangyi.forDoctor.activity");
//		        finish();
		        
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
