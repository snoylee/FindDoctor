package com.guangyi.finddoctor.adapter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.OnLineGridItem;

//主页gridView的适配器  
public class OnLineMainGridAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<OnLineGridItem> mList;
	public OnLineMainGridAdapter(Context context,List<OnLineGridItem> list)
	{
		this.mContext=context;
		this.mList=list;
		
		
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
		final ViewHolder _ViewHolder;
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
		_ViewHolder.imageView.setImageResource(mList.get(position).getImageId());
//		_ViewHolder.imageView.setOnKeyListener(new OnKeyListener() {
//			
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if(event.getAction()==KeyEvent.ACTION_DOWN)
//				{
//					_ViewHolder.imageView.setImageResource(R.drawable.icon);
//				}
//				else if(event.getAction()==KeyEvent.ACTION_UP)
//				{
//					_ViewHolder.imageView.setImageResource(mList.get(position).getImageId());
//				}
//				return false;
//			}
//		});
		_ViewHolder.textView.setText(mList.get(position).getTextStr());
		return convertView;
	}

}
