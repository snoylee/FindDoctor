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
import com.guangyi.finddoctor.model.Drugstore;

public class SelfServiceCircleDrugstoreListAdapter extends BaseAdapter{

	private Context mContext;
	private List<Drugstore> mList;
	public  SelfServiceCircleDrugstoreListAdapter(Context context,List<Drugstore> list)
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.self_service_circum_drugstore_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tv_hospital=(TextView) convertView.findViewById(R.id.tv_hospital);
			viewHolder.tv_distince=(TextView) convertView.findViewById(R.id.tv_distince);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.tv_hospital.setTextColor(mContext.getResources()
				.getColor(R.color.common_grey));
		viewHolder.tv_distince.setTextColor(mContext.getResources()
				.getColor(R.color.common_grey));
		
		viewHolder.tv_hospital.setText(mList.get(position).getName()+"");
		Float distance=Float.parseFloat(mList.get(position).getDistance());
		DecimalFormat fnum = new DecimalFormat("##0.00"); 
		if(distance>1000)
		{
			viewHolder.tv_distince.setText(fnum.format(distance/1000)+"วงรื");
			
		}
		else
		{
			viewHolder.tv_distince.setText(mList.get(position).getDistance()+"รื");
		}
		
		
		return convertView;
		
		
	
}
	class ViewHolder
	{
		TextView tv_hospital,tv_class,tv_distince;
	}

}
