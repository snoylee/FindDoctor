package com.guangyi.finddoctor.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.Hospital;
public class SelfServiceCircleHospitalListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Hospital> mList;

	public SelfServiceCircleHospitalListAdapter(Context context,
			List<Hospital> list) {
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
					R.layout.self_service_circum_hospital_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_hospital = (TextView) convertView
					.findViewById(R.id.tv_hospital);
			viewHolder.tv_class = (TextView) convertView
					.findViewById(R.id.tv_class);
			viewHolder.tv_distince = (TextView) convertView
					.findViewById(R.id.tv_distince);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (mList.get(position).getMapType() == 0) {
			viewHolder.tv_hospital.setTextColor(mContext.getResources()
					.getColor(R.color.common_grey));
			viewHolder.tv_class.setTextColor(mContext.getResources().getColor(
					R.color.common_grey));
			viewHolder.tv_distince.setTextColor(mContext.getResources()
					.getColor(R.color.common_grey));
		}

		viewHolder.tv_hospital.setText(mList.get(position).getHospName() + "");
//		if (mList.get(position).getDistance().length() > 0) {
//			
//			viewHolder.tv_distince.setText(mList.get(position).getDistance()
//					+ "米");
//		}
		
		Float distance=Float.parseFloat(mList.get(position).getDistance());
		DecimalFormat fnum = new DecimalFormat("##0.00"); 
		if(distance>1000)
		{
			viewHolder.tv_distince.setText(fnum.format(distance/1000)+"千米");
			
		}
		else
		{
			viewHolder.tv_distince.setText(mList.get(position).getDistance()+"米");
		}
		

//		if (mList.get(position).getHospClass() == 11) {
//			viewHolder.tv_class.setText("一级丙等");
//		}
//		if (mList.get(position).getHospClass() == 12) {
//			viewHolder.tv_class.setText("一级乙等");
//		}
//		if (mList.get(position).getHospClass() == 13) {
//			viewHolder.tv_class.setText("一级甲等");
//		}
//
//		if (mList.get(position).getHospClass() == 21) {
//			viewHolder.tv_class.setText("二级丙等");
//		}
//		if (mList.get(position).getHospClass() == 22) {
//			viewHolder.tv_class.setText("二级乙等");
//		}
//		if (mList.get(position).getHospClass() == 23) {
//			viewHolder.tv_class.setText("二级甲等");
//		}
//
//		if (mList.get(position).getHospClass() == 31) {
//			viewHolder.tv_class.setText("三级丙等");
//		}
//		if (mList.get(position).getHospClass() == 32) {
//			viewHolder.tv_class.setText("三级乙等");
//		}
//		if (mList.get(position).getHospClass() == 33) {
//			viewHolder.tv_class.setText("三级甲等");
//		}
		return convertView;

	}

	class ViewHolder {
		TextView tv_hospital, tv_class, tv_distince;
	}

}
