package com.guangyi.finddoctor.personCenter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.utils.Config;
/**
* <p>Title: 网络医院运营支撑平台-APP个人版</p>
* <p>Description:意见反馈 </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:中国移动有限公司东莞分公司 </p>
* @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
* @version：1.0
* @since：2013-9-23
*/
public class SuggestActivity extends BasicActivity {
	
	class MyHandler extends Handler{
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
						showToast(reason);
						finish();
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
	private Button ib_back, ib_post;
	private TextView tv_fraction;
	private EditText et_message,et_phone;
	private int fraction;
	private String message = "";
	private String phone = "";
	private String fractionText;
	
	private Handler mHandler;
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
	private void initData() {
	    String fractionStr = tv_fraction.getText().toString();
	    if(fractionStr.equals("非常满意")){
	    	fraction = 5;
	    }else if(fractionStr.equals("满意")){
	    	fraction = 4;
	    }else if(fractionStr.equals("一般")){
	    	fraction = 3;
	    }else if(fractionStr.equals("不满意")){
	    	fraction = 2;
	    }else if(fractionStr.equals("非常不满意")){
	    	fraction = 1;
	    }
		message = et_message.getText().toString();
		if(et_phone.getText().toString().trim().length()!=11){
			showToast("手机号码格式不正确!");
			return;
		}else if(et_message.getText().toString().trim().length()<=0){
			showToast("反馈内容不能为空!");
			
		}
		else
		{
			phone = et_phone.getText().toString().trim();
			postData();
		}
		
		
	}


	private void initListener(){
		tv_fraction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SuggestActivity.this,SuggestFraction.class);
				startActivityForResult(intent, 0);
				
			}
		});
		
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		
		ib_post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initData();
				
				
			}
		
		});
	}
	
	private void initParams(){
		mHandler = new MyHandler();
	}
	private void initView() {
		ib_back = (Button) findViewById(R.id.ib_back);
		ib_post = (Button) findViewById(R.id.ib_post);
		tv_fraction = (TextView) findViewById(R.id.tv_fraction);
		et_message = (EditText) findViewById(R.id.et_message);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setText(getDataSf().getString("userMobile", ""));
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == ResultCodeCategory.RESULT_CODE_FRCTION){
			fractionText = data.getExtras().getString("fraction");
			tv_fraction.setText(fractionText);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggest_activity);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initListener();
	}

private void postData() {
	mRunnable=new Runnable() {
		
		@Override
		public void run() {
			Message msg = new Message();
			Map<String, String> param = new HashMap<String, String>();
			param.put("message", message);
			param.put("phone", phone);
			param.put("fraction", String.valueOf(fraction));
			msg.arg1 = 1;
			msg.obj = new ApiHttpUtil().postMethod(Config.getProperty("SENDFEEDBACK",""), param);
			mHandler.sendMessage(msg);
			
			
		}
	};
	commonThreadStart();
	
}
}
