package com.guangyi.forDoctor.http;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import android.net.Uri;
import android.util.Log;

public class ApiHttpUtil {
	private static int CONNTIME = 15000;
	private static int SOCONNTIME = 20000;
	public static String CONNTIMEOUT = "1";
	public static String SOCONNTIMEOUT = "2";
	public static String getMethod(String apiUrl, List<String> parms) {
		String jsonString = "";
		HttpClient httpClient = new HttpClient();
		try {
			String jsonParams = getParams(parms);
			apiUrl = apiUrl + jsonParams;
			GetMethod httpGet = new GetMethod(apiUrl);
			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(CONNTIME);
			httpClient.getHttpConnectionManager().getParams()
					.setSoTimeout(SOCONNTIME);
			int statusCode = httpClient.executeMethod(httpGet);
			if (statusCode == HttpStatus.SC_OK) {
				jsonString = httpGet.getResponseBodyAsString();
				if (jsonString != null) {
					return jsonString;
				}
			}
		} catch (ConnectTimeoutException e) {
			jsonString = CONNTIMEOUT;
			return jsonString;
		} 
		catch (SocketTimeoutException e)
		{
			jsonString = SOCONNTIMEOUT;
			return jsonString;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	public String getMethod(String apiUrl, String params) {
		String jsonString = "";
		HttpClient httpClient = new HttpClient();

		try {
			GetMethod httpPost = new GetMethod(apiUrl + params);
			int statusCode = httpClient.executeMethod(httpPost);
			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(CONNTIME);
			httpClient.getHttpConnectionManager().getParams()
					.setSoTimeout(SOCONNTIME);
			if (statusCode == HttpStatus.SC_OK) {
				jsonString = httpPost.getResponseBodyAsString();
				if (jsonString != null) {
					return jsonString;
				}
			}
		} catch (ConnectTimeoutException e) {
			jsonString = CONNTIMEOUT;
			return jsonString;
		}
		catch (SocketTimeoutException e)
		{
			jsonString = SOCONNTIMEOUT;
			return jsonString;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}

	public String postMethod(String apiUrl, Map parms) {
		String jsonString = "";
		HttpClient httpClient = new HttpClient();
		PostMethod httpPost = new PostMethod(apiUrl);
		// httpPost.setRequestHeader("Content","text/html,charset=utf-8");
		String jsonParams;
		try {
			jsonParams = postParams(parms);
			httpPost.setRequestBody(jsonParams);
			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(CONNTIME);
			httpClient.getHttpConnectionManager().getParams()
					.setSoTimeout(SOCONNTIME);
			int statusCode = httpClient.executeMethod(httpPost);
			jsonString = httpPost.getResponseBodyAsString();
			if (jsonString != null) {
				return jsonString;
			}
		} 
		catch (ConnectTimeoutException e) {
			jsonString = CONNTIMEOUT;
			return jsonString;
		}
		catch (SocketTimeoutException e) {
			jsonString = SOCONNTIMEOUT;
			return jsonString;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	public String postFileMethod(String apiUrl, Map parms) {
		String jsonString = "";
		HttpClient httpClient = new HttpClient();
		try {
			PostMethod httpPost = new PostMethod(apiUrl);
			String jsonParams = postParams(parms);
			httpPost.setRequestBody(jsonParams);
			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(CONNTIME);
			httpClient.getHttpConnectionManager().getParams()
					.setSoTimeout(SOCONNTIME);
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode == HttpStatus.SC_OK) {
				jsonString = httpPost.getResponseBodyAsString();
				if (jsonString != null) {
					return jsonString;
				}
			}
		} catch (ConnectTimeoutException e) {
			jsonString = CONNTIMEOUT;
			return jsonString;
		}
		catch (SocketTimeoutException e) {
			jsonString = SOCONNTIMEOUT;
			return jsonString;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	public static String post(String actionUrl, Map<String, String> params,
			String fileName, File file) {
		String jsonString = "";
		try {
			String enterNewline = "\r\n";
			String fix = "--";
			String boundary = "######";
			String MULTIPART_FORM_DATA = "multipart/form-data";
			byte[] mbyte;

			URL url = new URL(actionUrl);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(CONNTIME);
			con.setReadTimeout(SOCONNTIME);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty(
					"Accept",
					"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
					+ ";boundary=" + boundary);

			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			Set<String> keySet = params.keySet();
			Iterator<String> it = keySet.iterator();

			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key);
				Log.i("key", key);
				Log.i("value", value);
				ds.writeBytes(fix + boundary + enterNewline);
				ds.writeBytes("Content-Disposition: form-data; " + "name=\""
						+ key + "\"" + enterNewline);
				ds.writeBytes(enterNewline);
				ds.write(value.getBytes("UTF-8"));
				// ds.writeBytes(value);//如果有中文乱码，保存改用上面的ds.writeBytes(enterNewline);那句代码
				ds.writeBytes(enterNewline);
			}

			if (file != null && file.length() > 0) {
				mbyte = getByte(file);
				ds.writeBytes(fix + boundary + enterNewline);
				ds.writeBytes("Content-Disposition: form-data; " + "name=\""
						+ "file" + "\"" + "; filename=\"" + fileName + "\""
						+ enterNewline);
				ds.writeBytes(enterNewline);
				ds.write(mbyte);
				ds.writeBytes(enterNewline);

			}

			ds.writeBytes(fix + boundary + fix + enterNewline);
			ds.flush();

			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();

			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			ds.close();
			jsonString = b.toString().trim();

			return jsonString;
		}
		catch(ConnectTimeoutException e)
		{
			jsonString=CONNTIMEOUT;
			return jsonString;
			
		}
		catch(SocketTimeoutException e)
		{
			jsonString=SOCONNTIMEOUT;
			return jsonString;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String postParams(Map params) throws UnsupportedEncodingException {
		String jsonString = "";
		Set key = params.entrySet();
		Set<Map.Entry<String, String>> set = params.entrySet();
		String mapJson = "{";
		for (Iterator<Map.Entry<String, String>> it = set.iterator(); it
				.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it
					.next();
			mapJson = mapJson + entry.getKey() + ":" + "\""
					+ Uri.encode(entry.getValue()) + "\"";
			// mapJson=mapJson+entry.getKey() + ":" +
			// "\""+entry.getValue(),"UTF-8")+"\"";
			if (it.hasNext()) {
				mapJson += ",";
			}
		}
		mapJson = mapJson + "}";
		System.out.println(mapJson);
		return mapJson;
	}

	private static byte[] getByte(File file) throws Exception {
		byte[] bytes = null;
		if (file != null) {
			InputStream is = new FileInputStream(file);
			int length = (int) file.length();
			if (length > Integer.MAX_VALUE) // 当文件的长度超过了int的最大值
			{
				System.out.println("this file is max ");
				return null;
			}
			bytes = new byte[length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			// 如果得到的字节长度和file实际的长度不一致就可能出错了
			if (offset < bytes.length) {
				System.out.println("file length is error");
				return null;
			}
			is.close();
		}
		return bytes;
	}

	private static String getParams(List<String> params) {

		String urlString = "";
		for (String p : params) {
			urlString = urlString + "/" + p;
		}
		return urlString;
	}

}
