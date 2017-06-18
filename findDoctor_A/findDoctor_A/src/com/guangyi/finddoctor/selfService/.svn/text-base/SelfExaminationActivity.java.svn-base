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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.BodyDisease;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.PictureUtil;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:症状自查-图片模式
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
public class SelfExaminationActivity extends BasicActivity implements
		View.OnTouchListener {

//	private Drawable mDrawable, wDrawable, mDrawableBack, wDrawableBack;
	private Button ib_model_change;
	private ImageView iv_person_direction, iv_sex_change, iv_person,iv_part;
	private EditText et_disease_search;
	private Handler mHandler;
	private Runnable mRunnable;
	private Thread mThread;
	private int  personTag;
//	10  男反
//	11  男正
//
//	20  女反
//	21  女正
	
	private Bitmap bitmap1;
	private void commonThreadStart() {
		if (mRunnable != null) {
			mThread = new Thread(mRunnable);
			mThread.start();
		}
	}

	protected void onStop() {
		mHandler.removeCallbacks(mRunnable);
		iv_part.setAlpha(0);
	
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		if(bitmap1!=null&&!bitmap1.isRecycled())
		{
			bitmap1.recycle();
			System.gc();
		}
		Log.i("destory", "destory");
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initSearch() {
		mHandler = new MyHandler();
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
					Intent it=new Intent(SelfExaminationActivity.this,SelfServiceSymtomSearch.class);
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
				msg.arg1 = 1;
				msg.obj = new ApiHttpUtil().postMethod(
						Config.getProperty("SEARCHLABEL", ""), params);
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
			Log.i("jsonString", jsonString);
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
							Intent it = new Intent(
									SelfExaminationActivity.this,
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_examination);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		 initparams();
		 initView();
		 initListener();
		 initSearch();

		
	}

	private void initparams() {
//		mDrawable = getResources().getDrawable(R.drawable.man);
//		mDrawableBack = getResources().getDrawable(R.drawable.man_back);
//		wDrawable = getResources().getDrawable(R.drawable.woman);
//		wDrawableBack = getResources().getDrawable(R.drawable.woman_back);
		
		personTag=11;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}


	private void initView()
	{
		iv_person = (ImageView) findViewById(R.id.iv_person_man);
		changePersonBg(personTag);
		iv_person_direction=(ImageView) findViewById(R.id.iv_person_direction);
		iv_sex_change=(ImageView) findViewById(R.id.iv_sex_change);
		iv_part=(ImageView) findViewById(R.id.iv_part);
		iv_part.setOnTouchListener(this);
		ib_model_change=(Button) findViewById(R.id.ib_model_change);
	}
	
	private void changePersonBg(int tag)
	{
		if(tag==10)
		{
			iv_person.setBackgroundResource(R.drawable.man_back);
		}
		else if(tag==11)
		{
			iv_person.setBackgroundResource(R.drawable.man);
		}
		else if(tag==20)
		{
			iv_person.setBackgroundResource(R.drawable.woman_back);
		}
		else if(tag==21)
		{
			iv_person.setBackgroundResource(R.drawable.woman);
		}
	}
	private void initListener() {

		Button ib_back=(Button) findViewById(R.id.ib_back); 
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		ib_model_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelfExaminationActivity.this,
						SelfExaminationBywords.class);
				startActivity(intent);

			}
		});

		iv_sex_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (personTag==10) {
					personTag=20;
					changePersonBg(personTag);
				}  
				else if(personTag==11) {
					personTag=21;
					changePersonBg(personTag);
				}  
				else if(personTag==20) {
					personTag=10;
					changePersonBg(personTag);
				}  
				else if(personTag==21) {
					personTag=11;
					changePersonBg(personTag);
				}  

			}
		});

		iv_person_direction.setOnClickListener(new OnClickListener() {
//			if(personTag==20) {
//				personTag=10;
//				changePersonBg(personTag);
//			}  
			@Override
			public void onClick(View v) {
				if (personTag==10) {
					personTag=11;
					changePersonBg(personTag);
					
					iv_person_direction
					.setImageResource(R.drawable.person_back);
					
				}  
				else if (personTag==11) {
					personTag=10;
					changePersonBg(personTag);
					iv_person_direction
					.setImageResource(R.drawable.person_front);
					
				}  
				else if (personTag==20) {
					personTag=21;
					changePersonBg(personTag);
					iv_person_direction
					.setImageResource(R.drawable.person_back);
					
				}  
				else if (personTag==21) {
					personTag=20;
					changePersonBg(personTag);
					iv_person_direction
					.setImageResource(R.drawable.person_front);
				}  

			}
		});

	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
//		Integer[] mRsid=new Integer[]{R.drawable.m_arm,R.drawable.m_basin,R.drawable.m_belly,R.drawable.m_chest,R.drawable.m_foot,R.drawable.m_hand,R.drawable.m_head,R.drawable.m_leg,R.drawable.m_neck,R.drawable.m_rump,R.drawable.m_shoulder};
//		Integer[] wRsid=new Integer[]{R.drawable.w_arm,R.drawable.w_basin,R.drawable.w_basin,R.drawable.w_chest,R.drawable.w_foot,R.drawable.w_hand,R.drawable.w_head,R.drawable.w_leg,R.drawable.w_neck,R.drawable.w_rump,R.drawable.w_shoulder};
		
		Integer[] mRsid=new Integer[]{R.drawable.m_arm,R.drawable.m_basin,R.drawable.m_belly,R.drawable.m_chest,R.drawable.m_foot,R.drawable.m_hand,R.drawable.m_head,R.drawable.m_leg,R.drawable.m_neck,R.drawable.m_rump,R.drawable.m_shoulder};
		Integer[] wRsid=new Integer[]{R.drawable.w_arm,R.drawable.w_basin,R.drawable.w_belly,R.drawable.w_chest,R.drawable.w_foot,R.drawable.w_hand,R.drawable.w_head,R.drawable.w_leg,R.drawable.w_neck,R.drawable.w_rump,R.drawable.w_shoulder};
		Integer[] Rsid = null;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 判断坐标点不超过图片得宽高
//			if (personTag==10) {
//				personTag=11;
//				changePersonBg(personTag);
//			}  
			if(personTag==10||personTag==11)
			{
				Rsid=mRsid;
			}
			else if(personTag==20||personTag==21)
			{
				Rsid=wRsid;
			}
			for (int i = 0; i < Rsid.length; i++) {
    			bitmap1=BitmapFactory.decodeResource(getResources(), Rsid[i]);
    			float scaleWidth = iv_person.getWidth();
    			float scaleHeight = iv_person.getHeight();
    			// 得到新的图片
					bitmap1=PictureUtil.readBitmapSize( bitmap1, scaleWidth, scaleHeight);

				// 判断点击得坐标点是否在图片范围内
				if ((int) event.getX() > bitmap1.getWidth()
						|| (int) event.getY() > bitmap1.getHeight()) 
				{
					break;
				}
				 
					// 判断坐标点是否是在图片得透明区域
					if (bitmap1.getPixel((int) event.getX(), (int) event.getY()) == 0) {
						continue;
					} else {
						iv_part.setAlpha(255);
						iv_part.setImageBitmap(bitmap1);
						String sex="";
						int flag = 0;
						if(personTag==10||personTag==11)
						{
							sex="男";
						}
						else if(personTag==20||personTag==21)
						{
							sex="女";
						}
						
						if(personTag==21||personTag==11)
						{
							flag=0;
						}
						else if(personTag==20||personTag==10)
						{
							flag=1;
						}
						startSystomActivity(i,sex,flag);
						break;
					}

				}

			break;

		default:
			break;
		}


		return true;
	}
	
private void startSystomActivity(int id,String sex,int flag)
{
//	String bodyname[]=new String[]{"上肢","盆骨","腹部","胸部","下肢","上肢","头部","下肢","颈部","臀部","肩部"};
	String bodyname[]=new String[]{"上肢","生殖器","腹部","胸部","下肢","上肢","头部","下肢","颈部","排泄部","上肢"};
	//flag 0前 1后
	
	Intent intent = new Intent(
			SelfExaminationActivity.this,
			BodySymptomActivity.class);
	if(id==3)
	{
		if(flag==0)
		{
			intent.putExtra("bodyname", "胸部");
		}
		else if(flag==1)
		{
			intent.putExtra("bodyname", "背部");
		}
		
	}
	else if(id==2)
	{
		if(flag==0)
		{
			intent.putExtra("bodyname", "腹部");
		}
		else if(flag==1)
		{
			intent.putExtra("bodyname", "腰部");
		}
		
	}
	else
	{
		intent.putExtra("bodyname", bodyname[id]);
	}
	
	intent.putExtra("sex", sex);
	startActivity(intent);}

}
