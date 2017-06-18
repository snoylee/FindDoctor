package com.guangyi.finddoctor.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.onlineAsk.OnLineDoctorList;
import com.guangyi.finddoctor.onlineAsk.OnLineDoctorTimeListActivity;
import com.guangyi.finddoctor.personCenter.UserLoginActivity;

public class OnLineDoctorListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Doctor> mList;
	private boolean isLogin;

	public OnLineDoctorListAdapter(Context context, List<Doctor> list,
			boolean isLogin) {
		this.mContext = context;
		this.mList = list;
		this.isLogin = isLogin;
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
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.doctor_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tv_doctor_name);
			viewHolder.position = (TextView) convertView
					.findViewById(R.id.tv_doctor_position);
			viewHolder.skill = (TextView) convertView
					.findViewById(R.id.tv_doctor_skill);
			viewHolder.grade = (TextView) convertView
					.findViewById(R.id.tv_grade);
			viewHolder.gradePeople = (TextView) convertView
					.findViewById(R.id.tv_grade_people);
			viewHolder.ratingBar1 = (RatingBar) convertView
					.findViewById(R.id.ratingBar1);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.iv_doctor_pic);
			viewHolder.button1 = (Button) convertView
					.findViewById(R.id.button1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		if(mList.get(position).getAttachFileByte().length()>0)
//		{
//			 byte[] tempb = Base64.decode(mList.get(position).getAttachFileByte(), Base64.DEFAULT);
//			 viewHolder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0 ,tempb.length));
//		}
		
		ImageManager2.from(mContext).displayImage(viewHolder.imageView, mList.get(position).getAttachFileByte(), R.drawable.touxiang);
		
		if (mList.get(position).getDoctName().toString().trim().length() <= 0) {
			viewHolder.name.setText("");
		} else {
			viewHolder.name.setText(mList.get(position).getDoctName());
		}

		if (mList.get(position).getDoctPosi().toString().trim().length() <= 0) {
			viewHolder.position.setText("");
		} else {
			viewHolder.position.setText(String.valueOf(mList.get(position)
					.getDoctPosi()));
		}

		if (mList.get(position).getDoctorPosition().toString().trim().length() <= 0) {
			viewHolder.skill.setText("");
		} else {
			viewHolder.skill.setText(mList.get(position).getDoctorPosition());
		}

		
			viewHolder.grade.setText(mList.get(position).getScore()*2 + "分");
		

		viewHolder.gradePeople.setText(mList.get(position).getSumScore()
				+ "人评");
	
			viewHolder.ratingBar1.setRating((Integer.valueOf(mList
					.get(position).getScore())) );
		
			viewHolder.button1.setText("电话预约");
		viewHolder.button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isLogin) {
					Intent it = new Intent();
					it.putExtra("doctor", mList.get(position));
					it.setClass(mContext, OnLineDoctorTimeListActivity.class);
					mContext.startActivity(it);
				} else {
					Toast.makeText(mContext, "您还没有登录，请登录!",
							Toast.LENGTH_LONG).show();
					Intent it = new Intent();
					it.setClass(mContext, UserLoginActivity.class);
					OnLineDoctorList activity = (OnLineDoctorList) mContext;
					activity.startActivityForResult(it, 0);
				}

			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView name, position, skill, grade, gradePeople;
		RatingBar ratingBar1;
		ImageView imageView;
		Button button1;
	}

}
