package com.guangyi.forDoctor.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.adapter.OnlineMainListAdapterNotReply.ViewHolder;
import com.guangyi.forDoctor.model.DoctorConsult;

public class OnLineMainListAdapterReply extends BaseAdapter{


	private Context mContext;
	private List<DoctorConsult> mList;
	public  OnLineMainListAdapterReply(Context context,List<DoctorConsult> list)
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.online_list_item_reply, null);
			viewHolder=new ViewHolder();
			viewHolder.tv_problem=(TextView) convertView.findViewById(R.id.tv_problem);
			viewHolder.tv_problem_time=(TextView) convertView.findViewById(R.id.tv_problem_time);
			viewHolder.tv_reply=(TextView) convertView.findViewById(R.id.tv_reply);
			viewHolder.tv_reply_time=(TextView) convertView.findViewById(R.id.tv_reply_time);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_problem.setText(mList.get(position).getConsProblem()+"");
		if(mList.get(position).getConsTime().length()>0)
		{
			viewHolder.tv_problem_time.setText(mList.get(position).getConsTime().substring(0, mList.get(position).getConsTime().length()-2));
		}
//		viewHolder.tv_problem_time.setText(mList.get(position).getConsTime()+"");
		viewHolder.tv_reply.setText(mList.get(position).getConsReplyProblem());
//		viewHolder.tv_reply_time=(TextView) convertView.findViewById(R.id.tv_reply_time);
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView tv_problem,tv_problem_time,tv_reply,tv_reply_time;
	}





}
