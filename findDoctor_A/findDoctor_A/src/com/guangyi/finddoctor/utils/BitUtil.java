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
	 * ����ת�����ֽ�����,Ҫ����Ķ������ʵ�����кŽӿ�.
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
//        	System.out.println("�����㴫��Ķ����Ƿ�̳���Serializable�ӿ�");
//            System.out.println("translation"+e.getMessage());
            e.printStackTrace();
        } 
        return bytes;
    }
	
	/**
	 * �ֽ�����ת���ɶ���
	 * @param bytes 
	 * @return Object ȡ�ý����ǿ��ת���������Ķ�������
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

