package com.guangyi.forDoctor.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.model.DoctorConsult;
import com.guangyi.forDoctor.model.Evaluate;

public class EvaluateListAdapter extends BaseAdapter{

	private Context mContext;
	private List<Evaluate> mList;
	public  EvaluateListAdapter(Context context,List<Evaluate> list)
	{
		this.mContext=context;
		this.mList=list;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.online_list_item_not_reply, null);
			viewHolder=new ViewHolder();
			viewHolder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.tv_content=(TextView) convertView.findViewById(R.id.tv_content);
			viewHolder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_title.setText("Q:"+mList.get(position).getConsProblem()+"");
		viewHolder.tv_content.setText("ÆÀ¼Û:"+mList.get(position).getEvaluateContext()+"");
		if(mList.get(position).getEvaluateTime().length()>0)
		{
		viewHolder.tv_time.setText(mList.get(position).getEvaluateTime().substring(0, mList.get(position).getEvaluateTime().length()-2));
		}
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView tv_title,tv_content,tv_time;
	}



}
