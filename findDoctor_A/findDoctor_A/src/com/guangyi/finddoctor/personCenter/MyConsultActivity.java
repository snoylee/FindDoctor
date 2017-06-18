package com.guangyi.finddoctor.personCenter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.MyConsultAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.MyConsultModel;
import com.guangyi.finddoctor.onlineAsk.OnlineChatActivity;
import com.guangyi.finddoctor.secondActivity.TabHomeActivity;
import com.guangyi.finddoctor.utils.Config;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:我的咨询
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
public class MyConsultActivity extends BasicActivity {
	private ListView mListView;
	private Handler mHandler;
	private int userId = -1;
	private SharedPreferences mSharedPreferences;
	private MyConsultAdapter myConsultAdapter;
	private List<MyConsultModel> listMyConsult;
	private Runnable mRunnable;
	private Thread mThread;
	private RadioGroup mRadioGroup;
		
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
		setContentView(R.layout.my_consult);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initListener();
		
	}

	@Override
	protected void onStart() {
		getData();
		super.onStart();
	}

	private void initParams() {
		initMySharedPreference();
		mHandler = new MyHandler();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_consult);
		listMyConsult=new ArrayList<MyConsultModel>();
		myConsultAdapter = new MyConsultAdapter(this, listMyConsult);
		mListView.setAdapter(myConsultAdapter);
		Button mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(TabHomeActivity.class,3);
				finish();

			}
		});
		RadioButton mRadioButtonHome, mRadioButtonMessage, mRadioButtonUser,mRadioButtonSearch;
		mRadioGroup = (RadioGroup) findViewById(R.id.radio_group_main);
		mRadioButtonHome = (RadioButton) findViewById(R.id.radio_home);
		mRadioButtonMessage = (RadioButton) findViewById(R.id.radio_message);
		mRadioButtonUser = (RadioButton) findViewById(R.id.radio_user);
		mRadioButtonSearch = (RadioButton) findViewById(R.id.radio_search);
		mRadioButtonSearch.setChecked(true);
		mRadioGroup
		.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if (checkedId == R.id.radio_home) {
					openActivity(TabHomeActivity.class,0);
				} else if (checkedId == R.id.radio_message) {
					openActivity(TabHomeActivity.class,1);
				} else if (checkedId == R.id.radio_user) {
					openActivity(TabHomeActivity.class,2);
				} else if (checkedId == R.id.radio_search) {
					openActivity(TabHomeActivity.class,3);
				} else {
				}
			}
		});
	}

	private void initListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MyConsultModel item = (MyConsultModel) arg0
						.getItemAtPosition(arg2);
				Intent intent = new Intent(MyConsultActivity.this,
						OnlineChatActivity.class);
				if(item.getConsState()==2||item.getCloseStatus()==1)
				{
					intent.putExtra("is_bottom_visible", 0);
				}
				intent.putExtra("myConsultModel",item);
				intent.putExtra("consId", item.getConsId() + "");
//				intent.putExtra("assessType", 4);
//				intent.putExtra("consId", mList.get(position).getConsId());
//				intent.putExtra("docIhttp://92.168.1.112:8080/nda/services/cancelConsultaion/{consId}d", mList.get(position).getConsDoctorId());
//			     mContext.startActivity(intent);
				Log.i("myConsId", item.getConsId() + "");

				startActivity(intent);

			}
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				MyConsultModel item = (MyConsultModel) arg0
						.getItemAtPosition(arg2);
				delDialog(item.getConsId(),arg2);
				return false;
			}
		});
	}
	private void delDialog(final int id,final int position) {
		new AlertDialog.Builder(MyConsultActivity.this).setTitle("删除咨询")
				.setMessage("是否删除？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						delMyConsult(id,position);
					}
				}).setNegativeButton("取消", null).show();
	}

	private void delMyConsult(final int id,final int position) {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.arg1=2;
				msg.arg2=position;
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(id));
				msg.obj = new ApiHttpUtil().getMethod(
						Config.getProperty("CANCELCONSULTAION", ""), list);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}
	private void initMySharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		userId = mSharedPreferences.getInt("userId", 0);

	}

	// 获取我的咨询信息
	private void getData() {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				new ApiHttpUtil();

				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(userId));
				list.add(String.valueOf(1));
				list.add(String.valueOf(10));
				msg.arg1 = 1;
				msg.obj = ApiHttpUtil.getMethod(
						Config.getProperty("GET_MY_CONSULT", ""), list);
				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			openActivity(TabHomeActivity.class,3);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

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
					e1.printStackTrace();
				}
				if(code==0)
				{
					
					if(msg.arg1==1)
					{
						try {
							JSONArray jsonArr = jsonObject
									.getJSONArray("myConsultingList");
							if(listMyConsult.size()>0)
							{
								listMyConsult.clear();
							}
							MyConsultModel model;
							for (int i = 0; i < jsonArr.length(); i++) {
								model = new MyConsultModel();
								model.setCloseStatus(jsonArr.getJSONObject(i).getInt("closeStatus"));
								model.setConsId(jsonArr.getJSONObject(i)
										.getInt("consId"));
								model.setConsTitle(jsonArr.getJSONObject(i)
										.getString("consTitle"));
								model.setConsState(jsonArr.getJSONObject(i)
										.getInt("consState"));
								model.setConsProblem(jsonArr.getJSONObject(i)
										.getString("consProblem"));
								model.setConsTime(jsonArr.getJSONObject(i)
										.getString("consTime"));
								model.setConsDoctorId(jsonArr.getJSONObject(i)
										.getInt("consDoctorId"));
								model.setCommContent(jsonArr.getJSONObject(i)
										.getString("commContent"));
								model.setCommId(jsonArr.getJSONObject(i)
										.getInt("commId"));
								model.setCommTime(jsonArr.getJSONObject(i)
										.getString("commTime"));
								model.setConsHospitalName(jsonArr
										.getJSONObject(i).getString(
												"consHospitalName"));
								model.setConsReplyProblem(jsonArr
										.getJSONObject(i).getString(
												"consReplyProblem"));
								model.setConsReplyTime(jsonArr.getJSONObject(i)
										.getString("consReplyTime"));
								model.setConsType(jsonArr.getJSONObject(i)
										.getInt("consType"));
								model.setConsUserName(jsonArr.getJSONObject(i)
										.getString("consUserName"));
								model.setConsUserReply(jsonArr.getJSONObject(i)
										.getString("consUserReply"));
								model.setEvaluateContext(jsonArr.getJSONObject(
										i).getString("evaluateContext"));
								model.setEvaluateId(jsonArr.getJSONObject(i)
										.getString("evaluateId"));
								model.setEvaluateTime(jsonArr.getJSONObject(i)
										.getString("evaluateTime"));
								model.setExpertName(jsonArr.getJSONObject(i)
										.getString("expertName"));
								model.setIsDoctorState(jsonArr.getJSONObject(i)
										.getInt(MyConsultModel.ISDOCTORSTATE));
								model.setReadCount(jsonArr.getJSONObject(i)
										.getInt("readCount"));
								listMyConsult.add(model);
							}
								
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						finally
						{
							View emptyView = LayoutInflater.from(MyConsultActivity.this).inflate(R.layout.empty_view, null);
							emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
							emptyView.setVisibility(View.GONE);   
							((ViewGroup)mListView.getParent()).addView(emptyView);   
							mListView.setEmptyView(emptyView);
							myConsultAdapter.notifyDataSetChanged();
						}
					}
					if(msg.arg1==2)
					{
//						getData();
						listMyConsult.remove(msg.arg2);
						myConsultAdapter.notifyDataSetChanged();
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
