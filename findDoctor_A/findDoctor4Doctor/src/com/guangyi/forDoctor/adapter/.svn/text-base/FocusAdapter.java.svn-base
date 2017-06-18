package com.guangyi.forDoctor.adapter;

import java.util.List;

import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.imageManager.ImageManager2;
import com.guangyi.forDoctor.model.Banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class FocusAdapter extends BaseAdapter {

	private Context context;
	private List<Banner> list;

//	 private int[] imgId;
	public FocusAdapter(Context context, List<Banner> list) {
		this.context = context;
		this.list = list;
//		 imgId=new int[]{R.drawable.focus_1,R.drawable.focus_2,R.drawable.focus_3,R.drawable.focus_4};
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Banner getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addObject(Banner banner) {
		list.add(banner);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.image_list, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.imageList_img);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(position==0)
		{
		ImageManager2.from(context).displayImage(viewHolder.image,
				list.get(position).getPicture(), R.drawable.focus_1);
		}
		else
			if(position==1)
			{
				ImageManager2.from(context).displayImage(viewHolder.image,
						list.get(position).getPicture(), R.drawable.focus_2);
			}
			else
			{
				ImageManager2.from(context).displayImage(viewHolder.image,
					list.get(position).getPicture(), R.drawable.focus_3);
				
			}

		return convertView;
	}

	class ViewHolder {
		ImageView image;
	}

}
