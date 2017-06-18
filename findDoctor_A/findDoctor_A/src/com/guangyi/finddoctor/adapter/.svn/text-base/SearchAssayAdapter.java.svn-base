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
import com.guangyi.finddoctor.model.Assay;
import com.guangyi.finddoctor.model.Disease;

public class SearchAssayAdapter extends BaseAdapter{


	private Context mContext;
	private List<Assay> mList;
	public  SearchAssayAdapter(Context context,List<Assay> list)
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_search_disease, null);
			viewHolder=new ViewHolder();
			viewHolder.tv_search_diseasename=(TextView) convertView.findViewById(R.id.tv_search_diseasename);
			viewHolder.tv_search_diseaseintroduce=(TextView) convertView.findViewById(R.id.tv_search_diseaseintroduce);
	
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_search_diseasename.setText(Html.fromHtml(mList.get(position).getName()+""));
		viewHolder.tv_search_diseaseintroduce.setText(Html.fromHtml(mList.get(position).getIntroduce()+""));
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView tv_search_diseasename,tv_search_diseaseintroduce;
	}





}
