package com.guangyi.finddoctor.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.hospitalRegister.DoctorHomeActivity;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.DoctorConsult;
import com.guangyi.finddoctor.onlineAsk.OnLineDoctorMessage;
import com.guangyi.finddoctor.onlineAsk.OnlineChatActivity;

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
		ViewHolder holder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.online_doctor_message_item, null);
//			TextView problem,replyProblem,doctorName;
//			ImageView iv_pic;
			holder=new ViewHolder();
			holder.problem=(TextView) convertView.findViewById(R.id.problem);
			holder.replyProblem=(TextView) convertView.findViewById(R.id.replyProblem);
			holder.doctorName=(TextView) convertView.findViewById(R.id.doctorName);
			holder.iv_pic=(ImageView) convertView.findViewById(R.id.iv_pic);
			convertView.setTag(holder);
		}
		
		else
		{
			holder=(ViewHolder) convertView.getTag();
		}

		holder.problem.setText("Q:"+listConsult.get(position).getConsProblem());
		holder.doctorName.setText(listConsult.get(position).getExpertName()+"");
		ImageManager2.from(mContext).displayImage(holder.iv_pic, listConsult.get(position).getAttachFileByte(), R.drawable.touxiang);
		holder.iv_pic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it=new Intent(mContext,DoctorHomeActivity.class);
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				it.putExtra("doctId", listConsult.get(position).getConsDoctorId());
				mContext.startActivity(it);
				
			}
		});
		if(listConsult.get(position).getConsUserReply().equals("1"))
		{
			holder.replyProblem.setText(listConsult.get(position).getConsReplyProblem()+"");
			holder.replyProblem.setBackgroundResource(R.drawable.online_ask_right);
		}
		else if(listConsult.get(position).getConsUserReply().equals("2"))
		{
			holder.replyProblem.setText("");
			holder.replyProblem.setBackgroundResource(R.drawable.chatto_pic);
		}
		else if(listConsult.get(position).getConsUserReply().equals("3"))
		{
			holder.replyProblem.setText("");
			holder.replyProblem.setBackgroundResource(R.drawable.chartto_voice);
		}
		else
		{
			holder.replyProblem.setText("");
			holder.replyProblem.setBackgroundResource(R.drawable.online_ask_right);
		}
		
	
		
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView problem,replyProblem,doctorName;
		ImageView iv_pic;
	}

}

