package com.guangyi.forDoctor.personCenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.adapter.IntroduceListAdapter;
import com.guangyi.forDoctor.model.Introduce;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class IntroduceActivity extends BasicActivity {
	private Button ib_back;
	
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext=this;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private TextView tv_progress;
	private Dialog mDownloadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduce_activity);
		initView();
		initListener();
	}

	private void initView() {
		ib_back = (Button) findViewById(R.id.ib_back);
		ListView list_view=(ListView) findViewById(R.id.list_view);
		final List<Introduce> list=new ArrayList<Introduce>();
		Introduce introduce1=new Introduce();
		introduce1.setName("找医生");
		introduce1.setImgId(R.drawable.edition1);
		introduce1.setUrl("");
		list.add(introduce1);
		
		Introduce introduce2=new Introduce();
		introduce2.setName("找医生(医生版)");
		introduce2.setImgId(R.drawable.edition2);
		introduce2.setUrl("");
		list.add(introduce2);
		
		Introduce introduce3=new Introduce();
		introduce3.setName("乐行东莞");
		introduce3.setImgId(R.drawable.lexingdongwan);
		introduce3.setUrl("http://211.139.194.252:9001/down/lxdg.apk");
		list.add(introduce3);
		list_view.setAdapter(new IntroduceListAdapter(this,list));
		
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showDialog(list.get(position).getName(),list.get(position).getUrl());
				
			}
		});
		
	}

	private void initListener() {
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}
	
	private void showDialog(final String name,final String url)
	{
			View view=LayoutInflater.from(this).inflate(R.layout.progress_dialog_pay, null);
	    	final Dialog dialog=new Dialog(this, R.style.Translucent_NoTitle);
	    	dialog.setContentView(view);
	    	TextView tv_progress=(TextView) view.findViewById(R.id.tv_progress);
	    	tv_progress.setText("您确定下载:"+name);
	    	Button btn_ok=(Button) view.findViewById(R.id.btn_ok);
	    	btn_ok.setText("确定");
	    	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
	    	btn_cancle.setText("取消");
	    	btn_cancle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
	    	btn_ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(url.length()>0)
					{
						cancelUpdate=false;
						mHashMap=new HashMap<String, String>();
						mHashMap.put("versionUrl", url);
						mHashMap.put("versionName", url.split("/")[url.split("/").length-1]);
						showDownloadDialog();
					}
					else
					{
						showToast("暂无下载链接!");
					}
					
					dialog.cancel();
					
				}
			});
	    	dialog.setCanceledOnTouchOutside(false);
	    	dialog.show();
	
	}
	
	
	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		
		View view=LayoutInflater.from(mContext).inflate(R.layout.alert_dialog, null);
//    	progressBar.setProgress(0);
		mDownloadDialog=new Dialog(mContext, R.style.Translucent_NoTitle);
		mDownloadDialog.setContentView(view);
    	tv_progress=(TextView) view.findViewById(R.id.tv_progress);
    	mProgress=(ProgressBar) view.findViewById(R.id.progress_bar);
    	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
//    			.setMessage("0k/"+totalSize/1000+"k")
//    	dialog.setOnDismissListener(new OnDismissListener() {
//			
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				isDismiss=true;
//				
//			}
//		});
    	btn_cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mDownloadDialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
				
				
			}
		});
    	
    	
    	mDownloadDialog.setCanceledOnTouchOutside(false);
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
    			
		
		
		

	}
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				tv_progress.setText(progress+"%");
				
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};
	
	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}
	

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 *@date 2012-4-26
	 *@blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("versionUrl"));
				
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("versionName"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("versionName"));
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

}
