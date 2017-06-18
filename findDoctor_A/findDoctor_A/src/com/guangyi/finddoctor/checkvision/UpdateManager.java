package com.guangyi.finddoctor.checkvision;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateManager
{
	public static String packageName="com.guangyi.finddoctor.activity";
	/* ������ */
	private static final int DOWNLOAD = 1;
	/* ���ؽ��� */
	private static final int DOWNLOAD_FINISH = 2;
	/* ���������XML��Ϣ */
	HashMap<String, String> mHashMap;
	/* ���ر���·�� */
	private String mSavePath;
	/* ��¼���������� */
	private int progress;
	/* �Ƿ�ȡ������ */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* ���½����� */
	private ProgressBar mProgress;
	private TextView tv_progress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// ��������
			case DOWNLOAD:
				// ���ý�����λ��
				mProgress.setProgress(progress);
				tv_progress.setText(progress+"%");
				
				break;
			case DOWNLOAD_FINISH:
				// ��װ�ļ�
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * ����������
	 */
	public void checkUpdate(HashMap<String, String> hashMap)
	{
		if (isUpdate(hashMap))
		{
			// ��ʾ��ʾ�Ի���
			showNoticeDialog();
		} else
		{
//			Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * �������Ƿ��и��°汾
	 * 
	 * @return
	 */
	private boolean isUpdate(HashMap<String, String> hashMap)
	{
		// ��ȡ��ǰ����汾
		int versionCode = getVersionCode(mContext);
		// ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ
//		InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
//		// ����XML�ļ��� ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
//		ParseXmlService service = new ParseXmlService();
		try
		{
			mHashMap = hashMap;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (null != mHashMap)
		{
			int serviceCode = Integer.valueOf(mHashMap.get("versionCode"));
			// �汾�ж�
			if (serviceCode > versionCode)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * ��ȡ����汾��
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			// ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * ��ʾ������¶Ի���
	 */
	private void showNoticeDialog()
	{
		
				View view=LayoutInflater.from(mContext).inflate(R.layout.update_dialog, null);
		    	final Dialog dialog=new Dialog(mContext, R.style.Translucent_NoTitle);
		    	dialog.setContentView(view);
		    	TextView tv_progress=(TextView) view.findViewById(R.id.tv_progress);
		    	tv_progress.setText(mHashMap.get("updateLog"));
		    	Button btn_ok=(Button) view.findViewById(R.id.btn_ok);
		    	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
		    	//����
		    	btn_cancle.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
						// ��ʾ���ضԻ���
						showDownloadDialog();
					}
				});
		    	
		     //�Ժ����	
		    	btn_ok.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Activity activity=(Activity) mContext;
						FindDoctorApplication application=(FindDoctorApplication) activity.getApplication();
						application.setIsCheckUpdate(1);
						dialog.cancel();
					}
				});
		    	dialog.setCanceledOnTouchOutside(false);
		    	dialog.show();
		
		
//		// ����Ի���
//		AlertDialog.Builder builder = new Builder(mContext);
//		builder.setTitle(R.string.soft_update_title);
//		builder.setMessage(mHashMap.get("updateLog"));
//		// ����
//		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				dialog.dismiss();
//				// ��ʾ���ضԻ���
//				showDownloadDialog();
//			}
//		});
//		// �Ժ����
//		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				Activity activity=(Activity) mContext;
//				FindDoctorApplication application=(FindDoctorApplication) activity.getApplication();
//				application.setIsCheckUpdate(1);
//				dialog.dismiss();
//			}
//		});
//		Dialog noticeDialog = builder.create();
//		noticeDialog.show();
	}

	/**
	 * ��ʾ������ضԻ���
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
				// ����ȡ��״̬
				cancelUpdate = true;
			}
		});
    	
    	mDownloadDialog.setCanceledOnTouchOutside(false);
		mDownloadDialog.show();
		// �����ļ�
		downloadApk();
    			
		
		
		
//		// ����������ضԻ���
//		AlertDialog.Builder builder = new Builder(mContext);
//		builder.setTitle(R.string.soft_updating);
//		// �����ضԻ������ӽ�����
//		final LayoutInflater inflater = LayoutInflater.from(mContext);
//		View v = inflater.inflate(R.layout.softupdate_progress, null);
//		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
//		tv_progress= (TextView) v.findViewById(R.id.tv_progress);
//		builder.setView(v);
//		// ȡ������
//		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				dialog.dismiss();
//				// ����ȡ��״̬
//				cancelUpdate = true;
//			}
//		});
//		mDownloadDialog = builder.create();
//		mDownloadDialog.show();
//		// �����ļ�
//		downloadApk();
	}

	/**
	 * ����apk�ļ�
	 */
	private void downloadApk()
	{
		// �������߳��������
		new downloadApkThread().start();
	}

	/**
	 * �����ļ��߳�
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
				// �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// ��ô洢����·��
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("versionUrl"));
					// ��������
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// ��ȡ�ļ���С
					int length = conn.getContentLength();
					// ����������
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// �ж��ļ�Ŀ¼�Ƿ����
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("versionName"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// ����
					byte buf[] = new byte[1024];
					// д�뵽�ļ���
					do
					{
						int numread = is.read(buf);
						count += numread;
						// ���������λ��
						progress = (int) (((float) count / length) * 100);
						// ���½���
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// �������
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// д���ļ�
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// ���ȡ����ֹͣ����.
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
			// ȡ�����ضԻ�����ʾ
			mDownloadDialog.dismiss();
		}
	};
	/**
	 * ��װAPK�ļ�
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("versionName"));
		if (!apkfile.exists())
		{
			return;
		}
		// ͨ��Intent��װAPK�ļ�
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
