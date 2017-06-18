package com.guangyi.finddoctor.spicalHosptalHome;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.guangyi.finddoctor.hospitalRegister.DoctorListActivity;
import com.guangyi.finddoctor.model.Department;

public class MyLinearLayoutForListAdapter extends LinearLayout {
	
	public AdapterForLinearLayout adapter;
	public OnClickListener mOnClickListener = null;
	private Context mContext;
	private List<Department> mList;


	public MyLinearLayoutForListAdapter(Context context) {
		super(context);
	}
	
	public MyLinearLayoutForListAdapter(Context context, AttributeSet attrs) {
	        super(context, attrs);
	}
	
	public void bindLinearLayout(){
		int count = adapter.getCount();
		for( int i=0;i<count;i++){
			final int arg2=i;
			View v = adapter.getDropDownView(arg2, null, null);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mList.get(arg2).getGuanghaoStatus()>0)
						{
							Intent intent = new Intent(mContext,
									DoctorListActivity.class);
							intent.putExtra(Department.HDEPTID, mList.get(arg2).getHdeptId());
							mContext.startActivity(intent);
						}
					
				}
			});
			this.addView(v, i);
		}
	}
	
	public AdapterForLinearLayout getAdapter(){
		return adapter;
	}
	
	public void setAdapter(AdapterForLinearLayout adapter,Context context,List<Department> list){
		this.adapter = adapter;
		this.mContext=context;
		this.mList=list;
		bindLinearLayout();
	}
	
//	public void setOnClickListener(OnClickListener mOnClickListener){
//		this.mOnClickListener = mOnClickListener;
//	}
	
//	public OnClickListener getOnClickListener(){
//		return mOnClickListener;
//	}

}
