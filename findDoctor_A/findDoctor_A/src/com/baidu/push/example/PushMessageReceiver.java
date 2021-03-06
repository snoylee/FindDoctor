package com.baidu.push.example;

import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.guangyi.finddoctor.activity.BannerDetail;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.secondActivity.SpersonCenter;
import com.guangyi.finddoctor.utils.StateInfos;

/**
 * Push消息处理receiver
 */
public class PushMessageReceiver extends BroadcastReceiver {
	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();
	AlertDialog.Builder builder;

	/**
	 * @param context
	 *            Context
	 * @param intent
	 *            接受的intent
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {

		Log.d(TAG, ">>> Receive intent: \r\n" + intent);

		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {

			SharedPreferences mSharedPreferences = context
					.getSharedPreferences("personCenter", Context.MODE_PRIVATE);
			// intent.putExtra(ResultCodeCategory.fLAG, 3);
			// intent.putExtra("userMobile",
			// mSharedPreferences.getString("userMobile",""));
			// intent.putExtra("userId",
			// mSharedPreferences.getInt("userId",-1));

			if (mSharedPreferences.getInt("userId", -1) > -1) {
				// 获取消息的内容
				String message = intent.getExtras().getString(
						PushConstants.EXTRA_PUSH_MESSAGE_STRING);
				// 消息的用户自定义内容的读取方式
				Log.i("message", "onMessage: " + message);
				//自定义内容的json串
				Log.d("message",
						"EXTRA_EXTRA = "
								+ intent.getStringExtra(PushConstants.EXTRA_EXTRA));

//				Toast.makeText(context, message, Toast.LENGTH_LONG).show();

				// 用户在此自定义处理消息，以下代码是为demo界面展示用
				// Intent responseIntent = null;
				// responseIntent = new Intent(Utils.ACTION_MESSAGE);
				// responseIntent.putExtra(Utils.EXTRA_MESSAGE, message);
				// responseIntent.setClass(context, MainActivity.class);
				// responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// context.startActivity(responseIntent);

				
				//此处做个判断
				if(message.contains("id")){
					showNotification(context, message,1);
				}else
					{Editor mEditor = mSharedPreferences.edit();
					mEditor.putInt("msgCount", Integer.valueOf(message));
					mEditor.commit();
					showNotification(context,Integer.valueOf(message));
					}
			}

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {

			SharedPreferences mSharedPreferences = context
					.getSharedPreferences("personCenter", Context.MODE_PRIVATE);
			// intent.putExtra(ResultCodeCategory.fLAG, 3);
			// intent.putExtra("userMobile",
			// mSharedPreferences.getString("userMobile",""));
			// intent.putExtra("userId",
			// mSharedPreferences.getInt("userId",-1));

			if (mSharedPreferences.getInt("userId", -1) > -1) {
				// 处理绑定等方法的返回数据
				// PushManager.startWork()的返回值是通过PushConstants.METHOD_BIND得到的

				// 获取方法
				final String method = intent
						.getStringExtra(PushConstants.EXTRA_METHOD);
				// 方法返回错误码，若绑定返回错误（非0），则应用将不能正常的接受消息
				// 绑定失败的原因有很多种，如网络原因，或access token 过期
				// 请不要再出错是进行简单的startWork 的调用，这有可能导致死循环
				// 可以通过限制重试的次数，或者在其他时机重新调用来解决
				int errorCode = intent.getIntExtra(
						PushConstants.EXTRA_ERROR_CODE,
						PushConstants.ERROR_SUCCESS);
				String content = "";
				if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
					// 返回内容
					content = new String(
							intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
				}

				// 用户在此自定义处理消息，以下代码是为demo界面展示用
				Log.d(TAG, "onMessage: method : " + method);
				Log.d(TAG, "onMessage: result : " + errorCode);
				Log.d(TAG, "onMessage: content : " + content);
//				Toast.makeText(
//						context,
//						"method : " + method + "\n result: " + errorCode
//								+ "\n content = " + content, Toast.LENGTH_LONG)
//						.show();

				String appid = mSharedPreferences.getInt("userId", -1)+"";
				String channelid = "";
				String userid = "";

				try {
					JSONObject jsonContent = new JSONObject(content);
					JSONObject params = jsonContent
							.getJSONObject("response_params");
//					appid = params.getString("appid");
					channelid = params.getString("channel_id");
					userid = params.getString("user_id");
				} catch (JSONException e) {
					Log.e(Utils.TAG, "Parse bind json infos errorOne: " + e);
				}

				try {
					new StateInfos(context, userid, channelid, appid)
							.postPushInfo();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}

		}
		// else if (intent.getAction().equals(
		// PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
		// Log.d(TAG, "intent=" + intent.toUri(0));
		//
		// //自定义内容的json串

		// Log.d(TAG, "EXTRA_EXTRA = " +
		// intent.getStringExtra(PushConstants.EXTRA_EXTRA));
		//
		// Intent aIntent = new Intent();
		// aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// aIntent.setClass(context, CustomActivity.class);
		// String title = intent
		// .getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
		// aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, title);
		// String content = intent
		// .getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
		// aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, content);
		//
		// context.startActivity(aIntent);
		// }
	}
	/**
	 * 图片的推送
	 * @param mContext
	 * @param message
	 * @param type 
	 */
	@SuppressWarnings("deprecation")
	private void showNotification(Context mContext, String message ,int type) {
		Notification mNotification;
		NotificationManager mManager = (NotificationManager) mContext
				.getSystemService("notification");
		int icon = R.drawable.icon;
		String id;
		mNotification = new Notification();
		mNotification.icon = icon;
		mNotification.tickerText = "新信息！";
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		//Intent i = new Intent(mContext, MainActivity.class);
		
		try {
			JSONObject jsonContent = new JSONObject(message);
			id = jsonContent.getString("id");
			System.out.println("-------------------------------------"+message);
			Intent i = new Intent(mContext,BannerDetail.class);
			i.putExtra("id", id);//通过意图带过去这个id 的数据 添加到 地址后面
			PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i,
					Intent.FLAG_ACTIVITY_NEW_TASK);
			mNotification.setLatestEventInfo(mContext, mContext.getResources()
					.getString(R.string.app_name), "找医生发来一条消息!",
					pendingIntent);
			mManager.notify(0, mNotification);
			
		} catch (JSONException e) {
			Log.e(Utils.TAG, "Parse bind json infos errorTwo: " + e);
			e.printStackTrace();
		}
	}
	//新闻的推送
	private void showNotification(Context mContext, int count) {
		Notification mNotification;
		NotificationManager mManager = (NotificationManager) mContext
				.getSystemService("notification");
		int icon = R.drawable.icon;
		mNotification = new Notification();
		mNotification.icon = icon;
		mNotification.tickerText = "新信息！";
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		//Intent i = new Intent(mContext, MainActivity.class);
		Intent i = new Intent(mContext,SpersonCenter.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		mNotification.setLatestEventInfo(mContext, mContext.getResources()
				.getString(R.string.app_name), "收到" + count + "条新回复!",
				pendingIntent);
		mManager.notify(0, mNotification);
	}
}
