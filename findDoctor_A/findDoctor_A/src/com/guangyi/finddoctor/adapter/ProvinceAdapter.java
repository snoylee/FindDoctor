package com.guangyi.finddoctor.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.City;

public class ProvinceAdapter extends BaseAdapter {
	private Context mContext;
	private List<City> mList;
	public  ProvinceAdapter(Context context,List<City> list)
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.list_view_item, null);
			viewHolder=new ViewHolder();
			viewHolder.regionName=(TextView) convertView.findViewById(R.id.tv_progress);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.regionName.setText(mList.get(position).getRegionName());
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView regionName;
	}

}

