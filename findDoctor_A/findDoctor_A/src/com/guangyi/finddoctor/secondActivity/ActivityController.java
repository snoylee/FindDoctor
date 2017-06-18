package com.guangyi.finddoctor.secondActivity;
import android.content.Context;	
import android.content.Intent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
public class ActivityController
{
	public static String hospitalRegister = "finddoctor.intent.action.hospitalRegister";
	public static String onlineAsk = "finddoctor.intent.action.onlineAsk";
	public static String selfService = "finddoctor.intent.action.selfService";
	public static String personCenter = "finddoctor.intent.action.personCenter";
	public static Context mInstance = null;
	public static TabHost mTabHost = null;
	public static void empty()
	{
		if (mTabHost != null)
		{
			mTabHost.clearAllTabs();
			mTabHost = null;
		}
		mInstance = null;
	}

	public static void init(Context context)
	{
		if (mTabHost != null && mInstance != null)
		{
			TabSpec specHospitalRegister = mTabHost
					.newTabSpec("hospitalRegister")
					.setIndicator("")
					.setContent(
							new Intent(context, ShospitalRegister.class)
									.setAction(ActivityController.hospitalRegister));
			TabSpec speOnlineAsk = mTabHost
					.newTabSpec("onlineAsk")
					.setIndicator("")
					.setContent(
							new Intent(context, SonlineAsk.class)
									.setAction(ActivityController.onlineAsk));
			TabSpec specSelfService = mTabHost
					.newTabSpec("selfService")
					.setIndicator("")
					.setContent(
							new Intent(context, SselfService.class)
									.setAction(ActivityController.selfService));
			TabSpec specPersonCenter = mTabHost
					.newTabSpec("personCenter")
					.setIndicator("")
					.setContent(
							new Intent(context, SpersonCenter.class)
									.setAction(ActivityController.personCenter));
			mTabHost.addTab(specHospitalRegister);
			mTabHost.addTab(speOnlineAsk);
			mTabHost.addTab(specSelfService);
			mTabHost.addTab(specPersonCenter);
		}
	}
	public static void startView(Intent intent)
	{
		if (mTabHost != null && mInstance != null)
		{
			String intentAction = intent.getAction();
			if (intentAction.equals(ActivityController.hospitalRegister))
			{
				mTabHost.setCurrentTabByTag("hospitalRegister");
			} else if (intentAction.equals(ActivityController.onlineAsk))
			{
				mTabHost.setCurrentTabByTag("onlineAsk");
			} else if (intentAction.equals(ActivityController.selfService))
			{
				mTabHost.setCurrentTabByTag("selfService");
			} else if (intentAction.equals(ActivityController.personCenter))
			{
				mTabHost.setCurrentTabByTag("personCenter");
			} 
		}
	}
}
