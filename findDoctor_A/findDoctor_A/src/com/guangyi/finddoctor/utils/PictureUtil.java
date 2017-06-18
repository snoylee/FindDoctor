package com.guangyi.finddoctor.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

public class PictureUtil {
	public static String SDCARD_PIC_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
	public static String PIC_CACHE_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/find_doctor/pic_cache/";
	
	/**
	 * 把bitmap转换成String
	 * 
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(String filePath) {
		Bitmap bm = getSmallBitmap(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}
	
	public static String saveMyBitmap(Bitmap bitmap,String fileName) throws IOException {
		String filePath = SDCARD_PIC_PATH+"/find_doctor/pic_cache/";
		File f=new File(filePath);
        if(!f.exists())
        {
          f.mkdirs();
          
          f.createNewFile();
        }
        File bit = new File(filePath+fileName);
        if(!bit.exists())
        {
        	bit.createNewFile();
        }
        
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(bit);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
                fOut.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
        try {
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return filePath+fileName;
}
	
	
	public static Bitmap getMyBitmap(String fileName) throws IOException {
		Bitmap bitmap=null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 3;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		InputStream _is = null;
			String filePath = SDCARD_PIC_PATH+"/find_doctor/pic_cache/"+fileName;
			File file=new File(filePath);
			if(file.exists())
			{
			_is=new FileInputStream(new File(filePath));
			bitmap = BitmapFactory.decodeStream(_is, null, opt);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 设置想要的大小
			int newWidth = 90;
			int newHeight = 45;
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			 bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
			return bitmap;
			}
			else 
			{
				return null;
			}
	}

	
	
	/**
	 * 把bitmap转换成String
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap file) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		file.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	
	
	public static String saveMyBitmap(Bitmap bitmap) throws IOException {
		String filePath = SDCARD_PIC_PATH+"/find_doctor/pre_picture/";
		String fileName = "imgview.png";
		File f=new File(filePath);
        if(!f.exists())
        {
          f.mkdirs();
          
          f.createNewFile();
        }
        File bit = new File(filePath+fileName);
        if(!bit.exists())
        {
        	bit.createNewFile();
        }
        
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(bit);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
                fOut.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
        try {
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return filePath+fileName;
}
	
	public static Bitmap getSmallPic(String filePath) 
	{ 
        final BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(filePath, options); 

        // Calculate inSampleSize  
        options.inSampleSize = calculateInSampleSize(options, 480, 800); 

        // Decode bitmap with inSampleSize set  
        options.inJustDecodeBounds = false; 

        Bitmap bm = BitmapFactory.decodeFile(filePath, options); 
        if(bm == null){ 
            return  null; 
        } 
        int degree = readPictureDegree(filePath); 
        bm = rotateBitmap(bm,degree) ; 
        ByteArrayOutputStream baos = null ; 
        try{ 
            baos = new ByteArrayOutputStream(); 
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos); 
        }finally{ 
            try { 
                if(baos != null) 
                    baos.close() ; 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
        return bm; 
    }
	
	
	private static int readPictureDegree(String path) { 
        int degree  = 0; 
        try { 
                ExifInterface exifInterface = new ExifInterface(path); 
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL); 
                switch (orientation) { 
                case ExifInterface.ORIENTATION_ROTATE_90: 
                        degree = 90; 
                        break; 
                case ExifInterface.ORIENTATION_ROTATE_180: 
                        degree = 180; 
                        break; 
                case ExifInterface.ORIENTATION_ROTATE_270: 
                        degree = 270; 
                        break; 
                } 
        } catch (IOException e) { 
                e.printStackTrace(); 
        } 
        return degree; 
    }
	
	
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate){
		  if(bitmap == null)
		   return null ;

		  int w = bitmap.getWidth();
		  int h = bitmap.getHeight();
		  // Setting post rotate to 90
		  Matrix mtx = new Matrix();
		  mtx.postRotate(rotate);
		  return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
		 }
	
	
	

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		// 1.获取原始图片的长和宽
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		// 缩放并压缩图片
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	
	
	
	public static Bitmap readBitmapSmallByPath(String _path) throws IOException {
		Bitmap bitmap=null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 3;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		InputStream _is = null;

		try {
			_is = new FileInputStream(_path);
			bitmap = BitmapFactory.decodeStream(_is, null, opt);
			
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 设置想要的大小
			int newWidth = 90;
			int newHeight = 45;
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			 bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			_is.close();
		}
		return bitmap;
	}
	
	
	
	
	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 计算压缩比例
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	

	/**
	 * 根据路径删除图片
	 * 
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
	/**
	 * 添加到图库
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}




	// 第一：质量压缩法
	public static  Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	// 第二种：图片按比例大小压缩方法（根据路径获取图片并压缩）：
	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	// 第三种:图片按比例大小压缩方法（根据Bitmap图片压缩）
	public static Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		newOpts.inPreferredConfig = Config.RGB_565;// 降低图片从ARGB888到RGB565
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}
	
	
	
	
	public static Bitmap readBitmapSmall(String _path) throws IOException {
		Bitmap bitmap=null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 8;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		InputStream _is = null;

		try {
			_is = new FileInputStream(_path);
			bitmap = BitmapFactory.decodeStream(_is, null, opt);
			
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 设置想要的大小
			int newWidth = 90;
			int newHeight = 45;
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			 bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			_is.close();
		}
		return bitmap;

	}
	
	
	public static Bitmap readBitmapSmall(Bitmap bitmap) throws IOException {
//		Bitmap bitmap=null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 3;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		InputStream _is = null;
		Bitmap mBitmap;
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 设置想要的大小
		int newWidth = 90;
		int newHeight = 43;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		 mBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return mBitmap;

	}
	
	
	public static Bitmap readBitmapSize(Bitmap bitmap,float newWidth,float newHeight)  {
//		Bitmap bitmap=null;
//		BitmapFactory.Options opt = new BitmapFactory.Options();
//		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
////		opt.inSampleSize = 3;// 缩放比例 2的指数倍
//		opt.inInputShareable = true;
//		opt.inPurgeable = true;// 设置图片可以被回收
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 设置想要的大小
//		int newWidth = 90;
//		int newHeight = 43;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return bitmap;

	}
	
	public static Bitmap readBitmap(InputStream _InputStream) throws IOException {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inSampleSize = 3;// 缩放比例 2的指数倍
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		return BitmapFactory.decodeStream(_InputStream, null, opt);
	}

	
	

}