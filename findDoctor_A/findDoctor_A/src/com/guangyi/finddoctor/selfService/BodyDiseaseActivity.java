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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.adapter.DiseaseMaybeAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.Disease;
import com.guangyi.finddoctor.reflashView.PullToRefreshView;
import com.guangyi.finddoctor.reflashView.PullToRefreshView.OnFooterRefreshListener;
import com.guangyi.finddoctor.reflashView.PullToRefreshView.OnHeaderRefreshListener;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.DateTools;
/**
* <p>Title: 网络医院运营支撑平台-APP个人版</p>
* <p>Description:身体部位相关症状 </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:中国移动有限公司东莞分公司 </p>
* @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
* @version：1.0
* @since：2013-9-23
*/
public class BodyDiseaseActivity extends BasicActivity implements OnScrollListener{
	private ListView listView;
	private int pageNo = 1, pageSize = 10;
	private PullToRefreshView mPullToRefreshView;
	private String lableName;
	private Button ib_back;
	private TextView tv_topbar_center_text;
	private Handler mHandler;
	private ApiHttpUtil mApiHttpUtil;
	private Runnable mRunnable;
	private Thread mThread;
	private List<Disease> list ;
	private   DiseaseMaybeAdapter adapter;
	private int listFsize=0;
	private int mvisibleItemCount=0;
	
		
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
		setContentView(R.layout.disease_of_body);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initListener();
		initProgressDialog();
		getData();
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	}

	private void initParams() {
		lableName = this.getIntent().getExtras().getString("lableName");
		mHandler = new MyHandler();
		 list = new ArrayList<Disease>();
       adapter = new DiseaseMaybeAdapter(this, list);
		
	
		
		
	}

	private void initView() {
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.closeFootView();
		listView= (ListView) findViewById(R.id.mlist);
		listView.setOnScrollListener(this);
		ib_back = (Button) findViewById(R.id.ib_back);
		tv_topbar_center_text = (TextView) findViewById(R.id.tv_topbar_center_text);
		tv_topbar_center_text.setText(lableName);
		
		
		mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefresh());
		mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefresh());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Disease item = (Disease) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent(BodyDiseaseActivity.this,
						SelfServiceLibraryDieseaseHome.class);
				intent.putExtra("disease", item);
				startActivity(intent);

			}
		});

	}

	private void initListener() {
		ib_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});

	
	}
    //获取相关症状数据
	private void getData() {
		mRunnable=new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				mApiHttpUtil = new ApiHttpUtil();
				Map<String, String> params = new HashMap<String, String>();
				params.put("lableName", URLEncoder.encode(lableName));
				params.put("pageNo", pageNo+"");
				params.put("pageSize", pageSize+"");

				msg.arg1 = 1;
				msg.obj = mApiHttpUtil.postMethod(
						Config.getProperty("DISEASE_BODY_LABLE",""), params);

				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();
	}


	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			onLoaded();
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
							JSONArray jsonArray = jsonObject.getJSONArray("disList");
							
							Disease disease;
							listFsize=list.size();
							for (int i = 0; i < jsonArray.length(); i++) {
								disease = new Disease();
								disease.setAliases(jsonArray.getJSONObject(i)
										.getString(Disease.ALIASES));
								disease.setDescript(jsonArray.getJSONObject(i)
										.getString(Disease.DESCRIPT));
								disease.setDietary(jsonArray.getJSONObject(i)
										.getString(Disease.DIETARY));
								disease.setExercise(jsonArray.getJSONObject(i)
										.getString(Disease.EXERCISE));
								disease.setFamilyHistory(jsonArray.getJSONObject(i)
										.getString(Disease.FAMILYHISTORY));
								disease.setHdeptid(jsonArray.getJSONObject(i)
										.getString(Disease.HDEPTID));
								disease.setHdeptName(jsonArray.getJSONObject(i)
										.getString(Disease.HDEPTNAME));
								disease.setId(jsonArray.getJSONObject(i).getInt(
										Disease.ID));
								disease.setLaboratory(jsonArray.getJSONObject(i)
										.getString(Disease.LABORATORY));
								disease.setLocation(jsonArray.getJSONObject(i)
										.getString(Disease.LOCATION));
								disease.setMedication(jsonArray.getJSONObject(i)
										.getString(Disease.MEDICATION));
								disease.setName(jsonArray.getJSONObject(i)
										.getString(Disease.NAME).trim());
								disease.setOutline(jsonArray.getJSONObject(i)
										.getString(Disease.OUTLINE));
								disease.setPathogen(jsonArray.getJSONObject(i)
										.getString(Disease.PATHOGEN));
								disease.setPerson(jsonArray.getJSONObject(i).getString(
										Disease.PERSON));
								disease.setRehabilitation(jsonArray.getJSONObject(i)
										.getString(Disease.REHABILITATION));
								disease.setSymptoms(jsonArray.getJSONObject(i)
										.getString(Disease.SYMPTOMS));
								disease.setSymptomsLabel(jsonArray.getJSONObject(i)
										.getString(Disease.SYMPTOMSLABEL));
								// disease.setWeights(jsonArray.getJSONObject(i).get(Disease.WEIGHTS));

								list.add(disease);
							}
							
							if (list.size() > 0) {
								mPullToRefreshView.openFootView();
							}
							else
							{
								mPullToRefreshView.closeFootView();
							}
							
							View emptyView = LayoutInflater.from(BodyDiseaseActivity.this).inflate(R.layout.empty_view, null);
							emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
							emptyView.setVisibility(View.GONE);   
							((ViewGroup)listView.getParent()).addView(emptyView);   
							listView.setEmptyView(emptyView);
							adapter.notifyDataSetChanged();
							
							
							if(pageNo==1)
							{
							
								if (list.size() > listFsize) {
									pageNo++;
									
								}
							}
							
							else
							{ 
								if (list.size() > listFsize) {
									pageNo++;
									adapter.notifyDataSetChanged();
									listView.setSelection(listFsize-mvisibleItemCount+1);
								}
								else
								{
									mPullToRefreshView.stopCanFlash();
								}
							}
							
							

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					else if(msg.arg1==2)
					{
						
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
	
	
	
	private void onLoaded() {
		mPullToRefreshView.onHeaderRefreshComplete(getResources().getString(R.string.pull_to_refresh_pull_last_time)+DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView.onFooterRefreshComplete();
	}


	class OnHeaderRefresh implements OnHeaderRefreshListener
	{

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			getData();
			
		}
		
	}
	
	class OnFooterRefresh implements OnFooterRefreshListener
	{
		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			getData();
			
		}
		
	}



	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mvisibleItemCount=visibleItemCount;
		
	}
}
