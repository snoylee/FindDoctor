package com.guangyi.finddoctor.personCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
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
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.MD5;

/**
 * <p>
 * Title: ����ҽԺ��Ӫ֧��ƽ̨-APP���˰�
 * </p>
 * <p>
 * Description:��������
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

public class ForgetPasswordActivity extends BasicActivity implements View.OnClickListener,OnFocusChangeListener{
	private Button mBack,mRegister;
	private EditText mUserMobile, mInputAuthCode, mUserFirstPwd,
			mUserSecondPwd;
	private Handler mHandler;
	private TextView tv_get_auth_code;
	ApiHttpUtil apiHttpUtil;
	private String mobile;
	private String firstpwd;
	private String secondpwd;
	private String checkCode;
	private String autoCode;
	private LinearLayout ll_get_auto_code;
	private Runnable mRunnable;
	private Thread mThread;
	private  boolean isVisiable;
	
	private Chronometer timer = null;
	private long timeTotalInS = 0;
	private long timeLeftInS = 0;
		
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
		setContentView(R.layout.forget_password);
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
		mUserMobile = (EditText) findViewById(R.id.et_user_mobile);
		mUserFirstPwd = (EditText) findViewById(R.id.et_user_first_pwd);
		mUserSecondPwd = (EditText) findViewById(R.id.et_user_second_pwd);
		mInputAuthCode = (EditText) findViewById(R.id.et_input_auth_code);
		mRegister = (Button) findViewById(R.id.ib_submit);
		ll_get_auto_code = (LinearLayout) findViewById(R.id.ll_get_auto_code);
		tv_get_auth_code=(TextView) findViewById(R.id.tv_get_auth_code);
		
		
		mUserMobile.setOnFocusChangeListener(this);
		mUserFirstPwd.setOnFocusChangeListener(this);
		mUserSecondPwd.setOnFocusChangeListener(this);
		mInputAuthCode.setOnFocusChangeListener(this);
		
//		CheckBox ckeckBox=(CheckBox) findViewById(R.id.checkBox);
//		ckeckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(isChecked)
//				{
//					mRegister.setEnabled(true);
//				}
//				else
//				{
//					mRegister.setEnabled(false);
//				}
//				
//			}
//		});

	}

	private void bindData(){
		mobile = mUserMobile.getText().toString().trim();
		firstpwd = mUserFirstPwd.getText().toString().trim();
		secondpwd = mUserSecondPwd.getText().toString().trim();
		checkCode = mInputAuthCode.getText().toString().trim();
    }
	private void initListener() {
		mBack.setOnClickListener(this);
		mRegister.setOnClickListener(this);
		ll_get_auto_code.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		bindData();
		int id = v.getId();
		if (id == R.id.ib_back) {
			finish();
		} else if (id == R.id.ib_submit) {
			if (mobile == null || "".equals(mobile)) {
				showToast("�ֻ����벻��Ϊ��!");
				return;
			}
			if (mobile.length()>0&&mobile.length()!=11) {
				showToast("�ֻ������ʽ����ȷ!");
				return;
			}
			if(checkCode == null || "".equals(checkCode)){
				showToast("��֤�벻��Ϊ��!");
				return;
			}
			if (firstpwd == null || "".equals(firstpwd)|| firstpwd.length()<6) {
				showToast("���볤������Ϊ6λ��������10λ!");
				return;
			}
			if (secondpwd == null || "".equals(secondpwd)) {
				showToast("ȷ�����벻��Ϊ��!");
				return;
			}
			if (!firstpwd.equals(secondpwd)) {
				showToast("�������벻һ�£�����������!");
				return;
			}
			//			if(!checkCode.equals(autoCode)){
//				Toast.makeText(ForgetPasswordActivity.this, "��֤��������������룡",
//						Toast.LENGTH_LONG).show();
//				return;
//			}
			sumbitNow();
		} else if (id == R.id.ll_get_auto_code) {
			if (mobile == null || "".equals(mobile)) {
				showToast("�ֻ����벻��Ϊ��!");
				return;
			}
			if (mobile.length()>0&&mobile.length()!=11) {
				showToast("�ֻ������ʽ����ȷ!");
				return;
			}
			getCheckCode();
		} else {
		}

	}

	private void getCheckCode() {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				synchronized (this){
				Message msg = new Message();
				List<String> params = new ArrayList<String>();
				params.add(mobile);
				params.add("-1");

				msg.arg1 = 1;
				msg.obj = apiHttpUtil.getMethod(
						Config.getProperty("FINDPASSWORD", ""), params);
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
							autoCode = jsonObject.getString("Code");
							Log.i(autoCode, autoCode);
							if(isVisiable)
							{
//							new AlertDialog.Builder(ForgetPasswordActivity.this)
//							.setMessage("��֤���ѷ��͵������ֻ�").setPositiveButton("��֪����", null)
//							.show();
								
								showToast("��֤���ѷ��͵������ֻ�!");
								initTimer(60);
								timer.setVisibility(View.VISIBLE);
								tv_get_auth_code.setVisibility(View.GONE);
								ll_get_auto_code.setClickable(false);
								timer.start();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						
					
					
					}
					
					else if(msg.arg1==2)
					{
						JSONTokener jsonParser = new JSONTokener(jsonString);
						try {
							JSONObject person = (JSONObject) jsonParser.nextValue();
							showToast(person.getString("reason"));
							Log.i("code", person.getInt("code") + "");
							if (person.getInt("code") == 0) {
								finish();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
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



	private void sumbitNow() {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				Map<String, String> params = new HashMap<String, String>();
				String passwd = MD5.GetMD5Code(firstpwd);
				params.put("mobile", mobile);
				params.put("md5passwd", passwd);
				params.put("checkCode", checkCode);
				msg.arg1 = 2;
				msg.obj = apiHttpUtil.postMethod(
						Config.getProperty("EDITPASSWORD", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

	}
	
	
	
	//��ʱ������
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
		 * ������ʱ��ʾ����Ļ��
		 * ���������������½� 
		 */
		private void refreshTimeLeft() {
			
			
			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
			SpannableStringBuilder builder = new SpannableStringBuilder(timeLeftInS+" �������");
			builder.setSpan(redSpan, 0, String.valueOf(timeLeftInS).length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			this.timer.setText(builder);
		}


		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			bindData();
			if (hasFocus) {
				if(v.getId()==R.id.et_input_auth_code)
				{
					if (mobile == null || "".equals(mobile)) {
						showToast( "�ֻ����벻��Ϊ��!");
						return;
					}
					if (mobile.length()>0&&mobile.length()!=11) {
						showToast( "�ֻ������ʽ����ȷ!");
						return;
					}
				}
				else if(v.getId()==R.id.et_user_first_pwd)
				{
					if (mobile == null || "".equals(mobile)) {
						showToast( "�ֻ����벻��Ϊ��!");
						return;
					}
					if (mobile.length()>0&&mobile.length()!=11) {
						showToast( "�ֻ������ʽ����ȷ!");
						return;
					}
					if (checkCode == null || "".equals(checkCode)) {
						showToast( "��֤�벻��Ϊ��!");
						return;
					}
				}
				
				
				else if(v.getId()==R.id.et_user_first_pwd)
				{
					if (mobile == null || "".equals(mobile)) {
						showToast( "�ֻ����벻��Ϊ��!");
						return;
					}
					if (mobile.length()>0&&mobile.length()!=11) {
						showToast( "�ֻ������ʽ����ȷ!");
						return;
					}
					if (checkCode == null || "".equals(checkCode)) {
						showToast( "��֤�벻��Ϊ��!");
						return;
					}
				}
				
				
				else if(v.getId()==R.id.et_user_second_pwd)
				{
					if (mobile == null || "".equals(mobile)) {
						showToast( "�ֻ����벻��Ϊ��!");
						return;
					}
					if (mobile.length()>0&&mobile.length()!=11) {
						showToast( "�ֻ������ʽ����ȷ!");
						return;
					}
					if (checkCode == null || "".equals(checkCode)) {
						showToast( "��֤�벻��Ϊ��!");
						return;
					}
					if (firstpwd == null || "".equals(firstpwd)|| firstpwd.length()<6) {
						showToast( "���볤������Ϊ6λ��������10λ!");
						return;
					}
				}
			
//				if (checkCode == null || "".equals(checkCode)) {
//					showToast( "��֤�벻��Ϊ��!");
//					return;
//				}
//				if (firstpwd == null || "".equals(firstpwd)|| firstpwd.length()<6) {
//					showToast( "���볤������Ϊ6λ��������10λ!");
//					return;
//				}
//				if (secondpwd == null || "".equals(secondpwd)) {
//					showToast( "ȷ�����벻��Ϊ��!");
//					return;
//				}
//				if (!firstpwd.equals(secondpwd)) {
//					showToast( "�������벻һ�£�����������!");
//					return;
//				}
				
			}
			
		}


}
