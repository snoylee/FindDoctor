package com.guangyi.finddoctor.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.Disease;

public class DiseaseMaybeAdapter extends BaseAdapter {
	private Context mContext;
	private List<Disease> mList;
	
	public DiseaseMaybeAdapter(Context context,List<Disease> list){
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_disease_body, null);
			holder = new ViewHolder();
			holder.tv_disease_name = (TextView) convertView.findViewById(R.id.tv_disease_name);
			holder.tv_disease_introduce = (TextView) convertView.findViewById(R.id.tv_disease_introduce);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_disease_name.setText(mList.get(position).getName());
		holder.tv_disease_introduce.setText(mList.get(position).getDescript());
		
		return convertView;
	}
class ViewHolder{
	TextView tv_disease_name,tv_disease_introduce;
}
}
