package com.guangyi.finddoctor.secondActivity;

import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.MainActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.custview.BadgeView;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.personCenter.CommonPatientActivity;
import com.guangyi.finddoctor.personCenter.DeclareActivity;
import com.guangyi.finddoctor.personCenter.EditionActivity;
import com.guangyi.finddoctor.personCenter.IntroduceActivity;
import com.guangyi.finddoctor.personCenter.MyCollectActivity;
import com.guangyi.finddoctor.personCenter.MyConsultActivity;
import com.guangyi.finddoctor.personCenter.MyRegisterActivity;
import com.guangyi.finddoctor.personCenter.SuggestActivity;
import com.guangyi.finddoctor.personCenter.UserLoginActivity;
import com.guangyi.finddoctor.personCenter.UserRegisterActivity;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.StateInfos;
public class SpersonCenter extends BasicActivity {
	private SharedPreferences mSharedPreferences;
	private Button mLogin, mRegister;
	private RelativeLayout mCommonPatient;
	private RelativeLayout mAddLocalPwd;
	private LinearLayout mLinearLoginOpen;
	private LinearLayout mLinearLoginSuccessClose;
	private TextView mUserName;
	private String mUserNameString;
	private Button mLogOut;
	private boolean isLogin;
	private RelativeLayout relative_add_local_pwd;
	private RelativeLayout relative_my_collection;
	private RelativeLayout relative_my_register;
	private RelativeLayout relative_my_consult;
	private RelativeLayout relative_suggest;
	private RelativeLayout relative_introduce;
	private RelativeLayout relative_edition;
	private RelativeLayout relative_declare;
	String firstpwd = null;
	String secondpwd = null;
	
	private Runnable mRunnable;
	private Thread mThread;
	private Handler mHandler;
	private TextView tv_my_consult;
	private CheckBox check_box;
	private BadgeView badgeView;
	
	
	
	
//	mSharedPreferences = getSharedPreferences("personCenter",
//			Context.MODE_PRIVATE);
//	mEditor = mSharedPreferences.edit();
//	mEditor.putInt("userId", data.getExtras().getInt("userId"));
	
	private void commonThreadStart()
	{
		if(mRunnable!=null)
		{
		mThread=new Thread(mRunnable);
		mThread.start();
		}
	}

	protected void onStop() {
		mHandler.removeCallbacks(mRunnable);
		super.onStop();
	}
	
	//取数据的地方
	private void getData(final int userId) {
		mRunnable=new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				String params=userId+"";
				msg.arg1=1;
				msg.obj = new ApiHttpUtil().getMethod(
						Config.getProperty("CANREADCOUNT", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

	}
	
	@Override
	protected void onResume() {
		 SharedPreferences sharedPreferences =
				 getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
				int count=sharedPreferences.getInt("msgCount", -1);
				
				badgeView.setText(count+"");
				if(count>0)
				{
					badgeView.show();
				}
				else
				{
					badgeView.hide();
				}
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		initParams();
		try {
			new StateInfos(SpersonCenter.this,"个人中心",1).postStateInfo();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initMySharedPreference();
		if (isLogin) {
			mLinearLoginOpen.setVisibility( View.VISIBLE);
			mUserName.setText(mUserNameString);
			mLinearLoginSuccessClose.setVisibility( View.GONE);
		}
		
		 SharedPreferences sharedPreferences =
				 getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
				int count=sharedPreferences.getInt("msgCount", -1);
				
				badgeView.setText(count+"");
				if(count>0)
				{
					badgeView.show();
				}
				else
				{
					badgeView.hide();
				}
				
				int userId=getSharedPreferences("personCenter",Context.MODE_PRIVATE).getInt("userId", -1);
				if(userId>-1)
				{
					getData(userId);
				}
		super.onStart();
	}
	
	
	private class MyHandler extends Handler {
			@Override
			public void handleMessage(Message msg) {

				canclePregressDialog();
				JSONObject jsonObject = null;
				String jsonString = msg.obj.toString();
				Log.i("canreadcount", jsonString);
				if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
//					showToast(getResources().getString(R.string.soconntimeout));
				} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
//					showToast(getResources().getString(R.string.conntimeout));
				} else {
					int code = -1;
					String reason = getResources().getString(
							R.string.network_preoblem);
					try {
						jsonObject = new JSONObject(msg.obj.toString());
						code = jsonObject.getInt("code");
						reason = jsonObject.getString("reason");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (code == 0) {
						if (msg.arg1 == 1) {
							try {
								SharedPreferences sharedPreferences =
										 getSharedPreferences("personCenter",
										Context.MODE_PRIVATE);
										sharedPreferences.edit().putInt("msgCount", jsonObject.getInt("canReadCount")).commit();
								if(jsonObject.getInt("canReadCount")>0)
								{
								badgeView.setText(jsonObject.getInt("canReadCount")+"");
								badgeView.show();
								}
								else
								{
									badgeView.hide();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}


					}
//					else {
//						showToast(reason);
//					}

				}

				super.handleMessage(msg);

			}
		

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_center);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		
		
		initView();
		initPersonCenterView();
		initPersonCenterListener();
	
	}
	
	
	
	private void initMySharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		isLogin = mSharedPreferences.getBoolean("isLogin", false);
		mUserNameString = mSharedPreferences.getString("userMobile", "");
	}
	
	
	private void initParams()
	{
		 
		
		mHandler=new MyHandler();
		check_box.setChecked(getDataSf().getBoolean("isBannerCan", true));
//		 check_box=(CheckBox) findViewById(R.id.check_box);
//			check_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//					Editor editor=getDataSfEditor();
//					editor.putBoolean("isBannerCan", isChecked);
//					editor.commit();
//				}
//			});
	}
	
	
	private void initView() {
		tv_my_consult=(TextView) findViewById(R.id.tv_my_consult);
		badgeView=new BadgeView(this,tv_my_consult);
		Button mBack;mBack = (Button) findViewById(R.id.ib_back);
		
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(MainActivity.class);
				finish();


			}
		});
		
	}
	
	
	
	private void initPersonCenterView() {
		mLogin = (Button)  findViewById(R.id.ib_login);
		mRegister = (Button)  findViewById(R.id.ib_register);
		mCommonPatient = (RelativeLayout) 
				findViewById(R.id.realivelay_common_patient);
		mAddLocalPwd = (RelativeLayout) 
				findViewById(R.id.realivelay_add_local_pwd);
		mUserName = (TextView)  findViewById(R.id.tv_user_name);
		mLogOut = (Button)  findViewById(R.id.ib_logout);
		mLinearLoginOpen = (LinearLayout) 
				findViewById(R.id.linear_login_open);
		mLinearLoginSuccessClose = (LinearLayout) 
				findViewById(R.id.linear1);
		
		
		relative_my_collection = (RelativeLayout) 
				findViewById(R.id.relative_my_collection);
		relative_my_register = (RelativeLayout) 
				findViewById(R.id.relative_my_register);
		relative_my_consult = (RelativeLayout) 
				findViewById(R.id.relative_my_consult);
		relative_suggest = (RelativeLayout) 
				findViewById(R.id.relative_suggest);
		relative_introduce = (RelativeLayout) 
				findViewById(R.id.relative_introduce);
		relative_edition = (RelativeLayout) 
				findViewById(R.id.relative_edition);
		relative_declare = (RelativeLayout) 
				findViewById(R.id.relative_declare);
		relative_add_local_pwd = (RelativeLayout) 
				findViewById(R.id.realivelay_add_local_pwd);
		
		    check_box=(CheckBox) findViewById(R.id.check_box);
			check_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Editor editor=getDataSfEditor();
					editor.putBoolean("isBannerCan", isChecked);
					editor.commit();
				}
			});
	

	}
	
	private void initPersonCenterListener() {
		mLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpersonCenter.this,
						UserLoginActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		mRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(SpersonCenter.this, UserRegisterActivity.class);
				startActivityForResult(it, 0);
//				openActivity(UserRegisterActivity.class);
			}
		});

		mCommonPatient.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpersonCenter.this,CommonPatientActivity.class);
				intent.putExtra("tagtype", 0);
				startActivity(intent);

			}
		});
		mAddLocalPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		mLogOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mLinearLoginOpen.setVisibility(View.GONE);
				mLinearLoginSuccessClose.setVisibility(View.VISIBLE);
				mSharedPreferences = getSharedPreferences("personCenter",
						MODE_PRIVATE);
				Editor editor=mSharedPreferences.edit();
				editor.remove("userId");
				editor.remove("isLogin");
				editor.remove("isShow");
				editor.remove("userMobile");
				editor.remove(getResources().getString(R.string.sendType));
				editor.commit();

			}
		});

		relative_my_collection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpersonCenter.this,
						MyCollectActivity.class);
				startActivity(intent);

			}
		});

		relative_my_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpersonCenter.this,
						MyRegisterActivity.class);
				intent.putExtra("flagtype", 0);
				startActivity(intent);

			}
		});

		relative_my_consult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpersonCenter.this,
						MyConsultActivity.class);
				startActivity(intent);

			}
		});

		relative_add_local_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		relative_suggest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpersonCenter.this,
						SuggestActivity.class);
				startActivity(intent);

			}
		});

		relative_introduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpersonCenter.this,
						IntroduceActivity.class);
				startActivity(intent);

			}
		});

		relative_edition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpersonCenter.this,
						EditionActivity.class);
				startActivity(intent);

			}
		});

		relative_declare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpersonCenter.this,
						DeclareActivity.class);
				startActivity(intent);
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ResultCodeCategory.RESULT_CODE_LOGIN||resultCode == ResultCodeCategory.RESULT_CODE_REGISTER) {
//			mSharedPreferences = getSharedPreferences("personCenter",
//					Context.MODE_PRIVATE);
//			mEditor = mSharedPreferences.edit();
//			mEditor.putInt("userId", data.getExtras().getInt("userId"));
//			mEditor.putBoolean("isLogin", data.getExtras()
//					.getBoolean("isLogin"));
//			mEditor.putBoolean("isShow", true);
//			mEditor.putString("userMobile",
//					data.getExtras().getString("userMobile"));
//			mEditor.commit();
//			mLinearLoginOpen.setVisibility(View.VISIBLE);
//			mUserName.setText(data.getExtras().getString("userMobile"));
//			mLinearLoginSuccessClose.setVisibility(View.GONE);
			
			
			initMySharedPreference();
			if (isLogin) {
				mLinearLoginOpen.setVisibility( View.VISIBLE);
				mUserName.setText(mUserNameString);
				mLinearLoginSuccessClose.setVisibility( View.GONE);
			}

		}

	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
			openActivity(MainActivity.class);
			finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
