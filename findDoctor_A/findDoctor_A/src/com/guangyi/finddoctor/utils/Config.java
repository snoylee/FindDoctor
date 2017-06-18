package com.guangyi.finddoctor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 系统级静态常 可过guangyi.properties初始,同时保持常量static & final.
 * 
 * @author zhaoch
 * 
 */
public class Config {

	private static final String FILE_NAME = "guangyi.properties";
	protected static Properties p = new Properties();

    /**
     *  从constants.properties中读取constant.message_bundle_key的，如果配置文件不存在或配置文件中不存在时，默认取"messages"
     */
    public final static int DEFAULT_PAGE_SIZE = Integer.parseInt(getProperty("constant.default_page_size", String.valueOf(25)));
    
    
    //静初始化读入constants.properties中的设置
    static {
    	init(FILE_NAME);
    }
    
	/**
	 *  静态读入属性文件到Properties p变量中
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
	 * 封装了Properties类的getProperty函数,使p变量对子类透明.
	 * 
	 * @param key
	 *            property key.
	 * @param defaultValue
	 *            当使用property key在properties中取不到值时的默认值.
	 */
	public static String getProperty(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

}
