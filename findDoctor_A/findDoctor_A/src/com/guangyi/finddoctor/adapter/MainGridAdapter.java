package com.guangyi.finddoctor.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.R;

//主页gridView的适配器  
public class MainGridAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int[] mItemId;
	private String[] mTextItem;
	public MainGridAdapter(Context context,int[] itemId,String[] textItem)
	{
		this.mContext=context;
		this.mItemId=itemId;
		this.mTextItem=textItem;

		
		
	}
	
	
	//静态viewholder类  提高gridview加载数据的效率
	private static class ViewHolder
	{
		ImageView imageView;
		TextView textView;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemId.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItemId[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder _ViewHolder;
		if(convertView==null)
		{
			mLayoutInflater=LayoutInflater.from(mContext);
			_ViewHolder=new ViewHolder();
			convertView=mLayoutInflater.inflate(R.layout.main_grid_item, null);
			_ViewHolder.imageView=(ImageView) convertView.findViewById(R.id.iv_grid_item);
			_ViewHolder.textView=(TextView) convertView.findViewById(R.id.tv_grid_item);
			convertView.setTag(_ViewHolder);
			
		}
		else {
			 _ViewHolder=(ViewHolder) convertView.getTag();
		}
		_ViewHolder.imageView.setImageResource(mItemId[position]);
		_ViewHolder.textView.setText(mTextItem[position].toString());
		return convertView;
	}

}
