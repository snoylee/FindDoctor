package com.guangyi.finddoctor.personCenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.MD5;


/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:用户注册
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
public class UserRegisterActivity extends BasicActivity implements
		View.OnClickListener,OnFocusChangeListener {
	private Button mBack, mRegister;
	private EditText mUserMobile, mInputAuthCode, mUserFirstPwd,
			mUserSecondPwd;
	private TextView register_ruleView,tv_get_auth_code;
	private Handler mHandler;
	private ApiHttpUtil apiHttpUtil;
	private String mobile;
	private String firstpwd;
	private String secondpwd;
	private String checkCode;
	private LinearLayout ll_get_auto_code;
	private Runnable mRunnable;
	private Thread mThread;
	
	
	private Chronometer timer = null;
	private long timeTotalInS = 0;
	private long timeLeftInS = 0;
	
	private  boolean isVisiable;
		
	private void commonThreadStart()
		{
			if(mRunnable!=null)
			{
			mThread=new Thread(mRunnable);
			mThread.start();
			}
		}

	@Override
	protected void onStart() {
		isVisiable=true;
		super.onStart();
	}
		protected void onStop() {
			mHandler.removeCallbacks(mRunnable);
			isVisiable=false;
			super.onStop();
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_reigster);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initListener();
	}

	private void initParams() {
		mHandler = new MyHandler();
		apiHttpUtil = new ApiHttpUtil();
	}

	private void initView() {
		mBack = (Button) findViewById(R.id.ib_back);
		timer = (Chronometer) findViewById(R.id.timer);
		tv_get_auth_code=(TextView) findViewById(R.id.tv_get_auth_code);
		mUserMobile = (EditText) findViewById(R.id.et_user_mobile);
		mUserFirstPwd = (EditText) findViewById(R.id.et_user_first_pwd);
		mUserSecondPwd = (EditText) findViewById(R.id.et_user_second_pwd);
		mInputAuthCode = (EditText) findViewById(R.id.et_input_auth_code);
		mRegister = (Button) findViewById(R.id.ib_submit);
		mUserMobile.setOnFocusChangeListener(this);
		mUserFirstPwd.setOnFocusChangeListener(this);
		mUserSecondPwd.setOnFocusChangeListener(this);
		mInputAuthCode.setOnFocusChangeListener(this);
		ll_get_auto_code = (LinearLayout) findViewById(R.id.ll_get_auto_code);
		register_ruleView = (TextView) findViewById(R.id.register_rule);
		CheckBox ckeckBox=(CheckBox) findViewById(R.id.checkBox);
		ckeckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					mRegister.setEnabled(true);
				}
				else
				{
					mRegister.setEnabled(false);
				}
				
			}
		});

	}

	private void bindData() {
		mobile = mUserMobile.getText().toString().trim();
		firstpwd = mUserFirstPwd.getText().toString().trim();
		secondpwd = mUserSecondPwd.getText().toString().trim();
		checkCode = mInputAuthCode.getText().toString().trim();
	}

	private void initListener() {
		mBack.setOnClickListener(this);
		mRegister.setOnClickListener(this);
		ll_get_auto_code.setOnClickListener(this);
		register_ruleView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		bindData();
		int id = v.getId();
		if (id == R.id.ib_back) {
			finish();
		} else if (id == R.id.ib_submit) {
			if (mobile == null || "".equals(mobile)) {
				showToast( "手机号码不能为空!");
				return;
			}
			if (mobile.length()>0&&mobile.length()!=11) {
				showToast( "手机号码格式不正确!");
				return;
			}
			if (checkCode == null || "".equals(checkCode)) {
				showToast( "验证码不能为空!");
				return;
			}
			if (firstpwd == null || "".equals(firstpwd)|| firstpwd.length()<6) {
				showToast( "密码长度至少为6位，不超过10位!");
				return;
			}
			if (secondpwd == null || "".equals(secondpwd)) {
				showToast( "确认密码不能为空!");
				return;
			}
			if (!firstpwd.equals(secondpwd)) {
				showToast( "两次密码不一致，请重新输入!");
				return;
			}
			//			if (!checkCode.equals(autoCode)) {
//				Toast.makeText(UserRegisterActivity.this, "验证码错误，请重新输入！",
//						Toast.LENGTH_LONG).show();
//				return;
//			}
			RegisterNow();
		} else if (id == R.id.ll_get_auto_code) {
			if (mobile == null || "".equals(mobile)) {
				showToast( "手机号码不能为空!");
				return;
			}
			if (mobile.length()>0&&mobile.length()!=11) {
				showToast( "手机号码格式不正确!");
				return;
			}
			getCheckCode();
		} else if (id == R.id.register_rule) {
			openActivity(RegisterRuleActivity.class);
		} else {
		}

	}

	private void getCheckCode() {
		initProgressDialog();
		mRunnable=new Runnable() {
			@Override
			public void run() {
				synchronized (this) {
					Message msg = new Message();
					List<String> params = new ArrayList<String>();
					params.add(mobile);
					params.add("-1");
					msg.arg1 = 1;
					msg.obj = ApiHttpUtil.getMethod(
							Config.getProperty("GETCHECKCODE", ""), params);
					mHandler.sendMessage(msg);
				}
				
			}
			
		};
		
		commonThreadStart();
		

	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.i("json string test", jsonString);
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
				showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
				showToast(getResources().getString(R.string.conntimeout));
			}
			else
			{
				
				int code = -1;
				String reason=getResources().getString(R.string.network_preoblem);
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					code = jsonObject.getInt("code");
					reason=jsonObject.getString("reason");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(code==0)
				{
					
					if(msg.arg1==1)
					{
						try {
							if (jsonObject.getInt("code") == 0) {
								if(isVisiable)
								{
									showToast("验证码已发送到您的手机!");
									initTimer(60);
									timer.setVisibility(View.VISIBLE);
									tv_get_auth_code.setVisibility(View.GONE);
									ll_get_auto_code.setClickable(false);
									timer.start();
//								jsonObject.getString("Code");
//								new AlertDialog.Builder(UserRegisterActivity.this)
//								.setMessage("验证码已发送到您的手机").setPositiveButton("我知道了", new OnClickListener() {
//									
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										initTimer(10);
//										timer.setVisibility(View.VISIBLE);
//										tv_get_auth_code.setVisibility(View.GONE);
//										ll_get_auto_code.setClickable(false);
//										timer.start();
//										
//									}
//								})
//								.show();
								}
								
							} else {
								showToast(reason);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					else if(msg.arg1==2)
					{
							showToast(reason);
								//调用登录接口，登录到个人中心
								loginNow();
					}
					
					else if(msg.arg1==3)
					{
						try {
							JSONTokener jsonParser = new JSONTokener(jsonString);
							JSONObject person = (JSONObject) jsonParser.nextValue();
							JSONObject userObj = person.getJSONObject("user");
							if (person.getInt("code") == 0) {
//								Intent intent = new Intent();
//								intent.putExtra(ResultCodeCategory.fLAG, 3);
//								intent.putExtra("userMobile",userObj.getString("userMoble"));
//								intent.putExtra("userId",userObj.getInt("id"));
//								intent.putExtra("isLogin", true);
								//跳转到个人中心
//								intent.setClass(UserRegisterActivity.this, SpersonCenter.class);
								SharedPreferences mSharedPreferences = getSharedPreferences("personCenter",
										Context.MODE_PRIVATE);
								Editor mEditor = mSharedPreferences.edit();
								mEditor.putInt("userId",userObj.getInt("id"));
								mEditor.putBoolean("isLogin",true);
								mEditor.putBoolean("isShow", true);
								mEditor.putString("userMobile",userObj.getString("userMoble"));
								mEditor.commit();
//								startActivity(intent);
								setResult(ResultCodeCategory.RESULT_CODE_REGISTER);
								finish();
							}
	
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
						
				}
				else
				{
					showToast(reason);
				}
				
			}
			
			super.handleMessage(msg);

		}

	}

	private void RegisterNow() {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				Map<String, String> params = new HashMap<String, String>();
				String transactionId = String.valueOf(new Date().getTime());
				String passwd = MD5.GetMD5Code(firstpwd);
//				String code = MD5.GetMD5Code(checkCode);
				params.put("mobile", mobile);
				params.put("passwd", passwd);
				params.put("transactionId", transactionId);
				params.put("checkCode", checkCode);
				msg.arg1 = 2;
				msg.obj = apiHttpUtil.postMethod(
						Config.getProperty("REGISTOR_STRING", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

	}
	
	//调用登录接口，登录到个人中心
	private void loginNow() {
		initProgressDialog();
      mRunnable=new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				Map<String, String> params = new HashMap<String, String>();
				String transactionId = String.valueOf(new Date().getTime());
				String passwd = MD5.GetMD5Code(firstpwd);
				params.put("mobile", mobile);
				params.put("passwd", passwd);
				params.put("transactionId", transactionId);
				params.put("isDoctorVersion", "0");
				msg.arg1 = 3;
				msg.obj = apiHttpUtil.postMethod(
						Config.getProperty("lOGIN_STRING", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

	}
	
	
	
	//定时器操作
	private void initTimer(long total) {
		this.timeTotalInS = total;
		this.timeLeftInS = total;
		timer.setOnChronometerTickListener(new OnChronometerTickListener() {
			
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				if (timeLeftInS <= 0) {
					timer.stop();
					timer.setVisibility(View.GONE);
					tv_get_auth_code.setVisibility(View.VISIBLE);
					ll_get_auto_code.setClickable(true);
					return;
				}

				timeLeftInS--;
				refreshTimeLeft();
			}
		});
	}
	
	
	/**
	 * 将倒计时显示在屏幕上
	 * 初步决定放在右下角 
	 */
	private void refreshTimeLeft() {
		
		
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		SpannableStringBuilder builder = new SpannableStringBuilder(timeLeftInS+" 秒后重试");
		builder.setSpan(redSpan, 0, String.valueOf(timeLeftInS).length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		this.timer.setText(builder);
	}

	
	
//	mUserMobile = (EditText) findViewById(R.id.et_user_mobile);
//	mUserFirstPwd = (EditText) findViewById(R.id.et_user_first_pwd);
//	mUserSecondPwd = (EditText) findViewById(R.id.et_user_second_pwd);
//	mInputAuthCode = (EditText) findViewById(R.id.et_input_auth_code);
//	mRegister = (Button) findViewById(R.id.ib_submit);
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		bindData();
		if (hasFocus) {
			if(v.getId()==R.id.et_input_auth_code)
			{
				if (mobile == null || "".equals(mobile)) {
					showToast( "手机号码不能为空!");
					return;
				}
				if (mobile.length()>0&&mobile.length()!=11) {
					showToast( "手机号码格式不正确!");
					return;
				}
			}
			else if(v.getId()==R.id.et_user_first_pwd)
			{
				if (mobile == null || "".equals(mobile)) {
					showToast( "手机号码不能为空!");
					return;
				}
				if (mobile.length()>0&&mobile.length()!=11) {
					showToast( "手机号码格式不正确!");
					return;
				}
				if (checkCode == null || "".equals(checkCode)) {
					showToast( "验证码不能为空!");
					return;
				}
			}
			
			
			else if(v.getId()==R.id.et_user_first_pwd)
			{
				if (mobile == null || "".equals(mobile)) {
					showToast( "手机号码不能为空!");
					return;
				}
				if (mobile.length()>0&&mobile.length()!=11) {
					showToast( "手机号码格式不正确!");
					return;
				}
				if (checkCode == null || "".equals(checkCode)) {
					showToast( "验证码不能为空!");
					return;
				}
			}
			
			
			else if(v.getId()==R.id.et_user_second_pwd)
			{
				if (mobile == null || "".equals(mobile)) {
					showToast( "手机号码不能为空!");
					return;
				}
				if (mobile.length()>0&&mobile.length()!=11) {
					showToast( "手机号码格式不正确!");
					return;
				}
				if (checkCode == null || "".equals(checkCode)) {
					showToast( "验证码不能为空!");
					return;
				}
				if (firstpwd == null || "".equals(firstpwd)|| firstpwd.length()<6) {
					showToast( "密码长度至少为6位，不超过10位!");
					return;
				}
			}
		
//			if (checkCode == null || "".equals(checkCode)) {
//				showToast( "验证码不能为空!");
//				return;
//			}
//			if (firstpwd == null || "".equals(firstpwd)|| firstpwd.length()<6) {
//				showToast( "密码长度至少为6位，不超过10位!");
//				return;
//			}
//			if (secondpwd == null || "".equals(secondpwd)) {
//				showToast( "确认密码不能为空!");
//				return;
//			}
//			if (!firstpwd.equals(secondpwd)) {
//				showToast( "两次密码不一致，请重新输入!");
//				return;
//			}
			
		}
		
	}

	
	

}
