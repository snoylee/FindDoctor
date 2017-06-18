package com.guangyi.finddoctor.adapter;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.TimeList;

//主页gridView的适配器  
public class OnLineDoctorTimeGridAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<TimeList> mList;

	public OnLineDoctorTimeGridAdapter(Context context, List<TimeList> list) {
		this.mContext = context;
		this.mList = list;

	}

	// 静态viewholder类 提高gridview加载数据的效率
	private static class ViewHolder {
		Button button;
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
		final ViewHolder _ViewHolder;
		if (convertView == null) {
			mLayoutInflater = LayoutInflater.from(mContext);
			_ViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(
					R.layout.doctor_time_list_grid_item, null);
			_ViewHolder.button = (Button) convertView
					.findViewById(R.id.btn_grid_item);
			convertView.setTag(_ViewHolder);

		} else {
			_ViewHolder = (ViewHolder) convertView.getTag();
		}
//		_ViewHolder.button.setHeight(80);
		_ViewHolder.button.setHint("0");
		_ViewHolder.button.setText(mList.get(position).getTimeList());
		_ViewHolder.button.setClickable(false);
		_ViewHolder.button
				.setBackgroundResource(R.drawable.time_list_button_false);

		if (mList.get(position).getBtnState() == 1) {
			_ViewHolder.button.setClickable(true);
			_ViewHolder.button
					.setBackgroundResource(R.drawable.time_list_button);
			_ViewHolder.button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (_ViewHolder.button.getHint().equals("1")) {
						_ViewHolder.button.setHint("0");
						_ViewHolder.button
								.setBackgroundResource(R.drawable.time_list_button);
					} else if (_ViewHolder.button.getHint().equals("0")) {
						_ViewHolder.button.setHint("1");
						_ViewHolder.button
								.setBackgroundResource(R.drawable.time_list_button_select);
					}
				}
			});

		}
		// else if(mList.get(position).getBtnState() == 0) {
		// _ViewHolder.textView.setEnabled(false);
		// _ViewHolder.textView.setClickable(false);
		// _ViewHolder.textView.setBackgroundResource(R.drawable.time_list_button_false);
		// }

		return convertView;
	}

}
