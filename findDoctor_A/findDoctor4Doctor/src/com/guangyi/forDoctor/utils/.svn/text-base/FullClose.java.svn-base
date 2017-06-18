package com.guangyi.forDoctor.utils;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

//活动于整个activity的生命周期，通过单例模式获得，用于关闭activity
public class FullClose {
	 private static FullClose mFullClose;
	 private List<Activity> activityList;
	 private  FullClose()
	 {
		 activityList = new ArrayList<Activity>();
	 }
	 public static FullClose getFullClose() {
		if(mFullClose==null)
		{
			mFullClose=new FullClose();
			return mFullClose;
		}
		else {
			return mFullClose;
		}
	}
	 
	 public void addActivity(Activity activity)
	 {
		 if (activityList.contains(activity)) {
			return;
		 }
		 else {
			 activityList.add(activity);
		}
		 
	 }
	 
	 public void removeActivity(Activity activity)
	 {
		 if (activityList.contains(activity)) {
			 activityList.remove(activity);
			 }
			 else {
				 return;
			}
	 }
	 
	 public void coloseActivity()
	 {
		 if (activityList.size()>0) {
			 for (Activity _activity : activityList) {
				_activity.finish();
			}
			
		}
		 
	 }
	 
}