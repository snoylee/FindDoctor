package com.guangyi.finddoctor.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.hospitalRegister.DoctorHomeActivity;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.model.DoctorConsult;
import com.guangyi.finddoctor.model.DoctorDiscuss;

public class SearchDoctorListAdapter extends BaseAdapter {

	private List<Doctor> mList;
	private Context mContext;
	
	public  SearchDoctorListAdapter(Context context,List<Doctor> list)
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_search_doctor, null);
			viewHolder=new ViewHolder();
			viewHolder.name=(TextView) convertView.findViewById(R.id.tv_doctName);
			viewHolder.position=(TextView) convertView.findViewById(R.id.tv_doctPosi);
			viewHolder.tv_doctHospName=(TextView) convertView.findViewById(R.id.tv_doctHospName);
			viewHolder.skill=(TextView) convertView.findViewById(R.id.tv_doctSpecialty);
			viewHolder.grade=(TextView) convertView.findViewById(R.id.tv_score);
			viewHolder.gradePeople=(TextView) convertView.findViewById(R.id.tv_scoreNum);
			viewHolder.ratingBar1=(RatingBar) convertView.findViewById(R.id.ratingbar_score);
			viewHolder.imageView=(ImageView) convertView.findViewById(R.id.iv_doctor_pic);
			convertView.setTag(viewHolder);
			
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		
//		if(mList.get(position).getAttachFileByte().length()>0)
//		{
//		 byte[] tempb = Base64.decode(mList.get(position).getAttachFileByte(), Base64.DEFAULT);
//		 viewHolder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0 ,tempb.length));
//		}
		
		ImageManager2.from(mContext).displayImage(viewHolder.imageView, mList.get(position).getAttachFileByte(), R.drawable.touxiang);
		if(mList.get(position).getDoctName().toString().trim().length()<=0)
		{
			viewHolder.name.setText("");
		}
		else
		{
		viewHolder.name.setText(Html.fromHtml(mList.get(position).getDoctName()));
		}
		
		
		if(mList.get(position).getDoctPosi().toString().trim().length()<=0)
		{
			viewHolder.position.setText("");
		}
		else
		{
			viewHolder.position.setText(Html.fromHtml(String.valueOf(mList.get(position).getDoctPosi())));
		}
		
		if(mList.get(position).getDoctorPosition().toString().trim().length()<=0)
		{
			viewHolder.skill.setText("");
		}
		else
		{
			viewHolder.skill.setText(Html.fromHtml(mList.get(position).getDoctorPosition()));
		}
		
//		if(mList.get(position).getComplex()!=null)
//		{
			viewHolder.grade.setText(mList.get(position).getScore()*2+"·Ö");
//		}
//		
		viewHolder.gradePeople.setText(mList.get(position).getSumScore()+"ÈËÆÀ");
//		if(mList.get(position).getComplex()!=null)
//		{
		viewHolder.ratingBar1.setRating(mList.get(position).getScore());
//		}
		
		
//		viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent it = new Intent();
//				it.setClass(mContext, DoctorHomeActivity.class);
//				it.putExtra("doctor", (Serializable)mList.get(position));
////				it.putExtra("listConsult", (Serializable)listConsults);
////				it.putExtra("listDicuss", (Serializable)listDicuss);
//				
//				mContext.startActivity(it);
//				
//			}
//		});
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView name,position,skill,grade,gradePeople,tv_doctHospName;
		RatingBar ratingBar1;
		ImageView imageView;
	}



}
