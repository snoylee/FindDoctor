package com.guangyi.finddoctor.custview;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.onlineAsk.OnlineChatActivity;

/***
 * 文件上传工具类，
 * 具体调用参考button监听事件
 * @author apple
 *
 */
public class UplodeProgress{  
	
//	    private Button upload;  
	    private Context mContext;
	    private String actionUrl;  
	    private HashMap<String,String> params;  
	    private String fileName;
	    private File uploadFile;
	    private FileUploadTask  fileuploadtask;
	    private String voicePath;
	    private String picPath;
	    private int type;
	    private TextView tv_progress;
	    
	    
		public static final String TAG = "alipay-sdk";

		private static final int RQF_PAY = 1;

		private static final int RQF_LOGIN = 2;
	    
	    public UplodeProgress(Context context,String actionUrl,File file,String fileName,HashMap<String, String> params,  int type,  String voicePath,
				String picPath)
	    {
	    	this.mContext=context;
	    	this.fileName=fileName;
	    	this.actionUrl=actionUrl;
	    	this.uploadFile=file;
	    	this.voicePath=voicePath;
	    	this.picPath=picPath;
	    	this.type=type;
	    	this.params=params;
	    
	    }
	      
	    //初始化文件信息  
	   
	    public void initUploadPic() {  
//	        actionUrl = "http://192.168.1.99:8080/Mobile_Police_Service/servlet/UploadFileServlet";//request url   
//	    	String fileDesc="上传图片";
//	        params = new HashMap<String,String>();  
//	        String date = new SimpleDateFormat("yy-MM-dd HH:mm").format(new Date());  
//	        params.put("fileDesc", fileDesc);//文件描述  
//	        params.put("uploadTime", date);//上传时间     
	    	if(uploadFile.length()<8*1024*1024)
	    	{
	    		fileuploadtask = new FileUploadTask();  
	            fileuploadtask.execute();  
	    	}
	    	else
	    	{
	    		Toast.makeText(mContext, "文件大小不能超过8MB！", Toast.LENGTH_SHORT).show();
	    	}
	          
	    }  
	    
	    public void cancleUpload()
	    {
	    	if(!fileuploadtask.isCancelled())
	    	{
	    		fileuploadtask.cancel(true);
	    	}
	    }
	      
	      
	    //消息框  
//	    private void showDialog(String mess){  
//	        new AlertDialog.Builder(mContext).setTitle("Message")  
//	            .setMessage(mess)  
//	            .setNegativeButton("确定",new DialogInterface.OnClickListener(){       
//	                public void onClick(DialogInterface dialog, int which){}  
//	            })  
//	            .show();  
//	    }  
	      
	    class FileUploadTask extends AsyncTask<Object, Integer, String> {  
	        private Dialog dialog = null;  
	        private ProgressBar progressBar=null;
	        HttpURLConnection connection = null;  
	        DataOutputStream outputStream = null;  
	        DataInputStream inputStream = null;  
	        //the server address to process uploaded file  
	        String end ="\r\n";//回车换行符  
	               String twoHyphens ="--";//分隔符前后缀  
	               String boundary ="######";//分隔符  
	  
	        long totalSize = uploadFile.length(); // Get size of file, bytes  
	  
	        @Override  
	        protected void onPreExecute() {  
	        	View view=LayoutInflater.from(mContext).inflate(R.layout.alert_dialog, null);
//	        	progressBar.setProgress(0);
	        	dialog=new Dialog(mContext, R.style.Translucent_NoTitle);
	        	dialog.setContentView(view);
	        	tv_progress=(TextView) view.findViewById(R.id.tv_progress);
	        	progressBar=(ProgressBar) view.findViewById(R.id.progress_bar);
	        	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
//	        			.setMessage("0k/"+totalSize/1000+"k")
//	        	dialog.setOnDismissListener(new OnDismissListener() {
//					
//					@Override
//					public void onDismiss(DialogInterface dialog) {
//						isDismiss=true;
//						
//					}
//				});
	        	btn_cancle.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						dialog.dismiss();
						cancleUpload();
						
						
					}
				});
	        	
	        	
	        	dialog.setCanceledOnTouchOutside(false);
	        	dialog.show();
	        			
	        			
	        			
//	            dialog = new ProgressDialog(mContext);  
//	            dialog.setTitle("正在上传...");  
//	            dialog.setMessage("0k/"+totalSize/1000+"k");  
//	            dialog.setIndeterminate(false);  
//	            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
//	            dialog.setCanceledOnTouchOutside(false);
//	            dialog.setProgress(0);  
//	            dialog.show();  
//	            dialog.setOnDismissListener(new OnDismissListener() {
//					@Override
//					public void onDismiss(DialogInterface dialog) {
//						cancleUpload();
//					}
//				});
	        }  
	  
	        @SuppressWarnings("finally")
			@Override  
	        protected String doInBackground(Object... arg0) {  
	            String result = "";  
	            long length = 0;  
	            int progress;  
	            int bytesRead, bytesAvailable, bufferSize;  
	            byte[] buffer;  
	            int maxBufferSize = 50 * 1024;// 10KB  
	            try {  
	                URL url = new URL(actionUrl);  
	                connection = (HttpURLConnection) url.openConnection();  
	                // Set size of every block for post  
	                connection.setChunkedStreamingMode(128 * 1024);// 128KB  
	                // Allow Inputs & Outputs  
	                connection.setDoInput(true);  
	                connection.setDoOutput(true);  
	                connection.setUseCaches(false);  
	                // Enable POST method  
	                connection.setRequestMethod("POST");  
	                connection.setRequestProperty("Connection", "Keep-Alive");  
	                connection.setRequestProperty("Charset", "UTF-8");  
	                connection.setRequestProperty("Content-Type",  
	                        "multipart/form-data;boundary=" + boundary);  
	                outputStream = new DataOutputStream(connection.getOutputStream());  
	                //写入普通属性  
	                if(params != null){  
	                    Set<String> keys = params.keySet();  
	                    for (Iterator<String> it = keys.iterator(); it.hasNext();) {  
	                        String key = it.next();  
	                        String value = params.get(key);  
	                        outputStream.writeBytes(twoHyphens + boundary + end);  
	                        outputStream.writeBytes("Content-Disposition: form-data; "+  
	                                  "name=\""+key+"\""+end);  
	                        outputStream.writeBytes(end);  
	                        outputStream.writeBytes(value);  
	                        outputStream.writeBytes(end);  
	                    }  
	                }  
	                //文件写入  
	                outputStream.writeBytes(twoHyphens + boundary + end);  
	                outputStream.writeBytes("Content-Disposition: form-data; "+  
	                          "name=\""+"file"+"\";filename=\""+  
	                          fileName +"\""+ end);
	                outputStream.writeBytes(end);  
	                FileInputStream fileInputStream = new FileInputStream(uploadFile);  
	                bytesAvailable = fileInputStream.available();  
	                bufferSize = Math.min(bytesAvailable, maxBufferSize);//设置每次写入的大小  
	                buffer = new byte[bufferSize];  
	                // Read file  
	                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	                while (bytesRead > 0&&!fileuploadtask.isCancelled()) {  
	                    outputStream.write(buffer, 0, bufferSize);  
	                    length += bufferSize;  
	                    Thread.sleep(100);  
	                    progress = (int) ((length * 100) / totalSize);  
	                    publishProgress(progress,(int)length);  
	                    bytesAvailable = fileInputStream.available();  
	                    bufferSize = Math.min(bytesAvailable, maxBufferSize);  
	                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
	                }  
	                outputStream.writeBytes(end);  
	                outputStream.writeBytes(twoHyphens + boundary + twoHyphens  
	                        + end);  
	                outputStream.flush(); 
	                
	                
	                InputStream is = connection.getInputStream();  
	                int ch;  
	                StringBuffer b =new StringBuffer();  
	                while((ch = is.read()) != -1)  
	                {  
	                  b.append((char)ch);  
	                }  
	                result  =new String(b.toString().getBytes(),"UTf-8");                 
	                is.close();  
	                fileInputStream.close();  
	               
	                outputStream.close();  
	            } catch (Exception ex) { 
	            	ex.printStackTrace();
	            }
	            finally {
	    			return result;
	    		}
	        }  
	        @Override  
	        protected void onProgressUpdate(Integer... progress) {  
	        	progressBar.setProgress(progress[0]);
	        	Float a=Float.valueOf(progress[1]);
	        	int result =  Math.round(a/totalSize*100); 
	        	tv_progress.setText(result+"%");
	        }  
	  
	        @Override  
	        protected void onPostExecute(String result) {  
	        	
	        	OnlineChatActivity activity=(OnlineChatActivity) mContext;
	        	String reason=mContext.getResources().getString(R.string.network_preoblem);
	            try {  
	                if(result.length()>0){ 
//	                	String reason=mContext.getResources().getString(R.string.network_preoblem);
//	                	String detialOnCost = "";
						int code=-1;
						try {
							Log.d("pic voice result", "test"+result);
							JSONObject jsonObject=new JSONObject(result);
							code=jsonObject.getInt("code");
							reason=jsonObject.getString("reason");
							reason=new String(Base64.decode(reason, Base64.DEFAULT),"gbk");
//							Log.d("reason", code+reason);
//							int ruleType=jsonObject.getJSONObject("results").getJSONObject("costRule").getInt("ruleType");
//							String costRule=jsonObject.getJSONObject("results").getJSONObject("costRule").getJSONObject("ruleDetial").getString("costRule");
//							String noCostNum=jsonObject.getJSONObject("results").getJSONObject("costRule").getJSONObject("ruleDetial").getString("noCostNum");
//						    detialOnCost=jsonObject.getJSONObject("results").getJSONObject("costRule").getJSONObject("ruleDetial").getString("detialOnCost");
//							Log.w("ruleType", ruleType+"");
//							Log.w("costRule", costRule+"");
//							Log.w("noCostNum", noCostNum+"");
//							Log.w("detialOnCost", detialOnCost+"");
							
							if(code==0)
							{
								Toast.makeText(mContext,reason,Toast.LENGTH_SHORT ).show();
								activity.freeCount=activity.freeCount-1;
//								freeCount=freeCount-1;
				                    if(activity.isVisiable)
				                    {
				                    	activity.showItem(type, result, uploadFile,  voicePath,
				                    			 picPath);
										if(0<activity.freeCount&&activity.freeCount<=2)
										{
											activity.showNoPayDialog(activity.freeCount);
										}
				                    }
							}
							
//							else if(code==2)
//							{
//								TOAST.MAKETEXT(MCONTEXT,REASON,TOAST.LENGTH_SHORT ).SHOW();
//								if(activity.isVisiable)
//			                    {
//									showAlertDialog(reason,consId,detialOnCost);
//			                    }
//							}
							
							
						} catch (Exception e) {
							Toast.makeText(mContext,"json数据解析错误!",Toast.LENGTH_LONG ).show(); 
						}
	                	
	                }else{  
	                    Toast.makeText(mContext,reason,Toast.LENGTH_LONG ).show();  
	                }  
	            } catch (Exception e) {  
	            }finally{  
	                try{  
	                    dialog.dismiss();  
//	                    upload.setClickable(true);  
	                }catch(Exception e){  
	                      
	                }                     
	            }  
	        }  
	    }     
	    
	
}