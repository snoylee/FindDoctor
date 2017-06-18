package com.guangyi.finddoctor.adapter;

import java.util.List;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.CommonPatient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommonPatientAdapter extends BaseAdapter {
	private Context mContext;
	private List<CommonPatient> mList;

	public CommonPatientAdapter(Context context, List<CommonPatient> list) {
		this.mContext = context;
		this.mList = list;
	}

	public void addItem(CommonPatient item) {
		mList.add(item);
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.common_patient_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tv_patient_name);
			viewHolder.age = (TextView) convertView
					.findViewById(R.id.tv_patient_age);
			viewHolder.sex = (TextView) convertView
					.findViewById(R.id.tv_patient_sex);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(mList.get(position).getName());
		viewHolder.age.setText(String.valueOf(mList.get(position).getAge()));
		int i = mList.get(position).getSex();
		if (i == 1) {
			viewHolder.sex.setText("ÄÐ");
		} else if (i == 2) {
			viewHolder.sex.setText("Å®");
		} else if (i == 3) {
			viewHolder.sex.setText("Î´Öª");
		}
		return convertView;
	}

	class ViewHolder {
		TextView name, sex, age;
	}

}
