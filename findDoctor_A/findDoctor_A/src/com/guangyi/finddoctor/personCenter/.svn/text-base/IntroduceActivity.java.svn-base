package com.guangyi.finddoctor.personCenter;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.IntroduceListAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Introduce;
import com.guangyi.finddoctor.utils.Config;

public class IntroduceActivity extends BasicActivity {
	private Button ib_back;
	private Handler mHandler;
	private Runnable mRunnable;
	private Thread mThread;
	private ListView list_view;
	
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
		setContentView(R.layout.introduce_activity);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initparams();
		initListener();
		getData();
	}

	private void initparams() {
		ib_back=(Button) findViewById(R.id.ib_back);
		 list_view=(ListView) findViewById(R.id.list_view);
		
	}

	private void initListener() {
		mHandler = new MyHandler();
		ib_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	private void getView(final List<Introduce> list) {
		IntroduceListAdapter adapter =new IntroduceListAdapter(this, list);
		list_view.setAdapter(adapter);
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it=new Intent();
				it.setClass(IntroduceActivity.this,introduceDetail.class);
				it.putExtra("introduce", list.get(position));
				startActivity(it);
				
				
			}
		});
	}
	
	
	private void getData() {
		initProgressDialog();
		mRunnable=new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				List<String> params = new ArrayList<String>();
				params.add("");
				msg.arg1 = 1;
				msg.obj =new ApiHttpUtil().getMethod(Config.getProperty("GETPRODOCTLIST",""), params);
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
			Log.w("indroduce", msg.obj.toString());
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
							JSONArray jsonArr = jsonObject.getJSONArray("aliResult");
							List<Introduce>  _List = new ArrayList<Introduce>();
							Introduce model ;
							for(int i = 0; i < jsonArr.length(); i++){
								model = new Introduce();
								model.setName(jsonArr.getJSONObject(i).getString("name"));
								model.setDesc(jsonArr.getJSONObject(i).getString("title"));
								model.setDetialUrl(jsonArr.getJSONObject(i).getString("detialUrl"));
								model.setLogoUrl(Config.getProperty("FILEURL", "")+jsonArr.getJSONObject(i).getString("logoUrl"));
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
	
	
	


	/**
	 * 安装APK文件
	 */
//	private void installApk()
//	{
//		File apkfile = new File(mSavePath, mHashMap.get("versionName"));
//		if (!apkfile.exists())
//		{
//			return;
//		}
//		// 通过Intent安装APK文件
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//		mContext.startActivity(i);
//	}

}
