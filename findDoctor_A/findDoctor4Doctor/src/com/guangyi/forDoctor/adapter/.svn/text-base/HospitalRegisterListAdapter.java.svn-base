package com.guangyi.forDoctor.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.model.PhoneOrHospital;

public class HospitalRegisterListAdapter extends BaseAdapter{



	private Context mContext;
	private List<PhoneOrHospital> mList;
	public  HospitalRegisterListAdapter(Context context,List<PhoneOrHospital> list)
	{
		this.mContext=context;
		this.mList=list;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null)
		{

			convertView=LayoutInflater.from(mContext).inflate(R.layout.hospital_register_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
			viewHolder.tv_sex=(TextView) convertView.findViewById(R.id.tv_sex);
			viewHolder.tv_age=(TextView) convertView.findViewById(R.id.tv_age);
			viewHolder.tv_diagnose=(TextView) convertView.findViewById(R.id.tv_diagnose);
			viewHolder.tv_hosp_register=(TextView) convertView.findViewById(R.id.tv_hosp_register);
			viewHolder.tv_disease=(TextView) convertView.findViewById(R.id.tv_disease);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}

		viewHolder.tv_time.setText(mList.get(position).getTimesection());
		viewHolder.tv_date.setText(mList.get(position).getAppoTime().split(" ")[0].toString());
		viewHolder.tv_age.setText(mList.get(position).getAge()+"岁");
		viewHolder.tv_disease.setText(mList.get(position).getDisease());
		if(mList.get(position).getSex().equals("1"))
		{
			viewHolder.tv_sex.setText("男");
		}
		if(mList.get(position).getSex().equals("2"))
		{
			viewHolder.tv_sex.setText("女");
		}
		if(mList.get(position).getSex().equals("3"))
		{
			viewHolder.tv_sex.setText("未知");
		}
		
		if(mList.get(position).getIsFrist().equals("0"))
		{
			viewHolder.tv_hosp_register.setText("首次预约");
		}
		if(mList.get(position).getIsFrist().equals("1"))
		{
			viewHolder.tv_hosp_register.setText("非首次预约");
		}
		if(mList.get(position).getReturnflag().equals("0"))
		{
			viewHolder.tv_diagnose.setText("初诊");
		}
		if(mList.get(position).getReturnflag().equals("1"))
		{
			viewHolder.tv_diagnose.setText("复诊");
		}
		if(mList.get(position).getReturnflag().equals("2"))
		{
			viewHolder.tv_diagnose.setText("出院复诊");
		}
		
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView tv_time,tv_date,tv_sex,tv_age,tv_diagnose,tv_hosp_register,tv_disease;
	}







}
