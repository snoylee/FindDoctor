package com.guangyi.finddoctor.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.Hospital;

public class SearchHospitalAdapter extends BaseAdapter{

	private Context mContext;
	private List<Hospital> mList;
	public  SearchHospitalAdapter(Context context,List<Hospital> list)
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_search_hospital, null);
			viewHolder=new ViewHolder();
			viewHolder.tv_search_hospname=(TextView) convertView.findViewById(R.id.tv_search_hospname);
			viewHolder.ttv_search_hospclass=(TextView) convertView.findViewById(R.id.ttv_search_hospclass);
			viewHolder.tv_search_tel=(TextView) convertView.findViewById(R.id.tv_search_tel);
			viewHolder.tv_search_address=(TextView) convertView.findViewById(R.id.tv_search_address);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		


		
		viewHolder.tv_search_hospname.setText(Html.fromHtml(mList.get(position).getHospName()));
//		if(mList.get(position).getHospClass()==0)
//		{
//			viewHolder.ttv_search_hospclass.setText("一级医院");
//		}
//		if(mList.get(position).getHospClass()==1)
//		{
//			viewHolder.ttv_search_hospclass.setText("二级医院");
//		}
//		if(mList.get(position).getHospClass()==2)
//		{
//			viewHolder.ttv_search_hospclass.setText("三级医院");
//		}
//		if(mList.get(position).getHospClass()==3)
//		{
//			viewHolder.ttv_search_hospclass.setText("三级甲等");
//		}
		
//		13-一级甲等 
//		12-一级乙等 
//		11-一级丙等 
//		23-二级甲等 
//		22-二级乙等 
//		21-二级丙等 
//		34-三级特等 
//		33-三级甲等 
//		32-三级乙等 
//		31- 三 级 丙 等

		
		if(mList.get(position).getHospClass()==13)
			{
				viewHolder.ttv_search_hospclass.setText("一级甲等 ");
			}
			if(mList.get(position).getHospClass()==12)
			{
				viewHolder.ttv_search_hospclass.setText("一级乙等");
			}
			if(mList.get(position).getHospClass()==11)
			{
				viewHolder.ttv_search_hospclass.setText("一级丙等 ");
			}
			if(mList.get(position).getHospClass()==23)
			{
				viewHolder.ttv_search_hospclass.setText("三级甲等");
			}
			if(mList.get(position).getHospClass()==22)
			{
				viewHolder.ttv_search_hospclass.setText("二级乙等");
			}
			if(mList.get(position).getHospClass()==21)
			{
				viewHolder.ttv_search_hospclass.setText("二级丙等 ");
			}
			if(mList.get(position).getHospClass()==34)
			{
				viewHolder.ttv_search_hospclass.setText("三级特等");
			}
			if(mList.get(position).getHospClass()==33)
			{
				viewHolder.ttv_search_hospclass.setText("三级甲等 ");
			}
			if(mList.get(position).getHospClass()==32)
			{
				viewHolder.ttv_search_hospclass.setText("三级乙等 ");
			}
			if(mList.get(position).getHospClass()==31)
			{
				viewHolder.ttv_search_hospclass.setText("三级丙等");
			}
			else
			{
				viewHolder.ttv_search_hospclass.setText("");
			}
		viewHolder.tv_search_tel.setText(mList.get(position).getHospTel());
		viewHolder.tv_search_address.setText(Html.fromHtml(mList.get(position).getHospAddr()));
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView tv_search_hospname,ttv_search_hospclass,tv_search_tel,tv_search_address;
	}



}
