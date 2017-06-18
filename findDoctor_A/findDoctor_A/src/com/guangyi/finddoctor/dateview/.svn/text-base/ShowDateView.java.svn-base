package com.guangyi.finddoctor.dateview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.guangyi.finddoctor.activity.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;



public class ShowDateView {
	private Context mContext;
	private WheelMain wheelMain;
	EditText txttime;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public ShowDateView(Context context)
	{
		this.mContext=context;
	}
	public void showTime()
	{
		

		LayoutInflater inflater=LayoutInflater.from(mContext);
		final View timepickerview=inflater.inflate(R.layout.timepicker, null);
		Activity activity=(Activity) mContext;
		ScreenInfo screenInfo = new ScreenInfo(activity);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		String time = txttime.getText().toString();
		Calendar calendar = Calendar.getInstance();
		if(JudgeDate.isDate(time, "yyyy-MM-dd")){
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year,month,day);
		new AlertDialog.Builder(mContext)
		.setTitle("选择时间")
		.setView(timepickerview)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				txttime.setText(wheelMain.getTime());
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
	

	}

}
