package com.guangyi.finddoctor.adapter;

import java.util.List;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.hospitalRegister.DoctorTimeListActivity;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.onlineAsk.OnLineAddConsultInfoForChart;
import com.guangyi.finddoctor.onlineAsk.OnLineDoctorTimeListActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class CollectDoctorAadpter extends BaseAdapter {
	private Context mContext;
	private List<Doctor> mList;

	public CollectDoctorAadpter(Context context, List<Doctor> list) {
		this.mContext = context;
		this.mList = list;
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.collect_doctor_list_item, null);
			holder = new ViewHolder();
			holder.tv_doctName = (TextView) convertView
					.findViewById(R.id.tv_doctName);
			holder.tv_doctPosi = (TextView) convertView
					.findViewById(R.id.tv_doctPosi);
			holder.tv_doctHospName = (TextView) convertView
					.findViewById(R.id.tv_doctHospName);
			holder.tv_doctSpecialty = (TextView) convertView
					.findViewById(R.id.tv_doctSpecialty);
			holder.ratingbar_score = (RatingBar) convertView
					.findViewById(R.id.ratingbar_score);
			holder.tv_score = (TextView) convertView
					.findViewById(R.id.tv_score);
			holder.tv_scoreNum = (TextView) convertView
					.findViewById(R.id.tv_scoreNum);
			holder.iv_online_talk = (ImageView) convertView
					.findViewById(R.id.iv_online_talk);
			holder.iv_register_talk = (ImageView) convertView
					.findViewById(R.id.iv_register_talk);
			holder.iv_tel_talk = (ImageView) convertView
					.findViewById(R.id.iv_tel_talk);
			holder.iv_doctor_pic = (ImageView) convertView
					.findViewById(R.id.iv_doctor_pic);
			convertView.setTag(holder);
		}

		else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		
		holder.iv_online_talk  .setBackgroundResource(R.drawable.online_normal);
holder.iv_register_talk .setBackgroundResource(R.drawable.register_normal);
holder.iv_tel_talk .setBackgroundResource(R.drawable.tel_normal);
		holder.tv_doctName.setText(mList.get(position).getDoctName());
		holder.tv_doctPosi.setText(mList.get(position).getDoctPosi());
		holder.tv_doctHospName.setText(mList.get(position).getHospName());
		holder.tv_doctSpecialty
				.setText(mList.get(position).getDoctorPosition());
		holder.ratingbar_score.setRating(mList.get(position).getScore());
		holder.tv_score.setText(mList.get(position).getScore() * 2 + " 分");
		holder.tv_scoreNum.setText(String.valueOf(mList.get(position)
				.getSumScore()) + " 人评");
		int ISCANAPPOIMENT = mList.get(position).getIsCanAppoiment();
		// if(mList.get(position).getAttachFileByte().length()>0)
		// {
		// byte[] tempb = Base64.decode(mList.get(position).getAttachFileByte(),
		// Base64.DEFAULT);
		// iv_doctor_pic.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0
		// ,tempb.length));
		// }
		
		ImageManager2.from(mContext).displayImage(holder.iv_doctor_pic, mList.get(position).getAttachFileByte(), R.drawable.touxiang);
		if (ISCANAPPOIMENT == 1) {
			holder.iv_register_talk.setImageResource(R.drawable.register_press);
			holder.iv_register_talk
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent();
							it.setClass(mContext, DoctorTimeListActivity.class);
							it.putExtra("doctor", mList.get(position));
							it.putExtra("consType", 4);
							mContext.startActivity(it);
						}
					});

		} else if (ISCANAPPOIMENT == 0) {
			holder.iv_register_talk
					.setImageResource(R.drawable.register_normal);

			holder.iv_register_talk
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
						}
					});
		}

		if (mList.get(position).getIsCanCons() == 1) {
			holder.iv_online_talk.setImageResource(R.drawable.online_press);
			holder.iv_online_talk
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Doctor doctor=mList.get(position);
							showPayDialog(doctor, doctor.getMoney()+"", doctor.getRemainNum(), doctor.getCostType());
						}
					});

		} else if (mList.get(position).getIsCanCons() == 0) {
			holder.iv_online_talk.setImageResource(R.drawable.online_normal);

			holder.iv_online_talk
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
						}
					});
		}

		if (mList.get(position).getIsCanPhonePay() == 1) {
			holder.iv_tel_talk.setImageResource(R.drawable.tel_press);
			holder.iv_tel_talk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent it = new Intent();
					it.setClass(mContext, OnLineDoctorTimeListActivity.class);
					it.putExtra("doctor", mList.get(position));
					mContext.startActivity(it);

				}
			});

		} else if (mList.get(position).getIsCanPhonePay() == 0) {
			holder.iv_tel_talk.setImageResource(R.drawable.tel_normal);
			holder.iv_tel_talk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			});
		}

		// else if(shcetype == 2){
		// viewHolder.iv_online_talk.setImageResource(R.drawable.online_press);
		// }else if(shcetype == 4){
		// viewHolder.iv_tel_talk.setImageResource(R.drawable.tel_press);
		// }else if(shcetype == 5){
		// viewHolder.iv_register_talk.setImageResource(R.drawable.register_press);
		// viewHolder.iv_tel_talk.setImageResource(R.drawable.tel_press);
		// }else if(shcetype == 6){
		// viewHolder.iv_online_talk.setImageResource(R.drawable.online_press);
		// viewHolder.iv_tel_talk.setImageResource(R.drawable.tel_press);
		// }else if(shcetype == 7){
		// viewHolder.iv_online_talk.setImageResource(R.drawable.online_press);
		// viewHolder.iv_register_talk.setImageResource(R.drawable.register_press);
		// viewHolder.iv_tel_talk.setImageResource(R.drawable.tel_press);
		// }

		return convertView;
	}

	class ViewHolder {
		TextView tv_doctName, tv_doctPosi, tv_doctHospName, tv_doctSpecialty,
				tv_score, tv_scoreNum;
		RatingBar ratingbar_score;
		ImageView iv_online_talk, iv_register_talk, iv_tel_talk, iv_doctor_pic;
	}
	private void showPayDialog(final Doctor doctor,final String money,final int remainNum,int costType)
	{
			View view=LayoutInflater.from(mContext).inflate(R.layout.progress_dialog_pay, null);
	    	final Dialog dialog=new Dialog(mContext, R.style.Translucent_NoTitle);
	    	dialog.setContentView(view);
	    	TextView tv_progress=(TextView) view.findViewById(R.id.tv_progress);
	    	if(costType==0)
	    	{
	    		if(remainNum<=0)
	    		{
	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问需要支付"
	    					+ money + "元/条");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 9, money.length() + 9,
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			tv_progress.setText(builder);
	    		}
	    		else
	    		{
	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问前"
	    					+remainNum+ "条免费,继续追问需要"+money+"元/条");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 6, String.valueOf(remainNum).length() + 6,
	    					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 16+(remainNum+"").length(), money.length() + 16+(remainNum+"").length(),
	    					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	    			tv_progress.setText(builder);
	    		}
	    	
	    	}
	    	
	    	else if(costType==1)
	    	{
	    		if(remainNum<=0)
	    		{
	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问需要支付"
	    					+ money + "元");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 9, money.length() + 9,
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			tv_progress.setText(builder);
	    		}
	    		else
	    		{
	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问前"
	    					+ remainNum+ "条免费,继续追问需要"+money+"元");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 6, String.valueOf(remainNum).length() + 6,
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 16+(remainNum+"").length(), money.length() + 16+(remainNum+"").length(),
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			tv_progress.setText(builder);
	    		}
	    	}
	    	
	     	else 
	    	{

	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问免费");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 5, 7,
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			tv_progress.setText(builder);
	    	}
//			SpannableStringBuilder builder = new SpannableStringBuilder("已经有"
//					+ count + "人成功预约!");
//			builder.setSpan(redSpan, 3, count.length() + 3,
//					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//	    	tv_progress.setText("您的免费咨询条数已经用完，继续咨询需要付费:"+money+"元");
	    	Button btn_ok=(Button) view.findViewById(R.id.btn_ok);
	    	btn_ok.setText("去咨询");
	    	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
	    	btn_cancle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
	    	btn_ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent it = new Intent();
					it.setClass(mContext,
							OnLineAddConsultInfoForChart.class);
					it.putExtra("doctor", doctor);
					mContext.startActivity(it);
					dialog.cancel();
					
				}
			});
	    	dialog.setCanceledOnTouchOutside(false);
	    	dialog.show();
	
	}

}
