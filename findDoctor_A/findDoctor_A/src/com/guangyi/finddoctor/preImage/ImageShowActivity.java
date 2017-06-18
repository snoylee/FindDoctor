/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.guangyi.finddoctor.preImage;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.preImage.PhotoViewAttacher.OnMatrixChangedListener;
import com.guangyi.finddoctor.preImage.PhotoViewAttacher.OnPhotoTapListener;
import com.guangyi.finddoctor.utils.PictureUtil;

public class ImageShowActivity extends Activity {

//	static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %%";
	private ImageView mImageView;
	private TextView mCurrMatrixTv;
	private PhotoViewAttacher mAttacher;
	private Toast mCurrentToast;
	private Button backIbt;
	private Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		mImageView = (ImageView) findViewById(R.id.iv_photo);
		mCurrMatrixTv = (TextView) findViewById(R.id.tv_current_matrix);
		
		String filePath = getIntent().getStringExtra("filePath");
		 bitmap = null;
    	//从文件路径获取文件，一般为本地文件
    	if(null != filePath && filePath.length()>0)
    	{

	    	 bitmap =  PictureUtil.getImage(filePath);
	    	 Drawable bitmapDrable= new BitmapDrawable(bitmap);
	 		 mImageView.setImageDrawable(bitmapDrable);
    	}
    	
    	
		
//		Drawable bitmap = getResources().getDrawable(R.drawable.focus_1);
//		Bitmap bitmap=(Bitmap) getIntent().getExtras().get("bitmap");
		
		// The MAGIC happens here!
		mAttacher = new PhotoViewAttacher(mImageView);
		// Lets attach some listeners, not required though!
		mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
		mAttacher.setOnPhotoTapListener(new PhotoTapListener());
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main_menu, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
	
	
	private void initParams()
	{
		backIbt = (Button) findViewById(R.id.btn_back);
		backIbt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (null != bitmap && !bitmap.isRecycled()){
					 bitmap.recycle();
					 bitmap = null;
					}
		    	System.gc();
				finish();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Need to call clean-up
		mAttacher.cleanup();
	}

//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		MenuItem zoomToggle = menu.findItem(R.id.menu_zoom_toggle);
//		zoomToggle.setTitle(mAttacher.canZoom() ? R.string.menu_zoom_disable : R.string.menu_zoom_enable);
//		return super.onPrepareOptionsMenu(menu);
//	}
	
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case R.id.menu_zoom_toggle:
//				mAttacher.setZoomable(!mAttacher.canZoom());
//				return true;
//
//			case R.id.menu_scale_fit_center:
//				mAttacher.setScaleType(ScaleType.FIT_CENTER);
//				return true;
//
//			case R.id.menu_scale_fit_start:
//				mAttacher.setScaleType(ScaleType.FIT_START);
//				return true;
//
//			case R.id.menu_scale_fit_end:
//				mAttacher.setScaleType(ScaleType.FIT_END);
//				return true;
//
//			case R.id.menu_scale_fit_xy:
//				mAttacher.setScaleType(ScaleType.FIT_XY);
//				return true;
//
//			case R.id.menu_scale_scale_center:
//				mAttacher.setScaleType(ScaleType.CENTER);
//				return true;
//
//			case R.id.menu_scale_scale_center_crop:
//				mAttacher.setScaleType(ScaleType.CENTER_CROP);
//				return true;
//
//			case R.id.menu_scale_scale_center_inside:
//				mAttacher.setScaleType(ScaleType.CENTER_INSIDE);
//				return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	private class PhotoTapListener implements OnPhotoTapListener {
		@Override
		public void onPhotoTap(View view, float x, float y) {
			float xPercentage = x * 100f;
			float yPercentage = y * 100f;

//			if (null != mCurrentToast) {
//				mCurrentToast.cancel();
//			}
//
//			mCurrentToast = Toast.makeText(SimpleSampleActivity.this,
//					String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage), Toast.LENGTH_SHORT);
//			mCurrentToast.show();
		}
	}

	private class MatrixChangeListener implements OnMatrixChangedListener {

		@Override
		public void onMatrixChanged(RectF rect) {
			mCurrMatrixTv.setText(rect.toString());
		}
	}

}
