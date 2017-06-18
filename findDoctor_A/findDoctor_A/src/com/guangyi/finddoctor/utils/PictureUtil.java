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
	 * ��bitmapת����String
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
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// ��ʾ16λλͼ 565�����Ӧ��ԭɫռ��λ��
		opt.inSampleSize = 3;// ���ű��� 2��ָ����
		opt.inInputShareable = true;
		opt.inPurgeable = true;// ����ͼƬ���Ա�����
		InputStream _is = null;
			String filePath = SDCARD_PIC_PATH+"/find_doctor/pic_cache/"+fileName;
			File file=new File(filePath);
			if(file.exists())
			{
			_is=new FileInputStream(new File(filePath));
			bitmap = BitmapFactory.decodeStream(_is, null, opt);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// ������Ҫ�Ĵ�С
			int newWidth = 90;
			int newHeight = 45;
			// �������ű���
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// ȡ����Ҫ���ŵ�matrix����
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// �õ��µ�ͼƬ
			 bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
			return bitmap;
			}
			else 
			{
				return null;
			}
	}

	
	
	/**
	 * ��bitmapת����String
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
	 * ����·�����ͻ�Ʋ�ѹ������bitmap������ʾ
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		// 1.��ȡԭʼͼƬ�ĳ��Ϳ�
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		// ���Ų�ѹ��ͼƬ
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	
	
	
	public static Bitmap readBitmapSmallByPath(String _path) throws IOException {
		Bitmap bitmap=null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// ��ʾ16λλͼ 565�����Ӧ��ԭɫռ��λ��
		opt.inSampleSize = 3;// ���ű��� 2��ָ����
		opt.inInputShareable = true;
		opt.inPurgeable = true;// ����ͼƬ���Ա�����
		InputStream _is = null;

		try {
			_is = new FileInputStream(_path);
			bitmap = BitmapFactory.decodeStream(_is, null, opt);
			
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// ������Ҫ�Ĵ�С
			int newWidth = 90;
			int newHeight = 45;
			// �������ű���
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// ȡ����Ҫ���ŵ�matrix����
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// �õ��µ�ͼƬ
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
	 * ����ͼƬ������ֵ
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// ����ѹ������
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
	 * ����·��ɾ��ͼƬ
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
	 * ��ӵ�ͼ��
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}




	// ��һ������ѹ����
	public static  Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			options -= 10;// ÿ�ζ�����10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
		return bitmap;
	}

	// �ڶ��֣�ͼƬ��������Сѹ������������·����ȡͼƬ��ѹ������
	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// ��ʱ����bmΪ��

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// ���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
		float hh = 800f;// �������ø߶�Ϊ800f
		float ww = 480f;// �������ÿ��Ϊ480f
		// ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
		int be = 1;// be=1��ʾ������
		if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// �������ű���
		// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// ѹ���ñ�����С���ٽ�������ѹ��
	}

	// ������:ͼƬ��������Сѹ������������BitmapͼƬѹ����
	public static Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// �ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// ����ѹ��50%����ѹ��������ݴ�ŵ�baos��
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// ���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
		float hh = 800f;// �������ø߶�Ϊ800f
		float ww = 480f;// �������ÿ��Ϊ480f
		// ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
		int be = 1;// be=1��ʾ������
		if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// �������ű���
		newOpts.inPreferredConfig = Config.RGB_565;// ����ͼƬ��ARGB888��RGB565
		// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// ѹ���ñ�����С���ٽ�������ѹ��
	}
	
	
	
	
	public static Bitmap readBitmapSmall(String _path) throws IOException {
		Bitmap bitmap=null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// ��ʾ16λλͼ 565�����Ӧ��ԭɫռ��λ��
		opt.inSampleSize = 8;// ���ű��� 2��ָ����
		opt.inInputShareable = true;
		opt.inPurgeable = true;// ����ͼƬ���Ա�����
		InputStream _is = null;

		try {
			_is = new FileInputStream(_path);
			bitmap = BitmapFactory.decodeStream(_is, null, opt);
			
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// ������Ҫ�Ĵ�С
			int newWidth = 90;
			int newHeight = 45;
			// �������ű���
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// ȡ����Ҫ���ŵ�matrix����
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// �õ��µ�ͼƬ
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
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// ��ʾ16λλͼ 565�����Ӧ��ԭɫռ��λ��
		opt.inSampleSize = 3;// ���ű��� 2��ָ����
		opt.inInputShareable = true;
		opt.inPurgeable = true;// ����ͼƬ���Ա�����
		InputStream _is = null;
		Bitmap mBitmap;
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// ������Ҫ�Ĵ�С
		int newWidth = 90;
		int newHeight = 43;
		// �������ű���
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// �õ��µ�ͼƬ
		 mBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return mBitmap;

	}
	
	
	public static Bitmap readBitmapSize(Bitmap bitmap,float newWidth,float newHeight)  {
//		Bitmap bitmap=null;
//		BitmapFactory.Options opt = new BitmapFactory.Options();
//		opt.inPreferredConfig = Bitmap.Config.RGB_565;// ��ʾ16λλͼ 565�����Ӧ��ԭɫռ��λ��
////		opt.inSampleSize = 3;// ���ű��� 2��ָ����
//		opt.inInputShareable = true;
//		opt.inPurgeable = true;// ����ͼƬ���Ա�����
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// ������Ҫ�Ĵ�С
//		int newWidth = 90;
//		int newHeight = 43;
		// �������ű���
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// �õ��µ�ͼƬ
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return bitmap;

	}
	
	public static Bitmap readBitmap(InputStream _InputStream) throws IOException {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// ��ʾ16λλͼ 565�����Ӧ��ԭɫռ��λ��
		opt.inSampleSize = 3;// ���ű��� 2��ָ����
		opt.inInputShareable = true;
		opt.inPurgeable = true;// ����ͼƬ���Ա�����
		return BitmapFactory.decodeStream(_InputStream, null, opt);
	}

	
	

}