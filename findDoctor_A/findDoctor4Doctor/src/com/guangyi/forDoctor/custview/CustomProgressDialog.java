package com.guangyi.forDoctor.custview;

import com.guangyi.forDoctor.activity.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class CustomProgressDialog extends ProgressDialog {

	public CustomProgressDialog(Context context,int theme) {
		super(context,theme);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress);
	}

}
