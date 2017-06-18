package com.guangyi.finddoctor.adapter;

import java.util.List;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.Hospital;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectHospitalAdapter extends BaseAdapter {
	private Context mContext;
	private List<Hospital> mList;
	
	public CollectHospitalAdapter(Context context,List<Hospital> list){
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
	public View getView(int position, View convertView, ViewGroup aparent) {
		ViewHolder viewHolder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.collect_hospital_item, null);
			viewHolder = new ViewHolder();
			viewHolder.isCan=(ImageView) convertView.findViewById(R.id.isCan);
			viewHolder.tv_collect_hospName = (TextView) convertView.findViewById(R.id.tv_collect_hospName);
			viewHolder.tv_collect_hospClass = (TextView) convertView.findViewById(R.id.tv_collect_hospClass);
			viewHolder.tv_collect_hospTel = (TextView) convertView.findViewById(R.id.tv_collect_hospTel);
			viewHolder.tv_collect_hospAddr = (TextView) convertView.findViewById(R.id.tv_collect_hospAddr);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.isCan.setVisibility(View.INVISIBLE);
		viewHolder.tv_collect_hospName.setText(mList.get(position).getHospName());
//		int i = mList.get(position).getHospClass();
//		if(i == 1){
//			viewHolder.tv_collect_hospClass.setText("一级");
//		}else if(i == 2){
//			viewHolder.tv_collect_hospClass.setText("二级");
//		}else if(i == 3){
//			viewHolder.tv_collect_hospClass.setText("三级");
//		}
		
		
		if(mList.get(position).getHospClass()==13)
		{
			viewHolder.tv_collect_hospClass.setText("一级甲等 ");
		}
		else if(mList.get(position).getHospClass()==12)
		{
			viewHolder.tv_collect_hospClass.setText("一级乙等");
		}
		else if(mList.get(position).getHospClass()==11)
		{
			viewHolder.tv_collect_hospClass.setText("一级丙等 ");
		}
		else if(mList.get(position).getHospClass()==23)
		{
			viewHolder.tv_collect_hospClass.setText("三级甲等");
		}
		else if(mList.get(position).getHospClass()==22)
		{
			viewHolder.tv_collect_hospClass.setText("二级乙等");
		}
		else if(mList.get(position).getHospClass()==21)
		{
			viewHolder.tv_collect_hospClass.setText("二级丙等 ");
		}
		else if(mList.get(position).getHospClass()==34)
		{
			viewHolder.tv_collect_hospClass.setText("三级特等");
		}
		else if(mList.get(position).getHospClass()==33)
		{
			viewHolder.tv_collect_hospClass.setText("三级甲等 ");
		}
		else if(mList.get(position).getHospClass()==32)
		{
			viewHolder.tv_collect_hospClass.setText("三级乙等 ");
		}
		else if(mList.get(position).getHospClass()==31)
		{
			viewHolder.tv_collect_hospClass.setText("三级丙等");
		}
		else
		{
			viewHolder.tv_collect_hospClass.setText("");
		}
		
		if(mList.get(position).getGuanghaoStatus()>0)
		{
			viewHolder.isCan.setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.isCan.setVisibility(View.INVISIBLE);
		}
		viewHolder.tv_collect_hospTel.setText(mList.get(position).getHospTel());
		viewHolder.tv_collect_hospAddr.setText(mList.get(position).getHospAddr());
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_collect_hospName,tv_collect_hospClass,tv_collect_hospTel,tv_collect_hospAddr;
		ImageView isCan;
	}

}
