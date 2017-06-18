package com.guangyi.finddoctor.selfService;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.BodyAdapter;
import com.guangyi.finddoctor.adapter.BodyDiseaseAdapter4Wenzi;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.BodyDisease;
import com.guangyi.finddoctor.utils.Config;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:症状自查-文字模式
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
public class SelfExaminationBywords extends BasicActivity {

	private ListView lv_parent, lv_child;
	private List<Map<String, String>> bodyList;
	private String body = null;
	private Handler mHandler;
	private ApiHttpUtil mApiHttpUtil;
	private Button ib_model_change, ib_back;
	private EditText et_disease_search;
	private BodyAdapter bodyAdapter;
	private Runnable mRunnable;
	private Thread mThread;

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

	private void initSearch() {
		et_disease_search = (EditText) findViewById(R.id.et_diease_search);
		ImageView iv_search_btn = (ImageView) findViewById(R.id.iv_search_btn);
		iv_search_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
				//得到InputMethodManager的实例 
				if (imm.isActive()) { 
				//如果开启 
				imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); 
				//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的 
				} 
				
				if(et_disease_search.getText().toString().trim().length()>0)
				{
//					geSearchtData();
					if(isNetworkConnected())
					{
					Intent it=new Intent(SelfExaminationBywords.this,SelfServiceSymtomSearch.class);
					it.putExtra("word", URLEncoder.encode(et_disease_search
							.getText().toString().trim()));
					startActivity(it);
					}
				}
				else
				{
					showToast("搜索内容不能为空!");
				}
				
				
			}
		});
	}

	private void geSearchtData() {
		initProgressDialog();
		mRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				Map<String, String> params = new HashMap<String, String>();
				params.put("word", URLEncoder.encode(et_disease_search
						.getText().toString()));
				params.put("pageNo", "1");
				params.put("pageSize", "1000");
				msg.arg1 = 2;
				msg.obj = new ApiHttpUtil().postMethod(
						Config.getProperty("SEARCHLABEL", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_examination_bywords);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initData();
		initListener();
		initSearch();
		
		//默认头部数据
		bodyAdapter.setSelectItem(0);
		bodyAdapter.notifyDataSetInvalidated();
		getData("头部");
	}

	private void initParams() {
		mHandler = new MyHandler();
	}

	private void initView() {
		lv_parent = (ListView) findViewById(R.id.lv_parent);
		lv_child = (ListView) findViewById(R.id.lv_child);
		ib_back = (Button) findViewById(R.id.ib_back);
		ib_model_change = (Button) findViewById(R.id.ib_model_change);
	}

	private void getView(List<BodyDisease> list) {
		BodyDiseaseAdapter4Wenzi adapter = new BodyDiseaseAdapter4Wenzi(this, list);
		lv_child.setAdapter(adapter);
	}

	private void initData() {
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		bodyList = new ArrayList<Map<String, String>>();
		Map<String, String> item1 = new HashMap<String, String>();
		item1.put("bodyname", "头部");
		bodyList.add(item1);
		Map<String, String> item2 = new HashMap<String, String>();
		item2.put("bodyname", "耳眼口鼻");
		bodyList.add(item2);
		Map<String, String> item3 = new HashMap<String, String>();
		item3.put("bodyname", "皮肤");
		bodyList.add(item3);
		Map<String, String> item4 = new HashMap<String, String>();
		item4.put("bodyname", "颈部");
		bodyList.add(item4);
		Map<String, String> item5 = new HashMap<String, String>();
		item5.put("bodyname", "胸部");
		bodyList.add(item5);
		Map<String, String> item6 = new HashMap<String, String>();
		item6.put("bodyname", "腹部");
		bodyList.add(item6);
		Map<String, String> item7 = new HashMap<String, String>();
		item7.put("bodyname", "背部");
		bodyList.add(item7);
		Map<String, String> item8 = new HashMap<String, String>();
		item8.put("bodyname", "四肢");
		bodyList.add(item8);
		Map<String, String> item9 = new HashMap<String, String>();
		item9.put("bodyname", "上肢");
		bodyList.add(item9);
		Map<String, String> item10 = new HashMap<String, String>();
		item10.put("bodyname", "下肢");
		bodyList.add(item10);
		Map<String, String> item11 = new HashMap<String, String>();
		item11.put("bodyname", "生殖器");
		bodyList.add(item11);
		Map<String, String> item12 = new HashMap<String, String>();
		item12.put("bodyname", "排泄部");
		bodyList.add(item12);
		Map<String, String> item13 = new HashMap<String, String>();
		item13.put("bodyname", "其他");
		bodyList.add(item13);
//		Map<String, String> item14 = new HashMap<String, String>();
//		item14.put("bodyname", "");
//		bodyList.add(item14);
//
//		Map<String, String> item15 = new HashMap<String, String>();
//		item15.put("bodyname", "");
//		bodyList.add(item15);
//		Map<String, String> item16 = new HashMap<String, String>();
//		item16.put("bodyname", "");
//		bodyList.add(item16);
		// lv_parent.setAdapter(new SimpleAdapter(SelfExaminationBywords.this,
		// bodyList, R.layout.item_parent, new String[] { "bodyname" },
		// new int[] { R.id.tv_body_name }));
		bodyAdapter = new BodyAdapter(this, bodyList);
		lv_parent.setAdapter(bodyAdapter);

	}

	private void initListener() {

		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		ib_model_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelfExaminationBywords.this,
						SelfExaminationActivity.class);
				startActivity(intent);

			}
		});

		lv_parent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				bodyAdapter.setSelectItem(arg2);
				bodyAdapter.notifyDataSetInvalidated();
				Map<String, String> item = (Map<String, String>) arg0
						.getItemAtPosition(arg2);
				body = item.get("bodyname");
				getData(body);

			}
		});

		lv_child.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BodyDisease item = (BodyDisease) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent(SelfExaminationBywords.this,
						BodyDiseaseActivity.class);
				intent.putExtra("lableName", item.getLableName());
				startActivity(intent);

			}
		});
	}

	private void getData(final String bodyname) {
		mRunnable = new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				mApiHttpUtil = new ApiHttpUtil();

				Map<String, String> params = new HashMap<String, String>();
				params.put("body", URLEncoder.encode(bodyname));
				params.put("sex", "-1");
				msg.arg1 = 1;
				msg.obj = mApiHttpUtil.postMethod(
						Config.getProperty("DISEASE_BODY_STRING", ""), params);

				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();
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
							JSONArray jsonArr = jsonObject
									.getJSONArray("diseBody");
							List<BodyDisease> _List = new ArrayList<BodyDisease>();
							BodyDisease model;
							for (int i = 0; i < jsonArr.length(); i++) {
								model = new BodyDisease();
								model.setId(jsonArr.getJSONObject(i).getInt(
										"id"));
								model.setDisId(jsonArr.getJSONObject(i).getInt(
										"disId"));
								model.setBody(jsonArr.getJSONObject(i)
										.getString("body"));
								model.setLableName(jsonArr.getJSONObject(i)
										.getString("lableName"));
								_List.add(model);
							}

							getView(_List);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					else if (msg.arg1 == 2) {
						try {
							JSONArray jsonArr = jsonObject
									.getJSONArray("labelNames");
							List<BodyDisease> _List = new ArrayList<BodyDisease>();
							BodyDisease model;
							for (int i = 0; i < jsonArr.length(); i++) {
								model = new BodyDisease();
								model.setId(jsonArr.getJSONObject(i).getInt(
										"id"));
								model.setDisId(jsonArr.getJSONObject(i).getInt(
										"disId"));
								model.setBody(jsonArr.getJSONObject(i)
										.getString("body"));
								model.setLableName(jsonArr.getJSONObject(i)
										.getString("lableName"));
								_List.add(model);
							}
							// listBodyDisease
							Intent it = new Intent(SelfExaminationBywords.this,
									SelfServiceSymtomSearch.class);
							it.putExtra("listBodyDisease", (Serializable) _List);
							startActivity(it);
							// getSearchView(_List);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				} else {
					showToast(reason);
				}

			}

			super.handleMessage(msg);

		}
	}
}
