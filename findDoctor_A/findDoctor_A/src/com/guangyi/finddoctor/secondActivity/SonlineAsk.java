package com.guangyi.finddoctor.secondActivity;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.MainActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.OnLineMainGridAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.model.OnLineGridItem;
import com.guangyi.finddoctor.onlineAsk.OnLineAddConsultInfoForChart;
import com.guangyi.finddoctor.onlineAsk.OnLineDoctorMessage;
import com.guangyi.finddoctor.personCenter.UserLoginActivity;
import com.guangyi.finddoctor.utils.StateInfos;
public class SonlineAsk extends BasicActivity {
	private String[] stringItem;
	private GridView mOnLineGridView;
	private List<OnLineGridItem> onLineGridlist;
	private boolean isLogin;
	private SharedPreferences mSharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_ask);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initView();
		initOnLineView();
		bindOnLineData();
		initOnLineListener();
	
	}
	private void getSharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		isLogin = mSharedPreferences.getBoolean("isLogin", false);
	}
	
	@Override
	protected void onStart() {
		try {
			new StateInfos(SonlineAsk.this,"ÔÚÏß×ÉÑ¯",1).postStateInfo();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStart();
	}
	
	private void initView() {

		
		Button mBack;mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(MainActivity.class);
				finish();
			}
		});
	}
	private void initOnLineView() {
		mOnLineGridView = (GridView)findViewById(R.id.gv_main);
		onLineGridlist = new ArrayList<OnLineGridItem>();
//		final EditText editText1 = (EditText)  findViewById(R.id.editText1);
//		editText1.setOnKeyListener(new OnKeyListener() {
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_ENTER) {
//					Intent it = new Intent();
//					it.putExtra("word", editText1.getText().toString().trim());
//					it.setClass(getApplicationContext(),
//							MainSearchActivity.class);
//					startActivity(it);
//				}
//				return false;
//			}
//
//		});

		TextView iv_search=(TextView) findViewById(R.id.iv_search);
		iv_search.setOnClickListener(new  OnClickListener() {

			@Override
			public void onClick(View v) {
				getSharedPreference();
				if (isLogin) {
					openActivity(OnLineAddConsultInfoForChart.class);
				} else {
					Toast.makeText(getApplicationContext(), "Äú»¹Ã»ÓÐµÇÂ¼£¬ÇëµÇÂ¼!",
							Toast.LENGTH_LONG).show();
					openActivityforResult(UserLoginActivity.class);
				}

			}
		});
	}
	
	private void bindOnLineData() {
		int[] imageID = { R.drawable.online_item01, R.drawable.online_item02,
				R.drawable.online_item03, R.drawable.online_item04,
				R.drawable.online_item05, R.drawable.online_item06,
				R.drawable.online_item07, R.drawable.online_item08,
				R.drawable.online_item09, R.drawable.online_item10,
				R.drawable.online_item11, R.drawable.online_item12,
				R.drawable.online_item13, R.drawable.online_item14,
				R.drawable.online_item15, R.drawable.online_item16,
				R.drawable.online_item17, R.drawable.online_item18 };
		
		int[] imageIDPress = { R.drawable.online_item01_press, R.drawable.online_item02_press,
				R.drawable.online_item03_press, R.drawable.online_item04_press,
				R.drawable.online_item05_press, R.drawable.online_item06_press,
				R.drawable.online_item07_press, R.drawable.online_item08_press,
				R.drawable.online_item09_press, R.drawable.online_item10_press,
				R.drawable.online_item11_press, R.drawable.online_item12_press,
				R.drawable.online_item13_press, R.drawable.online_item14_press,
				R.drawable.online_item15_press, R.drawable.online_item16_press,
				R.drawable.online_item17_press, R.drawable.online_item18_press };
		stringItem = getResources().getStringArray(
				R.array.online_ask_grid_item_arry);

		OnLineGridItem item;
		for (int i = 0; i < stringItem.length; i++) {
			item = new OnLineGridItem();
			item.setImageId(imageID[i]);
			item.setImageIdPress(imageIDPress[i]);
			item.setTextStr(stringItem[i]);
			;
			onLineGridlist.add(item);

		}

		OnLineMainGridAdapter gridAdapter = new OnLineMainGridAdapter(this,
				onLineGridlist);
		mOnLineGridView.setAdapter(gridAdapter);

	}

	private void initOnLineListener() {
		mOnLineGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent();
				it.setClass(getApplicationContext(), OnLineDoctorMessage.class);
				it.putExtra("title", stringItem[position]);
				it.putExtra(OnLineGridItem.DEPID, onLineGridlist.get(position)
						.getDepId());
				startActivity(it);

			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	    
			openActivity(MainActivity.class);
			finish();
			
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
