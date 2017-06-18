package com.guangyi.finddoctor.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.hospitalRegister.DoctorTimeDialogActivity;
import com.guangyi.finddoctor.hospitalRegister.HospitalRegisterConfirmOrder;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.model.TimeList;

//主页gridView的适配器  
public class DoctorTimeGridAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<TimeList> mList;
	private Doctor doctor;
	private int regtotal;
	private int allDay;
	private int isCanRegister;
	public DoctorTimeGridAdapter(Context context, List<TimeList> list,Doctor doctor,int regtotal,int allDay,int isCanRegister) {
		this.mContext = context;
		this.mList = list;
		this.doctor=doctor;
		this.regtotal=regtotal;
		this.allDay=allDay;
		this.isCanRegister=isCanRegister;

	}

	private static class ViewHolder {
		Button button;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder _ViewHolder;
		if (convertView == null) {
			mLayoutInflater = LayoutInflater.from(mContext);
			_ViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(
					R.layout.doctor_time_list_grid_item, null);
			_ViewHolder.button = (Button) convertView
					.findViewById(R.id.btn_grid_item);
			convertView.setTag(_ViewHolder);
		} else {
			_ViewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(isCanRegister==0)
		{
			_ViewHolder.button.setEnabled(false);
		}
		else
		{
			_ViewHolder.button.setEnabled(true);
			
		}
		if(mList.get(position).getTimeList().trim().length()<=5)
		{
			if(allDay==1)
			{
				_ViewHolder.button.setText("上午");
			}
			else if(allDay==2)
			{
				_ViewHolder.button.setText("下午");
			}
			else if(allDay==3)
			{
				_ViewHolder.button.setText("晚上");
			}
			
			else if(allDay==4)
			{
				_ViewHolder.button.setText("全天");
			}
		}
		else
		{
		_ViewHolder.button.setText(mList.get(position).getTimeList().trim());
		}

		if(regtotal>0)
		{
			if (mList.get(position).getBtnState() == 1) {
				_ViewHolder.button.setEnabled(true);
				_ViewHolder.button.setBackgroundResource(R.drawable.time_button_selector);
				_ViewHolder.button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						FindDoctorApplication app=(FindDoctorApplication) mContext.getApplicationContext();
						app.setScid(mList.get(position).getScid());
						Intent it = new Intent();
						it.setClass(mContext, HospitalRegisterConfirmOrder.class);
						it.putExtra("time", mList.get(position).getShiftdate()+","+mList.get(position).getTimeList().trim());
						it.putExtra("depCategory", mList.get(position).getClinicidname());
						it.putExtra("doctor", doctor);
						it.putExtra("regfee", mList.get(position).getRegfee());
						it.putExtra("daySectionFlag", allDay);
						if(_ViewHolder.button.getText().length()<=5)
						{
						
							it.putExtra("allDay", allDay);
						}
						DoctorTimeDialogActivity activity=(DoctorTimeDialogActivity) mContext;
						activity.startActivity(it);
						activity.finish();
					}
				});
				
//				else if(mList.get(position).getBtnState() == 0) 
			} else  {
				_ViewHolder.button.setBackgroundResource(R.drawable.time_list_button_false);
			}
		}
		
		
		else
		{
			_ViewHolder.button.setBackgroundResource(R.drawable.time_list_button_false);
		}

		return convertView;
	}

}
