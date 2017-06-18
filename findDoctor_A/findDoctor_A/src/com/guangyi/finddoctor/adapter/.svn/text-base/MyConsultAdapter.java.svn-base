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
import com.guangyi.finddoctor.custview.BadgeView;
import com.guangyi.finddoctor.model.MyConsultModel;
public class MyConsultAdapter extends BaseAdapter {
	private Context mContext;
	private List<MyConsultModel> mList;
	public MyConsultAdapter(Context context, List<MyConsultModel> list){
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
//		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.consult_list_item, null);
//			holder = new ViewHolder();
			TextView tv_consult_question = (TextView) convertView.findViewById(R.id.tv_consult_question);
			TextView tv_consult_date = (TextView) convertView.findViewById(R.id.tv_consult_date);
			ImageView iv_consult_state = (ImageView) convertView.findViewById(R.id.iv_consult_state);
			TextView tv_state=(TextView) convertView.findViewById(R.id.tv_state);
//			convertView.setTag(holder);
//		}else{
//			holder = (ViewHolder) convertView.getTag();
//		}
		
		if(mList.get(position).getIsDoctorState()==1)
		{
			tv_state.setText("指定医生");
		}
		else if(mList.get(position).getIsDoctorState()==0)
		{
			tv_state.setText("不指定医生");
		}
		tv_consult_question.setText(mList.get(position).getConsProblem());
//		BadgeView badgeView=new  BadgeView(mContext,tv_consult_question);
//		badgeView.setText("0");
//		badgeView.show();
		
		if(mList.get(position).getReadCount()>0)
		{
			BadgeView badgeView=new  BadgeView(mContext,tv_consult_question);
			badgeView.setText(mList.get(position).getReadCount()+"");
		 badgeView.show();
		}
		
		
		String time = mList.get(position).getConsTime();
		if(time.length()>0)
		{
         tv_consult_date.setText(mList.get(position).getConsTime().substring(0, time.length()-2));
		}
		else
		{
			tv_consult_date.setText("");
		}
		int state = mList.get(position).getConsState();
		if(state == 0){
			iv_consult_state.setImageResource(R.drawable.no_answer);
		}else if(state == 1){
			iv_consult_state.setImageResource(R.drawable.answered);

		}else if(state == 2){
			iv_consult_state.setImageResource(R.drawable.assessed);
		}
		
		
		return convertView;
	}
	
//	class ViewHolder{
//		TextView tv_consult_question,tv_consult_date,tv_state;
//		ImageView iv_consult_state;
//	}

}
