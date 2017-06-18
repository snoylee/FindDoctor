package com.guangyi.forDoctor.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.guangyi.forDoctor.config.Config;
import com.guangyi.forDoctor.http.ApiHttpUtil;
import com.guangyi.forDoctor.model.Banner;

public class StateInfos  {
	private Context mContext;
	private String userId,channelId,appId;
	public StateInfos(Context context, String userId, String channelId,String appId) {
		this.mContext = context;
		this.userId = userId;
		this.channelId = channelId;
		this.appId = appId;

	}
	
	
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

	public StateInfos(Context context) {
		this.mContext = context;

	}





	public void getPicture() throws SocketException {
		getPictureTask task = new getPictureTask();
		task.execute();
	}

	class getPictureTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String a = new ApiHttpUtil().getMethod(
					Config.getProperty("GETPUSHIMAGE", ""), "");
			return a;
		}

		@Override 
		protected void onPostExecute(String result) {
			Log.w("pic result", result+"tt");
			if (result.length() > 0) {
				JSONObject jsonObject;
				int code = -1;
				try {
					jsonObject = new JSONObject(result);
					 SharedPreferences mSharedPreferences = mContext.getSharedPreferences("doctor", Context.MODE_PRIVATE);
					 Editor editor=mSharedPreferences.edit();
					 JSONArray jsonArray1=jsonObject.getJSONObject("results").getJSONArray("version");
					 for (int i = 0; i < jsonArray1.length(); i++) {
						if(jsonArray1.getJSONObject(i).getInt("appType")==1)
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

