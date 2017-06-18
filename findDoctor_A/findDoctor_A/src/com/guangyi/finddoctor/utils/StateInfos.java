package com.guangyi.finddoctor.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Banner;

public class StateInfos  {
	private Context mContext;
	private String visitContent;
	private int sourceType;
	
	private String userId,channelId,appId;

	public StateInfos(Context context, String visitContent, int sourceType) {
		this.mContext = context;
		this.visitContent = visitContent;
		this.sourceType = sourceType;

	}
	public StateInfos(Context context, String userId, String channelId,String appId) {
		this.mContext = context;
		this.userId = userId;
		this.channelId = channelId;
		this.appId = appId;

	}

	private String getLocalIPAddress() throws SocketException {
		for (Enumeration<NetworkInterface> en = NetworkInterface
				.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
					.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress()
						&& (inetAddress instanceof Inet4Address)) {
					return inetAddress.getHostAddress().toString();
				}
			}
		}
		return "null";
	}

	private String getUserId() {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(
				"personCenter", Context.MODE_PRIVATE);

		return mSharedPreferences.getInt("userId", 0) + "";
	}
	//针对的是push 的消息推送的进行的数据存储了
	public void postPushInfo() throws SocketException {
		PostPushInfo postInfoTask = new PostPushInfo();
		postInfoTask.execute();
	}
	
	class PostPushInfo extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			HashMap<String, String> mparams = new HashMap<String, String>();
			mparams.put("userId", userId + "");
			mparams.put("channelId", channelId + "");
			mparams.put("appId", appId + "");
			String a = new ApiHttpUtil().postMethod(
					Config.getProperty("SAVEANDROIDCHANNEL", ""), mparams);
			Log.i("推送信息提交", "推送信息提交");
			return null;
		}
	}
	

	public void postStateInfo() throws SocketException {
		PostInfoTask postInfoTask = new PostInfoTask();
		postInfoTask.execute();
	}
	
	class PostInfoTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			HashMap<String, String> mparams = new HashMap<String, String>();
			mparams.put("sourceType", sourceType + "");
			try {
				mparams.put("ip", getLocalIPAddress());
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mparams.put("visitContent", URLEncoder.encode(visitContent));
			mparams.put("userId", getUserId());
			String a = new ApiHttpUtil().postMethod(
					Config.getProperty("SETAPPCOUNT", ""), mparams);
			
			Log.i("页面统计", "页面统计");
			return null;
		}

	}

	public void getPicture() throws SocketException {
		getPictureTask task = new getPictureTask();
		task.execute();
	}
//获取图片的首页的数据
	class getPictureTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String a = new ApiHttpUtil().getMethod(
					Config.getProperty("GETPUSHIMAGE", ""), "");
			return a;
		}
		@Override 
		protected void onPostExecute(String result) {
			Log.w("pic result", result + "ttt");
			//此处主要判断的是数据的参数是存在
			System.out.println("pic result-------"+result);
			if (result.length() > 0) {
				JSONObject jsonObject;
				int code = -1;
				try {
					jsonObject = new JSONObject(result);
					//此处的数据是可以进行的默认的跳转的 首页的图片
					 SharedPreferences mSharedPreferences = mContext.getSharedPreferences("personCenter",
								Context.MODE_PRIVATE); 
					 Editor editor=mSharedPreferences.edit();
					 JSONArray jsonArray1=jsonObject.getJSONObject("results").getJSONArray("version");
					 for (int i = 0; i < jsonArray1.length(); i++) {
						if(jsonArray1.getJSONObject(i).getInt("appType")==0)
						{
					    editor.putInt("versionCode", jsonArray1.getJSONObject(i).getInt("versionNo"));
						editor.putString("versionName",  jsonArray1.getJSONObject(i).getString("versionName"));
						editor.putString("versionUrl", Config.getProperty("FILEURL", "")+jsonArray1.getJSONObject(i).getString("fileUrl"));
						editor.putString("updateLog", jsonArray1.getJSONObject(i).getString("versionDesc"));
						editor.commit();
						}
					}
					code = jsonObject.getInt("code");
					if (code == 0) {
						JSONArray jsonArray=jsonObject.getJSONArray("pushImage");
						List<Banner>  list=new ArrayList<Banner>();
						Banner banner;
						Map<String,Object> map;
						for (int i = 0; i < jsonArray.length(); i++) {
							banner=new Banner();
							banner.setId(jsonArray.getJSONObject(i).getString("id"));
							banner.setPicture(Config.getProperty("FILEURL", "")+jsonArray.getJSONObject(i).getString("fileUrl"));
							banner.setTitle(jsonArray.getJSONObject(i).getString("title"));
							if(i==2)
							{
								break;
							}
							list.add(banner);
						}
						if(list.size()>0&&mSharedPreferences.getBoolean("isBannerCan", true))
						{
							saveObject(list);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			super.onPostExecute(result);
		}
		

		  private void saveObject(List<Banner> list) {  
		        SharedPreferences mSharedPreferences = mContext.getSharedPreferences("base64", Context.MODE_PRIVATE);  
		        try {  
		            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		            ObjectOutputStream oos = new ObjectOutputStream(baos);  
		            oos.writeObject(list);  
		  
		            String personBase64 = new String(Base64.encode(baos.toByteArray(),Base64.DEFAULT));  
		            SharedPreferences.Editor editor = mSharedPreferences.edit();  
		            editor.putString("picListObject", personBase64);  
		            editor.commit();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
		    } 
	}
}

