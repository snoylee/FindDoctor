package com.guangyi.forDoctor.adapter;

import java.io.Serializable;
import java.util.List;

import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.model.Introduce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


public class IntroduceListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Introduce> mList;


	public IntroduceListAdapter(Context context, List<Introduce> list) {
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
		ViewHolder viewHolder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.introduce_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tv_introduce);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.iv_introduce);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(mList.get(position).getName());
		viewHolder.imageView.setBackgroundResource(mList.get(position).getImgId());
		return convertView;
	}

	class ViewHolder {
		TextView name;
		ImageView imageView;
	}

}
