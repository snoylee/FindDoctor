package com.guangyi.finddoctor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * ϵͳ����̬�� �ɹ�guangyi.properties��ʼ,ͬʱ���ֳ���static & final.
 * 
 * @author zhaoch
 * 
 */
public class Config {

	private static final String FILE_NAME = "guangyi.properties";
	protected static Properties p = new Properties();

    /**
     *  ��constants.properties�ж�ȡconstant.message_bundle_key�ģ���������ļ������ڻ������ļ��в�����ʱ��Ĭ��ȡ"messages"
     */
    public final static int DEFAULT_PAGE_SIZE = Integer.parseInt(getProperty("constant.default_page_size", String.valueOf(25)));
    
    
    //����ʼ������constants.properties�е�����
    static {
    	init(FILE_NAME);
    }
    
	/**
	 *  ��̬���������ļ���Properties p������
	 */
	protected static void init(String propertyFileName) {
		InputStream in = null;
		try {
			in = Config.class.getClassLoader()
					.getResourceAsStream(propertyFileName);
			if (in != null)
				p.load(in);
		} catch (IOException e) {
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ��װ��Properties���getProperty����,ʹp����������͸��.
	 * 
	 * @param key
	 *            property key.
	 * @param defaultValue
	 *            ��ʹ��property key��properties��ȡ����ֵʱ��Ĭ��ֵ.
	 */
	public static String getProperty(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

}
