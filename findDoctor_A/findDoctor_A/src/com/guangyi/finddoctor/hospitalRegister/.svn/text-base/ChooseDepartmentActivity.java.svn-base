package com.guangyi.finddoctor.hospitalRegister;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.DepartmentAdapter;
import com.guangyi.finddoctor.adapter.ProvinceAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.City;
import com.guangyi.finddoctor.model.Department;
import com.guangyi.finddoctor.model.Hospital;
import com.guangyi.finddoctor.utils.Config;
/**
* <p>Title: 网络医院运营支撑平台-APP个人版</p>
* <p>Description:选择科室 </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:中国移动有限公司东莞分公司 </p>
* @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
* @version：1.0
* @since：2013-9-23
*/
public class ChooseDepartmentActivity extends BasicActivity {
	
	private ListView mListView;
	private Button mBack;
	private Handler mHandler;
	private ApiHttpUtil mApiHttpUtil;
	private List<Department> mListItem;
	private int mHospitalID;
	private Runnable mRunnable;
	private Thread mThread;
	private String department;
		
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
		setContentView(R.layout.choose_department);
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
	
	
	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_chose);
		mBack = (Button) findViewById(R.id.ib_back);
		

	}
	
	private  void initParams()
	{
		mHandler=new MyHandler();
		mApiHttpUtil=new ApiHttpUtil();
		mListItem=new ArrayList<Department>();
		mHospitalID=getIntent().getExtras().getInt(Hospital.HOSPID);
		department=getIntent().getStringExtra("department");
		
	}


	private void bindData(List<Department> list) {
			canclePregressDialog();
			DepartmentAdapter adapter = new DepartmentAdapter(this, list);
			
			View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
			emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
			emptyView.setVisibility(View.GONE);   
			((ViewGroup)mListView.getParent()).addView(emptyView);   
			mListView.setEmptyView(emptyView);
			
			mListView.setAdapter(adapter);
		
	}
	//获取科室信息
	private void  getData()
	{
		initProgressDialog();
		mRunnable=new Runnable() {
			
			@Override
			public void run() {
				Message msg=new Message();
				List<String> list=new ArrayList<String>();
				list.add(String.valueOf(mHospitalID));
				list.add(department);
				msg.arg1=1;
				msg.obj=mApiHttpUtil.getMethod(Config.getProperty("DEPARTMENT_STRING",""), list);
				mHandler.sendMessage(msg);
				
			}
		};
		commonThreadStart();
		
	}

	private void initListener() {

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
					Intent it=new Intent();
					it.putExtra(Department.HDEPTID, mListItem.get(position).getHdeptId());
					it.putExtra(Department.DEPANAME, mListItem.get(position).getDepaName());
					setResult(ResultCodeCategory.RESULT_CODE_CHOSE_DEPARTMENT, it);
					finish();
					
			}
		});
		
		
	}
	
	
	
	
	class MyHandler extends Handler
	{
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
							JSONArray jsonArray=jsonObject.getJSONArray("Department");
							 Department department;
							for (int i = 0; i < jsonArray.length(); i++) {
								department=new Department();
								System.out.println(jsonArray.getJSONObject(i).getString(Department.DEPANAME));
								department.setHdeptId(jsonArray.getJSONObject(i).getString(Department.HDEPTID));
								department.setDepaName(jsonArray.getJSONObject(i).getString(Department.DEPANAME));
								
								mListItem.add(department);
							}
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						finally
						{
							bindData(mListItem);
						}
					}
					
					else if(msg.arg1==2)
					{
						
//						try {
//							List<City> dialoglistItem=new ArrayList<City>();
//							JSONArray jsonArray=jsonObject.getJSONArray("Region");
//							City city;
//							for (int i = 0; i < jsonArray.length(); i++) {
//								city=new City();
//								System.out.println(jsonArray.getJSONObject(i).getString("regionName"));
//								city.setId(jsonArray.getJSONObject(i).getInt("id"));
//								city.setParentId(jsonArray.getJSONObject(i).getInt("parentId"));
//								city.setPkId(jsonArray.getJSONObject(i).getInt("pkId"));
//								city.setRegionName(jsonArray.getJSONObject(i).getString("regionName"));
//								city.setRegionType(jsonArray.getJSONObject(i).getInt("regionType"));
//								dialoglistItem.add(city);
//							}
//							showAlertDialog(dialoglistItem);
//							
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
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
	
	
	
	private void showAlertDialog(final List<City> list)
	{
		
			AlertDialog.Builder builder = new Builder(this);
			View _View = getLayoutInflater().inflate(R.layout.choose_city_dialog,
					null);
			builder
			.setView(_View)
					.show();
			ProvinceAdapter adapter=new ProvinceAdapter(getApplicationContext(), list);
			ListView listView =(ListView) _View.findViewById(R.id.lv_chose);
			listView.setAdapter(adapter);
				
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent it=new Intent();
						it.putExtra(City.ID, list.get(position).getId());
						it.putExtra(City.REGIONNAME, list.get(position).getRegionName());
						setResult(ResultCodeCategory.RESULT_CODE_CHOSE_DEPARTMENT, it);
						finish();
					}
				});
				
	}
	

}
