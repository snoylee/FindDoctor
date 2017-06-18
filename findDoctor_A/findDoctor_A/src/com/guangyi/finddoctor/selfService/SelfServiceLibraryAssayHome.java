package com.guangyi.finddoctor.selfService;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.model.Assay;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:化验详情
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:中国移动有限公司东莞分公司
 * </p>
 * 
 * @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
 * @version：1.0
 * @since：2013-9-23
 */
public class SelfServiceLibraryAssayHome extends BasicActivity {

	private Drawable mDrawable1, mDrawable2;
	private TextView tv_disease_name, tv_ddisease_detail;
	private ToggleButton toggle_pathogen, toggle_symptom, toggle_treatment,
			toggle_drug;
	private TextView tv_pathogen, tv_symptom, tv_treatment, tv_drug;
	private Assay assay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_service_library_assay_detail);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initDetailView();
		getView();

	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	private void getView() {
		//
		// private TextView tv_disease_name,tv_ddisease_detail;
		// private TextView tv_pathogen,tv_symptom,tv_treatment,tv_drug;
		tv_disease_name.setText(Html.fromHtml(assay.getName() + ""));
		tv_ddisease_detail.setText(Html.fromHtml(assay.getIntroduce() + ""));
		tv_pathogen.setText(Html.fromHtml(assay.getNormalValue()));
		tv_symptom.setText(Html.fromHtml(assay.getSense() + ""));
		tv_treatment.setText(Html.fromHtml(assay.getNotes() + ""));
		tv_drug.setText(Html.fromHtml(assay.getOtherassay() + ""));

	}

	private void initDetailView() {

		tv_ddisease_detail = (TextView) findViewById(R.id.tv_ddisease_detail);
		tv_disease_name = (TextView) findViewById(R.id.tv_disease_name);
		tv_pathogen = (TextView) findViewById(R.id.tv_pathogen);
		tv_symptom = (TextView) findViewById(R.id.tv_symptom);
		tv_treatment = (TextView) findViewById(R.id.tv_treatment);
		tv_drug = (TextView) findViewById(R.id.tv_drug);

		toggle_pathogen = (ToggleButton) findViewById(R.id.toggle_pathogen);
		toggle_symptom = (ToggleButton) findViewById(R.id.toggle_symptom);
		toggle_treatment = (ToggleButton) findViewById(R.id.toggle_treatment);
		toggle_drug = (ToggleButton) findViewById(R.id.toggle_drug);

		toggle_pathogen
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							toggle_pathogen.setBackgroundDrawable(mDrawable1);
							tv_pathogen.setVisibility(View.GONE);
						} else {
							toggle_pathogen.setBackgroundDrawable(mDrawable2);
							tv_pathogen.setVisibility(View.VISIBLE);
						}

					}
				});
		toggle_symptom
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							toggle_symptom.setBackgroundDrawable(mDrawable1);
							tv_symptom.setVisibility(View.GONE);
						} else {
							toggle_symptom.setBackgroundDrawable(mDrawable2);
							tv_symptom.setVisibility(View.VISIBLE);
						}

					}
				});

		toggle_treatment
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							toggle_treatment.setBackgroundDrawable(mDrawable1);
							tv_treatment.setVisibility(View.GONE);
						} else {
							toggle_treatment.setBackgroundDrawable(mDrawable2);
							tv_treatment.setVisibility(View.VISIBLE);
						}

					}
				});
		toggle_drug.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					toggle_drug.setBackgroundDrawable(mDrawable1);
					tv_drug.setVisibility(View.GONE);
				} else {
					toggle_drug.setBackgroundDrawable(mDrawable2);
					tv_drug.setVisibility(View.VISIBLE);
				}

			}
		});

	}

	private void initParams() {
		assay = (Assay) getIntent().getSerializableExtra("assay");
		mDrawable1 = this.getResources().getDrawable(R.drawable.img1);
		mDrawable2 = this.getResources().getDrawable(R.drawable.img2);
		Log.i("disease", assay.getName());
	}

	private void initView() {
		Button mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

}
