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

public class SearchdisAdapter extends BaseAdapter {  
	private Context mContext;
	private List<Disease> mList;

	public SearchdisAdapter(Context mContext,List<Disease> list){
		this.mContext = mContext;
		this.mList = list;
		
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvCatalog = (TextView)convertView.findViewById(R.id.contactitem_catalog);
			viewHolder.tvNick = (TextView)convertView.findViewById(R.id.contactitem_nick);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.tvCatalog.setVisibility(viewHolder.tvCatalog.GONE);
		viewHolder.tvNick.setText(mList.get(position).getName());

		return convertView;
	}
	
	static class ViewHolder{
		TextView tvCatalog;
		TextView tvNick;
	}

}