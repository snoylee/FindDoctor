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
//			viewHolder.ttv_search_hospclass.setText("һ��ҽԺ");
//		}
//		if(mList.get(position).getHospClass()==1)
//		{
//			viewHolder.ttv_search_hospclass.setText("����ҽԺ");
//		}
//		if(mList.get(position).getHospClass()==2)
//		{
//			viewHolder.ttv_search_hospclass.setText("����ҽԺ");
//		}
//		if(mList.get(position).getHospClass()==3)
//		{
//			viewHolder.ttv_search_hospclass.setText("�����׵�");
//		}
		
//		13-һ���׵� 
//		12-һ���ҵ� 
//		11-һ������ 
//		23-�����׵� 
//		22-�����ҵ� 
//		21-�������� 
//		34-�����ص� 
//		33-�����׵� 
//		32-�����ҵ� 
//		31- �� �� �� ��

		
		if(mList.get(position).getHospClass()==13)
			{
				viewHolder.ttv_search_hospclass.setText("һ���׵� ");
			}
			if(mList.get(position).getHospClass()==12)
			{
				viewHolder.ttv_search_hospclass.setText("һ���ҵ�");
			}
			if(mList.get(position).getHospClass()==11)
			{
				viewHolder.ttv_search_hospclass.setText("һ������ ");
			}
			if(mList.get(position).getHospClass()==23)
			{
				viewHolder.ttv_search_hospclass.setText("�����׵�");
			}
			if(mList.get(position).getHospClass()==22)
			{
				viewHolder.ttv_search_hospclass.setText("�����ҵ�");
			}
			if(mList.get(position).getHospClass()==21)
			{
				viewHolder.ttv_search_hospclass.setText("�������� ");
			}
			if(mList.get(position).getHospClass()==34)
			{
				viewHolder.ttv_search_hospclass.setText("�����ص�");
			}
			if(mList.get(position).getHospClass()==33)
			{
				viewHolder.ttv_search_hospclass.setText("�����׵� ");
			}
			if(mList.get(position).getHospClass()==32)
			{
				viewHolder.ttv_search_hospclass.setText("�����ҵ� ");
			}
			if(mList.get(position).getHospClass()==31)
			{
				viewHolder.ttv_search_hospclass.setText("��������");
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
