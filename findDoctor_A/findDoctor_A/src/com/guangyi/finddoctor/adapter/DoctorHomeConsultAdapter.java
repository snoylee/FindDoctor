package com.guangyi.finddoctor.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.DoctorConsult;

public class DoctorHomeConsultAdapter extends BaseAdapter {
	private Context mContext;
	private List<DoctorConsult> listConsult;

	// private String mDoctorId;
	public DoctorHomeConsultAdapter(Context context,
			List<DoctorConsult> listConsult) {
		this.mContext = context;
		this.listConsult = listConsult;
		// this.mDoctorId=DoctorId;
		// filter();

	}

	// private void filter()
	// {
	//
	// for (int i = 0; i < listConsult.size(); i++) {
	// if(listConsult.get(i).getExpertId()!=mDoctorId)
	// {
	// listConsult.remove(i);
	// }
	// }
	// }

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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.doctor_home_consult_item, null);
//			 tv_discuss_title, tv_discuss_time, tv_discuss_question,
//			tv_discuss_reply, tv_doct_name;
//	        ImageView iv_doctor_pic;
	        
			holder=new ViewHolder();
			holder.tv_discuss_title = (TextView) convertView
					.findViewById(R.id.tv_discuss_title);
			holder.tv_discuss_time = (TextView) convertView
					.findViewById(R.id.tv_discuss_time);
			holder.tv_discuss_question = (TextView) convertView
					.findViewById(R.id.tv_discuss_question);
			holder.tv_discuss_reply = (TextView) convertView
					.findViewById(R.id.tv_discuss_reply);
			// viewtv_dictor_score= (TextView)
			// convertView.findViewById(R.id.tv_dictor_score);
			holder.tv_doct_name = (TextView) convertView
					.findViewById(R.id.tv_doct_name);
			holder.iv_doctor_pic=(ImageView) convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
			
			
		}
		
		else
		{
			holder=(ViewHolder) convertView.getTag();
		}


			ImageManager2.from(mContext).displayImage(holder.iv_doctor_pic, listConsult.get(position).getAttachFileByte(), R.drawable.touxiang);
			holder.tv_discuss_title.setVisibility(View.GONE);
			holder.tv_discuss_time.setText(listConsult
					.get(position)
					.getConsTime()
					.substring(0,
							listConsult.get(position).getConsTime().length() - 2));
			holder.tv_discuss_question.setText(listConsult.get(position)
					.getConsProblem());
			holder.tv_doct_name
					.setText(listConsult.get(position).getDoctName());
		
		if (listConsult.get(position).getConsTitle().trim().length() > 0) {
			holder.tv_discuss_title.setVisibility(View.VISIBLE);
			holder.tv_discuss_title.setText(listConsult.get(position)
					.getConsTitle());
		}
		
		else
		{
			holder.tv_discuss_title.setVisibility(View.GONE);
			holder.tv_discuss_title.setText("");
		}

		

	
		

		
		
		if(listConsult.get(position).getConsUserReply().equals("1"))
		{
			holder.tv_discuss_reply.setText(listConsult.get(position)
					.getConsReplyProblem());
			holder.tv_discuss_reply.setBackgroundResource(R.drawable.doctor_reply);
		}
		else if(listConsult.get(position).getConsUserReply().equals("2"))
		{
			holder.tv_discuss_reply.setText("");
			holder.tv_discuss_reply.setBackgroundResource(R.drawable.chatto_pic);
		}
		else if(listConsult.get(position).getConsUserReply().equals("3"))
		{
			holder.tv_discuss_reply.setText("");
			holder.tv_discuss_reply.setBackgroundResource(R.drawable.chartto_voice);
		}
		
		else
		{
			holder.tv_discuss_reply.setText("");
			holder.tv_discuss_reply.setBackgroundResource(R.drawable.doctor_reply);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_discuss_title, tv_discuss_time, tv_discuss_question,
				tv_discuss_reply, tv_doct_name;
		
		ImageView iv_doctor_pic;
	}

}
