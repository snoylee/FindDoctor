package com.guangyi.finddoctor.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class BitUtil {
	
	/**
	 * 对象转换成字节数组,要求传入的对象必须实现序列号接口.
	 * @param obj
	 * @return byte[]
	 */
	public static byte[] ObjectToByte(java.lang.Object obj)
    {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();    
        }
        catch(Exception e) {
//        	System.out.println("请检查你传入的对象是否继承了Serializable接口");
//            System.out.println("translation"+e.getMessage());
            e.printStackTrace();
        } 
        return bytes;
    }
	
	/**
	 * 字节数组转换成对象
	 * @param bytes 
	 * @return Object 取得结果后强制转换成你存入的对象类型
	 */
	public static Object ByteToObject(byte[] bytes){
        java.lang.Object obj = null;
        try {
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bi);

        obj = oi.readObject();

        bi.close();
        oi.close();
        }
        catch(Exception e) {
            System.out.println("translation"+e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
	
	

	
	
}

