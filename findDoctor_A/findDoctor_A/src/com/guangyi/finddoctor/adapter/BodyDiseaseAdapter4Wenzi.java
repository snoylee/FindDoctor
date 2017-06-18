package com.guangyi.finddoctor.adapter;

import java.util.List;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.BodyDisease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BodyDiseaseAdapter4Wenzi extends BaseAdapter {
	private Context mContext;
	private List<BodyDisease> mList;
	
	
	public BodyDiseaseAdapter4Wenzi(Context context,List<BodyDisease> list){
		this.mContext =context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_child_wenzi, null);
			holder = new ViewHolder();
			holder.tv_disease_name = (TextView) convertView.findViewById(R.id.tv_disease_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_disease_name.setText(mList.get(position).getLableName());
		
		return convertView;
	}

	class ViewHolder{
		TextView tv_disease_name;
	}
}
