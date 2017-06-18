package com.guangyi.forDoctor.custview;


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

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.onlineConsult.OnlineChatActivity;

/***
 * 文件上传工具类，
 * 具体调用参考button监听事件
 * @author apple
 *
 */
public class UplodeProgressActivity{  
	
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
//	    private boolean isDismiss=false;
	    private TextView tv_progress;
	    
	    public UplodeProgressActivity(Context context,String actionUrl,File file,String fileName,HashMap<String, String> params,  int type,  String voicePath,
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
	        	dialog=new Dialog(mContext, R.style.Translucent_NoTitle);
	        	dialog.setContentView(view);
	        	tv_progress=(TextView) view.findViewById(R.id.tv_progress);
	        	progressBar=(ProgressBar) view.findViewById(R.id.progress_bar);
	        	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
	        	btn_cancle.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						dialog.dismiss();
						cancleUpload();
						
						
					}
				});
	        	
	        	
	        	dialog.setCanceledOnTouchOutside(false);
	        	dialog.show();
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
//	                publishProgress(100,(int)length);  
	                // Responses from the server (code and message)  
	                /* 取得Response内容 */  
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
	                outputStream.flush();  
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
	            try {  
	                if(result.length()>0){  
	                    Toast.makeText(mContext,"上传成功!",Toast.LENGTH_LONG ).show();  
	                    OnlineChatActivity activity=(OnlineChatActivity) mContext;
	                    if(activity.isVisiable)
	                    {
	                    	
	                    	activity.showItem(type, result, uploadFile,  voicePath,
	                    			 picPath);
	                    }
	                    
	                }else{  
	                    Toast.makeText(mContext,"上传失败!",Toast.LENGTH_LONG ).show();  
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