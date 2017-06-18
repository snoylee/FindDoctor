package com.guangyi.finddoctor.hospitalRegister;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity2;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.CommonPatient;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.model.Value;
import com.guangyi.finddoctor.model.WidgetInfo;
import com.guangyi.finddoctor.personCenter.MyRegisterActivity;
import com.guangyi.finddoctor.slidebutton.OnChangedListener;
import com.guangyi.finddoctor.slidebutton.SlideButton;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.PxDpTool;
/**
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:填写就诊信息
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
public class HospitalRegisterPost extends BasicActivity2 {
	private String timeStr;
	private CommonPatient commonPatient;
	private Doctor doctor;
//	private CheckBox checkbox1, checkbox2;
	private String returnFlag = "0", isfirst="0";
	private EditText et_question,et_card_number;
	private String symptom;
	private String transactionId;
	private SharedPreferences mSharedPreferences;
	private int userId = -1;
//	private String type = "1";
	private Handler mHandler;
	private Runnable mRunnable;
	private Thread mThread;
	private SlideButton mSlideButton1,mSlideButton2;
	private com.guangyi.finddoctor.custview.PopMenu popMenu;
	private TextView tv_popmenu;
	private LinearLayout linear_card,is_first_second;
	private String groupType;
	private int daySectionFlag;
	
	
//private 	List<View> views=new ArrayList<View>();
private 	 List<String> groupTypes=new ArrayList<String>();
//private 	List<WidgetInfo> WidgetInfos=new ArrayList<WidgetInfo>();

private 	List<WidgetInfo> WidgetInfos1=new ArrayList<WidgetInfo>();
private 	List<View> views1=new ArrayList<View>();

private 	List<WidgetInfo> WidgetInfos2=new ArrayList<WidgetInfo>();
private 	List<View> views2=new ArrayList<View>();
		
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_register_post);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initView();
		initParams();
		initListener();
	}

	private void initParams() {
		geSharedPreference();
		timeStr = getIntent().getStringExtra("time");
		daySectionFlag=getIntent().getIntExtra("daySectionFlag", 0);
		Log.i("daySectionFlag", daySectionFlag+"");
		// feeStr=getIntent().getStringExtra("fee");
		commonPatient = (CommonPatient) getIntent().getSerializableExtra(
				"commonPatient");
		doctor = (Doctor) getIntent().getSerializableExtra("doctor");
		// it.putExtra("time", timeStr);
		// it.putExtra("fee",feeStr);
		// it.putExtra("doctor", doctor);
		// it.putExtra("way",String.valueOf(way));
		// it.putExtra("commonPatient", commonPatient);
		transactionId = String.valueOf(new Date().getTime());
		Log.i("transactionId", transactionId + "");
	}

	private void geSharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		userId = mSharedPreferences.getInt("userId", 0);
	}

	private void initView() {
		mHandler = new MyHandler();
		linear_card=(LinearLayout) findViewById(R.id.linear_card);
		is_first_second=(LinearLayout) findViewById(R.id.is_first_second);
		et_question = (EditText) findViewById(R.id.et_question);
		mSlideButton1=(SlideButton) findViewById(R.id.slide_button1);
		mSlideButton2=(SlideButton) findViewById(R.id.slide_button2);
		
//		
//		popMenu=new com.guangyi.finddoctor.custview.PopMenu(this);
//		final String[] popStrArr=new String[] { "医保卡", "自费卡(医院下发的就诊卡)"};
//		popMenu.addItems(popStrArr);
//		
//		 tv_popmenu = (TextView) findViewById(R.id.tv_popmenu);
//		tv_popmenu.setText("医保卡");
//		tv_popmenu.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				popMenu.showAsDropDown(v);
//			}
//		});
//		
//		// 弹出菜单监听器
//		OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				System.out.println("下拉菜单点击" + position);
//				tv_popmenu.setText(popStrArr[position]);
//				popMenu.dismiss();
//			}
//		};
//		popMenu.setOnItemClickListener(popmenuItemClickListener);
//		mSlideButton1.setState(true);
//		mSlideButton1.setEnabled(false);
//		et_card_number=(EditText) findViewById(R.id.tv_card_number);
		TextView tv_see_rules=(TextView) findViewById(R.id.tv_see_rules);
		tv_see_rules.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(HospitalRegisterRule.class);
				
			}
		});
		Button Back = (Button) findViewById(R.id.ib_back);
//		checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
//		checkbox2 = (CheckBox) findViewById(R.id.checkbox2);

		
		FindDoctorApplication app=(FindDoctorApplication) getApplication();
		JSONArray jsonArray=app.getmJsonArray();
		if(jsonArray!=null)
		{
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				String groupType=jsonArray.getJSONObject(i).getString("groupType");
				groupTypes.add(jsonArray.getJSONObject(i).getString("groupType"));
				JSONArray jsonArray2=jsonArray.getJSONObject(i).getJSONArray("widgetInfos");
				for (int j = 0; j < jsonArray2.length(); j++) {
					JSONArray jsonArray3=jsonArray2.getJSONObject(j).getJSONArray("valueList");
					String widgetId=jsonArray2.getJSONObject(j).getString("widgetId");
					String widgetIsRequire=jsonArray2.getJSONObject(j).getString("widgetIsRequire");
					String widgetName=jsonArray2.getJSONObject(j).getString("widgetName");
					String widgetReg=jsonArray2.getJSONObject(j).getString("widgetReg");
					Log.d("widget------>",widgetId+widgetIsRequire+widgetName+widgetReg);
					WidgetInfo widgetInfo=new WidgetInfo();
					widgetInfo.setWidgetId(widgetId);
					widgetInfo.setWidgetIsRequire(widgetIsRequire);
					widgetInfo.setWidgetName(widgetName);
					widgetInfo.setWidgetReg(widgetReg);
				if(groupType.trim().equals("0"))
				{
					WidgetInfos1.add(widgetInfo);
				}
					if(groupType.trim().equals("1"))
					{
						WidgetInfos2.add(widgetInfo);
					}
					if(jsonArray3.length()>0)
					{
						List<Value> values=new ArrayList<Value>();
						for (int k = 0; k < jsonArray3.length(); k++) {
							Value value=new Value();
							value.setWidgetCommon(jsonArray3.getJSONObject(k).getString("widgetCommon"));
							value.setWidgetValue(jsonArray3.getJSONObject(k).getString("widgetValue"));
							values.add(value);
						}
						widgetInfo.setListValue(values);
						Spinner spinner=new Spinner(HospitalRegisterPost.this);
						LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,PxDpTool.dip2px(HospitalRegisterPost.this,50));
						params.setMargins(0, PxDpTool.dip2px(HospitalRegisterPost.this,5), 0, 0);
						spinner.setLayoutParams(params);
						spinner.setBackgroundResource(R.drawable.spinner_background);
						String[] strArr=new String[values.size()];
						for (int l = 0; l < values.size(); l++) {
							strArr[l]=values.get(l).getWidgetCommon();
						}
						ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
								HospitalRegisterPost.this, R.layout.spinner_layout,
								strArr);
						spinnerAdapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spinner.setAdapter(spinnerAdapter);
						spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {}
							@Override
							public void onNothingSelected(AdapterView<?> arg0) {}
						});
//						linear_card.addView(spinner);
						if(groupType.trim().equals("0"))
						{
							views1.add(spinner);
						}
							if(groupType.trim().equals("1"))
							{
								views2.add(spinner);
							}
						
					}
					
					else
					{
						if(widgetName.length()>0)
						{
							EditText tv=new EditText(HospitalRegisterPost.this);
							tv.setBackgroundResource(R.drawable.ic_preference_all_circle1);
							LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,PxDpTool.dip2px(HospitalRegisterPost.this,50));
							params.setMargins(0, PxDpTool.dip2px(HospitalRegisterPost.this,5), 0, 0);
							String a="";
							if(widgetIsRequire.trim().equals("0"))
							{
								a="(必填)";
							}
							tv.setHint("请输入"+widgetName+a);
							tv.setLayoutParams(params);
//							linear_card.addView(tv);
							
							if(groupType.trim().equals("0"))
							{
								views1.add(tv);
							}
								if(groupType.trim().equals("1"))
								{
									views2.add(tv);
								}
						}
					}
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		
		
		
		Log.d("grouptype", groupTypes.toString()+"-------");
		if(groupTypes.size()>=2)
		{
			mSlideButton1.setData(new String[]{"初诊","复诊"});
			mSlideButton1.SetOnChangedListener(new OnChangedListener() {
				@Override
				public void OnChanged(boolean CheckState) {
					if(CheckState)
					{
						returnFlag="1";
						
							linear_card.removeAllViews();
							for (int j = 0; j < views2.size(); j++) {
								linear_card.addView(views2.get(j));
							}
					}
					else
					{
						returnFlag="0";
						linear_card.removeAllViews();
						for (int j = 0; j < views1.size(); j++) {
							linear_card.addView(views1.get(j));
						}
					}
				}
			});
		}
		else if(groupTypes.size()==1)
		{
			TextView tv1,tv2;
			tv1=(TextView) findViewById(R.id.tv_isFirst);
			tv2=(TextView) findViewById(R.id.tv_isSecond);
			mSlideButton1.setVisibility(View.GONE);
			is_first_second.setVisibility(View.VISIBLE);
			if(groupTypes.get(0).trim().equals("0"))
			{
				returnFlag="0";
				
				tv1.setTextColor(getResources().getColor(R.color.white));
				tv2.setTextColor(getResources().getColor(R.color.text_color));
				tv1.setBackgroundResource(R.drawable.a0_btnpopwindowedit_normal);
			}
			
			else if(groupTypes.get(0).trim().equals("1"))
			{
				returnFlag="1";
				tv1.setTextColor(getResources().getColor(R.color.text_color));
				tv2.setTextColor(getResources().getColor(R.color.white));
				tv2.setBackgroundResource(R.drawable.a0_btnpopwindowedit_normal);
			}
            is_first_second.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(returnFlag=="0")
					{
						showToast("此医生只接受初诊!");
					}
					
					else if(returnFlag=="1")
					{
						showToast("此医生只接受复诊!");
					}
				}
			});

		}
		
		
		if(returnFlag=="0")
		{
			linear_card.removeAllViews();
			for (int j = 0; j < views1.size(); j++) {
				linear_card.addView(views1.get(j));
			}
		}
		
		if(returnFlag=="1")
		{
			linear_card.removeAllViews();
			for (int j = 0; j < views2.size(); j++) {
				linear_card.addView(views2.get(j));
			}
		}

		
		
		mSlideButton2.setData(new String[]{"确诊","未确诊"});
		mSlideButton2.SetOnChangedListener(new OnChangedListener() {
			@Override
			public void OnChanged(boolean CheckState) {
				if(CheckState)
				{
					et_question.setHint("请描述症状");
				}
				else
				{
					et_question.setHint("请输入疾病名称");
				}
			}
		});
		
		

		Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button ib_post = (Button) findViewById(R.id.ib_post);
		ib_post.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				boolean isCanPost=true;
				String keyValue="";
				HashMap<String, String> params=new HashMap<String, String>();
				if(returnFlag=="0")
				{
					for (int i = 0; i < views1.size(); i++) {
						if(views1.get(i) instanceof Spinner)
						{
							Spinner spinner=(Spinner) views1.get(i);
							int id=(int) spinner.getSelectedItemId();
							keyValue+=WidgetInfos1.get(i).getWidgetId()+":"+WidgetInfos1.get(i).getListValue().get(id).getWidgetValue()+",";
						}
						
						else if(views1.get(i) instanceof EditText)
						{
							EditText et=(EditText) views1.get(i);
							keyValue+=WidgetInfos1.get(i).getWidgetId()+":"+et.getText().toString().trim()+",";
							
							if(WidgetInfos1.get(i).getWidgetIsRequire().trim().equals("0"))
							{
								if(WidgetInfos1.get(i).getWidgetReg().length()!=0)
								{
									Pattern regex = Pattern.compile(WidgetInfos1.get(i).getWidgetReg());
									Log.d("正则", "test"+WidgetInfos1.get(i).getWidgetReg());
									Matcher matcher = regex.matcher(et.getText().toString().trim());
									if(!matcher.matches())
									{
										showToast(WidgetInfos1.get(i).getWidgetName()+"格式不正确 !");
										isCanPost=false;
										return;
									}
								}
									if(et.getText().toString().trim().length()==0)
									{
										showToast("必填项不能为空!");
										isCanPost=false;
										return;
									}
							}
						}
					}
				}
				
				else if(returnFlag=="1")
				{
					for (int i = 0; i < views2.size(); i++) {
						if(views2.get(i) instanceof Spinner)
						{
							Spinner spinner=(Spinner) views2.get(i);
							int id=(int) spinner.getSelectedItemId();
							keyValue+=WidgetInfos2.get(i).getWidgetId()+":"+WidgetInfos2.get(i).getListValue().get(id).getWidgetValue()+",";
						}
						
						else if(views2.get(i) instanceof EditText)
						{
							EditText et=(EditText) views2.get(i);
							keyValue+=WidgetInfos2.get(i).getWidgetId()+":"+et.getText().toString().trim()+",";
							
							if(WidgetInfos2.get(i).getWidgetIsRequire().trim().equals("0"))
							{
								
								if(WidgetInfos2.get(i).getWidgetReg().length()!=0)
								{
									Pattern regex = Pattern.compile(WidgetInfos2.get(i).getWidgetReg());
									Matcher matcher = regex.matcher(et.getText().toString().trim());
									if(!matcher.matches())
									{
										showToast(WidgetInfos2.get(i).getWidgetName()+"格式不正确 !");
										isCanPost=false;
										return;
									}
								}
									if(et.getText().toString().trim().length()==0)
									{
										showToast("必填项不能为空!");
										isCanPost=false;
										return;
									}
							}
							

						}
					}
				}
				
				
				
				
				
				
				
				if(isCanPost)
				{
					if(keyValue.length()>0)
					{
						keyValue=keyValue.substring(0, keyValue.length()-1);
					params.put("widget", keyValue);
					}
					getData(params);
				}
				

			}
		});

	}

	private void initListener() {

//		checkbox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (isChecked) {
//					returnFlag = "1";
//				} else {
//					returnFlag = "0";
//				}
//			}
//		});
//		checkbox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (isChecked) {
//					isfirst = "1";
//				} else {
//					isfirst = "0";
//				}
//                  
//			}
//		});

	}

	// 提交就诊信息
	private void getData(final HashMap<String, String> params) {
		initProgressDialog();
		mRunnable=new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				ApiHttpUtil mApiHttpUtil = new ApiHttpUtil();
//				HashMap<String, String> params = new HashMap<String, String>();
				symptom = et_question.getText().toString().trim();
				FindDoctorApplication app=(FindDoctorApplication) HospitalRegisterPost.this.getApplicationContext();
				params.put("scid", app.getScid());
				params.put("timeSection", timeStr.toString().split(",")[1]);
				params.put("transactionId", transactionId);
				params.put("phone", commonPatient.getUserMoble() + "");
				params.put("symptom", commonPatient.getDieaseName() + "");
				params.put("userCId", commonPatient.getId()+"");
				params.put("disease", symptom);
				params.put("userId", String.valueOf(userId));
				params.put("doctId", String.valueOf(doctor.getDoctId() + ""));
				params.put("type", "1");
				params.put("sex", String.valueOf(commonPatient.getSex() + ""));
				params.put("patientName", commonPatient.getName() + "");
				params.put("certificateType", "1");
				params.put("certificateNo",
						String.valueOf(commonPatient.getUserCard() + ""));
				params.put("address", commonPatient.getUserAddress() + "");
				params.put("birthday", "");
				params.put("returnFlag", returnFlag);// 初诊0 复诊1
				params.put("isFirstCome", isfirst);// 初诊0 复诊1
				params.put("proxyName", "");
				params.put("proxyCertificateTyp", "");
				params.put("proxyCertificateNo", "");
				params.put("proxyPhone", "");
				params.put("widgetId", "");
				params.put("widgetValue", "");
				params.put("isNeedPay", "");
				params.put("state", "");
				params.put("appTime", timeStr.split(",")[0]);
				params.put("daySectionFlag",daySectionFlag+"");
				Log.d("params=========>>>>", params.toString());
				msg.arg1 = 1;
				msg.obj = mApiHttpUtil.post(
						Config.getProperty("ON_LINE_post", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

		// }
	}

	class MyHandler extends Handler {
		@SuppressLint("ShowToast")
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
//				String reason=getResources().getString(R.string.network_preoblem);
				String reason="提交失败请重试！";
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					code = jsonObject.getInt("code");
					Log.i("qian", jsonObject.getString("reason"));
					String a=jsonObject.getString("reason");
					try {
						reason=new String(Base64.decode(a, Base64.DEFAULT),"gbk");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(code==0)
				{
					
					if(msg.arg1==1)
					{
//						showToast("预约成功，短信已发送至您的手机！");
//						Intent intent = new Intent(
//								HospitalRegisterPost.this,
//								MyRegisterActivity.class);
//						intent.putExtra("flagtype", 0);
//						startActivity(intent);
						showDialog("预约成功,是否立即查看?", 0);
					}
					
						
				}
				
//				 else if (code == 2) {
//					
//					if (msg.arg1 == 1) {
//						showToast("当前排班已经被预约！");
//					}
//
//					
//
//				}
//				
//				else if (code == 99) {
//					
//					if (msg.arg1 == 1) {
//						showToast("服务器内部错误！");
//					}
//					
//
//				} 
					
				else
				{
			         
//			         Dialog dialog = new AlertDialog.Builder(HospitalRegisterPost.this)
//			           .setTitle("提示")
//			           .setMessage(reason)
//			          .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog,int whichButton){
//			                    dialog.cancel();
//			            }
//			          }).create();
//			          dialog.show();
					showDialog(reason, 1);
				}
				
			}
			
			super.handleMessage(msg);
			
		}
	}
	
	
	
	private void showDialog(final String reason,final int code)
	{
			View view=LayoutInflater.from(this).inflate(R.layout.progress_dialog_pay, null);
	    	final Dialog dialog=new Dialog(this, R.style.Translucent_NoTitle);
	    	dialog.setContentView(view);
	    	TextView tv_progress=(TextView) view.findViewById(R.id.tv_progress);
	    	tv_progress.setText(reason);
	    	Button btn_ok=(Button) view.findViewById(R.id.btn_ok);
	    	btn_ok.setText("确定");
	    	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
	    	btn_cancle.setText("取消");
	    	btn_cancle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
	    	btn_ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(code==0)
					{
						
						Intent intent = new Intent(
								HospitalRegisterPost.this,
								MyRegisterActivity.class);
						intent.putExtra("flagtype", 0);
						startActivity(intent);
					}
					dialog.cancel();
					
				}
			});
	    	dialog.setCanceledOnTouchOutside(false);
	    	dialog.show();
	
	}
	
	

}
