package com.guangyi.finddoctor.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.Banner;
import com.guangyi.finddoctor.utils.Config;

public class BannerDetail extends BasicActivity{
	private  TextView tv_banner_title,tv_phone,tv_banner_time;
	private LinearLayout linear_phone;
	private ImageView iv_banner_pic;
	private WebView mWebView; 
	private Runnable mRunnable;
	private MyHandler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.banner_detail);
		initView();
		initData();
		getData();
	}
	
	private void initView()
	{
		linear_phone=(LinearLayout) findViewById(R.id.linear_phone);
		tv_banner_title=(TextView) findViewById(R.id.tv_banner_title);
//		tv_banner_content=(TextView) findViewById(R.id.tv_banner_content);
		tv_phone=(TextView) findViewById(R.id.tv_phone);
		iv_banner_pic=(ImageView) findViewById(R.id.iv_banner_pic);
		tv_banner_time=(TextView) findViewById(R.id.tv_banner_time);
		mWebView=(WebView) findViewById(R.id.webView);
//		mWebView.setWebViewClient(new WebViewClient(){
//			
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				//当有新连接时，使用当前的 WebView  
//               
//                //调用拨号程序
//                if (url.startsWith("mailto:") || url.startsWith("geo:") ||url.startsWith("tel:")) {
//                  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                   startActivity(intent);
//                  }
////                else
////                {
////                	 view.loadUrl(url);
////                }
//                
//                return true;
//			       }
//			
//		});
		
//		mWebView.loadUrl("http://m.hao123.com/");
		Button mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initData()
	{	//数据的接口，推送的时候进行的数据获取
		final String id=getIntent().getStringExtra("id");
	
		mHandler=new MyHandler();
		mRunnable = new Runnable() {
			@Override
			public void run() {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Message msg = new Message();
						Map<String,String> params=new HashMap<String, String>();
						params.put("id", id);//id 百度返回的 接口数据
					
						msg.arg1 = 1;
						msg.obj = new ApiHttpUtil().postMethod(
									Config.getProperty("GETPUSHDETIAL", ""), params);
						mHandler.sendMessage(msg);
					}
				}).start();
			}
		};
	}
	private void getData()
	{
		initProgressDialog();
		mHandler.post(mRunnable);
	}
	
	@Override
	protected void onDestroy() {
		mHandler.removeCallbacks(mRunnable);
		super.onDestroy();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			Intent intent = new Intent(BannerDetail.this,MainActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.i("banner deatail  jsonString-------=>", jsonString);
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
					e1.printStackTrace();
				}
				if (code == 0) {
					if (msg.arg1 == 1) {
						Banner banner;
							banner=new Banner();
							try {
								banner.setId(jsonObject.getJSONObject("pushDetial").getString("id"));
								banner.setPicture(Config.getProperty("FILEURL", "")+jsonObject.getJSONObject("pushDetial").getString("fileUrl"));
								banner.setTitle(jsonObject.getJSONObject("pushDetial").getString("title"));
//								banner.setContent(jsonObject.getJSONObject("pushDetial").getString("content"));
								banner.setTime(jsonObject.getJSONObject("pushDetial").getString("pushTime"));
								banner.setContentUrl(jsonObject.getJSONObject("pushDetial").getString("requestUrl"));
								String phoneNumber=jsonObject.getJSONObject("pushDetial").getString("pushPhone");
								tv_banner_title.setText(banner.getTitle());
//								 tv_banner_content.setText(banner.getContent());
									tv_banner_time.setText(banner.getTime());
									mWebView.loadUrl(banner.getContentUrl());
									ImageManager2.from(BannerDetail.this).displayImage(iv_banner_pic, banner.getPicture(), R.drawable.banner_detail_default);
										if(phoneNumber.length()>0)
										{
											tv_phone.setText(phoneNumber);
											linear_phone.setVisibility(View.VISIBLE);
										}
										iv_banner_pic.setVisibility(View.VISIBLE);
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
}
