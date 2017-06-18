package com.guangyi.finddoctor.selfService;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.BodyDiseaseAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.BodyDisease;
import com.guangyi.finddoctor.reflashView.PullToRefreshView;
import com.guangyi.finddoctor.reflashView.PullToRefreshView.OnFooterRefreshListener;
import com.guangyi.finddoctor.reflashView.PullToRefreshView.OnHeaderRefreshListener;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.DateTools;

public class SelfServiceSymtomSearch extends BasicActivity implements OnScrollListener
{
	private ListView listView;
	private int pageNo = 1, pageSize = 10;
	private PullToRefreshView mPullToRefreshView;
	private Button ib_back;
	private TextView tv_topbar_center_text;
	private Handler mHandler;
	private ApiHttpUtil mApiHttpUtil;
	private Runnable mRunnable;
	private Thread mThread;
	private List<BodyDisease> list ;
	private BodyDiseaseAdapter  adapter;
	private int listFsize=0;
	private String word;
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
		setContentView(R.layout.symtom_search);
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
		word = this.getIntent().getExtras().getString("word");
		mHandler = new MyHandler();
		 list = new ArrayList<BodyDisease>();
       adapter = new BodyDiseaseAdapter (this, list);
	}

	private void initView() {
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.closeFootView();
		listView= (ListView) findViewById(R.id.mlist);
		listView.setOnScrollListener(this);
		ib_back = (Button) findViewById(R.id.ib_back);
		
		
		mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefresh());
		mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefresh());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BodyDisease item = (BodyDisease) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent(SelfServiceSymtomSearch.this,
						BodyDiseaseActivity.class);
				intent.putExtra("lableName", item.getLableName());
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
	private void getData() {
	    mRunnable = new Runnable() {

		@Override
		public void run() {
			Message msg = new Message();
			Map<String, String> params = new HashMap<String, String>();
			params.put("word", word);
			params.put("pageNo", pageNo+"");
			params.put("pageSize", pageSize+"");
			msg.arg1 = 1;
			msg.obj = new ApiHttpUtil().postMethod(
					Config.getProperty("SEARCHLABEL", ""), params);
			mHandler.sendMessage(msg);

		}
	};
	commonThreadStart();}


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
							listFsize=list.size();
							JSONArray jsonArr = jsonObject
									.getJSONArray("labelNames");
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
								list.add(model);
							}
							if (list.size() > 0) {
								mPullToRefreshView.openFootView();
							}
							else
							{
								mPullToRefreshView.closeFootView();
							}
							
							View emptyView = LayoutInflater.from(SelfServiceSymtomSearch.this).inflate(R.layout.empty_view, null);
							ImageView imageView1=(ImageView) emptyView.findViewById(R.id.imageView1);
							TextView textView1=(TextView) emptyView.findViewById(R.id.textView1);
							imageView1.setVisibility(View.GONE);
							textView1.setText("未搜索到相关数据");
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
							
							

							// getSearchView(_List);
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
