package com.guangyi.forDoctor.personCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.http.ApiHttpUtil;
import com.guangyi.forDoctor.model.TimeList;
import com.guangyi.forDoctor.utils.MD5;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.guangyi.forDoctor.config.Config;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ForgetPasswordActivity extends BasicActivity implements
		View.OnClickListener {
	private Button mBack, mRegister;
	private EditText mUserMobile, mInputAuthCode, mUserFirstPwd,
			mUserSecondPwd;
	private Handler mHandler;
	ApiHttpUtil apiHttpUtil;
	private String mobile;
	private String firstpwd;
	private String secondpwd;
	private String checkCode;
	private String autoCode;
	private LinearLayout ll_get_auto_code;
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
			super.onStop();
		}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		SysApplication.getInstance().addActivity(this);
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
		mUserMobile = (EditText) findViewById(R.id.et_user_mobile);
		mUserFirstPwd = (EditText) findViewById(R.id.et_user_first_pwd);
		mUserSecondPwd = (EditText) findViewById(R.id.et_user_second_pwd);
		mInputAuthCode = (EditText) findViewById(R.id.et_input_auth_code);
		mRegister = (Button) findViewById(R.id.ib_submit);
		ll_get_auto_code = (LinearLayout) findViewById(R.id.ll_get_auto_code);

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

	}

	@Override
	public void onClick(View v) {
		bindData();
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.ib_submit:
			
			if (mobile == null || "".equals(mobile)) {
				Toast.makeText(ForgetPasswordActivity.this, "手机号码不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (mobile.length()>0&&mobile.length()!=11) {
				Toast.makeText(ForgetPasswordActivity.this, "手机号码格式不正确！",
						Toast.LENGTH_SHORT).show();
				return;
			}
		
			
			if (checkCode == null || "".equals(checkCode)) {
				Toast.makeText(ForgetPasswordActivity.this, "验证码不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			if (firstpwd == null || "".equals(firstpwd)|| firstpwd.length()<6) {
				Toast.makeText(ForgetPasswordActivity.this, "密码长度至少为6位，不超过10位！",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (secondpwd == null || "".equals(secondpwd)) {
				Toast.makeText(ForgetPasswordActivity.this, "确认密码不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (!firstpwd.equals(secondpwd)) {
				Toast.makeText(ForgetPasswordActivity.this, "密码不一致，请重新输入！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!checkCode.equals(autoCode)) {
				Toast.makeText(ForgetPasswordActivity.this, "验证码错误，请重新输入！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			sumbitNow();
			break;

		case R.id.ll_get_auto_code:
			
			if (mobile == null || "".equals(mobile)) {
				Toast.makeText(ForgetPasswordActivity.this, "手机号码不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (mobile.length()>0&&mobile.length()!=11) {
				Toast.makeText(ForgetPasswordActivity.this, "手机号码格式不正确！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			getCheckCode();

			break;

		default:
			break;
		}

	}

	private void getCheckCode() {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				List<String> params = new ArrayList<String>();
				params.add(mobile);
				params.add("-1");
				msg.arg1 = 1;
				msg.obj = apiHttpUtil.getMethod(
						Config.getProperty("FINDPASSWORD", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();

			List<TimeList> mlist = new ArrayList<TimeList>();
			String jsonString = msg.obj.toString();
			Log.i("result jsonString", jsonString);
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
				showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
				showToast(getResources().getString(R.string.conntimeout));
			} else {
				JSONObject jsonObject;
				int code = -1;
				try {
					jsonObject = new JSONObject(jsonString);
					code = jsonObject.getInt("code");
					if (code == 0) {
						if (msg.arg1 == 1) {
							autoCode = jsonObject.getString("Code");
							AlertDialog.Builder builder = new Builder(
									ForgetPasswordActivity.this);
							builder.setPositiveButton("确定",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).setMessage("验证码已发送，请注意查收!").show();

						} else if (msg.arg1 == 2) {
							AlertDialog.Builder builder = new Builder(
									ForgetPasswordActivity.this);
							builder.setPositiveButton("确定",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											openActivity(UserLoginActivity.class);
											finish();
										}
									}).setMessage("新密码修改成功，请重新登录！").show();
						}
					} else {
						showToast(jsonObject.getString("reason"));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
				msg.arg1 = 2;
				msg.obj = apiHttpUtil.postMethod(
						Config.getProperty("EDITPASSWORD", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

	}

}
