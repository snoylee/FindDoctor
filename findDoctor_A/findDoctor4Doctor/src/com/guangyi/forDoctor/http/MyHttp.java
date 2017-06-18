package com.guangyi.forDoctor.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;
import android.util.Log;

public class MyHttp {
	//先将参数放入List，再对参数进行URL编码
	List<BasicNameValuePair> mParams;
//	List<BasicNameValuePair> mParams = new LinkedList<BasicNameValuePair>();
//	mParams = new LinkedList<BasicNameValuePair>();
//	mParams.add(new BasicNameValuePair("param1", "Post方法"));
//	mParams.add(new BasicNameValuePair("param2", "第二个参数"));
	String mUrlString;
	private HttpClient httpClient;
	public MyHttp(List<BasicNameValuePair> params,String urlString)
	{
		this.mParams=params;
		this.mUrlString=urlString;
		httpClient = new DefaultHttpClient();
	}
	
	public String myHttpGet()
	{
		
		
		
//		mParams.add(new BasicNameValuePair("param1", "中国"));
//		mParams.add(new BasicNameValuePair("param2", "value2"));
//		//对参数编码
//		String param = URLEncodedUtils.format(mParams, "UTF-8");
//		//baseUrl			
//		
		//将URL与参数拼接
		HttpGet getMethod = new HttpGet(mUrlString);
					
		try {
		    HttpResponse response = httpClient.execute(getMethod); //发起GET请求

		    Log.i("TAG", "resCode = " + response.getStatusLine().getStatusCode()); //获取响应码
		    Log.i("TAG", "result = " + EntityUtils.toString(response.getEntity(), "utf-8"));//获取服务器响应内容
		    String a=EntityUtils.toString(response.getEntity(), "utf-8");
		    return EntityUtils.toString(response.getEntity(), "utf-8");
		    
		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		return null;

	}
	
	
	public String myHttpPost()
	{
		try {
		    HttpPost postMethod = new HttpPost(mUrlString);
		    postMethod.setEntity(new UrlEncodedFormEntity(mParams, "utf-8")); //将参数填入POST Entity中
						
		    HttpResponse response = httpClient.execute(postMethod); //执行POST方法
		    Log.i("TAG", "resCode = " + response.getStatusLine().getStatusCode()); //获取响应码
		    Log.i("TAG", "result = " + EntityUtils.toString(response.getEntity(), "utf-8")); //获取响应内容
		    return  EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (UnsupportedEncodingException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return null;
	}
	
	
}
