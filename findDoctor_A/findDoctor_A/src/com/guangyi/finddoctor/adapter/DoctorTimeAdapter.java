package com.guangyi.finddoctor.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.hospitalRegister.DoctorTimeDialogActivity;
import com.guangyi.finddoctor.hospitalRegister.HospitalRegisterConfirmOrder;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.model.NewTimeItem;
import com.guangyi.finddoctor.model.TimeList;
import com.guangyi.finddoctor.utils.DateTools;
import com.guangyi.finddoctor.utils.WeekTool;

public class DoctorTimeAdapter extends BaseAdapter {
	private Context mContext;
	private List<NewTimeItem> mList;
	private Doctor mDoctor;
	private int isCanRegister;


	public DoctorTimeAdapter(Context context, List<NewTimeItem> list,Doctor doctor,int isCanRegister) {
		this.mContext = context;
		this.mList = list;
		this.mDoctor=doctor;
		this.isCanRegister=isCanRegister;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.new_doctror_time_list_item, null);
//			TextView tv_clinic_type, tv_money,tv_date,tv_week;
//			Button btn_am,btn_pm,btn_all,btn_all_str,btn_night;
			viewHolder = new ViewHolder();
			viewHolder.tv_clinic_type = (TextView) convertView
					.findViewById(R.id.tv_clinic_type);
			viewHolder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			viewHolder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);
			viewHolder.tv_week = (TextView) convertView
					.findViewById(R.id.tv_week);
			
			viewHolder.btn_am=(Button) convertView.findViewById(R.id.btn_am);
			viewHolder. btn_pm=(Button) convertView.findViewById(R.id.btn_pm);
			viewHolder. btn_all=(Button) convertView.findViewById(R.id.btn_all);
			viewHolder. btn_all_str=(Button) convertView.findViewById(R.id.btn_all_str);
			viewHolder. btn_night=(Button) convertView.findViewById(R.id.btn_night);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(isCanRegister==0)
		{
			viewHolder.btn_am.setEnabled(false);
			viewHolder. btn_pm.setEnabled(false);
			viewHolder. btn_all.setEnabled(false);
			viewHolder. btn_all_str.setEnabled(false);
			viewHolder. btn_night.setEnabled(false);
		}
		else
		{
			viewHolder.btn_am.setEnabled(true);
			viewHolder. btn_pm.setEnabled(true);
			viewHolder. btn_all.setEnabled(true);
			viewHolder. btn_all_str.setEnabled(true);
			viewHolder. btn_night.setEnabled(true);
		}
		viewHolder.btn_am.setVisibility(View.GONE);
		viewHolder. btn_pm.setVisibility(View.GONE);
		viewHolder. btn_all.setVisibility(View.GONE);
		viewHolder. btn_all_str.setVisibility(View.GONE);
		viewHolder. btn_night.setVisibility(View.GONE);
		viewHolder.tv_clinic_type.setText(mList.get(position).getClinicidname().trim());
		viewHolder.tv_money.setText(mList.get(position).getRegfee()+"元");
		final String time=mList.get(position).getShiftdate().trim();
		if(time.length()>0)
			{
			viewHolder.tv_week.setText(DateTools.getChinaDayOfWeek(time));
			viewHolder.tv_date.setText(time.split("-")[1]+"月"+time.split("-")[2]+"日 ");
			}
		
		else
		{
			viewHolder.tv_week.setText("");
			viewHolder.tv_date.setText("");
		}
		if(mList.get(position).getClinicidname().trim().equals("专家门诊"))
		{
			viewHolder.tv_clinic_type.setBackgroundResource(R.drawable.expert_doctor);
		}
		
		else if(mList.get(position).getClinicidname().trim().equals("普通门诊"))
		{
			viewHolder.tv_clinic_type.setBackgroundResource(R.drawable.common_doctor);
		}
		
		else
		{
			viewHolder.tv_clinic_type.setBackgroundResource(R.drawable.common_doctor);
		}
		final List<TimeList> list=mList.get(position).getListTimemeList();
		if (mList.get(position).isAM()) {
			viewHolder.btn_am.setVisibility(View.VISIBLE);
			if(mList.get(position).getRegtotalAM()>0)
			{
				viewHolder.btn_am.setBackgroundResource(R.drawable.time_button_selector);
				viewHolder.btn_am.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(list.size()>0&&list.get(0).getTimeList().length()<=5)
						{
							 FindDoctorApplication app=(FindDoctorApplication) mContext.getApplicationContext();
								app.setScid(mList.get(position).getScid());
								Intent it = new Intent();
								it.setClass(mContext, HospitalRegisterConfirmOrder.class);
								it.putExtra("time", mList.get(position).getShiftdate()+","+mList.get(position).getListTimemeList().get(0).getTimeList());
								it.putExtra("depCategory", mList.get(position).getClinicidname());
								it.putExtra("doctor", mDoctor);
								it.putExtra("regfee", mList.get(position).getRegfee());
								it.putExtra("allDay", 1);
								it.putExtra("daySectionFlag", 1);
								mContext.startActivity(it);
						}
						else
						{
							Intent it = new Intent();
							it.setClass(mContext, DoctorTimeDialogActivity.class);
							it.putExtra("doctor", mDoctor);
							it.putExtra("timeList", (Serializable)list);
							it.putExtra("time", time);
							it.putExtra("tag", 1);
							it.putExtra("daySectionFlag", 1);
							mContext.startActivity(it);
						}
					}
				});
			}
		}
		if (mList.get(position).isPM())
		{	  viewHolder. btn_pm.setVisibility(View.VISIBLE);
			if(mList.get(position).getRegtotalPM()>0)
		{
		    
				viewHolder.btn_pm.setBackgroundResource(R.drawable.time_button_selector);
				viewHolder.btn_pm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(list.size()>0&&list.get(0).getTimeList().length()<=5)
					{
					
						 FindDoctorApplication app=(FindDoctorApplication) mContext.getApplicationContext();
							app.setScid(mList.get(position).getScid());
							Intent it = new Intent();
							it.setClass(mContext, HospitalRegisterConfirmOrder.class);
							it.putExtra("time", mList.get(position).getShiftdate()+","+mList.get(position).getListTimemeList().get(0).getTimeList());
							it.putExtra("depCategory", mList.get(position).getClinicidname());
							it.putExtra("doctor", mDoctor);
							it.putExtra("regfee", mList.get(position).getRegfee());
							it.putExtra("allDay", 2);
							it.putExtra("daySectionFlag", 2);
							mContext.startActivity(it);
					}
					else
					{
					Intent it = new Intent();
					it.setClass(mContext, DoctorTimeDialogActivity.class);
					it.putExtra("doctor", mDoctor);
					it.putExtra("timeList", (Serializable)list);
					it.putExtra("time", time);
					it.putExtra("tag", 2);
					it.putExtra("daySectionFlag", 2);
					mContext.startActivity(it);
					}
					
				}
			});
		}
			
		}
		
		if (mList.get(position).isNight())
		{
			if(mList.get(position).getRegtotalNight()>0)
	 		{
				viewHolder. btn_night.setVisibility(View.VISIBLE);
				viewHolder.btn_night.setBackgroundResource(R.drawable.time_button_selector);
				viewHolder.btn_night.setOnClickListener(new OnClickListener() {
	 				@Override
	 				public void onClick(View v) {
	 					if(list.size()>0&&list.get(0).getTimeList().length()<=5)
						{
						
							 FindDoctorApplication app=(FindDoctorApplication) mContext.getApplicationContext();
								app.setScid(mList.get(position).getScid());
								Intent it = new Intent();
								it.setClass(mContext, HospitalRegisterConfirmOrder.class);
								it.putExtra("time", mList.get(position).getShiftdate()+","+mList.get(position).getListTimemeList().get(0).getTimeList());
								it.putExtra("depCategory", mList.get(position).getClinicidname());
								it.putExtra("doctor", mDoctor);
								it.putExtra("regfee", mList.get(position).getRegfee());
								it.putExtra("allDay", 3);
								it.putExtra("daySectionFlag", 3);
								mContext.startActivity(it);
						}
						else
						{
	 					Intent it = new Intent();
	 					it.setClass(mContext, DoctorTimeDialogActivity.class);
	 					it.putExtra("doctor", mDoctor);
	 					it.putExtra("timeList", (Serializable)list);
	 					it.putExtra("time", time);
	 					it.putExtra("tag", 3);
	 					it.putExtra("daySectionFlag", 3);
	 					mContext.startActivity(it);
						}
	 				}
	 			});
	 		}
		}
		if (mList.get(position).isAll())
		{
			viewHolder.btn_all.setVisibility(View.VISIBLE);
			viewHolder. btn_all_str.setVisibility(View.VISIBLE);
			if(mList.get(position).getRegtotalAll()>0)
	 		{
				viewHolder.btn_all.setBackgroundResource(R.drawable.time_button_selector);
				viewHolder.btn_all.setOnClickListener(new OnClickListener() {
	 				@Override
	 				public void onClick(View v) {	
	 					if(list.size()>0&&list.get(0).getTimeList().length()<=5)
						{
							 FindDoctorApplication app=(FindDoctorApplication) mContext.getApplicationContext();
								app.setScid(mList.get(position).getScid());
								Intent it = new Intent();
								it.setClass(mContext, HospitalRegisterConfirmOrder.class);
								it.putExtra("time", mList.get(position).getShiftdate()+","+mList.get(position).getListTimemeList().get(0).getTimeList());
								it.putExtra("depCategory", mList.get(position).getClinicidname());
								it.putExtra("doctor", mDoctor);
								it.putExtra("regfee", mList.get(position).getRegfee());
								it.putExtra("allDay", 4);
								it.putExtra("daySectionFlag", 4);
								mContext.startActivity(it);
						}
	 					
	 					else
						{
	 					Intent it = new Intent();
	 					it.setClass(mContext, DoctorTimeDialogActivity.class);
	 					it.putExtra("doctor", mDoctor);
	 					it.putExtra("timeList", (Serializable)list);
	 					it.putExtra("time", time);
	 					it.putExtra("tag", 4);
	 					it.putExtra("daySectionFlag", 4);
	 					mContext.startActivity(it);
						}
	 					}
	 			});
	 		}
		}

	   
	  
		
		return convertView;
	}

	class ViewHolder {
		TextView tv_clinic_type, tv_money,tv_date,tv_week;
		Button btn_am,btn_pm,btn_all,btn_all_str,btn_night;
	}

}
