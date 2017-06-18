package com.guangyi.forDoctor.personCenter;

import java.util.HashMap;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.application.FindDoctorApplication;
import com.guangyi.forDoctor.checkvision.UpdateManager;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
* <p>Title: 网络医院运营支撑平台-APP个人版</p>
* <p>Description:版本信息 </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:中国移动有限公司东莞分公司 </p>
* @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
* @version：1.0
* @since：2013-9-23
*/
public class EditionActivity extends BasicActivity {
	private Button ib_back,btn_update;
	private TextView tv_version_name,tv_is_last_version;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edition_activity);
		SysApplication.getInstance().addActivity(this);
		initView();
		initListener();
		
	}
	private void initView() {
		ib_back = (Button) findViewById(R.id.ib_back);
		btn_update= (Button) findViewById(R.id.btn_update);
		tv_version_name=(TextView) findViewById(R.id.tv_version_name);
		tv_is_last_version=(TextView) findViewById(R.id.tv_is_last_version);
		String versionName="";
		try {
			versionName = getPackageManager().getPackageInfo("com.guangyi.forDoctor.activity", 0).versionName;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tv_version_name.setText("当前版本:"+versionName);
		int currentVersion = 0;
		int lasteVersion = 0;
		String lastVersionName="";
		 try {
			 currentVersion=getPackageManager().getPackageInfo("com.guangyi.forDoctor.activity", 0).versionCode;
			 lasteVersion=getDataSf().getInt("versionCode", 0);  
			 lastVersionName=getDataSf().getString("versionName", "");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if(lasteVersion>currentVersion)
		 {
			 btn_update.setVisibility(View.VISIBLE);
			 tv_is_last_version.setTextColor(getResources().getColor(R.color.version_color_red));
			 tv_is_last_version.setText("最新版本:"+lastVersionName);
			 
			 final HashMap<String, String> hashMap=new HashMap<String, String>();
				hashMap.put("versionCode", getDataSf().getInt("versionCode", 0)+"");
				hashMap.put("versionName", getDataSf().getString("versionName", ""));
				hashMap.put("versionUrl", getDataSf().getString("versionUrl", ""));
				hashMap.put("updateLog", getDataSf().getString("updateLog", ""));
				btn_update.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						UpdateManager manager = new UpdateManager(EditionActivity.this);
						// 检查软件更新
						manager.checkUpdate(hashMap);
					}
				});
		 }
	
		
		
		
	}

	private void initListener() {
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}

}
