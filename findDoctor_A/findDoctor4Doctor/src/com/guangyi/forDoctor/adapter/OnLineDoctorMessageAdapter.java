package com.guangyi.forDoctor.adapter;

import java.util.List;

import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.model.DoctorConsult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OnLineDoctorMessageAdapter extends BaseAdapter {
	private Context mContext;
	private List<DoctorConsult> listConsult;

	public  OnLineDoctorMessageAdapter(Context context,List<DoctorConsult> listConsult)
	{
		this.mContext=context;
		this.listConsult=listConsult;	
		
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listConsult.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listConsult.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.online_doctor_message_item, null);
			viewHolder=new ViewHolder();
			viewHolder.problem=(TextView) convertView.findViewById(R.id.problem);
			viewHolder.replyProblem=(TextView) convertView.findViewById(R.id.replyProblem);
			viewHolder.doctorName=(TextView) convertView.findViewById(R.id.doctorName);


			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.problem.setText("Q:"+listConsult.get(position).getConsProblem());
		viewHolder.replyProblem.setText(listConsult.get(position).getConsReplyProblem()+"");
		viewHolder.doctorName.setText(listConsult.get(position).getExpertName()+"");
		
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView problem,replyProblem,doctorName;
	}

}

