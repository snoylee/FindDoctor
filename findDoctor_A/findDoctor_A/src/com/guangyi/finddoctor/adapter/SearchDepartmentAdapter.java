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
import com.guangyi.finddoctor.model.Department;

public class SearchDepartmentAdapter extends BaseAdapter {

	private Context mContext;
	private List<Department> mList;
	public  SearchDepartmentAdapter(Context context,List<Department> list)
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_search_department, null);
			viewHolder=new ViewHolder();
			viewHolder.tv_search_deptname=(TextView) convertView.findViewById(R.id.tv_search_deptname);
			viewHolder.tv_search_deptintroduce=(TextView) convertView.findViewById(R.id.tv_search_deptintroduce);
			viewHolder.tv_search_hospital=(TextView) convertView.findViewById(R.id.tv_search_hospital);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_search_deptname.setText(Html.fromHtml(mList.get(position).getDepaName()));
		viewHolder.tv_search_deptintroduce.setText(Html.fromHtml(mList.get(position).getIntroduction()));
		if(mList.get(position).getDeptHospName().trim().length()>0)
		{
			viewHolder.tv_search_hospital.setText(Html.fromHtml("ҽԺ:"+mList.get(position).getDeptHospName()));
		}
		
		else
		{
			viewHolder.tv_search_hospital.setText("");
		}
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView tv_search_deptname,tv_search_deptintroduce,tv_search_hospital;
	}



}
