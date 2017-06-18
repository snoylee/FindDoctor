package com.guangyi.finddoctor.onlineAsk;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.guangyi.finddoctor.custview.SelectPicActivity;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.personCenter.MyConsultActivity;
import com.guangyi.finddoctor.personCenter.UserLoginActivity;
import com.guangyi.finddoctor.utils.Config;
/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:填写在线图文咨询信息
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
public class OnLineAddConsultInfoForChart extends BasicActivity {
	
	public static final int TO_SELECT_PHOTO = 3;
	private TextView tv_doctname, tv_docHospital, tv_docDep, tv_upload;
	private EditText et_age, et_title, et_disease, et_question;
	private ImageView iv_preview,imageView1;
	private Button btn_delete;
	private RelativeLayout relative_preview;
	// 图片文件地址
	private String filePath;
	// 证件类型
	int certificateType;
	// 性别
	private int sex = 1;
	// 陪同人证件类型
	int proxyCertificateTyp = -1;
	private String userId;
	private String doctorId = "";
	private Doctor doctor;
	private LinearLayout linear_doctor;
	private Handler mHandler;
	private Bitmap  file = null;
	private Runnable mRunnable;
	private Thread mThread;
	private Bitmap bm=null;
	private boolean isCanPost=true;
	private boolean isVisiable;
	String money;
	private int remainNum,costType;
	
	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	private void commonThreadStart()
		{
			if(mRunnable!=null)
			{
			mThread=new Thread(mRunnable);
			mThread.start();
			}
		}

		protected void onStop() {
			isVisiable=false;
			super.onStop();
		}
		
		@Override
		protected void onStart() {
			isVisiable=true;
			super.onStart();
		}
		
		@Override
		protected void onDestroy() {
			mHandler.removeCallbacks(mRunnable);
			if (bm!=null&&!bm.isRecycled()) {
				bm.recycle();
				System.gc();
			}
			super.onDestroy();
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.online_add_consult_info_for_chart);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initListener();
	}

	private void initParams() {
		userId = String.valueOf(getSharedPreferences("personCenter",
				Context.MODE_PRIVATE).getInt("userId", -1));
        mHandler = new MyHandler();
		doctor = (Doctor) getIntent().getSerializableExtra("doctor");
		if (doctor != null) {
			doctorId = String.valueOf(doctor.getDoctId());
			money=doctor.getMoney();
			remainNum=doctor.getRemainNum();
			costType=doctor.getCostType();
			if(costType==-1)
			{
				remainNum=-1;
			}
		}
		else
		{
			remainNum=-1;
		}
		
//		Log.d("remainNum", remainNum+"");
//		Log.d("money", money+"");
	}

	private void initView() {
		linear_doctor = (LinearLayout) findViewById(R.id.linear_doctor);
		tv_doctname = (TextView) findViewById(R.id.tv_doctname);
		tv_docHospital = (TextView) findViewById(R.id.tv_docHospital);
		tv_docDep = (TextView) findViewById(R.id.tv_docDep);
		tv_upload = (TextView) findViewById(R.id.tv_upload);
		et_age = (EditText) findViewById(R.id.et_age);
		et_title = (EditText) findViewById(R.id.et_title);
		et_disease = (EditText) findViewById(R.id.et_disease);
		et_question = (EditText) findViewById(R.id.et_question);
		tv_doctname = (TextView) findViewById(R.id.tv_doctname);
		tv_docHospital = (TextView) findViewById(R.id.tv_docHospital);
		tv_docDep = (TextView) findViewById(R.id.tv_docDep);
		relative_preview = (RelativeLayout) findViewById(R.id.relative_preview);
		iv_preview = (ImageView) findViewById(R.id.iv_preview);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				relative_preview.setVisibility(View.GONE);
				iv_preview.setImageBitmap(null);
				filePath = null;
				isCanPost=true;
				if (bm!=null&&!bm.isRecycled()) {
					bm.recycle();
					System.gc();
				}
			}
		});
		if (doctor != null) {
			tv_doctname.setText(doctor.getDoctName() + "");
			tv_docHospital.setText(doctor.getHospName() + "");
			tv_docDep.setText(doctor.getDepaName() + "");
			linear_doctor.setVisibility(View.VISIBLE);
//			if(doctor.getAttachFileByte()!=null&&doctor.getAttachFileByte().length()>0)
//			{
//				byte[] tempb = Base64.decode(doctor.getAttachFileByte(), Base64.DEFAULT);
//				imageView1.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0 ,tempb.length));
//			}
			
			ImageManager2.from(this).displayImage(imageView1, doctor.getAttachFileByte(), R.drawable.touxiang);

		}
		else
		{
			TextView tv_free_str=(TextView) findViewById(R.id.tv_free_str);
			tv_free_str.setText("当前咨询由全科医生团队为您免费解答");
			
		}
	}

	private void initListener() {
		final TextView tv_sex=(TextView) findViewById(R.id.tv_sex);
		tv_sex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tv_sex.getText().toString().equals("男")) {
					tv_sex.setText("女");
					sex = 2;
					
				} else if (tv_sex.getText().toString().equals("女")) {
					tv_sex.setText("男");
					sex = 1;
				}
				
				Log.d("sex", sex+"");

			}
		});

		
		
//		SlideButton slide_sex=(SlideButton) findViewById(R.id.slide_sex);
//		slide_sex.setData(new String[]{"男","女"});
//		slide_sex.SetOnChangedListener(new OnChangedListener() {
//			@Override
//			public void OnChanged(boolean CheckState) {
//			
//				if(CheckState)
//				{
//					sex = 2;
//				}
//				else
//				{
//					sex = 1;
//				}
//				Log.d("CheckState", CheckState+""+sex);
//			}
//		});
		Button Back = (Button) findViewById(R.id.ib_back);

		Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button ib_post = (Button) findViewById(R.id.ib_post);
		ib_post.setOnClickListener(new OnClickListener() {
			//点击后 返回键 没有响应的操作啊
			 public boolean onKeyDown(int keyCode, KeyEvent event) {
			  if(keyCode==KeyEvent.KEYCODE_HOME){
				  return true;
			  }else if(keyCode == KeyEvent.KEYCODE_BACK){
				  return true;
			  }
			  return true;
			 } 
			 
			@Override
			public void onClick(View v) {
				if (userId.equals("-1")) {
					showToast("请先登陆后重试");
					openActivity(UserLoginActivity.class);
				}else
				{
					String ageStr=et_age.getText().toString().trim();
					if(ageStr.equals(""))
					{
						showToast("年龄不能为空!");
						return;	
					}
					int age=Integer.valueOf(ageStr);
					if (age <= 0|| age >= 120) {
						showToast("年龄输入不正确，请重新输入！");
						return;
					}
					if (et_question.getText().toString().trim().equals("")) {
						showToast("疾病详情不能为空！");
						return;
					}
					if(isCanPost)
					{
					toUploadFile();
					}
					else
					{
						showToast("文件大小不能超过8MB！");
					}
				}
			}
		});

		tv_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OnLineAddConsultInfoForChart.this,
						SelectPicActivity.class);
				startActivityForResult(intent, TO_SELECT_PHOTO);
			}
		});

	}
	
	/*//屏蔽home键
	@Override
	 public void onAttachedToWindow() {
	  // TODO Auto-generated method stub
	  this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
	  super.onAttachedToWindow();
	 }
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  if(keyCode==KeyEvent.KEYCODE_HOME){
	   return true;
	  }else if(keyCode == KeyEvent.KEYCODE_BACK){
		  return true;
	  }
	  return super.onKeyDown(keyCode, event);
	 } */

		    
	private void toUploadFile() {
		final HashMap<String, String> params = new HashMap<String, String>();
					if(remainNum==0)
					{
					params.put("isPayStatus", 1+"");
					}
					params.put("docId", doctorId);
					params.put("userId", userId);
					params.put("username", "");
					params.put("userAge", et_age.getText().toString());
					params.put("userSex", String.valueOf(sex));
					params.put("title", et_title.getText().toString());
					params.put("disease", et_disease.getText().toString());
					params.put("disDis", et_question.getText().toString());
					File myFile = null;
					if (filePath != null) {
						params.put("extFileType", "1");
						//对图片进行压缩
//						file = PictureUtil.getSmallBitmap(filePath);
						 myFile=new File(filePath);
					} else {
						params.put("extFileType", "-1");
					}
					if(myFile!=null)
					{
					UplodeProgress1 up=new UplodeProgress1(this, Config.getProperty("requestURL", ""), myFile, "pic_droid.jpg", params);
					up.initUploadPic();
					}
					else
					{
						initProgressDialog();
						mRunnable=new Runnable() {
							@Override
							public void run() {
								try {
									Message msg = new Message();
									msg.arg1 = 1;
									msg.obj =new ApiHttpUtil().postWithPic(
											Config.getProperty("requestURL", ""), params,
											"pic_droid.jpg", file);
									mHandler.sendMessage(msg);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						};
						commonThreadStart();
						
						
					}
//					msg.obj =new ApiHttpUtil().postWithPic(
//							Config.getProperty("requestURL", ""), params,
//							"pic_droid.jpg", file);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			filePath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			Log.i("pic", "最终选择的图片=" + filePath);
			if (filePath.length() > 0) {

				relative_preview.setVisibility(View.VISIBLE);
			    BitmapFactory.Options opt = new BitmapFactory.Options();
			     opt.inPreferredConfig=Bitmap.Config.RGB_565;//表示16位位图 565代表对应三原色占的位数
			     opt.inSampleSize = 8;//缩放比例  2的指数倍
			     opt.inInputShareable=true;
			     opt.inPurgeable=true;//设置图片可以被回收
			     InputStream _is = null;
			      try {
					_is = new FileInputStream(filePath);
					File file=new File(filePath);
					if(file.length()<8*1024*1024)
					{
						isCanPost=true;
					}
					else
					{
						isCanPost=false;
					}
					bm= BitmapFactory.decodeStream(_is, null, opt);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			      finally
			      {
			    	  try {
						_is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	  finally
			    	  {
			    		  try {
							_is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	  }
			      }
			     
			     if(bm!=null)
			     {
//			    	 BitmapDrawable bd=new BitmapDrawable(bm);
//						iv_preview.setBackground(bd);
			    	 iv_preview.setImageBitmap(bm);
						relative_preview.setVisibility(View.VISIBLE);
			     }
				
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.i("zixun", jsonString);
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
					String a=jsonObject.getString("reason");
					Log.d("在线咨询提交前reason", a+"---->");
					try {
						reason=new String(Base64.decode(a, Base64.DEFAULT),"gbk");
						Log.d("在线咨询提交Houreason", reason+"---->");
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
			           int consId = 0;
					try {
						consId = jsonObject.getJSONObject("message").getInt("consId");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
						int sendType=getDataSf().getInt(getResources().getString(R.string.sendType), -1);
						
						if(sendType==0)
						{
							
							if(remainNum!=0)
							{
								showToast("在线咨询成功!");
							}
							
						}
						else if(sendType==1)
						{
							if(remainNum!=0)
							{
								showToast("在线咨询成功需要审核通过后才能显示!");
							}

						}
						
						if (isVisiable) {
							if(remainNum!=0)
							{
							Intent intent = new Intent(
									OnLineAddConsultInfoForChart.this,
									MyConsultActivity.class);
							startActivity(intent);
							}
							
							else if(remainNum==0)
							{
								toPay(consId+"", money+"");
//								toPay(consId+"", "0.01");
								Log.d("consId", consId+"");
								Log.d("money", money+"");
							}
						}
					}
				}
						
//				else if(code==3)
//				{
//					showtoast("登录用户已失效！");
//				}
//				
//				else if(code==4)
//				{
//					showtoast("图片上传终止！");
//				}
//				
//				else if(code==99)
//				{
//					showtoast("在线咨询失败！");
//				}
				
				else 
				{
					showToast(reason);
				}
				
			}
			super.handleMessage(msg);
		}
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

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(OnLineAddConsultInfoForChart.this, payHanlder);
					
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
			Toast.makeText(OnLineAddConsultInfoForChart.this, R.string.remote_call_failed,
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
						
						Intent intent = new Intent(
								OnLineAddConsultInfoForChart.this,
								MyConsultActivity.class);
						startActivity(intent);
					}
					
					else if(result.getResult().trim().length()>0)
					{
						Toast.makeText(OnLineAddConsultInfoForChart.this, result.getResult(),
								Toast.LENGTH_SHORT).show();
					}
				case RQF_LOGIN: {
					if(result.getResult().trim().length()!=0)
					{
						Toast.makeText(OnLineAddConsultInfoForChart.this, result.getResult(),
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
	
	
	
	
	
	
	
	
	
	public class UplodeProgress1{  
		
	    private Context mContext;
	    private String actionUrl;  
	    private HashMap<String,String> params;  
	    private String fileName;
	    private File uploadFile;
	    private FileUploadTask  fileuploadtask;
	    private TextView tv_progress;
	    
	    
	    
	    public UplodeProgress1(Context context,String actionUrl,File file,String fileName,HashMap<String, String> params)
	    {
	    	this.mContext=context;
	    	this.fileName=fileName;
	    	this.actionUrl=actionUrl;
	    	this.uploadFile=file;
	    	this.params=params;
	    
	    }
	      
	    //初始化文件信息  
	   
	    public void initUploadPic() {  
//	    	if(uploadFile.length()<8*1024*1024)
//	    	{
	    		fileuploadtask = new FileUploadTask();  
	            fileuploadtask.execute();  
//	    	}
//	    	else
//	    	{
//	    		Toast.makeText(mContext, "文件大小不能超过8MB！", Toast.LENGTH_SHORT).show();
//	    	}
	          
	    }  
	    
	    public void cancleUpload()
	    {
	    	if(!fileuploadtask.isCancelled())
	    	{
	    		fileuploadtask.cancel(true);
	    	}
	    }
	      
	      
	      
	    class FileUploadTask extends AsyncTask<Object, Integer, String> {  
	        private Dialog dialog = null;  
	        private ProgressBar progressBar=null;
	        HttpURLConnection connection = null;  
	        DataOutputStream outputStream = null;  
	        DataInputStream inputStream = null;  
	        //the server address to process uploaded file  
	        String end ="\r\n";//回车换行符  
	               String twoHyphens ="--";//分隔符前后缀  
	               String boundary ="######";//分隔符  
	  
	        long totalSize = uploadFile.length(); // Get size of file, bytes  
	  
	        @Override  
	        protected void onPreExecute() {  
	        	View view=LayoutInflater.from(mContext).inflate(R.layout.alert_dialog, null);
	        	dialog=new Dialog(mContext, R.style.Translucent_NoTitle);
	        	dialog.setContentView(view);
	        	tv_progress=(TextView) view.findViewById(R.id.tv_progress);
	        	progressBar=(ProgressBar) view.findViewById(R.id.progress_bar);
	        	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
	        	btn_cancle.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						dialog.dismiss();
						cancleUpload();
						
						
					}
				});
	        	
	        	
	        	dialog.setCanceledOnTouchOutside(false);
	        	dialog.show();
	        			
	        			
	        			
	        }  
	  
	        @SuppressWarnings("finally")
			@Override  
	        protected String doInBackground(Object... arg0) {  
	            String result = "";  
	            long length = 0;  
	            int progress;  
	            int bytesRead, bytesAvailable, bufferSize;  
	            byte[] buffer;  
	            int maxBufferSize = 50 * 1024;// 10KB  
	            try {  
	                URL url = new URL(actionUrl);  
	                connection = (HttpURLConnection) url.openConnection();  
	                // Set size of every block for post  
	                connection.setChunkedStreamingMode(128 * 1024);// 128KB  
	                // Allow Inputs & Outputs  
	                connection.setDoInput(true);  
	                connection.setDoOutput(true);  
	                connection.setUseCaches(false);  
	                // Enable POST method  
	                connection.setRequestMethod("POST");  
	                connection.setRequestProperty("Connection", "Keep-Alive");  
	                connection.setRequestProperty("Charset", "UTF-8");  
	                connection.setRequestProperty("Content-Type",  
	                        "multipart/form-data;boundary=" + boundary);  
	                outputStream = new DataOutputStream(connection.getOutputStream());  
	                //写入普通属性  
	                if(params != null){  
	                    Set<String> keys = params.keySet();  
	                    for (Iterator<String> it = keys.iterator(); it.hasNext();) {  
	                        String key = it.next();  
	                        String value = params.get(key);  
	                        outputStream.writeBytes(twoHyphens + boundary + end);  
	                        outputStream.writeBytes("Content-Disposition: form-data; "+  
	                                  "name=\""+key+"\""+end);  
	                        outputStream.writeBytes(end);  
//	                        outputStream.writeBytes(value);  
	                        outputStream.write(value.getBytes("UTF-8")); 
	                        
	                        outputStream.writeBytes(end);  
	                    }  
	                }  
	                //文件写入  
	                outputStream.writeBytes(twoHyphens + boundary + end);  
	                outputStream.writeBytes("Content-Disposition: form-data; " + "name=\""
							+ "pic" + "\"" + "; filename=\"" + fileName + "\""
							+ end);
	                outputStream.writeBytes(end);  
	                FileInputStream fileInputStream = new FileInputStream(uploadFile);  
	                bytesAvailable = fileInputStream.available();  
	                bufferSize = Math.min(bytesAvailable, maxBufferSize);//设置每次写入的大小  
	                buffer = new byte[bufferSize];  
	                // Read file  
	                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	                while (bytesRead > 0&&!fileuploadtask.isCancelled()) {  
	                    outputStream.write(buffer, 0, bufferSize);  
	                    length += bufferSize;  
	                    Thread.sleep(100);  
	                    progress = (int) ((length * 100) / totalSize);  
	                    publishProgress(progress,(int)length);  
	                    bytesAvailable = fileInputStream.available();  
	                    bufferSize = Math.min(bytesAvailable, maxBufferSize);  
	                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
	                }  
	                outputStream.writeBytes(end);  
	                outputStream.writeBytes(twoHyphens + boundary + twoHyphens  
	                        + end);  
	                outputStream.flush(); 
	                
	                
	                InputStream is = connection.getInputStream();  
	                int ch;  
	                StringBuffer b =new StringBuffer();  
	                while((ch = is.read()) != -1)  
	                {  
	                  b.append((char)ch);  
	                }  
	                result  =new String(b.toString().getBytes(),"UTf-8");                 
	                is.close();  
	                fileInputStream.close();  
	                outputStream.close();  
	            } catch (Exception ex) { 
	            	ex.printStackTrace();
	            }
	            finally {
	    			return result;
	    		}
	        }  
	        @Override  
	        protected void onProgressUpdate(Integer... progress) {  
	        	progressBar.setProgress(progress[0]);
	        	Float a=Float.valueOf(progress[1]);
	        	int result =  Math.round(a/totalSize*100); 
	        	tv_progress.setText(result+"%");
	        }  
	  
	        @Override  
	        protected void onPostExecute(String result) {
	        	dialog.dismiss();
				JSONObject jsonObject = null;
				String jsonString = result;
				Log.i("zixun", jsonString);
				if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
					showToast(getResources().getString(R.string.soconntimeout));
				} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
					showToast(getResources().getString(R.string.conntimeout));
				}
				else
				{
					int code = -1;
//					String reason=getResources().getString(R.string.network_preoblem);
					String reason="提交失败请重试！";
					try {
						jsonObject = new JSONObject(jsonString);
						
						code = jsonObject.getInt("code");
						String a=jsonObject.getString("reason");
						Log.d("在线咨询提交前reason", a+"---->");
						try {
							reason=new String(Base64.decode(a, Base64.DEFAULT),"gbk");
							Log.d("在线咨询提交Houreason", reason+"---->");
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
				           int consId = 0;
						try {
							consId = jsonObject.getJSONObject("message").getInt("consId");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
							int sendType=getDataSf().getInt(getResources().getString(R.string.sendType), -1);
							
							if(sendType==0)
							{
								
								if(remainNum!=0)
								{
									showToast("在线咨询成功!");
								}
								
							}
							else if(sendType==1)
							{
								if(remainNum!=0)
								{
									showToast("在线咨询成功需要审核通过后才能显示!");
								}

							}
							
							if (isVisiable) {
								if(remainNum!=0)
								{
								Intent intent = new Intent(
										OnLineAddConsultInfoForChart.this,
										MyConsultActivity.class);
								startActivity(intent);
								}
								
								else if(remainNum==0)
								{
									toPay(consId+"", money+"");
//									toPay(consId+"", "0.01");
									Log.d("consId", consId+"");
									Log.d("money", money+"");
								}
								
							}
							
						
					}
							

					
					else 
					{
						showToast(reason);
					}
					
				}
				
				
			}  
	    }     
	    
	
}   
	
	
	
}
