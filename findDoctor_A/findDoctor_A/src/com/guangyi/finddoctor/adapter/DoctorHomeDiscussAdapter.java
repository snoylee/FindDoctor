package com.guangyi.finddoctor.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.DoctorDiscuss;

public class DoctorHomeDiscussAdapter extends BaseAdapter {
	private Context mContext;
	private List<DoctorDiscuss> listDicuss;
//	private String mDoctorId;
	public  DoctorHomeDiscussAdapter(Context context,List<DoctorDiscuss> listDicuss)
	{
		this.mContext=context;
		this.listDicuss=listDicuss;
//		this.mDoctorId=DoctorId;
//		filter();
		
		
	}
//	private void filter()
//	{
//
//		for (int i = 0; i < listDicuss.size(); i++) {
//			if(listDicuss.get(i).getExpertId()!=mDoctorId)
//			{
//				listDicuss.remove(i);
//			}
//		}
//		
//	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDicuss.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listDicuss.get(position);
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.doctor_home_discuss_item, null);
			viewHolder=new ViewHolder();
			viewHolder.CommentName=(TextView) convertView.findViewById(R.id.CommentName);
			viewHolder.CreateTime=(TextView) convertView.findViewById(R.id.CreateTime);
			viewHolder.Content=(TextView) convertView.findViewById(R.id.Content);
			viewHolder.iv_from = (ImageView) convertView.findViewById(R.id.iv_from);
			viewHolder.tv_dictor_score=(TextView) convertView.findViewById(R.id.tv_dictor_score);
			viewHolder.ratingbar_score= (RatingBar) convertView.findViewById(R.id.ratingbar_score);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.CommentName.setText(listDicuss.get(position).getEvalDocTitle());
		if(listDicuss.get(position).getCreateTime().length()>0)
		{
		viewHolder.CreateTime.setText(listDicuss.get(position).getCreateTime().substring(0, listDicuss.get(position).getCreateTime().length()-2));
		}
		viewHolder.Content.setText(listDicuss.get(position).getContent());
		int type = listDicuss.get(position).getType();
		if(type == 2){
			viewHolder.iv_from.setImageResource(R.drawable.doctor_home_discuss_form_hospital_register);
		}else if(type == 3){
			viewHolder.iv_from.setImageResource(R.drawable.doctor_home_discuss_form_phone);
		}else if(type == 4){
			viewHolder.iv_from.setImageResource(R.drawable.doctor_home_discuss_form_onlie);
		}
		viewHolder.tv_dictor_score.setText(listDicuss.get(position).getScore()*2+"·Ö");
		if(listDicuss.get(position).getScore()>0)
		{
		viewHolder.ratingbar_score.setRating(listDicuss.get(position).getScore());;
		}
		
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView CommentName,CreateTime,Content,tv_dictor_score;
		RatingBar ratingbar_score;
		ImageView iv_from;
	}

}

