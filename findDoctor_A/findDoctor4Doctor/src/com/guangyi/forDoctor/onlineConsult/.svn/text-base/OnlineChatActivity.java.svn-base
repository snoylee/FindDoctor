package com.guangyi.forDoctor.onlineConsult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.config.Config;
import com.guangyi.forDoctor.custview.SelectPicActivity;
import com.guangyi.forDoctor.custview.UplodeProgressActivity;
import com.guangyi.forDoctor.http.ApiHttpUtil;
import com.guangyi.forDoctor.imageview.ImageShowActivity;
import com.guangyi.forDoctor.model.ChatEntity;
import com.guangyi.forDoctor.model.Doctor;
import com.guangyi.forDoctor.utils.AudioRecorder;
import com.guangyi.forDoctor.utils.DateTools;
import com.guangyi.forDoctor.utils.PictureUtil;
//import android.media.MediaRecorder;
//import android.media.MediaRecorder.OnErrorListener;

public class OnlineChatActivity extends BasicActivity {

	public static final int TO_SELECT_PHOTO = 3;
	private EditText contentEditText = null;
	private ListView chatListView = null;
	private List<ChatEntity> chatList = null;
	private ChatAdapter chatAdapter = null;
	private Button btn_text_input, btn_voice_input, btn_pic_input,
			btn_start_voice;
	private TextView tv_sex, tv_age, tv_disease;
	private int IS_BOTTOM_VISIBLE = 1;// 顶部回复是否可见，0 不可见，1可见

	// private MediaRecorder mediaRecorder;
	private String voice_path;
	private String consId;
	private Handler handler;

	private int type; // 问题类型 1,2,3
	private RelativeLayout rl_bottom,btn_bottom;
	private Button btn_claim, ib_post;
	private String doctId;
	public boolean isVisiable = false;
	private Runnable mRunnable;
	private Thread mThread;
	// private Bitmap bitmap;
	// private boolean isPrepared = false;
	private static int RECORD_NO = 0; // 不在录音
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音
	private static int RECODE_STATE = 0; // 录音的状态
	// private Dialog dialog;
	private AudioRecorder recorder;
	private MediaPlayer mediaPlayer;
	private Bitmap mBitmap;;
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
	
	private int consState=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_chart_main);
		SysApplication.getInstance().addActivity(this);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initParams();
		initView();

	}

	private void commonThreadStart() {
		if (mRunnable != null) {
			mThread = new Thread(mRunnable);
			mThread.start();
		}
	}

	@Override
	protected void onStart() {
		isVisiable = true;
		super.onStart();
	}

	protected void onStop() {
		// cancleMyDialog();
		handler.removeCallbacks(mRunnable);
		isVisiable = false;
		super.onStop();
	}

	// private void showMyDialog() {
	// if (isVisiable) {
	// if (alertDialog == null) {
	// alertDialog = new AlertDialog.Builder(this).create();
	// alertDialog.setOnDismissListener(new OnDismissListener() {
	// @Override
	// public void onDismiss(DialogInterface dialog) {
	// if (!mBitmap.isRecycled()) {
	// mBitmap.recycle();
	// System.gc();
	// }
	//
	// }
	// });
	// }
	// // alertDialog.setTitle("图片浏览");
	//
	// if (mBitmap != null) {
	// mImageView.setImageBitmap(mBitmap);
	// alertDialog.setView(mImageView);
	// alertDialog.show();
	// }
	//
	// }
	//
	// }
	//
	// private void cancleMyDialog() {
	// if (alertDialog != null) {
	// alertDialog.dismiss();
	// }
	//
	// }

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			if (msg.arg1 == 4) {
				if (msg.obj instanceof Bitmap && msg.obj != null) {
					try {
						Bitmap bitmap = (Bitmap) msg.obj;
						String filePath = PictureUtil.saveMyBitmap(bitmap);
						// System.out.println("==[path==="+filePath);
						// 图片预览
						startViewImage(filePath, null);

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

				else {
					showToast("获取图片失 败！");
				}

			} else {
				Log.i("jsonString", msg.obj.toString());
				if (msg.obj.toString().equals(ApiHttpUtil.CONNTIMEOUT)) {
					showToast(getResources().getString(R.string.conntimeout));
				}

				else if (msg.obj.toString().equals(ApiHttpUtil.SOCONNTIMEOUT)) {
					showToast(getResources().getString(R.string.soconntimeout));
				}

				else {
					// 添加标题内容
					try {
						JSONObject _JsonObject = new JSONObject(
								msg.obj.toString());
						String tv_sex_str = _JsonObject.getJSONObject(
								"CommentResult").getString("userSex");
						String tv_age_str = _JsonObject.getJSONObject(
								"CommentResult").getString("userAge");
						String tv_disease_str = _JsonObject.getJSONObject(
								"CommentResult").getString("userDisease");
						// if(tv_sex_str.equals("0"))
						// {
						// tv_sex.setText("男");
						// }
						// if(tv_sex_str.equals("1"))
						// {
						// tv_sex.setText("女");
						// }
						// if(tv_sex_str.equals("2"))
						// {
						// tv_sex.setText("未知");
						// }
						if (tv_sex_str != null) {
							tv_sex.setText(tv_sex_str);
						}
						if (!tv_age_str.equals("") && tv_age_str != null
								&& tv_age_str != "null") {
							tv_age.setText(tv_age_str + "岁");
						}
						if (!tv_disease_str.equals("")
								&& tv_disease_str != null) {
							tv_disease.setText("所患疾病:" + tv_disease_str);
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (msg.arg1 == 1) {

						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(msg.obj.toString());
							consId = jsonObject.getJSONObject("message")
									.getString("consId");

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

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
							new ArrayList<ChatEntity>();
							ChatEntity _ChatEntity;
							jsonObject = new JSONObject(msg.obj.toString());
							Log.i("chatEnty", msg.obj.toString());

							JSONArray jsonArray = jsonObject.getJSONObject(
									"CommentResult")
									.getJSONArray("CommentList");
							for (int i = 0; i < jsonArray.length(); i++) {

								_ChatEntity = new ChatEntity();
								_ChatEntity.setImageBytes(jsonArray
										.getJSONObject(i).getString(
												"imageBytes"));
								_ChatEntity.setIsIntert(1);
								_ChatEntity
										.setChatTime(jsonArray.getJSONObject(i)
												.getString("commTime"));
								_ChatEntity.setCommuserName(jsonArray
										.getJSONObject(i).getString(
												"commuserName"));

								commContent = jsonArray.getJSONObject(i)
										.getString("commContent");
								_ChatEntity.setContent(commContent);
								fileType = jsonArray.getJSONObject(i)
										.getString("fileType");
								filePath = jsonArray.getJSONObject(i)
										.getString("fileUrl");
								// Log.i("filePath", filePath);
								// Log.i("fileType", fileType+"");
								commUserType = jsonArray.getJSONObject(i)
										.getInt("commUserType");
								if (fileType.equals("1")) {
									_ChatEntity.setType(0);
								}
								if (fileType.equals("2")) {
									_ChatEntity.setType(2);
									Log.i("filePath", filePath);
									_ChatEntity.setPicPath(filePath);

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

							if (chatList.size() > 0) {
								if (chatAdapter == null) {
									chatAdapter = new ChatAdapter(
											OnlineChatActivity.this, chatList);
									chatListView.setAdapter(chatAdapter);
								} else {
									chatAdapter.notifyDataSetChanged();
								}

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					else if (msg.arg1 == 3) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(msg.obj.toString());
							String reson = jsonObject.getString("reason");
							Log.i("json", msg.obj.toString());
							btn_claim.setVisibility(View.GONE);
//							contentEditText.setVisibility(View.VISIBLE);
							btn_bottom.setVisibility(View.VISIBLE);
							btn_pic_input.setVisibility(View.VISIBLE);
							btn_voice_input.setVisibility(View.VISIBLE);
							showToast(reson);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					else if (msg.arg1 == 5) {

						String reason=getResources().getString(R.string.network_preoblem);
						int code=-1;
						try {
							JSONObject jsonObject=new JSONObject(msg.obj.toString());
							Log.i("text_str------->", msg.obj.toString()+"");
							code=jsonObject.getInt("code");
							reason=jsonObject.getString("reason");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						if(code==0)
						{

							showItem(0, null, null, null, null);
						}
						
						else
						{
							showToast(reason);
						}
						
						
					}

				}
			}
			super.handleMessage(msg);
		}
	}

	private void initParams() {
		IS_BOTTOM_VISIBLE = getIntent().getIntExtra("is_bottom_visible", 1);
		consState=getIntent().getIntExtra("consState", 0);
		consId = getIntent().getStringExtra("consId");
		doctId = getCustomSharedPreference().getString(Doctor.DOCTID, "");
		type = getIntent().getExtras().getInt("type");
		
		
		chatList = new ArrayList<ChatEntity>();
		handler = new MyHandler();
		if (consId != null) {
			Log.i("consId", consId);
			getConsListData(consId);
		}

	}

	private void getConsListData(final String consId) {
		initProgressDialog();
		mRunnable = new Runnable() {

			@Override
			public void run() {
				List<String> params = new ArrayList<String>();
				params.add(consId);
				params.add("1");
				params.add("-1");
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

	class MyTask extends TimerTask {
		@Override
		public void run() {
			Message message = new Message();
			message.what = 5;
			handler.sendMessage(message);
		}
	} // 定时任务

	private void initView() {
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_age = (TextView) findViewById(R.id.tv_age);
		tv_disease = (TextView) findViewById(R.id.tv_disease);
		chatListView = (ListView) findViewById(R.id.listview);
		contentEditText = (EditText) findViewById(R.id.et_content);
		rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
		btn_bottom= (RelativeLayout) findViewById(R.id.btn_bottom);
		btn_claim = (Button) findViewById(R.id.btn_claim);
		btn_pic_input = (Button) findViewById(R.id.btn_pic_input);
		btn_text_input = (Button) findViewById(R.id.btn_text_input);
		btn_voice_input = (Button) findViewById(R.id.btn_voice_input);
		btn_start_voice = (Button) findViewById(R.id.btn_start_voice);
		ib_post = (Button) findViewById(R.id.ib_post);
		
		
		if (IS_BOTTOM_VISIBLE == 0) {
			rl_bottom.setVisibility(View.GONE);
		}
		if(consState==2)
		{
			rl_bottom.setVisibility(View.GONE);
			View addButton  = getLayoutInflater().inflate(
					R.layout.ui_add_button_evaluate, null);
			chatListView.addFooterView(addButton, null, false);
			Button btn_add = (Button) addButton.findViewById(R.id.btn_add);
			btn_add.setText("该问题已关闭！");
		}
		
		
		ib_post.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (contentEditText.getText().toString().trim().length() > 0) {
					send(0, null, null, null, null, "");
				} else {
					showToast("内容不能为空！");
				}

			}
		});
		btn_pic_input.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						SelectPicActivity.class);
				startActivityForResult(intent, TO_SELECT_PHOTO);
			}
		});
		btn_text_input.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_text_input.setVisibility(View.GONE);
				btn_voice_input.setVisibility(View.VISIBLE);
//				contentEditText.setVisibility(View.VISIBLE);
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
//				contentEditText.setVisibility(View.GONE);
				btn_start_voice.setVisibility(View.VISIBLE);

			}
		});

		btn_start_voice.setOnTouchListener(new OnTouchListener() {
			Long time1 = (long) 1;
			Long time2 = (long) 1;

			@Override
			public synchronized boolean onTouch(View v, MotionEvent event) {
				Date date = new Date();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btn_start_voice.setText("松开发送");
					btn_start_voice.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					time1 = date.getTime();
					if (RECODE_STATE != RECORD_ING) {
						recorder = new AudioRecorder();
						RECODE_STATE = RECORD_ING;
						showVoiceDialog(); // 显示录音图像
						try {
							voice_path = createMediePath();
							recorder.start(voice_path);

						} catch (IOException e) {
							e.printStackTrace();
						}
						// 显示动态录音效果
						new Thread(ImgThread).start();
					}
					break;
				case MotionEvent.ACTION_UP:
					btn_start_voice.setText("按住说话");
					btn_start_voice.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
					time2 = date.getTime();
					if (time2 - time1 <= 1 * 1000) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						AlertDialog _dialog = new AlertDialog.Builder(
								OnlineChatActivity.this)
								.setTitle("录音时间太短，最少1秒以上！")

								.setPositiveButton("确定", null).show();
						_dialog.setOnDismissListener(new OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {

								if (RECODE_STATE == RECORD_ING) {
									RECODE_STATE = RECODE_ED;
									try {
										recorder.stop();
										voiceValue = 0.0;
									} catch (IOException e) {
										e.printStackTrace();
									}
								}

							}
						});
					} else {
						if (RECODE_STATE == RECORD_ING) {
							RECODE_STATE = RECODE_ED;
							if (dialog.isShowing()) {
								dialog.dismiss();
							}
							try {
								recorder.stop();
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

		if (type == 2) {
			// rl_bottom.setVisibility(View.GONE);
		}
		if (type == 3) {
			btn_claim.setVisibility(View.VISIBLE);
//			contentEditText.setVisibility(View.GONE);
			btn_bottom.setVisibility(View.GONE);
			btn_pic_input.setVisibility(View.GONE);
			btn_voice_input.setVisibility(View.GONE);
			

		}

		btn_claim.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				btn_claim.setVisibility(View.GONE);
////				contentEditText.setVisibility(View.VISIBLE);
//				btn_bottom.setVisibility(View.VISIBLE);
//				btn_pic_input.setVisibility(View.VISIBLE);
//				btn_voice_input.setVisibility(View.VISIBLE);
				initProgressDialog();
				mRunnable = new Runnable() {
					@Override
					public void run() {
						Message msg = new Message();
						msg.arg1 = 3;
						List<String> params = new ArrayList<String>();
						params.add(consId);
						params.add(doctId);
						msg.obj = new ApiHttpUtil().getMethod(
								Config.getProperty("SETDOCTCONS", ""), params);
						handler.sendMessage(msg);

					}
				};
				commonThreadStart();

			}
		});

	}

	// 录音动画线程
	private Runnable ImgThread = new Runnable() {
		@Override
		public void run() {
			while (RECODE_STATE == RECORD_ING) {
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
	};

	public String createMediePath() {

		// 创建一个临时文件来存储音频
		String transationId = String.valueOf(new Date().getTime());

		// 首先检查用户SD卡是否可用，如果不可用，则先写入内存中，如果存在，则写入SD卡
		String state = android.os.Environment.getExternalStorageState();
		String filePath = Environment.getExternalStorageDirectory().getPath();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			// 写入内存
			filePath = "/data";
		}
		File file = new File(filePath + "/" + "find_doctor");
		if (!file.exists()) {
			file.mkdir();
		}
		voice_path = filePath + "/" + "find_doctor" + "/" + transationId
				+ ".aac";
		return voice_path;
	}

	// 监听返回键结束Activity 防止oncreate不执行问题

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void askFile(final String fileName, final File file, String type) {

		final Map<String, String> _Params = new HashMap<String, String>();
		_Params.put("extFileType", type);
		_Params.put("consId", consId);
		_Params.put("docId", doctId);// 动态获取 短id
		mRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					ApiHttpUtil.post(Config.getProperty("DOCTORASKFILE", ""),
							_Params, fileName, file);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		};
		commonThreadStart();

	}

	private void askText(final String text) {

		initProgressDialog();
		final Map<String, String> _Params = new HashMap<String, String>();
		String a = URLEncoder.encode(text);
		_Params.put("consId", consId);
		_Params.put("docId", doctId);// 动态获取 短id
		_Params.put("disDis", a);// 动态获取
		mRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					Message msg = new Message();
					msg.arg1 = 5;
					msg.arg2 =type;
					msg.obj = new ApiHttpUtil().postMethod(
							Config.getProperty("DOCTORASKTEXT", ""), _Params);
					handler.sendMessage(msg);
				} catch (Exception e) {
				}

			}
		};
		commonThreadStart();

	}

	private void send(int type, String fileName, File file, String voicePath,
			String picPath, String strType) {
		
		String str = contentEditText.getText().toString();
		if (file != null) {
			// askFile(fileName,file, strType);

			HashMap<String, String> _Params = new HashMap<String, String>();
			_Params.put("extFileType", strType);
			_Params.put("consId", consId);
			_Params.put("docId", doctId);// 动态获取 短id
			// _Params.put("userId", userId);// 动态获取
			UplodeProgressActivity uploade=new UplodeProgressActivity(this, Config.getProperty("DOCTORASKFILE", ""), file, fileName,_Params,type,voicePath,picPath);
			uploade.initUploadPic();
		} else {
			askText(str);
			
//			showItem( type,  fileName,  file,  voicePath,
//					 picPath);
//			contentEditText.setText("");
		}

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
		chatEntity.setComeMsg(true);
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
		contentEditText.setText("");
	}

	public void getBitMap(final String url) {
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
						PictureUtil.saveMyBitmap(mBitmap,
								url.split("/")[url.split("/").length - 1]);
					}
					msg.arg1 = 4;
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

	public Bitmap readBitmapSmall(String _path) throws IOException {
		Bitmap bitmap = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 3;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		InputStream _is = null;

		try {
			_is = new FileInputStream(_path);
			bitmap = BitmapFactory.decodeStream(_is, null, opt);

			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 设置想要的大小
			int newWidth = 90;
			int newHeight = 45;
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			_is.close();
		}
		return bitmap;

	}

	public Bitmap readBitmapSmall(Bitmap bitmap) throws IOException {
		// Bitmap bitmap=null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 3;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		InputStream _is = null;
		Bitmap mBitmap;

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 设置想要的大小
		int newWidth = 90;
		int newHeight = 43;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		mBitmap = Bitmap
				.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return mBitmap;

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

	//
	public void readBitmap(InputStream _InputStream) throws IOException {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 3;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		mBitmap = BitmapFactory.decodeStream(_InputStream, null, opt);
	}

	private void startPlaying(String myVoicePath) {
		try {
			canclePregressDialog();
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

			// 显示动态播语音放效果
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

	// 停止播放
	// private void stopPlaying() {
	// canclePregressDialog();
	// if (mediaPlayer != null) {
	// if (mediaPlayer.isPlaying()) {
	// mediaPlayer.stop();
	// }
	// mediaPlayer.release();
	// }
	// }

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
		} else if (voiceValue > 200.0 && voiceValue < 3200) {
			dialog_img.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 3200.0 && voiceValue < 14000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 14000.0 && voiceValue < 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_05);
		}
	}

	/**
	 * 播放语音图标动画
	 */
	private void playAudioAnimation() {
		// 定时器检查播放状态
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			String _picPath = data
					.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			Log.i("pic", "最终选择的图片=" + _picPath);
			if (_picPath != null) {
				File file = new File(_picPath);
				send(2, "pic_droid.jpg", file, null, _picPath, "1");
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	// 数据适配器

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
				convertView = inflater.inflate(R.layout.chat_from_item, null);
				RelativeLayout relative_left_text = (RelativeLayout) convertView
						.findViewById(R.id.relative_left_text);
				RelativeLayout relative_left_voice = (RelativeLayout) convertView
						.findViewById(R.id.relative_left_voice);
				RelativeLayout relative_left_pic = (RelativeLayout) convertView
						.findViewById(R.id.relative_left_pic);
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
				TextView tv_commuser_name_text = (TextView) convertView
						.findViewById(R.id.tv_commuser_name_text);
				TextView tv_commuser_name_voice = (TextView) convertView
						.findViewById(R.id.tv_commuser_name_voice);
				TextView tv_commuser_name_pic = (TextView) convertView
						.findViewById(R.id.tv_commuser_name_pic);

				TextView timeTextView = (TextView) convertView
						.findViewById(R.id.tv_time);
				TextView contentTextView = (TextView) convertView
						.findViewById(R.id.tv_left_content);
				ImageView userImageView = (ImageView) convertView
						.findViewById(R.id.iv_user_image);
				ImageView iv_left_pic = (ImageView) convertView
						.findViewById(R.id.iv_left_pic);

				if (chatList.get(position).getIsIntert() == 1) {
					if (chatList.get(position).getImageBytes().length() > 0) {
						byte[] tempb = Base64.decode(chatList.get(position)
								.getImageBytes(), Base64.DEFAULT);
						iv_left_pic.setImageBitmap(BitmapFactory
								.decodeByteArray(tempb, 0, tempb.length));
					}
				}

				else {
					if (chatList.get(position).getPicPath() != null) {
						try {
							iv_left_pic.setImageBitmap(PictureUtil
									.readBitmapSmall(chatList.get(position)
											.getPicPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				iv_left_pic.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (chatList.get(position).getPicPath() != null) {

							if (chatList.get(position).getIsIntert() == 1) {

								String fileName = chatList.get(position)
										.getPicPath().split("/")[chatList
										.get(position).getPicPath().split("/").length - 1];
								try {
									if (PictureUtil.getMyBitmap(fileName) != null) {
										startViewImage(
												PictureUtil.PIC_CACHE_PATH
														+ fileName, null);

									}

									else {
										getBitMap(Config.getProperty("FILEURL",
												"")
												+ chatList.get(position)
														.getPicPath());

									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {

								startViewImage(chatList.get(position)
										.getPicPath(), null);
							}

						}

					}
				});
				TextView tv_left_voice = (TextView) convertView
						.findViewById(R.id.tv_left_voice);
				tv_left_voice.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.i("voice intert", Config.getProperty("FILEURL", "")
								+ chatList.get(position).getVoicePath());

						if (chatList.get(position).getVoicePath() != null
								&& chatList.get(position).getVoicePath()
										.length() > 0) {
							if (chatList.get(position).getIsIntert() == 1) {
								initProgressDialog();
								startPlaying(Config.getProperty("FILEURL", "")
										+ chatList.get(position).getVoicePath());

							} else {
								startPlaying(chatList.get(position)
										.getVoicePath());
								Log.i("voice", "voice play");
							}

						}

					}
				});
				if (chatList.get(position).getIsIntert() == 1) {
					timeTextView
							.setText(chatList
									.get(position)
									.getChatTime()
									.substring(
											0,
											chatList.get(position)
													.getChatTime().length() - 2)
									.split(" ")[0]
									+ "\n"
									+ chatList
											.get(position)
											.getChatTime()
											.substring(
													0,
													chatList.get(position)
															.getChatTime()
															.length() - 2)
											.split(" ")[1]);
					// timeTextView.setText(chatList.get(position).getChatTime().substring(0,
					// chatList.get(position).getChatTime().length()-2));
				} else {
					timeTextView
							.setText(chatList.get(position).getChatTime()
									.split(" ")[0]
									+ "\n"
									+ chatList.get(position).getChatTime()
											.split(" ")[1]);
				}
				contentTextView.setText(chatList.get(position).getContent());
				tv_commuser_name_text.setText(chatList.get(position)
						.getCommuserName());
				tv_commuser_name_voice.setText(chatList.get(position)
						.getCommuserName());
				tv_commuser_name_pic.setText(chatList.get(position)
						.getCommuserName());
				userImageView.setImageResource(chatList.get(position)
						.getUserImage());

			} else {
				convertView = inflater.inflate(R.layout.chat_to_item, null);
				RelativeLayout relative_right_text = (RelativeLayout) convertView
						.findViewById(R.id.relative_right_text);
				RelativeLayout relative_right_voice = (RelativeLayout) convertView
						.findViewById(R.id.relative_right_voice);
				RelativeLayout relative_right_pic = (RelativeLayout) convertView
						.findViewById(R.id.relative_right_pic);
				Log.i("relative_right_voice", chatList.get(position).getType()
						+ "test");
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

				TextView timeTextView = (TextView) convertView
						.findViewById(R.id.tv_time);
				TextView contentTextView = (TextView) convertView
						.findViewById(R.id.tv_content);
				// ImageView userImageView = (ImageView) convertView
				// .findViewById(R.id.iv_user_image);
				ImageView iv_pic = (ImageView) convertView
						.findViewById(R.id.iv_pic);

				if (chatList.get(position).getIsIntert() == 1) {
					if (chatList.get(position).getImageBytes().length() > 0) {
						byte[] tempb = Base64.decode(chatList.get(position)
								.getImageBytes(), Base64.DEFAULT);
						iv_pic.setImageBitmap(BitmapFactory.decodeByteArray(
								tempb, 0, tempb.length));
					}

				}

				else {
					if (chatList.get(position).getPicPath() != null) {
						try {

							iv_pic.setImageBitmap(PictureUtil
									.readBitmapSmall(chatList.get(position)
											.getPicPath()));
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

							if (chatList.get(position).getIsIntert() == 1) {

								String fileName = chatList.get(position)
										.getPicPath().split("/")[chatList
										.get(position).getPicPath().split("/").length - 1];
								try {
									if (PictureUtil.getMyBitmap(fileName) != null) {
										startViewImage(
												PictureUtil.PIC_CACHE_PATH
														+ fileName, null);

									}

									else {
										getBitMap(Config.getProperty("FILEURL",
												"")
												+ chatList.get(position)
														.getPicPath());

									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {
								startViewImage(chatList.get(position)
										.getPicPath(), null);
							}

						}

					}
				});

				TextView tv_voice = (TextView) convertView
						.findViewById(R.id.tv_voice);
				tv_voice.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.i("voice intert", Config.getProperty("FILEURL", "")
								+ chatList.get(position).getVoicePath());

						if (chatList.get(position).getVoicePath() != null
								&& chatList.get(position).getVoicePath()
										.length() > 0) {
							if (chatList.get(position).getIsIntert() == 1) {
								initProgressDialog();
								startPlaying(Config.getProperty("FILEURL", "")
										+ chatList.get(position).getVoicePath());

							} else {
								startPlaying(chatList.get(position)
										.getVoicePath());
								Log.i("voice", "voice play");
							}

						}

					}
				});

				if (chatList.get(position).getIsIntert() == 1) {
					timeTextView
							.setText(chatList
									.get(position)
									.getChatTime()
									.substring(
											0,
											chatList.get(position)
													.getChatTime().length() - 2)
									.split(" ")[0]
									+ "\n"
									+ chatList
											.get(position)
											.getChatTime()
											.substring(
													0,
													chatList.get(position)
															.getChatTime()
															.length() - 2)
											.split(" ")[1]);
					// timeTextView.setText(chatList.get(position).getChatTime().substring(0,
					// chatList.get(position).getChatTime().length()-2));
				} else {
					timeTextView
							.setText(chatList.get(position).getChatTime()
									.split(" ")[0]
									+ "\n"
									+ chatList.get(position).getChatTime()
											.split(" ")[1]);
				}

				// if(chatList.get(position).getIsIntert()==1)
				// {
				// timeTextView.setText(chatList.get(position).getChatTime().substring(0,
				// chatList.get(position).getChatTime().length()-2));
				// }
				// else
				// {
				// timeTextView.setText(chatList.get(position).getChatTime());
				// }
				// timeTextView.setText(chatList.get(position).getChatTime());
				contentTextView.setText(chatList.get(position).getContent());
				// userImageView.setImageResource(chatList.get(position)
				// .getUserImage());
			}

			return convertView;
		}

	}

	/****
	 * 图片预览
	 */
	private void startViewImage(String filePath, Bitmap mBitmap) {
		Intent it = new Intent(OnlineChatActivity.this, ImageShowActivity.class);
		if (null != filePath && filePath.length() > 0) {
			it.putExtra("filePath", filePath);
		} else if (null != mBitmap) {
			it.putExtra("bitImag", mBitmap);
		}
		startActivity(it);
	}

}