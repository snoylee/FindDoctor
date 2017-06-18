package com.guangyi.finddoctor.personCenter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.Introduce;

public class introduceDetail extends BasicActivity{
	private  TextView tv_topbar_center_text;
	private WebView mWebView; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduce_detail);
		initView();
	}
	
	private void initView()
	{
		Introduce introduce=(Introduce) getIntent().getSerializableExtra("introduce");
		tv_topbar_center_text=(TextView) findViewById(R.id.tv_topbar_center_text);
		tv_topbar_center_text.setText(introduce.getName());
		mWebView=(WebView) findViewById(R.id.webView);
//		initProgressDialog();
		mWebView.loadUrl(introduce.getDetialUrl());
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
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	
	
		
	

	

	

}
