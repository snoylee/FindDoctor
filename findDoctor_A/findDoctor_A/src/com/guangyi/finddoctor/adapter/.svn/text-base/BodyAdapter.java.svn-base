package com.guangyi.finddoctor.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.R;

public class BodyAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, String>> mList;
	private int selectItem = -1;
	
	public BodyAdapter(Context context,List<Map<String, String>> list){
		this.mContext = context;
		this.mList = list;
	}
	public void setSelectItem(int selectItem){
		this.selectItem = selectItem;
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
		ViewHoler holer;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_parent, null);
			holer = new ViewHoler();
			holer.tv_body_name = (TextView) convertView.findViewById(R.id.tv_body_name);
			convertView.setTag(holer);
		}else{
			holer = (ViewHoler) convertView.getTag();
		}
		holer.tv_body_name.setText(mList.get(position).get("bodyname"));
		if(position == selectItem){
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}else{
			convertView.setBackgroundResource(R.drawable.ic_preference_normal);
		}
		return convertView;
	}
class ViewHoler{
	TextView tv_body_name;
}
}
