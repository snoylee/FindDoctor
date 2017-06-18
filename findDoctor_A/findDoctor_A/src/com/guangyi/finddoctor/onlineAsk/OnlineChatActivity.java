package com.guangyi.finddoctor.onlineAsk;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.alipay.android.msp.demo.Keys;
import com.alipay.android.msp.demo.Result;
import com.alipay.android.msp.demo.Rsa;
import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.custview.SelectPicActivity;
import com.guangyi.finddoctor.custview.UplodeProgress;
import com.guangyi.finddoctor.hospitalRegister.DoctorHomeActivity;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.ChatEntity;
import com.guangyi.finddoctor.model.MyConsultModel;
import com.guangyi.finddoctor.personCenter.AssessActivity;
import com.guangyi.finddoctor.preImage.ImageShowActivity;
import com.guangyi.finddoctor.utils.AudioRecorder;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.DateTools;
import com.guangyi.finddoctor.utils.PictureUtil;
/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:在线咨询
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
public class OnlineChatActivity extends BasicActivity {
	public static final int TO_SELECT_PHOTO = 3;
	private Map<String, String> params;
	private String requestURL = Config.getProperty("requestURL", "");
	private EditText contentEditText = null;
	private ListView chatListView = null;
	private ChatAdapter chatAdapter = null;
	private List<ChatEntity> chatList = null;
	private Bitmap file;
	private String userId;
	private Button btn_text_input, btn_voice_input, btn_pic_input;
	private Button btn_start_voice;
	private EditText et_content;
	private RelativeLayout rl_bottom,btn_bottom;
	private String voice_path;
	private String picPath;
	private String consId;
	private Handler handler;
	public boolean isVisiable = false;
	private Runnable mRunnable;
	private Thread mThread;
	// private boolean isPrepared = false;
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音
	private static int RECODE_STATE = 0; // 录音的状态
	private AudioRecorder recorder;
	private MediaPlayer mediaPlayer;
	private Bitmap mBitmap;
	private int IS_BOTTOM_VISIBLE = 1;// 顶部回复是否可见，0 不可见，1可见
	private Button  btn_send;
	private Button btn_add;

	// 动态显示录音效果
	private Dialog dialog;
	
	private static double voiceValue = 0.0; // 麦克风获取的音量值

	private ImageView dialog_img;

	// 语音动态播放效果
	// 语音动画控制器
	private Timer mTimer = null;
	// 语音动画控制任务
	private TimerTask mTimerTask = null;

	// 记录语音动画图片
	private int index = 1;
    private AudioAnimationHandler audioAnimationHandler;
    
	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	
	public int  freeCount=0;
	private String money="0";
	private int type;   //0指定，1不指定
	
	private String docPicStr="";
	
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_chart_main);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initParams();
	}

	@Override
	protected void onStart() {
		isVisiable = true;
		super.onStart();
	}

	@Override
	protected void onStop() {
		handler.removeCallbacks(mRunnable);
		isVisiable = false;
		super.onStop();
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			if(msg.arg1==4)
			{
					if(msg.obj instanceof Bitmap&&msg.obj!=null)
					{
						try {
							Bitmap bitmap=(Bitmap) msg.obj;
							String filePath = PictureUtil.saveMyBitmap(bitmap);
//							System.out.println("==[path==="+filePath);
		                    //图片预览
							startViewImage(filePath,null);
							
						}
						catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					}
					
					else
					{
						showToast("获取图片失败！");
					}
			}
			else {
				if (msg.arg1 == 1) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(msg.obj.toString());
						consId = jsonObject.getJSONObject("message").getString(
								"consId");
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (msg.arg1 == 5) {

					String reason=getResources().getString(R.string.network_preoblem);
//					String detialOnCost = "";
//					String noCostNum=null;
//					String nowNum = null;
					int code=-1;
					try {
						JSONObject jsonObject=new JSONObject(msg.obj.toString());
						code=jsonObject.getInt("code");
						reason=jsonObject.getString("reason");
//						int ruleType=jsonObject.getJSONObject("results").getJSONObject("costRule").getInt("ruleType");
//						String costRule=jsonObject.getJSONObject("results").getJSONObject("costRule").getJSONObject("ruleDetial").getString("costRule");
//						 noCostNum=jsonObject.getJSONObject("results").getJSONObject("costRule").getJSONObject("ruleDetial").getString("noCostNum");
//						 nowNum=jsonObject.getJSONObject("results").getString("nowNum");
//						 detialOnCost=jsonObject.getJSONObject("results").getJSONObject("costRule").getJSONObject("ruleDetial").getString("detialOnCost");
//						Log.w("ruleType", ruleType+"");
//						Log.w("costRule", costRule+"");
//						Log.w("noCostNum", noCostNum+"");
//						Log.w("detialOnCost", detialOnCost+"");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					if(code==0)
					{

						freeCount=freeCount-1;
						if(0<freeCount&&freeCount<=2)
						{
							showNoPayDialog(freeCount);
						}
							
//						showToast(reason);
						showItem(0, null, null, null, null);
						contentEditText.setText("");
					}
					
//					else if(code==2)
//					{
//						showPayDialog(",consId,detialOnCost);
//					}
					
					else
					{
						showToast(reason);
					}
					
					
				}

//				else if (msg.arg1 == 6) {
//					int nowNum = 0;
//					int maxNum = 0;
//					try {
//						nowNum = new JSONObject(msg.obj.toString())
//								.getJSONObject("results").getInt("nowNum");
//						maxNum = new JSONObject(msg.obj.toString())
//								.getJSONObject("results").getInt("maxNum");
//						Log.i("nowNum", nowNum + "");
//						Log.i("maxNum", maxNum + "");
//						if (nowNum > 0 && maxNum > 0 && (maxNum - nowNum) >= 0
//								&& (maxNum - nowNum) <= 2) {
//							showToast("免费咨询已用完，继续咨询将收费!");
//						}
//					} catch (JSONException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				} 
				else if (msg.arg1 == 2) {
					JSONObject jsonObject;
					try {
						// 内容
						String commContent;
						// 类型
						String fileType;
						// 文件地址
						String filePath;
						// 来源
						int commUserType;
						// 图片缩略图地址
						String bitFileUrl;
						chatList = new ArrayList<ChatEntity>();
						ChatEntity _ChatEntity;
						jsonObject = new JSONObject(msg.obj.toString());
						Log.i("chatEnty", msg.obj.toString());
						JSONObject myobj=jsonObject.getJSONObject(
								"CommentResult").getJSONObject("messageMap");
						System.out.println(myobj+"");
						docPicStr=Config.getProperty("FILEURL", "")+jsonObject.getJSONObject(
								"CommentResult").getString("attachFileUrl");
						type=myobj.getInt("type");
							
						//money=myobj.getInt("money")+"";
						int isPay=myobj.getInt("isPay");
						int costType=myobj.getInt("costType");
						money = myobj.getString("money");
						freeCount=myobj.getInt("remainNum");
						if(isPay==1)
						{
							if(costType==0)
							{
								freeCount=1;
							}
							
							else if(costType==1)
							{
								freeCount=100000;
							}
						}
						Log.d("freeCount", freeCount+"");
						Log.d("type", type+"");
						Log.d("money", money+"");
						Log.d("isPay", isPay+"");
						Log.d("costType", costType+"");
						
						
						JSONArray jsonArray = jsonObject.getJSONObject(
								"CommentResult").getJSONArray("CommentList");
						for (int i = 0; i < jsonArray.length(); i++) {
							
							_ChatEntity = new ChatEntity();
							_ChatEntity.setCommuserName(jsonArray.getJSONObject(i)
									.getString("doctorName"));
							_ChatEntity.setImageBytes(jsonArray.getJSONObject(i).getString("imageBytes"));
							_ChatEntity.setDoctId(jsonArray.getJSONObject(i)
									.getInt("doctId"));
							_ChatEntity.setIsIntert(1);
							_ChatEntity.setChatTime(jsonArray.getJSONObject(i)
									.getString("commTime"));
//							_ChatEntity
//									.setCommuserName(jsonArray.getJSONObject(i)
//											.getString("commuserName"));
							Log.i("doctorName1", jsonArray.getJSONObject(i)
									.getString("commuserName"));
							_ChatEntity
							.setConsUserId(jsonArray.getJSONObject(i)
									.getInt("consUserId"));
							commContent = jsonArray.getJSONObject(i).getString(
									"commContent");
							_ChatEntity.setContent(commContent);
							fileType = jsonArray.getJSONObject(i).getString(
									"fileType");
							Log.i("filetype", fileType);
							filePath = jsonArray.getJSONObject(i).getString(
									"fileUrl");
							bitFileUrl = jsonArray.getJSONObject(i).getString(
									"bitFileUrl");
							commUserType = jsonArray.getJSONObject(i).getInt(
									"commUserType");

							if (fileType.equals("1")) {
								_ChatEntity.setType(0);
							}
							if (fileType.equals("2")) {
								_ChatEntity.setType(2);
								Log.i("filePath", filePath);
								Log.i("bitfileurl", bitFileUrl);
								_ChatEntity.setPicPath(filePath);
								_ChatEntity.setBitFileUrl(bitFileUrl);
							}
							if (fileType.equals("3")) {

								_ChatEntity.setType(1);
								_ChatEntity.setVoicePath(filePath);
							}

							if (commUserType == 2) {
								_ChatEntity.setComeMsg(true);
							}

							if (commUserType == 1) {
								_ChatEntity.setComeMsg(false);
							}

							chatList.add(_ChatEntity);
						}
						if (chatList != null) {
							chatAdapter = new ChatAdapter(
									OnlineChatActivity.this, chatList);
							chatListView.setAdapter(chatAdapter);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
			super.handleMessage(msg);
		}
	}

	private void initParams() {
		final MyConsultModel myConsultModel = (MyConsultModel) getIntent()
				.getSerializableExtra("myConsultModel");
		IS_BOTTOM_VISIBLE = getIntent().getIntExtra("is_bottom_visible", 1);
		consId = getIntent().getStringExtra("consId");
		handler = new MyHandler();
		getIntent().getStringExtra("docId");
		// userId = getIntent().getStringExtra("userId");
		userId = getSharedPreferences("personCenter", Context.MODE_PRIVATE)
				.getInt("userId", -1) + "";
		Log.i("userId", userId);
		getIntent().getStringExtra("userName");
		getIntent().getStringExtra("userAge");
		getIntent().getStringExtra("userSex");
		getIntent().getStringExtra("title");
		getIntent().getStringExtra("disease");
		params = new HashMap<String, String>();
		params.put("docId", getIntent().getStringExtra("docId"));
		params.put("userId", getIntent().getStringExtra("userId"));
		params.put("username", getIntent().getStringExtra("userName"));
		params.put("userAge", getIntent().getStringExtra("userAge"));
		params.put("userSex", getIntent().getStringExtra("userSex"));
		params.put("title", getIntent().getStringExtra("title"));
		params.put("disease", getIntent().getStringExtra("disease"));
		params.put("disDis", getIntent().getStringExtra("disDis"));
		String filePath = getIntent().getStringExtra("filePath");
		contentEditText = (EditText) this.findViewById(R.id.et_content);
		chatListView = (ListView) this.findViewById(R.id.listview);
		if (IS_BOTTOM_VISIBLE == 0) {
			rl_bottom.setVisibility(View.GONE);
			btn_send.setVisibility(View.GONE);
		}
		chatList = new ArrayList<ChatEntity>();
		if (filePath != null) {
			picPath = filePath;
			// 对图片进行压缩
			file = PictureUtil.getSmallBitmap(getIntent().getStringExtra(
					"filePath"));
		} else {
			file = null;
		}
		if (myConsultModel != null) {

			if (myConsultModel.getConsState() == 1) {
				View addButton = getLayoutInflater().inflate(
						R.layout.ui_add_button_evaluate, null);
				chatListView.addFooterView(addButton, null, false);
				btn_add = (Button) addButton.findViewById(R.id.btn_add);
				btn_add.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(OnlineChatActivity.this,
								AssessActivity.class);
						intent.putExtra("assessType", 4);
						intent.putExtra("consId", myConsultModel.getConsId());
						intent.putExtra("docId",
								myConsultModel.getConsDoctorId());
						intent.putExtra("flagType", 2);
//						startActivityForResult(intent, 0);
						startActivity(intent);
					}
				});
			}

			else if (myConsultModel.getConsState() == 2) {
				View addButton  = getLayoutInflater().inflate(
						R.layout.ui_add_button_evaluate, null);
				chatListView.addFooterView(addButton, null, false);
				btn_add = (Button) addButton.findViewById(R.id.btn_add);
				btn_add.setText("该问题已关闭！");
			}
		}

		if (consId == null) {
			params.put("extFileType", "1");
			toUploadFile("pic_droid.jpg", file, params);
			ChatEntity chatEntity = null;
			chatEntity = new ChatEntity();
			chatEntity.setComeMsg(false);
			chatEntity.setChatTime(DateTools.getCurrentDateTime());
			if (picPath != null) {
				chatEntity.setPicPath(picPath);
			}
			if (file == null) {
				chatEntity.setType(0);
			}
			if (file != null) {
				chatEntity.setType(20);
			}
			if (getIntent().getStringExtra("disDis") != null) {
				chatEntity.setContent(getIntent().getStringExtra("disDis"));
				chatList.add(chatEntity);
				chatAdapter = new ChatAdapter(this, chatList);
				chatListView.setAdapter(chatAdapter);
			}
			
		} else {
//			if(userId.equals("-1"))
//			{
//				showToast("请登录后重试");
//				openActivity(UserLoginActivity.class);
//			}
//			else 
//			{
			getConsListData(consId);
//			}

		}

	}

	private void getConsListData(final String consId) {
		initProgressDialog();
		mRunnable = new Runnable() {
			@Override
			public void run() {
				List<String> params = new ArrayList<String>();
				params.add(consId);
				params.add("0");
				params.add(userId);
				String str = ApiHttpUtil.getMethod(
						Config.getProperty("GETCONSULTCOMMENTHISTORY", ""),
						params);
				Message msg = new Message();
				msg.arg1 = 2;
				msg.obj = str;
				handler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}

	private void initView() {
		rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
		btn_bottom= (RelativeLayout) findViewById(R.id.btn_bottom);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_pic_input = (Button) findViewById(R.id.btn_pic_input);
		btn_text_input = (Button) findViewById(R.id.btn_text_input);
		btn_voice_input = (Button) findViewById(R.id.btn_voice_input);
		btn_start_voice = (Button) findViewById(R.id.btn_start_voice);
		btn_pic_input.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OnlineChatActivity.this,
						SelectPicActivity.class);
				startActivityForResult(intent, TO_SELECT_PHOTO);
			}
		});
		btn_text_input.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_text_input.setVisibility(View.GONE);
				btn_voice_input.setVisibility(View.VISIBLE);
				btn_bottom.setVisibility(View.VISIBLE);
				btn_start_voice.setVisibility(View.GONE);
			}
		});

		btn_voice_input.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_text_input.setVisibility(View.VISIBLE);
				btn_voice_input.setVisibility(View.GONE);
				btn_bottom.setVisibility(View.GONE);
				btn_start_voice.setVisibility(View.VISIBLE);
			}
		});

		btn_start_voice.setOnTouchListener(new OnTouchListener() {
			Long time1 = (long) 1;
			Long time2 = (long) 1;

			@Override
			public  boolean onTouch(View v, MotionEvent event) {
				if(freeCount>0)
				{
				
				
					Date date = new Date();
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						btn_start_voice.setText("松开发送");
						btn_start_voice.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
						time1 = date.getTime();
						if (RECODE_STATE != RECORD_ING) {
							recorder = new AudioRecorder();
							try {
								voice_path = createMediePath();
								recorder.start(voice_path);
								RECODE_STATE = RECORD_ING;
							} catch (IOException e) {
								e.printStackTrace();
							}
							showVoiceDialog(); // 显示录音图像
							// 显示动态录音效果
							new Thread(ImgThread).start();
						}
						break;
					case MotionEvent.ACTION_UP:
						btn_start_voice.setText("按住说话");
						btn_start_voice.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
						time2 = date.getTime();
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						if (time2 - time1 <= 1 * 1000) {
//							if (dialog.isShowing()) {
//								dialog.dismiss();
//							}
							AlertDialog _dialog = new AlertDialog.Builder(
									OnlineChatActivity.this)
									.setTitle("录音时间太短，最少1秒以上！")
									.setPositiveButton("确定", null).show();
							_dialog.setOnDismissListener(new OnDismissListener() {
								@Override
								public void onDismiss(DialogInterface dialog) {
									if (RECODE_STATE == RECORD_ING) {
										try {
											recorder.stop();
											RECODE_STATE = RECODE_ED;
										} catch (IOException e) {
											e.printStackTrace();
										}
									}

								}
							});
						} else {
							if (RECODE_STATE == RECORD_ING) {
								try {
									recorder.stop();
									RECODE_STATE = RECODE_ED;
									voiceValue = 0.0;
									send(1, "voc_dorid.aac", new File(voice_path),
											voice_path, null, "2");
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						break;
					}
				}
				else
				{
					if(event.getAction()==MotionEvent.ACTION_DOWN)
					{
					if(type==1)
					{
						showNoPayDialogInfo();
					}
					else if(type==0)
					{
					showPayDialog(consId, money);
					}
					}
				}
					return false;
			}
		});

		Button Back = (Button) findViewById(R.id.ib_back);
		Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_send = (Button) findViewById(R.id.ib_post);
		btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (contentEditText.getText().toString().trim().length() > 0) {
					if(freeCount>0)
					{
						send(0, null, null, null, null, null);
					}
					
					else
					{
						if(type==1)
						{
							showNoPayDialogInfo();
						}
						else if(type==0)
						{
						showPayDialog(consId, money);
						}
					}
				} else {
					showToast("内容不能为空！");
				}
			}
		});

	}

	
	//录音动画线程
	private Runnable ImgThread = new Runnable() {
		@Override
		public void run() {
			while (RECODE_STATE==RECORD_ING) {
				try {
					Thread.sleep(200);
					if (RECODE_STATE == RECORD_ING) {
						voiceValue = recorder.getAmplitude();
						imgHandle.sendEmptyMessage(1);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	Handler imgHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setDialogImage();
				break;
			default:
				break;
			}
			
		}
	};
	
	
	
	private void toUploadFile(final String fileName, final Bitmap file,
			final Map<String, String> _Params) {
		mRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					Message msg = new Message();
					String str = new ApiHttpUtil().postWithPic(requestURL,
							_Params, fileName, file);
					msg.arg1 = 1;
					msg.obj = str;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		};
		commonThreadStart();

	}

//	private void askFile(final String fileName, final File file,
//			final String extFileType) {
//		initProgressDialog();
//		final Map<String, String> _Params = new HashMap<String, String>();
//		_Params.put("extFileType", extFileType);
//		_Params.put("consId", consId);
//		_Params.put("userId", userId);// 动态获取
//		initProgressDialog();
//		mRunnable = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Message msg = new Message();
//					msg.arg1 = 6;
//					msg.obj = new ApiHttpUtil().post(
//							Config.getProperty("REQUESTFILE", ""), _Params,
//							fileName, file);
//					handler.sendMessage(msg);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		};
//		commonThreadStart();
//	}

	private void askText(final String text,final int type) {
		initProgressDialog();
		final Map<String, String> _Params = new HashMap<String, String>();
		_Params.put("consId", consId);
		_Params.put("userId", userId);// 动态获取
		_Params.put("content", text);// 动态获取
		mRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					Message msg = new Message();
					msg.arg1 = 5;
					msg.arg2 =type;
					msg.obj = new ApiHttpUtil().postMethod(
							Config.getProperty("REQUESTTEXT", ""), _Params);
					handler.sendMessage(msg);
				} catch (Exception e) {
				}
			}
		};
		commonThreadStart();
	}

	private void send(int type, String fileName, File file, String voicePath,
			String picPath, String extFileType) {
		String str = contentEditText.getText().toString();
		params.put("disease", str);
		if (file != null) {
//			askFile(fileName, file, extFileType);
			HashMap<String, String> _Params=new HashMap<String, String>();
			_Params.put("extFileType", extFileType);
			_Params.put("consId", consId);
			_Params.put("userId", userId);// 动态获取
			UplodeProgress uploade=new UplodeProgress(this, Config.getProperty("REQUESTFILE", ""), file, fileName,_Params,type,voicePath,picPath);
			uploade.initUploadPic();
			// askFile(file,"file");
		} else {
//			showItem( type,  fileName,  file,  voicePath,
//					 picPath);
			askText(str,type);
//			contentEditText.setText("");
		}
		// Log.i("disease", params.get("disease"));
	}
	
	
	public void showItem(int type, String fileName, File file, String voicePath,
			String picPath)
	{
		ChatEntity chatEntity = new ChatEntity();
		chatEntity.setType(type);
		chatEntity.setVoicePath(voicePath);
		chatEntity.setPicPath(picPath);
		chatEntity.setChatTime(DateTools.getCurrentDateTime());
		chatEntity.setContent(contentEditText.getText().toString());
		chatEntity.setComeMsg(false);
		chatList.add(chatEntity);
		// chatAdapter=new ChatAdapter(getApplicationContext(), chatList);
		// chatListView.setAdapter(chatAdapter);
		// chatListView.setSelection(chatList.size() - 1);
		if (chatAdapter == null) {
			chatAdapter = new ChatAdapter(this, chatList);
			chatListView.setAdapter(chatAdapter);
		} else {
			chatAdapter.notifyDataSetChanged();
		}
		chatListView.setSelection(chatList.size() - 1);
	}

	
	
	private void startPlaying(String myVoicePath) {
		try {
			mediaPlayer = null;
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mediaPlayer.release();
					stopTimer();
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			});
			// 设置要播放的文件
			mediaPlayer.setDataSource(myVoicePath);
			mediaPlayer.prepare();
			mediaPlayer.start();
			
			//显示动态播语音放效果
			showVoiceDialog();
			playAudioAnimation();
			 

		} catch (Exception e) {
			if (null != mediaPlayer) {
				mediaPlayer.stop();
				mediaPlayer.release();
				stopTimer();
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				e.printStackTrace();
			}
		}
	}
	// //录音时显示Dialog
	void showVoiceDialog() {
		dialog = new Dialog(OnlineChatActivity.this, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_dialog);
		dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
		dialog.show();
	}
	
	// 录音Dialog图片随声音大小切换
	void setDialogImage() {
		if (voiceValue < 200.0) {
			 dialog_img.setImageResource(R.drawable.record_animate_01);
			 }else if (voiceValue > 200.0 && voiceValue < 3200) {
			 dialog_img.setImageResource(R.drawable.record_animate_02);
			 }else if (voiceValue > 3200.0 && voiceValue < 14000.0) {
			 dialog_img.setImageResource(R.drawable.record_animate_03);
			 }else if (voiceValue > 14000.0 && voiceValue < 28000.0) {
			 dialog_img.setImageResource(R.drawable.record_animate_04);
			 }else if (voiceValue > 28000.0) {
			 dialog_img.setImageResource(R.drawable.record_animate_05);
		}
	}

	
	/**
	 * 播放语音图标动画
	 */
	private void playAudioAnimation() {
	//定时器检查播放状态
	stopTimer();
	mTimer = new Timer();
		// 将要关闭的语音图片归位
		if (audioAnimationHandler != null) {
			Message msg = new Message();
			msg.what = 0;
			audioAnimationHandler.sendMessage(msg);
		}
		audioAnimationHandler = new AudioAnimationHandler();
		mTimerTask = new TimerTask() {
			public boolean hasPlayed = false;
			@Override
			public void run() {
				if (mediaPlayer.isPlaying()) {
					hasPlayed = true;
					index = (index + 1) % 3;
					Message msg = new Message();
					msg.what = index;
					audioAnimationHandler.sendMessage(msg);
				} else {
					// 当播放完时
					Message msg = new Message();
					msg.what = 0;
					audioAnimationHandler.sendMessage(msg);
					// 播放完毕时需要关闭Timer等
					if (hasPlayed) {
						stopTimer();
					}
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}
		};
		// 调用频率为500毫秒一次
		mTimer.schedule(mTimerTask, 0, 500);
	}
	

	/**
	 * 停止
	 */
	private void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}

	}
	
	@SuppressLint("HandlerLeak")
	class AudioAnimationHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 根据msg.what来替换图片，达到动画效果
			switch (msg.what) {
			case 0:
				 dialog_img.setImageResource(R.drawable.record_animate_01);
				break;
			case 1:
				 dialog_img.setImageResource(R.drawable.record_animate_02);
				break;
			case 2:
				 dialog_img.setImageResource(R.drawable.record_animate_05);
				break;
			default:
				 dialog_img.setImageResource(R.drawable.record_animate_01);
				break;
			}
		}
	}
	
	
	/****
	 * 创建音频文件
	 * 
	 * @return
	 * @throws IOException 
	 */
	public String createMediePath() throws IOException {
		// 创建一个临时文件来存储音频
		String transationId = String.valueOf(new Date().getTime());

		// 首先检查用户SD卡是否可用，如果不可用，则先写入内存中，如果存在，则写入SD卡
		String state = android.os.Environment.getExternalStorageState();
		String filePath = Environment.getExternalStorageDirectory().getPath();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
		}
		else
		{
		File file = new File(filePath + "/find_doctor/voice_cache/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();
		}
		
		voice_path = filePath +  "/find_doctor/voice_cache/"+ transationId
				+ ".aac";
		File file2=new File(voice_path);
		if (!file2.exists()) {
			file.createNewFile();
		}
		}
		return voice_path;
	}

	private void commonThreadStart() {
		if (mRunnable != null) {
			mThread = new Thread(mRunnable);
			mThread.start();
		}
	}

	public void getBitMap(final String url,final int arg1) {
		 initProgressDialog();
		mRunnable = new Runnable() {
			@Override
			public void run() {
				URL myFileUrl = null;
				Message msg = new Message();
				InputStream is = null;
				try {
					myFileUrl = new URL(url);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				try {
					myFileUrl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) myFileUrl
							.openConnection();
					conn.setDoInput(true);
					conn.connect();
					is = conn.getInputStream();
					if (is != null) {
						readBitmap(is);
						PictureUtil.saveMyBitmap(mBitmap,url.split("/")[url.split("/").length-1]);
					}
					msg.arg1 = arg1;
					msg.obj = mBitmap;
					handler.sendMessage(msg);

				} catch (IOException e) {
					e.printStackTrace();
				} finally {

					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
		commonThreadStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			String _picPath = data
					.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			// Log.i("pic", "最终选择的图片=" + _picPath);
			if (_picPath != null) {
				if(freeCount>0)
				{
				File file = new File(_picPath);
				send(2, "pic_droid.jpg", file, null, _picPath, "1");
				}
				else
				{
					if(type==1)
					{
						showNoPayDialogInfo();
					}
					else if(type==0)
					{
					showPayDialog(consId, money);
					}
				}
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
		
		if (resultCode == ResultCodeCategory.RESULT_CODE_EVA) {
			if(btn_add!=null)
			{
			btn_add.setClickable(false);
			btn_add.setText("该问题已关闭!");
			rl_bottom.setVisibility(View.GONE);
			btn_send.setVisibility(View.GONE);
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void readBitmap(String _path) throws IOException {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 3;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		InputStream _is = null;

		try {
			_is = new FileInputStream(_path);
			mBitmap = BitmapFactory.decodeStream(_is, null, opt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			_is.close();
		}
	}


	public void readBitmap(InputStream _InputStream) throws IOException {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 3;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		mBitmap = BitmapFactory.decodeStream(_InputStream, null, opt);
	}
	
	
	private void showPayDialog(final String id,final String detialOnCost)
	{
		View view=LayoutInflater.from(this).inflate(R.layout.progress_dialog_pay, null);
    	final Dialog dialog=new Dialog(this, R.style.Translucent_NoTitle);
    	dialog.setContentView(view);
    	TextView tv_progress=(TextView) view.findViewById(R.id.tv_progress);
    	
    	SpannableStringBuilder builder = new SpannableStringBuilder("您的免费咨询条数已经用完，继续咨询需要付费:"+detialOnCost+"元");
		builder.setSpan(new ForegroundColorSpan(Color.RED), 22, String.valueOf(detialOnCost).length() + 22,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    	tv_progress.setText(builder);
    	
    	tv_progress.setText(builder);
    	Button btn_ok=(Button) view.findViewById(R.id.btn_ok);
    	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
    	btn_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
    	btn_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toPay(id, detialOnCost);
				dialog.cancel();
			}
		});
    	dialog.setCanceledOnTouchOutside(false);
    	dialog.show();
	}
	
	public void showNoPayDialog(int count)
	{
		View view=LayoutInflater.from(this).inflate(R.layout.progress_dialog_nopay, null);
    	final Dialog dialog=new Dialog(this, R.style.Translucent_NoTitle);
    	dialog.setContentView(view);
    	TextView tv_progress=(TextView) view.findViewById(R.id.tv_progress);
    	SpannableStringBuilder builder = new SpannableStringBuilder("您还有"+count+"次追问的机会");
		builder.setSpan(new ForegroundColorSpan(Color.RED), 3, String.valueOf(count).length() + 3,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    	tv_progress.setText(builder);
    	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
    	btn_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
    	dialog.setCanceledOnTouchOutside(false);
    	dialog.show();
	}
	
	private void showNoPayDialogInfo()
	{
		View view=LayoutInflater.from(this).inflate(R.layout.progress_dialog_nopay, null);
    	final Dialog dialog=new Dialog(this, R.style.Translucent_NoTitle);
    	dialog.setContentView(view);
    	TextView tv_progress=(TextView) view.findViewById(R.id.tv_progress);
    	tv_progress.setText("您的免费咨询条数已用完!");
    	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
    	btn_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
    	dialog.setCanceledOnTouchOutside(false);
    	dialog.show();
	}

	class ViewHolder {
		TextView tv_doctName, tv_doctPosi, tv_doctHospName, tv_doctSpecialty,
				tv_score, tv_scoreNum;
		RatingBar ratingbar_score;
		ImageView iv_register_talk;
		// ImageView iv_online_talk,iv_register_talk,iv_tel_talk;
	}
	
//	支付宝操作
	
	
	private  void toPay(String id,String money)
	{
		try {
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo( id, money);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(OnlineChatActivity.this, payHanlder);
					
					//设置为沙箱模式，不设置默认为线上环境
					//alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					payHanlder.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(OnlineChatActivity.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private String getNewOrderInfo(String id,String money) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(getOutTradeNo());
		sb.append("\"&subject=\"");
		sb.append("在线咨询");
		sb.append("\"&body=\"");
		sb.append(id);
		sb.append("\"&total_fee=\"");
		sb.append(money);
		sb.append("\"&notify_url=\"");
		sb.append(URLEncoder.encode(Config.getProperty("GETALICOMMENTRESULT", "")));
		

		// 网址需要做URL编码
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}
	
	
	
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		Log.d(TAG, "outTradeNo: " + key);
		return key;
	}
	
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	Handler payHanlder = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			
			if(msg.obj!=null&&msg.obj.toString().length()>0)
			{
				Result result = new Result((String) msg.obj);
				switch (msg.what) {
				case RQF_PAY:
					Log.w("pay resul",result.getResult().trim()+"test");
					if(result.getResult().trim().length()==0)
					{
						getConsListData(consId);
					}
					
					else if(result.getResult().trim().length()>0)
					{
						Toast.makeText(OnlineChatActivity.this, result.getResult(),
								Toast.LENGTH_SHORT).show();
					}
				case RQF_LOGIN: {
					if(result.getResult().trim().length()!=0)
					{
						Toast.makeText(OnlineChatActivity.this, result.getResult(),
								Toast.LENGTH_SHORT).show();
					}
				}
					break;
				default:
					break;
				}
			}
		};
	};
	
	
	
	

	// 数据适配器

	
//	class ViewHolder1
//	{
//		RelativeLayout relative_left_text;
//		RelativeLayout relative_left_voice;
//		RelativeLayout relative_left_pic;
//
//		TextView tv_commuser_name_text;
//		TextView tv_commuser_name_voice;
//		TextView tv_commuser_name_pic;
//
//		TextView timeTextView ;
//		TextView contentTextView;
//		ImageView userImageView1;
//		ImageView userImageView2;
//		ImageView userImageView3;
//		
//		TextView tv_left_voice;
//				
//				 ImageView iv_left_pic;
//	}
	
//	class ViewHolder2
//	{
//		RelativeLayout relative_right_text;
//		RelativeLayout relative_right_voice;
//		RelativeLayout relative_right_pic;
//		TextView timeTextView;
//		TextView contentTextView;
//		ImageView iv_pic;
//		TextView tv_voice;
//	}
	@SuppressLint("NewApi")
	private class ChatAdapter extends BaseAdapter {
		private Context context = null;
		private List<ChatEntity> chatList = null;
		private LayoutInflater inflater = null;
		private int COME_MSG = 0;
		private int TO_MSG = 1;

		public ChatAdapter(Context context, List<ChatEntity> chatList) {
			this.context = context;
			this.chatList = chatList;
			inflater = LayoutInflater.from(this.context);
		}

		@Override
		public int getCount() {
			return chatList.size();
		}

		@Override
		public Object getItem(int position) {
			return chatList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			ChatEntity entity = chatList.get(position);
			if (entity.isComeMsg()) {
				return COME_MSG;
			} else {
				return TO_MSG;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (chatList.get(position).isComeMsg()) {
//				ViewHolder1 hodler1;
//				if (convertView==null) {
					convertView = inflater.inflate(R.layout.chat_from_item, null);
					RelativeLayout relative_left_text;
					RelativeLayout relative_left_voice;
					RelativeLayout relative_left_pic;

					TextView tv_commuser_name_text;
					TextView tv_commuser_name_voice;
					TextView tv_commuser_name_pic;

					TextView timeTextView ;
					TextView contentTextView;
					ImageView userImageView1;
					ImageView userImageView2;
					ImageView userImageView3;
					
					TextView tv_left_voice;
							
							 ImageView iv_left_pic;
//					hodler1=new ViewHolder1();
					relative_left_text = (RelativeLayout) convertView
							.findViewById(R.id.relative_left_text);
					 relative_left_voice = (RelativeLayout) convertView
							.findViewById(R.id.relative_left_voice);
					 relative_left_pic = (RelativeLayout) convertView
							.findViewById(R.id.relative_left_pic);
					 tv_commuser_name_text = (TextView) convertView
							.findViewById(R.id.tv_commuser_name_text);
					 tv_commuser_name_voice = (TextView) convertView
							.findViewById(R.id.tv_commuser_name_voice);
					 tv_commuser_name_pic = (TextView) convertView
							.findViewById(R.id.tv_commuser_name_pic);

					 timeTextView = (TextView) convertView
							.findViewById(R.id.tv_time);
					 contentTextView = (TextView) convertView
							.findViewById(R.id.tv_left_content);
					 userImageView1 = (ImageView) convertView
							.findViewById(R.id.iv_user_image1);
					 userImageView2 = (ImageView) convertView
							.findViewById(R.id.iv_user_image2);
					 userImageView3 = (ImageView) convertView
							.findViewById(R.id.iv_user_image3);
					
					tv_left_voice= (TextView) convertView
							.findViewById(R.id.tv_left_voice);
							
					iv_left_pic= (ImageView) convertView
							.findViewById(R.id.iv_left_pic);
//					convertView.setTag(hodler1);
//				}
				
//				else
//				{
//					=(ViewHolder1) convertView.getTag();
//		relative_left_text.setVisibility(View.GONE);
//				relative_left_voice.setVisibility(View.GONE);
//				relative_left_pic.setVisibility(View.GONE);
				
				if (chatList.get(position).getType() == 0) {
					relative_left_text.setVisibility(View.VISIBLE);

				}
				if (chatList.get(position).getType() == 1) {

		    relative_left_voice.setVisibility(View.VISIBLE);
				}
				if (chatList.get(position).getType() == 2) {

					relative_left_pic.setVisibility(View.VISIBLE);

				}
				if (chatList.get(position).getType() == 10) {

				relative_left_text.setVisibility(View.VISIBLE);
					relative_left_voice.setVisibility(View.VISIBLE);
				}
				if (chatList.get(position).getType() == 12) {
					relative_left_voice.setVisibility(View.VISIBLE);
					relative_left_pic.setVisibility(View.VISIBLE);
				}

				if (chatList.get(position).getType() == 20) {
					relative_left_text.setVisibility(View.VISIBLE);
					relative_left_pic.setVisibility(View.VISIBLE);
				}
				
//				if(docPicStr.length()>0)
//				{
//					byte[] tempb = Base64.decode(docPicStr, Base64.DEFAULT);
//					userImageView1.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0 ,tempb.length));
//					userImageView2.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0 ,tempb.length));
//					userImageView3.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0 ,tempb.length));
//					
//				}
				
				
				ImageManager2.from(context).displayImage(userImageView1, docPicStr, R.drawable.touxiang);
				ImageManager2.from(context).displayImage(userImageView2, docPicStr, R.drawable.touxiang);
				ImageManager2.from(context).displayImage(userImageView3, docPicStr, R.drawable.touxiang);

				userImageView1.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(OnlineChatActivity.this,
								DoctorHomeActivity.class);
						it.putExtra("doctId", chatList.get(position)
								.getDoctId());
						startActivity(it);
					}
				});

				userImageView2.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(OnlineChatActivity.this,
								DoctorHomeActivity.class);
						it.putExtra("doctId", chatList.get(position)
								.getDoctId());
						startActivity(it);
					}
				});

				userImageView3.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(OnlineChatActivity.this,
								DoctorHomeActivity.class);
						it.putExtra("doctId", chatList.get(position)
								.getDoctId());
						startActivity(it);

					}
				});

			 
			 if(chatList.get(position).getIsIntert()==1)
	         {
				 if(chatList.get(position).getImageBytes().length()>0)
				  {
					 byte[] tempb = Base64.decode(chatList.get(position).getImageBytes(), Base64.DEFAULT);
					 iv_left_pic.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0 ,tempb.length));
				  }
	         }
	         
	         else 
	         {
	        	 if(chatList.get(position).getPicPath()!=null)
	        	 {
	        	 try {
	        		 iv_left_pic.setImageBitmap(PictureUtil.readBitmapSmall(chatList.get(position).getPicPath()));
					} catch (IOException e) {
						e.printStackTrace();
					}
	        	 }
	         }

			  iv_left_pic.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (chatList.get(position).getPicPath() != null) {

							if (chatList.get(position).getIsIntert() == 1&&String.valueOf(chatList.get(position).getConsUserId()).equals(userId)) {

								String fileName=chatList.get(position).getPicPath().split("/")[chatList.get(position).getPicPath().split("/").length-1];
								try {
									if(PictureUtil.getMyBitmap(fileName)!=null)
									{
										startViewImage(PictureUtil.PIC_CACHE_PATH+fileName,null);
									
									}
									
									else 
									{
										getBitMap(Config.getProperty("FILEURL", "")
												+ chatList.get(position).getPicPath(),4);

									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							else if(chatList.get(position).getIsIntert() == 1&&!String.valueOf(chatList.get(position).getConsUserId()).equals(userId))
							{
								showToast("此图片涉及隐私问题,不能查看!");
							}
							else {

								startViewImage(chatList.get(position)
										.getPicPath(),null);
							}

						}

					}
				});
				tv_left_voice.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.i("voice intert", Config.getProperty("FILEURL", "")
								+ chatList.get(position).getVoicePath());

						if (chatList.get(position).getVoicePath() != null) {
							if (chatList.get(position).getIsIntert() == 1&&String.valueOf(chatList.get(position).getConsUserId()).equals(userId)) {
								if (chatList.get(position).getVoicePath()
										.length() > 0) {
									startPlaying(Config.getProperty("FILEURL",
											"")
											+ chatList.get(position)
													.getVoicePath());
								}

							} 
							
							else if(chatList.get(position).getIsIntert() == 1&&!String.valueOf(chatList.get(position).getConsUserId()).equals(userId))
							{
								showToast("此语音涉及隐私问题,不能播放!");
							}
							else {
								if (chatList.get(position).getVoicePath()
										.length() > 0) {
									startPlaying(chatList.get(position)
											.getVoicePath());
								}
								Log.i("voice", "voice play");
							}

						}

					}
				});
				timeTextView.setText(chatList.get(position).getChatTime()
						.substring(0, 19).split(" ")[0]
						+ "\n"
						+ chatList.get(position).getChatTime().substring(0, 19)
								.split(" ")[1]);
				contentTextView.setText(chatList.get(position).getContent());
				Log.i("doctorName2", chatList.get(position)
						.getCommuserName());
				tv_commuser_name_text.setText(chatList.get(position)
						.getCommuserName());
				tv_commuser_name_voice.setText(chatList.get(position)
						.getCommuserName());
				tv_commuser_name_pic.setText(chatList.get(position)
						.getCommuserName());

			} else {
//				ViewHolder2 holder2;
//				if(convertView==null)
//				{	
					convertView = inflater.inflate(R.layout.chat_to_item, null);
					RelativeLayout relative_right_text;
					RelativeLayout relative_right_voice;
					RelativeLayout relative_right_pic;
					TextView timeTextView;
					TextView contentTextView;
					ImageView iv_pic;
					TextView tv_voice;
//					holder2=new ViewHolder2();
					 relative_right_text = (RelativeLayout) convertView
						.findViewById(R.id.relative_right_text);
					 relative_right_voice = (RelativeLayout) convertView
						.findViewById(R.id.relative_right_voice);
					 relative_right_pic = (RelativeLayout) convertView
						.findViewById(R.id.relative_right_pic);
					 timeTextView = (TextView) convertView
						.findViewById(R.id.tv_time);
					 contentTextView = (TextView) convertView
						.findViewById(R.id.tv_content);
					 iv_pic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
					 tv_voice = (TextView) convertView
						.findViewById(R.id.tv_voice);
//					convertView.setTag(holder2);
//				}
				
//				else
//				{
//					 holder2 = (ViewHolder2) convertView.getTag();
//				}

			
				relative_right_text.setVisibility(View.GONE);
				relative_right_voice.setVisibility(View.GONE);
				relative_right_pic.setVisibility(View.GONE);
				
				if (chatList.get(position).getType() == 0) {

					relative_right_text.setVisibility(View.VISIBLE);
				}
				if (chatList.get(position).getType() == 1) {

					relative_right_voice.setVisibility(View.VISIBLE);
				}
				if (chatList.get(position).getType() == 2) {

					relative_right_pic.setVisibility(View.VISIBLE);

				}
				if (chatList.get(position).getType() == 10) {

					relative_right_text.setVisibility(View.VISIBLE);
					relative_right_voice.setVisibility(View.VISIBLE);
				}
				if (chatList.get(position).getType() == 12) {
					relative_right_voice.setVisibility(View.VISIBLE);
					relative_right_pic.setVisibility(View.VISIBLE);
				}

				if (chatList.get(position).getType() == 20) {
					relative_right_text.setVisibility(View.VISIBLE);
					relative_right_pic.setVisibility(View.VISIBLE);
				}

			
				  if(chatList.get(position).getIsIntert()==1)
			         {
					  if(chatList.get(position).getImageBytes().length()>0)
					  {
						  byte[] tempb = Base64.decode(chatList.get(position).getImageBytes(), Base64.DEFAULT);
						  iv_pic.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0 ,tempb.length));
					  }
			        	 
			         }
			         
			         else 
			         {
			        	 if(chatList.get(position).getPicPath()!=null)
			        	 {
			        	 try {
 
			        		 iv_pic.setImageBitmap(PictureUtil.readBitmapSmall(chatList.get(position).getPicPath()));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        	 }
			         }
		         
		        
				  iv_pic.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (chatList.get(position).getPicPath() != null) {

							if (chatList.get(position).getIsIntert() == 1&&String.valueOf(chatList.get(position).getConsUserId()).equals(userId)) {

								String fileName=chatList.get(position).getPicPath().split("/")[chatList.get(position).getPicPath().split("/").length-1];
								try {
									if(PictureUtil.getMyBitmap(fileName)!=null)
									{
										startViewImage(PictureUtil.PIC_CACHE_PATH+fileName,null);
									
									}
									
									else 
									{
										getBitMap(Config.getProperty("FILEURL", "")
												+ chatList.get(position).getPicPath(),4);

									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							else if(chatList.get(position).getIsIntert() == 1&&!String.valueOf(chatList.get(position).getConsUserId()).equals(userId))
							{
								showToast("此图片涉及隐私问题,不能查看!");
							}
							
							else {
								startViewImage(chatList.get(position)
										.getPicPath(),null);
							}

						}

					}
				});

			
				  tv_voice.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.i("voice intert", Config.getProperty("FILEURL", "")
								+ chatList.get(position).getVoicePath());

						if (chatList.get(position).getVoicePath() != null) {
							if (chatList.get(position).getIsIntert() == 1&&String.valueOf(chatList.get(position).getConsUserId()).equals(userId)) {
								if (chatList.get(position).getVoicePath()
										.length() > 0) {
									startPlaying(Config.getProperty("FILEURL",
											"")
											+ chatList.get(position)
													.getVoicePath());
								}

							}
							else if(chatList.get(position).getIsIntert() == 1&&!String.valueOf(chatList.get(position).getConsUserId()).equals(userId))
							{
								showToast("此语音涉及隐私问题,不能播放!");
							}
							
							else {
								if (chatList.get(position).getVoicePath()
										.length() > 0) {
									startPlaying(chatList.get(position)
											.getVoicePath());
								}
								Log.i("voice", "voice play");
							}

						}

					}
				});
				  timeTextView.setText(chatList.get(position).getChatTime()
						.substring(0, 19).split(" ")[0]
						+ "\n"
						+ chatList.get(position).getChatTime().substring(0, 19)
								.split(" ")[1]);
				  contentTextView.setText(chatList.get(position).getContent());
			}
			return convertView;
			
		}
	

	}
	
	
	/****
	 * 图片预览
	 */
//	private void startViewImage(String filePath,Bitmap mBitmap)
//	{
//		Intent it = new Intent(OnlineChatActivity.this,
//				ImageViewActivity.class);
//		if(null != filePath && filePath.length()>0)
//		{
//		  it.putExtra("filePath",filePath);
//		}else if(null != mBitmap)
//		{
//		  it.putExtra("bitImag",mBitmap);
//		}
//		startActivity(it);
//	}
	
	private void startViewImage(String filePath,Bitmap mBitmap)
	{
		if(isVisiable)
		{
		Intent it = new Intent(OnlineChatActivity.this,
				ImageShowActivity.class);
		if(null != filePath && filePath.length()>0)
		{
		  it.putExtra("filePath",filePath);
		}else if(null != mBitmap)
		{
		  it.putExtra("bitImag",mBitmap);
		}
		startActivity(it);
		}
	}
	

		

}