package com.guangyi.forDoctor.personCenter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.Utils;
import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.MainActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.config.Config;
import com.guangyi.forDoctor.http.ApiHttpUtil;
import com.guangyi.forDoctor.model.Doctor;
import com.guangyi.forDoctor.utils.MD5;

public class UserLoginActivity extends BasicActivity implements OnClickListener {
	private EditText mUserPhone, mUserPwd;
	private Button mButtonLogin;
	private Handler mHandler;
	private Button mBack;
	private TextView tv_forget;
	private Runnable mRunnable;
	private Thread mThread;
		
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
			PushManager.activityStarted(this);
			super.onStop();
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);
		SysApplication.getInstance().addActivity(this);
		initBack();
		initParams();
		initView();
		initListener();
	}

	private void initParams() {
		mHandler = new MyHandler();
	}
	


	private void initView() {

		mUserPhone = (EditText) findViewById(R.id.et_user_phone);
		mUserPwd = (EditText) findViewById(R.id.et_user_pwd);
		mButtonLogin = (Button) findViewById(R.id.ib_user_login);
		tv_forget=(TextView) findViewById(R.id.tv_forget);

	}

	private void initListener() {
		mButtonLogin.setOnClickListener(this);
		tv_forget.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_user_login:
			loginNow();
			break;
		case R.id.tv_forget:
			openActivity(ForgetPasswordActivity.class);
			break;
		default:
			break;
		}

	}

	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			String jsonString=msg.obj.toString();
			Log.i("login", jsonString);
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
				showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
				showToast(getResources().getString(R.string.conntimeout));
			}
			else
			{
				JSONObject jsonObject;
				String reason=getResources().getString(R.string.network_preoblem);
				
				int code = -1;
				try {
					jsonObject = new JSONObject(jsonString);
					code = jsonObject.getInt("code");
					reason=jsonObject.getString("reason");
					Log.i("jsonString", jsonString);
					if(code==0)
					{
						showToast(reason);
						SharedPreferences sp = getSharedPreferences("doctor", MODE_PRIVATE);
                       if(null != sp)
                       {
							//存入数据
					       Editor editor = sp.edit();
					       editor.putString(Doctor.USERMOBLE,jsonObject.getJSONObject("user").getString(Doctor.USERMOBLE));
					       editor.putInt(Doctor.DOCSCORE,jsonObject.getJSONObject("user").getJSONObject("doctor").getInt(Doctor.DOCSCORE));
					       editor.putString(Doctor.EXPERTID,jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.EXPERTID));
					       editor.putString(Doctor.DOCTID, jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTID));
					       editor.putString(Doctor.DOCTSEX, jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTSEX));
					       editor.putString(Doctor.DOCTPOSI, jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTPOSI));
					       editor.putString(Doctor.DOCTHOSPNAME, jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTHOSPNAME));
					       editor.putString(Doctor.DOCTDEPANAME, jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTDEPANAME));
					       editor.putString(Doctor.DOCTNAME, jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTNAME));
					       editor.putString(Doctor.DOCTSPECIALTY, jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTSPECIALTY));
					       editor.putString(Doctor.DOCTINTRODUCTION, jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTINTRODUCTION));
					       editor.putString(Doctor.DOCTHOSPNAME, jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTHOSPNAME));
					       editor.putInt("userId", jsonObject.getJSONObject("user").getInt("id"));
					       editor.putBoolean("isLogin", true);
					       editor.commit();
                       }
                       
				       openActivity(MainActivity.class);
				       startPushService();
				       finish();
					       
					}
					else
					{
						showToast(reason);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			super.handleMessage(msg);
		}

	}

	private void loginNow() {
		
		if(mUserPwd.getText().toString().trim().length()<=0)
		{
			showToast("请输入手机号码和密码!");
			
		}else if(mUserPhone.getText().toString().length() != 11)
		{
			showToast("请输入正确的手机号码!");
		}
		else
		{
			initProgressDialog();
		    mRunnable=new Runnable() {

			@Override
			public void run() {
				
			 Message msg = mHandler.obtainMessage();
				
				Map<String, String> params = new HashMap<String, String>();
				String transactionId = String.valueOf(new Date().getTime());
				String passwd = MD5.GetMD5Code(mUserPwd.getText().toString()
						.trim());
				params.put("mobile", mUserPhone.getText().toString().trim());
				params.put("passwd", passwd);
				params.put("transactionId", transactionId);
				params.put("isDoctorVersion", "1");
				msg.obj = new ApiHttpUtil().postMethod(
						Config.getProperty("DOCTOR_LOGIN_STRING",""), params);
				mHandler.sendMessage(msg);	
			}
		};
		commonThreadStart();
		}

	}
	
	
	
	private void initBack()
	{
	mBack=(Button) findViewById(R.id.ib_back);

	mBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
					
				}
			});
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		PushManager.activityStarted(this);
	}
	
	
	
	private void startPushService()
	{
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, 
				Utils.getMetaValue(UserLoginActivity.this, "api_key"));
	}



}
