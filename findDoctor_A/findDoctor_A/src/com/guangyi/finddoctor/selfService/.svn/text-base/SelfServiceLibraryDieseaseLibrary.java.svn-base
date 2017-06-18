package com.guangyi.finddoctor.selfService;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.LibraryDiseaseAdapter;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Disease;
import com.guangyi.finddoctor.reflashView.PullToRefreshView;
import com.guangyi.finddoctor.reflashView.PullToRefreshView.OnFooterRefreshListener;
import com.guangyi.finddoctor.reflashView.PullToRefreshView.OnHeaderRefreshListener;
import com.guangyi.finddoctor.sortletter.SideBar;
import com.guangyi.finddoctor.sortletter.SideBar.OnTouchingLetterChangedListener;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.DateTools;

public class SelfServiceLibraryDieseaseLibrary extends BasicActivity implements OnScrollListener{

	private Runnable mRunnable;
	private Thread mThread;
	private Handler mHandler;
	private List<Disease> listData;
	private LibraryDiseaseAdapter adapter;
	private int pageNo = 1, pageSize = 20;
	private EditText editText1;
	private ListView listView;
	private PullToRefreshView mPullToRefreshView;
	private SideBar sideBar;
	private TextView dialog;
	private int listFsize=0;
	private int mvisibleItemCount=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_main);
		initParams();
		initViews();
		initProgressDialog();
		getData();

	}

	private void initViews() {

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		listView = (ListView) findViewById(R.id.mlist);
		listView.setOnScrollListener(this);
		editText1 = (EditText) findViewById(R.id.editText1);
		ImageView iv_search_btn = (ImageView) findViewById(R.id.iv_search_btn);

		iv_search_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				// 得到InputMethodManager的实例
				if (imm.isActive()) {
					// 如果开启
					imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
							InputMethodManager.HIDE_NOT_ALWAYS);
					// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
				}
				if (editText1.getText().toString().trim().length() > 0) {
					if(isNetworkConnected())
					{
					searchData();
					}
				} else {
					showToast("搜索内容不能为空!");
				}
			}
		});

		Button Back = (Button) findViewById(R.id.ib_back);

		Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					listView.setSelection(position);
				}

			}
		});
		initListView(1);

	}

	private void initParams() {
		mHandler = new MyHandler();
		listData = new ArrayList<Disease>();
		adapter = new LibraryDiseaseAdapter(this, listData);
	}

	private void initListView(int from) {
		mPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefresh(from));
		mPullToRefreshView
				.setOnFooterRefreshListener(new OnFooterRefresh(from));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent();
				it.putExtra("disease", listData.get(arg2));
				it.setClass(SelfServiceLibraryDieseaseLibrary.this,
						SelfServiceLibraryDieseaseHome.class);
				startActivity(it);

			}
		});

	}

	private void onLoaded() {
		mPullToRefreshView.onHeaderRefreshComplete(getResources().getString(
				R.string.pull_to_refresh_pull_last_time)
				+ DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView.onFooterRefreshComplete();
	}

	class OnHeaderRefresh implements OnHeaderRefreshListener {
		private int from;
		public OnHeaderRefresh(int from) {
			this.from = from;
		}
		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			if (from == 1) {
				getData();
			} else if (from == 2) {
				mRunnable = new Runnable() {
					@Override
					public void run() {
						Message msg = new Message();
						msg.arg1 = 2;
						Map<String, String> params = new HashMap<String, String>();
						params.put("word", URLEncoder.encode(editText1
								.getText().toString()));
						params.put("pageNo", pageNo + "");
						params.put("pageSize", pageSize + "");
						msg.obj = new ApiHttpUtil()
								.postMethod(
										Config.getProperty("SEARCHDISEASE", ""),
										params);
						mHandler.sendMessage(msg);
					}
				};
				commonThreadStart();
			}
		}

	}

	class OnFooterRefresh implements OnFooterRefreshListener {
		private int from;

		public OnFooterRefresh(int from) {
			this.from = from;
		}

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			if (from == 1) {
				getData();
			} else if (from == 2) {
				mRunnable = new Runnable() {
					@Override
					public void run() {
						Message msg = new Message();
						msg.arg1 = 2;
						Map<String, String> params = new HashMap<String, String>();
						params.put("word", URLEncoder.encode(editText1
								.getText().toString()));
						params.put("pageNo", pageNo + "");
						params.put("pageSize", pageSize + "");
						msg.obj = new ApiHttpUtil()
								.postMethod(
										Config.getProperty("SEARCHDISEASE", ""),
										params);
						mHandler.sendMessage(msg);
					}
				};
				commonThreadStart();
			}
		}

	}

	private void getData() {
		mRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.arg1 = 1;
				Map<String, String> params = new HashMap<String, String>();
				params.put("word", "");
				params.put("type", "4");
				params.put("pageNo", pageNo + "");
				params.put("pageSize", pageSize + "");
				msg.obj = new ApiHttpUtil().postMethod(
						Config.getProperty("SEARCH", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}

	private void searchData() {
		pageNo = 1;
		listFsize=0;
		if (listData != null) {
			listData.clear();
			adapter.notifyDataSetChanged();
		}
		initListView(2);
		mRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.arg1 = 2;
				Map<String, String> params = new HashMap<String, String>();
				params.put("word",
						URLEncoder.encode(editText1.getText().toString()));
				params.put("pageNo", pageNo + "");
				params.put("pageSize", pageSize + "");
				msg.obj = new ApiHttpUtil().postMethod(
						Config.getProperty("SEARCHDISEASE", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		initProgressDialog();
		commonThreadStart();

	}

	private void commonThreadStart() {
		if (mRunnable != null) {
			mThread = new Thread(mRunnable);
			mThread.start();
		}
	}

	protected void onStop() {
		mHandler.removeCallbacks(mRunnable);
		// if(mThread.isAlive())
		// {
		// mThread.interrupt();
		// }
		super.onStop();
	}

	class MyHandler extends Handler {
		@SuppressLint("DefaultLocale")
		@Override
		public void handleMessage(Message msg) {
			onLoaded();
			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.i("disease", jsonString);
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
							listFsize=listData.size();
							Disease disease;
							JSONArray jsonArray = jsonObject.getJSONObject(
									"pageSearch").getJSONArray("disease");

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
								disease.setFamilyHistory(jsonArray
										.getJSONObject(i).getString(
												Disease.FAMILYHISTORY));
								disease.setHdeptid(jsonArray.getJSONObject(i)
										.getString(Disease.HDEPTID));
								disease.setHdeptName(jsonArray.getJSONObject(i)
										.getString(Disease.HDEPTNAME));
								disease.setId(jsonArray.getJSONObject(i)
										.getInt(Disease.ID));
								disease.setLaboratory(jsonArray
										.getJSONObject(i).getString(
												Disease.LABORATORY));
								disease.setLocation(jsonArray.getJSONObject(i)
										.getString(Disease.LOCATION));
								disease.setMedication(jsonArray
										.getJSONObject(i).getString(
												Disease.MEDICATION));
								disease.setName(jsonArray.getJSONObject(i)
										.getString(Disease.NAME).trim());
								disease.setOutline(jsonArray.getJSONObject(i)
										.getString(Disease.OUTLINE));
								disease.setPathogen(jsonArray.getJSONObject(i)
										.getString(Disease.PATHOGEN));
								disease.setPerson(jsonArray.getJSONObject(i)
										.getString(Disease.PERSON));
								disease.setRehabilitation(jsonArray
										.getJSONObject(i).getString(
												Disease.REHABILITATION));
								disease.setSymptoms(jsonArray.getJSONObject(i)
										.getString(Disease.SYMPTOMS));
								disease.setSymptomsLabel(jsonArray
										.getJSONObject(i).getString(
												Disease.SYMPTOMSLABEL));
								if (jsonArray.getJSONObject(i)
										.getString("pinYin").length() > 0) {

									disease.setIndex(getfirstLetter(jsonArray.getJSONObject(i)
											.getString("pinYin")));
								} else {
									disease.setIndex("#");
								}

								listData.add(disease);
							}
                             adapter.notifyDataSetChanged();
							
                         	
								if(pageNo==1)
								{
								
									if (listData.size() > listFsize) {
										pageNo++;
										
									}
								}
								
								else
								{
							if (listData.size() > listFsize) {
								pageNo++;
								listView.setSelection(listFsize-mvisibleItemCount+2);
							}
								}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					else if (msg.arg1 == 2) {
						try {
							listFsize=listData.size();
							Disease disease;
							JSONArray jsonArray = jsonObject
									.getJSONArray("diseaseList");
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
								disease.setFamilyHistory(jsonArray
										.getJSONObject(i).getString(
												Disease.FAMILYHISTORY));
								disease.setHdeptid(jsonArray.getJSONObject(i)
										.getString(Disease.HDEPTID));
								disease.setHdeptName(jsonArray.getJSONObject(i)
										.getString(Disease.HDEPTNAME));
								disease.setId(jsonArray.getJSONObject(i)
										.getInt(Disease.ID));
								disease.setLaboratory(jsonArray
										.getJSONObject(i).getString(
												Disease.LABORATORY));
								disease.setLocation(jsonArray.getJSONObject(i)
										.getString(Disease.LOCATION));
								disease.setMedication(jsonArray
										.getJSONObject(i).getString(
												Disease.MEDICATION));
								disease.setName(jsonArray.getJSONObject(i)
										.getString(Disease.NAME).trim());
								disease.setOutline(jsonArray.getJSONObject(i)
										.getString(Disease.OUTLINE));
								disease.setPathogen(jsonArray.getJSONObject(i)
										.getString(Disease.PATHOGEN));
								disease.setPerson(jsonArray.getJSONObject(i)
										.getString(Disease.PERSON));
								disease.setRehabilitation(jsonArray
										.getJSONObject(i).getString(
												Disease.REHABILITATION));
								disease.setSymptoms(jsonArray.getJSONObject(i)
										.getString(Disease.SYMPTOMS));
								disease.setSymptomsLabel(jsonArray
										.getJSONObject(i).getString(
												Disease.SYMPTOMSLABEL));
								if (jsonArray.getJSONObject(i)
										.getString("pinYin").length() > 0) {
									disease.setIndex(getfirstLetter(jsonArray.getJSONObject(i)
											.getString("pinYin")));
								} else {
									disease.setIndex("#");
								}
								listData.add(disease);
							}
							if (listData.size() > 0) {
								adapter.notifyDataSetChanged();
								mPullToRefreshView.openFootView();
								
								
								if(pageNo==1)
								{
								
									if (listData.size() > listFsize) {
										pageNo++;
										
									}
								}
								
								else
								{
									if (listData.size() > listFsize) {
										pageNo++;
										listView.setSelection(listFsize-mvisibleItemCount+2);
										
									}
									
									else
									{
										mPullToRefreshView.stopCanFlash();
									}
								}
								
								
							}

							else {
								mPullToRefreshView.closeFootView();
								View emptyView = LayoutInflater.from(
										SelfServiceLibraryDieseaseLibrary.this)
										.inflate(R.layout.empty_view, null);
								TextView tv = (TextView) emptyView
										.findViewById(R.id.textView1);
								ImageView imageView1 = (ImageView) emptyView
										.findViewById(R.id.imageView1);
								imageView1.setVisibility(View.GONE);
								tv.setText("未搜索到相关内容");
								emptyView.setLayoutParams(new LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.MATCH_PARENT));
								emptyView.setVisibility(View.GONE);
								((ViewGroup) listView.getParent())
										.addView(emptyView);
								listView.setEmptyView(emptyView);
							}

							
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

	private String getfirstLetter(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
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
