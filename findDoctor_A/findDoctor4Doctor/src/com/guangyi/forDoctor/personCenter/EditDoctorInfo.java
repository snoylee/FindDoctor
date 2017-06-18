package com.guangyi.forDoctor.personCenter;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.config.Config;
import com.guangyi.forDoctor.http.ApiHttpUtil;
import com.guangyi.forDoctor.model.Doctor;
import com.guangyi.forDoctor.utils.SharedPrefsUtil;

public class EditDoctorInfo extends BasicActivity {

	// 个人信息
	private EditText doctName;
	private TextView tv_sex;
	private EditText expert, introduce;
	private String doctId;
	private Handler mHandler;
	private SharedPreferences sp;
	private Runnable mRunnable;
	private Thread mThread;
	private String sx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.edit_doctor_info);
		SysApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		initView();

	}

	@Override
	protected void onStart() {
		initParams();
		super.onStart();
	}

	private void commonThreadStart() {
		if (mRunnable != null) {
			mThread = new Thread(mRunnable);
			mThread.start();
		}
	}

	protected void onStop() {
		mHandler.removeCallbacks(mRunnable);
		super.onStop();
	}

	private void refreshDoctorInfo() {
		initProgressDialog();
		mRunnable = new Runnable() {

			@Override
			public void run() {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("doctName", doctName.getText().toString());
				map.put("sex", sx);
				map.put("doctSpecialty", expert.getText().toString());
				map.put("doctIntroduction", introduce.getText().toString());
				map.put("doctId", doctId);
				Message msg = new Message();
				msg.arg1 = 1;
				msg.obj = new ApiHttpUtil().postMethod(
						Config.getProperty("UPDATE_DOCTOR", ""), map);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			String jsonString = msg.obj.toString();
			Log.i("json", jsonString);
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
				showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
				showToast(getResources().getString(R.string.conntimeout));
			} else {
				String reason = getResources().getString(
						R.string.network_preoblem);
				JSONObject jsonObject;
				
				
				int code = -1;
				try {
					jsonObject = new JSONObject(jsonString);
					reason=jsonObject.getString("reason");
					code = jsonObject.getInt("code");
					if (code == 0) {
						if (msg.arg1 == 1) {
							showToast(reason);
							sp = getSharedPreferences("doctor", MODE_PRIVATE);
							if (null != sp) {
								// 存入数据
								Editor editor = sp.edit();
								editor.putString(Doctor.USERMOBLE,
										jsonObject.getJSONObject("user")
												.getString(Doctor.USERMOBLE));
								editor.putInt(Doctor.DOCSCORE,
										jsonObject.getJSONObject("user")
												.getJSONObject("doctor")
												.getInt(Doctor.DOCSCORE));
								editor.putString(Doctor.EXPERTID,
										jsonObject.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(Doctor.EXPERTID));
								editor.putString(Doctor.DOCTID,
										jsonObject.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(Doctor.DOCTID));
								editor.putString(Doctor.DOCTSEX,
										jsonObject.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(Doctor.DOCTSEX));
								editor.putString(Doctor.DOCTPOSI,
										jsonObject.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(Doctor.DOCTPOSI));
								editor.putString(Doctor.DOCTHOSPNAME,
										jsonObject.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(Doctor.DOCTHOSPNAME));
								editor.putString(Doctor.DOCTDEPANAME,
										jsonObject.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(Doctor.DOCTDEPANAME));
								editor.putString(Doctor.DOCTNAME,
										jsonObject.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(Doctor.DOCTNAME));
								editor.putString(
										Doctor.DOCTSPECIALTY,
										jsonObject
												.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(Doctor.DOCTSPECIALTY));
								editor.putString(
										Doctor.DOCTINTRODUCTION,
										jsonObject
												.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(
														Doctor.DOCTINTRODUCTION));
								editor.putString(Doctor.DOCTHOSPNAME,
										jsonObject.getJSONObject("user")
												.getJSONObject("doctor")
												.getString(Doctor.DOCTHOSPNAME));
								editor.commit();
							}
							// 跳转到个人中心
							openActivity(PersonCenter.class);
							finish();
						}

					} else {
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

	private void initParams() {

		mHandler = new MyHandler();
		SharedPreferences sp = SharedPrefsUtil.getPreferences(this);
		if (null != sp) {
			// 存入数据
			doctName.setText(SharedPrefsUtil
					.getValue(this, Doctor.DOCTNAME, ""));
			sx = SharedPrefsUtil.getValue(this, Doctor.DOCTSEX, "");
			if (1 == Integer.parseInt(sx)) {
				tv_sex.setText("男");
			} else if (2 == Integer.parseInt(sx)) {
				tv_sex.setText("女");
			} else {
				tv_sex.setText("未知");
			}
			expert.setText(SharedPrefsUtil.getValue(this, Doctor.DOCTSPECIALTY,
					""));
			introduce.setText(SharedPrefsUtil.getValue(this,
					Doctor.DOCTINTRODUCTION, ""));
			doctId = SharedPrefsUtil.getValue(this, Doctor.DOCTID, "");
		}
	}

	private void initView() {
		doctName = (EditText) findViewById(R.id.doctName);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		expert = (EditText) findViewById(R.id.expert);
		introduce = (EditText) findViewById(R.id.introduce);
		Button back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		})	;
		Button btn_submit = (Button) findViewById(R.id.btn_submit);
		tv_sex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_sex.getText().toString().equals("男")) {
					tv_sex.setText("女");
					sx="2";
				} else {
					tv_sex.setText("男");
					sx="1";
				}

			}
		});
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean flag = true;
				if (doctName.getText().toString().length() < 0) {
					showToast("请输入医生姓名!");
					flag = false;
				}
				if (flag) {
					refreshDoctorInfo();
				}

			}
		});
	}
	// 个人中心部分初始化
	// private void initView() {
	//
	// //提交数据
	// Button btn_submit=(Button) findViewById(R.id.btn_submit);
	// btn_submit.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// boolean flag = true;
	// if(doctName.getText().toString().length() <0)
	// {
	// showToast("请输入医生姓名!");
	// flag = false;
	//
	// }
	//
	// if(flag)
	// {
	// Map map = new HashMap();
	// map.put("doctName", doctName);
	// // map.put("mobile", mobile);
	// map.put("sex", sex);
	// map.put("doctSpecialty", expert);
	// map.put("doctIntroduction", introduce);
	// map.put("doctId", doctId);
	//
	// String response = new ApiHttpUtil().postMethod(
	// Config.getProperty("UPDATE_DOCTOR",""),map);
	// try
	// {
	// JSONObject jsonObject = new JSONObject(response);
	// int code = jsonObject.getInt("code");
	// if(code==0)
	// {
	// SharedPreferences sp = getSharedPreferences("doctor", MODE_PRIVATE);
	// if(null != sp)
	// {
	// //存入数据
	// Editor editor = sp.edit();
	// editor.putString(Doctor.USERMOBLE,jsonObject.getJSONObject("user").getString(Doctor.USERMOBLE));
	// editor.putInt(Doctor.DOCSCORE,jsonObject.getJSONObject("user").getJSONObject("doctor").getInt(Doctor.DOCSCORE));
	// editor.putString(Doctor.EXPERTID,jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.EXPERTID));
	// editor.putString(Doctor.DOCTID,
	// jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTID));
	// editor.putString(Doctor.DOCTSEX,
	// jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTSEX));
	// editor.putString(Doctor.DOCTPOSI,
	// jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTPOSI));
	// editor.putString(Doctor.DOCTHOSPNAME,
	// jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTHOSPNAME));
	// editor.putString(Doctor.DOCTDEPANAME,
	// jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTDEPANAME));
	// editor.putString(Doctor.DOCTNAME,
	// jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTNAME));
	// editor.putString(Doctor.DOCTSPECIALTY,
	// jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTSPECIALTY));
	// editor.putString(Doctor.DOCTINTRODUCTION,
	// jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTINTRODUCTION));
	// editor.putString(Doctor.DOCTHOSPNAME,
	// jsonObject.getJSONObject("user").getJSONObject("doctor").getString(Doctor.DOCTHOSPNAME));
	// editor.commit();
	// }
	// //跳转到个人中心
	// openActivity(PersonCenter.class);
	// SysApplication.getInstance().exit();
	// }
	// else
	// {
	// showToast(jsonObject.getString("reason"));
	// }
	// }catch(Exception ex)
	// {
	// ex.printStackTrace();
	// }
	// }
	// }
	// });
	//
	//
	// }

}
