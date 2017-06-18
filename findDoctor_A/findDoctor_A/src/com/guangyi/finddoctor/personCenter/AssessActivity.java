package com.guangyi.finddoctor.personCenter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.onlineAsk.OnlineChatActivity;
import com.guangyi.finddoctor.utils.Config;


public class AssessActivity extends BasicActivity {
	private RatingBar ratingBar_speed, ratingBar_manner, ratingBar_profession;
	private TextView tv_speed_score, tv_manner_score, tv_profession_score;
	private EditText et_assess_words;
	private int appoId=0,consId = 0, docId = 0, speedScore = 0, mannerScore = 0,
			professionScore = 0;
	private String message = "";
	private Button ib_back, ib_post;
	private ApiHttpUtil mApiHttpUtil;
	private Handler mHandler;
	private SharedPreferences mSharedPreferences;
	private int userId = -1;
	private int type = 0;
	private int flagType = -1;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assess);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		bindData();
		initListener();
	}

	private void initParams() {
		initMySharedPreference();
		type = this.getIntent().getExtras().getInt("assessType");
		consId = this.getIntent().getExtras().getInt("consId");
		appoId = this.getIntent().getExtras().getInt("appoId");
		docId = this.getIntent().getExtras().getInt("docId");
		flagType = this.getIntent().getExtras().getInt("flagType");
		mHandler = new MyHandler();

	}

	private void initMySharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		userId = mSharedPreferences.getInt("userId", 0);

	}

	private void initView() {
		ratingBar_speed = (RatingBar) findViewById(R.id.ratingBar_speed);
		ratingBar_manner = (RatingBar) findViewById(R.id.RatingBar_manner);
		ratingBar_profession = (RatingBar) findViewById(R.id.RatingBar_profession);
		tv_speed_score = (TextView) findViewById(R.id.tv_speed_score);
		tv_manner_score = (TextView) findViewById(R.id.tv_manner_score);
		tv_profession_score = (TextView) findViewById(R.id.tv_profession_score);
		et_assess_words = (EditText) findViewById(R.id.et_assess_words);
		ib_back = (Button) findViewById(R.id.ib_back);
		ib_post =  (Button) findViewById(R.id.ib_post);
	}

	private void bindData() {
		ratingBar_speed
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						speedScore = (int) (rating * 2);
						tv_speed_score.setText(String.valueOf(speedScore));

					}
				});
		ratingBar_manner
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						mannerScore = (int) (rating * 2);
						tv_manner_score.setText(String.valueOf(mannerScore));

					}
				});
		ratingBar_profession
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						professionScore = (int) (rating * 2);
						tv_profession_score.setText(String
								.valueOf(professionScore));

					}
				});

		

	}

	private void initListener() {
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				if(flagType == 1){
//					Intent intent = new Intent(AssessActivity.this,MyRegisterActivity.class);
//					intent.putExtra("flagtype", 1);
//					startActivity(intent);
//				}else{
//					finish();
//				}
				
				finish();

			}
		});

		ib_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				message = et_assess_words.getText().toString().trim();
				postData();

			}
		});
	}
   //提交评价数据
	private void postData() {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				mApiHttpUtil = new ApiHttpUtil();

				Map<String, String> params = new HashMap<String, String>();
				params.put("transactionId", String.valueOf(System.currentTimeMillis()));
				params.put("docId", String.valueOf(docId));
				params.put("userId", String.valueOf(userId));
				params.put("service", String.valueOf(Math.round(mannerScore/2)));
				params.put("skill", String.valueOf(Math.round(professionScore/2)));
				params.put("message", message);
				params.put("type", String.valueOf(type));
				params.put("consId", String.valueOf(consId));
				params.put("efficiency",String.valueOf(Math.round(speedScore/2)));
				params.put("score", String.valueOf(0));
				params.put("appoId", String.valueOf(appoId));

				msg.arg1 = 1;
				msg.obj = mApiHttpUtil.postMethod(Config.getProperty("ASSESS",""),
						params);

				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();
				
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_BACK){
//			if(flagType == 1){
//				Intent intent = new Intent(AssessActivity.this,MyRegisterActivity.class);
//				intent.putExtra("flagtype", 1);
//				startActivity(intent);
//			}else{
//				finish();
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	
	private void backResultActivity() {
		Intent intent=new Intent();
		intent.setClass(this, OnlineChatActivity.class);
		setResult(ResultCodeCategory.RESULT_CODE_EVA, intent);
		this.finish();
	}

	class MyHandler extends Handler {
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
						int sendType=getDataSf().getInt(getResources().getString(R.string.sendType), -1);
						if(sendType==0)
						{
							
						}
						else if(sendType==1)
						{
							showToast("点评内容需要审核通过后才能发布!");

						}
						
						if(flagType == 1||flagType == 0){
							Intent intent = new Intent(AssessActivity.this,MyRegisterActivity.class);
							intent.putExtra("flagtype", flagType);
							startActivity(intent);
						}else if(flagType == 2){
							
//							backResultActivity();
							openActivity(MyConsultActivity.class);
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
}
