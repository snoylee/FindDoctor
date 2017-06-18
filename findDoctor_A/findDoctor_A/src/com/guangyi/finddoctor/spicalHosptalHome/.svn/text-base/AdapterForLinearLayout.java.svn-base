package com.guangyi.finddoctor.spicalHosptalHome;

import java.util.List;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.Department;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterForLinearLayout extends BaseAdapter {
	private Context mContext;
	private List<Department> mList;

	public AdapterForLinearLayout(Context context, List<Department> list) {
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
//		ViewHolder holder;
//		if (convertView == NULL) {
//			HOLDER = NEW VIEWHOLDER();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_hosphome_dept, null);
			TextView tv_hosphme_deptname = (TextView) convertView
					.findViewById(R.id.tv_hosphme_deptname);
			TextView tv_hosphome_doctnum = (TextView) convertView
					.findViewById(R.id.tv_hosphome_doctnum);
			ImageView iv_tag=(ImageView) convertView.findViewById(R.id.iv_tag);
			
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, position+"", Toast.LENGTH_SHORT).show();
					
				}
			});
		tv_hosphme_deptname.setText(mList.get(position)
				.getDepaName());
		tv_hosphome_doctnum.setText("Ò½Éú:"+mList.get(position).getDoctNum()
				+ "ÈË");
		if(mList.get(position).getGuanghaoStatus()>0)
		{
			iv_tag.setVisibility(View.VISIBLE);
		}
//		btn_hosphome_register
//				.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent(
//								SelfServiceHospitalHome.this,
//								DoctorListActivity.class);
//						intent.putExtra(Department.HDEPTID, mList.get(position).getHdeptId());
//						startActivity(intent);
//
//					}
//				});

		return convertView;
	}

//		class ViewHolder {
//		TextView tv_hosphme_deptname;
//		TextView tv_hosphome_doctnum;
//		ImageView iv_tag;
//	}
}
