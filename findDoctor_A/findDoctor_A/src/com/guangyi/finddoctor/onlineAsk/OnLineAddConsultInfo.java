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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.personCenter.MyRegisterActivity;
import com.guangyi.finddoctor.utils.Config;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:填写电话咨询信息
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
public class OnLineAddConsultInfo extends BasicActivity {
	private TextView tv_doctname, tv_docHospital, tv_docDep, tv_sex, tv_upload;
	private EditText et_age, et_mobile_phone, et_disease, et_question;
	private String timeStr, wayStr,feeStr;
	private Doctor mDoctor;
	public static final int TO_SELECT_PHOTO = 3;
	private String timeSection = "";
	private String patientName = "";
	private String filePath = "";
	// 证件类型
	int certificateType;
	// 证件号
	private String certificateNo = "";
	// 电话
	private String phone = "";
	// 地址
	private String address = "";
	// 性别
	private int sex = 1;
	// 生日
	private String birthday = "";

	private int returnFlag;
	// 所患疾病
	private String disease = "";
	// 病情描述
	private String symptom = "";
	// 陪同人姓名
	private String proxyName = "";
	// 陪同人证件类型
	int proxyCertificateTyp;
	// 陪同人证件号
	private String proxyCertificateNo = "";
	// 陪同人电话
	private String proxyPhone = "";
	// 控件ID 动态布局要求空间属性
	private String widgetId = "";
	// 控件取值 widgetValue
	private String widgetValue = "";
	// 是否需要支付 0:是 1:否
	private int isNeedPay;

	private String userId;
	private String doctId;
	private String transactionId = "", todayStr;
	private String state = "";
	private Handler mHandler;
	private ImageView iv_preview;
	private Button btn_delete;
	private RelativeLayout relative_preview;
	private Bitmap file = null;
	private String filename = null;
	private Runnable mRunnable;
	private Thread mThread;
	private Bitmap bm=null;
	private boolean isCanPost=true;
	
	
	
	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	
	private String transID;
	
	private boolean isVisiable;	

	private void commonThreadStart() {
		if (mRunnable != null) {
			mThread = new Thread(mRunnable);
			mThread.start();
		}
	}
	
	@Override
	protected void onStart() {
		isVisiable=true;
		super.onStart();
	}

	protected void onStop() {
		isVisiable=false;
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		if (bm!=null&&!bm.isRecycled()) {
			bm.recycle();
			System.gc();
		}
		mHandler.removeCallbacks(mRunnable);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_add_consult_info);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initView();
		initParams();
		initListener();
	}

	private void initParams() {
		mHandler = new MyHandler();
		transID=getOutTradeNo();
		timeStr = getIntent().getExtras().getString("time");
		todayStr = getIntent().getExtras().getString("todayStr");
		feeStr=getIntent().getStringExtra("fee");
//		feeStr="0.01";
//		Log.w("feeStr", feeStr+"fee");
		mDoctor = (Doctor) getIntent().getSerializableExtra("doctor");
		transactionId = String.valueOf(new Date().getTime());
		wayStr = getIntent().getStringExtra("way");
		tv_sex.setText("男");
		if (wayStr == "电话咨询") {
		}
		timeSection = timeStr;
		tv_doctname.setText(mDoctor.getDoctName());
		tv_docHospital.setText(mDoctor.getHospName());
		tv_docDep.setText(mDoctor.getDepaName());
	}

	private void initView() {
		tv_doctname = (TextView) findViewById(R.id.tv_doctname);
		tv_docHospital = (TextView) findViewById(R.id.tv_docHospital);
		tv_docDep = (TextView) findViewById(R.id.tv_docDep);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_upload = (TextView) findViewById(R.id.tv_upload);
		et_age = (EditText) findViewById(R.id.et_age);
		et_mobile_phone = (EditText) findViewById(R.id.et_mobile_phone);
		et_mobile_phone.setText(getSharedPreferences("personCenter",
					Context.MODE_PRIVATE).getString("userMobile", ""));
		et_disease = (EditText) findViewById(R.id.et_disease_disease);
		et_question = (EditText) findViewById(R.id.et_question);
		relative_preview = (RelativeLayout) findViewById(R.id.relative_preview);
		iv_preview = (ImageView) findViewById(R.id.iv_preview);
		btn_delete = (Button) findViewById(R.id.btn_delete);
	}

	private void onClickInit() {
		phone = et_mobile_phone.getText().toString().trim();
		disease = et_disease.getText().toString().trim();
		symptom = et_question.getText().toString().trim();
		birthday = et_age.getText().toString().trim();
		userId = String.valueOf(getSharedPreferences("personCenter",
				Context.MODE_PRIVATE).getInt("userId", -1));
		doctId = String.valueOf(mDoctor.getDoctId());
		// type
		// sex
	}

	private void getData() {
//		initProgressDialog();

		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("timeSection", timeSection);
		params.put("transactionId", transactionId);
		params.put("phone", phone);
		params.put("money", feeStr);
		params.put("disease", symptom);
		params.put("symptom", disease);
		params.put("userId", userId);
		params.put("doctId", doctId);
		params.put("type", "2");
		params.put("sex", String.valueOf(sex));
		params.put("money",feeStr);
		params.put("patientName", patientName);
		params.put("certificateType", String.valueOf(certificateType));
		params.put("certificateNo", certificateNo);
		params.put("address", address);
		params.put("birthday", birthday);
		params.put("returnFlag", String.valueOf(returnFlag));
		params.put("proxyName", proxyName);
		params.put("proxyCertificateTyp",
				String.valueOf(proxyCertificateTyp));
		params.put("proxyCertificateNo", proxyCertificateNo);
		params.put("proxyPhone", proxyPhone);
		params.put("widgetId", widgetId);
		params.put("widgetValue", widgetValue);
		params.put("isNeedPay", String.valueOf(isNeedPay));
		params.put("state", state);
		params.put("appTime", todayStr);
		params.put("out_trade_no",transID);
		Log.w("out_trade_no", transID);
		File myFile = null;
		if (filePath.length()>0) {
			// 对图片进行压缩
//			file = PictureUtil.getSmallBitmap(filePath);
			 myFile=new File(filePath);
			filename = "pic";
			params.put("extFileType", "1");
			UplodeProgress1 up=new UplodeProgress1(this, Config.getProperty("ON_LINE_post", ""), myFile, filename, params);
			up.initUploadPic();
		}
			
		
		else
		{
		initProgressDialog();
		mRunnable = new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.arg1 = 1;
				// msg.obj = mApiHttpUtil.pos(
				msg.obj = new ApiHttpUtil().postWithPic(
						Config.getProperty("ON_LINE_post", ""), params,
						filename, file);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
		}

	}

	private void initListener() {
		tv_sex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (tv_sex.getText().toString().equals("男")) {
					tv_sex.setText("女");
					sex = 2;
				} else if (tv_sex.getText().toString().equals("女")) {
					tv_sex.setText("男");
					sex = 1;
				} else {
					sex = 3;
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

		Button ib_post = (Button) findViewById(R.id.ib_post);
		ib_post.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickInit();
				
				if(isCanPost)
				{
					getData();
				}
				else
				{
					showToast("文件大小不能超过8MB！");
				}
				

			}

		});

		tv_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OnLineAddConsultInfo.this,
						SelectPicActivity.class);
				startActivityForResult(intent, TO_SELECT_PHOTO);

			}
		});

		btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				relative_preview.setVisibility(View.GONE);
				iv_preview.setImageBitmap(bm);
				filePath ="";
				isCanPost=true;
				if (bm!=null&&!bm.isRecycled()) {
					bm.recycle();
					System.gc();
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			filePath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			Log.i("pic", "最终选择的图片=" + filePath);
			// Bitmap bm = BitmapFactory.decodeFile(filePath);
			
			if (filePath.length() > 0) {
			    BitmapFactory.Options opt = new BitmapFactory.Options();
			     opt.inPreferredConfig=Bitmap.Config.RGB_565;//表示16位位图 565代表对应三原色占的位数
			     opt.inSampleSize = 8;//缩放比例  2的指数倍
			     opt.inInputShareable=true;
			     opt.inPurgeable=true;//设置图片可以被回收
			     InputStream _is = null;
			      try {
					_is = new FileInputStream(filePath);
					bm= BitmapFactory.decodeStream(_is, null, opt);
					File file=new File(filePath);
					if(file.length()<8*1024*1024)
					{
						isCanPost=true;
					}
					else
					{
						isCanPost=false;
					}
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
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
				showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
				showToast(getResources().getString(R.string.conntimeout));
			} else {

				int code = -1;
//				String reason = getResources().getString(
//						R.string.network_preoblem);
				String reason="提交失败请重试！";
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					code = jsonObject.getInt("code");
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
				if (code == 0) {
					if(isVisiable)
					{
					if (msg.arg1 == 1) {
						toPay();
					}
					}
					else
					{
						showToast("电话咨询成功,请先付款!");
					}

				} 
				
//				else if (code == 2) {
//					if (msg.arg1 == 1) {
//						showToast("当前排班已经被预约！");
//					}
//
//				}
//				
//				else if (code == 99) {
//					
//					if (msg.arg1 == 1) {
//						showToast("服务器内部错误！");
//					}
//				}
				else {
					showToast(reason);
				}

			}

			super.handleMessage(msg);
		}
	}
	
//	支付宝操作
	private  void toPay()
	{
		try {
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo();
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(OnLineAddConsultInfo.this, payHanlder);
					
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
			Toast.makeText(OnLineAddConsultInfo.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	
	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(transID);
		sb.append("\"&subject=\"");
		sb.append("电话咨询");
		sb.append("\"&body=\"");
		sb.append("电话咨询");
		sb.append("\"&total_fee=\"");
		sb.append(feeStr);
		sb.append("\"&notify_url=\"");
		sb.append(URLEncoder.encode(Config.getProperty("GETALIRESULT", "")));

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
					//应该在此处进行的修改的 跳转到 我的咨询页面
					Intent intent = new Intent(OnLineAddConsultInfo.this,
							MyRegisterActivity.class);
					intent.putExtra("flagtype", 1);
					startActivity(intent);
				case RQF_LOGIN: {
					if(result.getResult().trim().length()!=0)
					{
						Toast.makeText(OnLineAddConsultInfo.this, result.getResult(),
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
				if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
					showToast(getResources().getString(R.string.soconntimeout));
				} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
					showToast(getResources().getString(R.string.conntimeout));
				} else {

					int code = -1;
//					String reason = getResources().getString(
//							R.string.network_preoblem);
					String reason="提交失败请重试！";
					try {
						jsonObject = new JSONObject(result);
						code = jsonObject.getInt("code");
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
					if (code == 0) {
						if(isVisiable)
						{
//						if (msg.arg1 == 1) {
							toPay();
//						}
						}
						else
						{
							showToast("电话咨询成功,请先付款!");
						}

					} 
					
//					else if (code == 2) {
//						if (msg.arg1 == 1) {
//							showToast("当前排班已经被预约！");
//						}
	//
//					}
//					
//					else if (code == 99) {
//						
//						if (msg.arg1 == 1) {
//							showToast("服务器内部错误！");
//						}
//					}
					else {
						showToast(reason);
					}

				}

			
	        }  
	    }     
	    
	
} 
	
	
	
	

}
