package com.guangyi.finddoctor.selfService;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.adapter.BodySymptomAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.BodyDisease;
import com.guangyi.finddoctor.utils.Config;

/**
* <p>Title: 网络医院运营支撑平台-APP个人版</p>
* <p>Description:身体部位症状</p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:中国移动有限公司东莞分公司 </p>
* @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
* @version：1.0
* @since：2013-9-23
*/
public class BodySymptomActivity extends BasicActivity {


	private Button ib_back;
	private TextView tv_topbar_center_text;
	private ListView lv_body_symptom;
	String sex;
	String bodyname;
	private Handler mHandler;
	private ApiHttpUtil mApiHttpUtil;
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
		setContentView(R.layout.body_symptom);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initListener();
		getData();
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	}
	
	private void initParams() {
		bodyname = this.getIntent().getExtras().getString("bodyname");
		sex = this.getIntent().getExtras().getString("sex");
		mHandler = new MyHandler();
		mApiHttpUtil = new ApiHttpUtil();

	}

	private void initView() {
		ib_back = (Button) findViewById(R.id.ib_back);
		tv_topbar_center_text = (TextView) findViewById(R.id.tv_topbar_center_text);
		lv_body_symptom = (ListView) findViewById(R.id.lv_body_symptom);
		tv_topbar_center_text.setText(bodyname);
	}
	//获取部位相关症状
	private void getData() {
		initProgressDialog();
		mRunnable=new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				Map<String, String> params = new HashMap<String, String>();
				params.put("body", URLEncoder.encode(bodyname));
				params.put("sex", URLEncoder.encode(sex));
				msg.arg1 = 1;
				msg.obj = mApiHttpUtil.postMethod(Config.getProperty("DISEASE_BODY_STRING",""), params);
				mHandler.sendMessage(msg);
				
				
			}
		};
		commonThreadStart();
		
	}

	private void getView(List<BodyDisease> list) {
		BodySymptomAdapter adapter =new BodySymptomAdapter(this, list);
		
		lv_body_symptom.setAdapter(adapter);
		
		
	}
	private void initListener() {
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		
		lv_body_symptom.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BodyDisease item = (BodyDisease) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent(BodySymptomActivity.this,BodyDiseaseActivity.class);
				intent.putExtra("lableName", item.getLableName());
				startActivity(intent);
				
			}
		});

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
						try {
							JSONArray jsonArr = jsonObject.getJSONArray("diseBody");
							List<BodyDisease> _List = new ArrayList<BodyDisease>();
							BodyDisease model ;
							for(int i = 0; i < jsonArr.length(); i++){
								model = new BodyDisease();
								model.setId(jsonArr.getJSONObject(i).getInt("id"));
								model.setDisId(jsonArr.getJSONObject(i).getInt("disId"));
								model.setBody(jsonArr.getJSONObject(i).getString("body"));
								model.setLableName(jsonArr.getJSONObject(i).getString("lableName"));
								_List.add(model);
							}
							
							getView(_List);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
//					else if(msg.arg1==2)
//					{
//						
//					}
						
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
