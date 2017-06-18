package com.guangyi.forDoctor.application;


import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import android.app.Activity;
import android.app.Application;

public class FindDoctorApplication extends Application {
	
//	private static FindDoctorApplication instance = null;

//	public static synchronized FindDoctorApplication getInstance() {
//
//		if (instance == null) {
//			instance = new FindDoctorApplication();
//		}
//		return instance;
//
//	}
	
	private int msgCount;
	private String scid;
	private JSONArray mJsonArray;
	private int isCheckUpdate;
	private String docPicStr;
	private String registerRule;
	
	
	

	
	
	public String getRegisterRule() {
		return registerRule;
	}

	public void setRegisterRule(String registerRule) {
		this.registerRule = registerRule;
	}

	public String getDocPicStr() {
		return docPicStr;
	}

	public void setDocPicStr(String docPicStr) {
		this.docPicStr = docPicStr;
	}

	public int getIsCheckUpdate() {
		return isCheckUpdate;
	}

	public void setIsCheckUpdate(int isCheckUpdate) {
		this.isCheckUpdate = isCheckUpdate;
	}

	public JSONArray getmJsonArray() {
		return mJsonArray;
	}

	public void setmJsonArray(JSONArray mJsonArray) {
		this.mJsonArray = mJsonArray;
	}

	public String getScid() {
		return scid;
	}

	public void setScid(String scid) {
		this.scid = scid;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}
	
	
	
	//异步图片操作
	private HttpClient httpClient;
	public boolean isLogin = false;

	@Override
	public void onCreate() {
		super.onCreate();
		httpClient = this.createHttpClient();
		 mList = new LinkedList<Activity>();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		this.shutdownHttpClient();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		this.shutdownHttpClient();
	}

	// 创建HttpClient实例
	private HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager connMgr = new ThreadSafeClientConnManager(
				params, schReg);

		return new DefaultHttpClient(connMgr, params);
	}

	// 关闭连接管理器并释放资源
	private void shutdownHttpClient() {
		if (httpClient != null && httpClient.getConnectionManager() != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}

	// 对外提供HttpClient实例
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	
	
	
	
	
	
	//activity 循环关闭
	
	//运用list来保存们每一个activity是关键
    private List<Activity> mList;
	
	  // add Activity  
    public void addActivity(Activity activity) { 
    	if(mList.contains(activity))
    	{
    		return;
    	}
    	else
    	{
        mList.add(activity); 
    	}
    } 
    //关闭每一个list内的activity
    public void exit() { 
        try { 
            for (Activity activity:mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
    
    
    public void closeOtherActivity(Activity pActivity) { 
    	for (Activity activity:mList) { 
            if (activity != null&&activity!=pActivity) 
                activity.finish(); 
        } 
    } 

}
