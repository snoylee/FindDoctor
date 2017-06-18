package com.guangyi.forDoctor.utils;

import java.io.IOException;

import android.media.MediaRecorder;

public class AudioRecorder
{
	private static int SAMPLE_RATE_IN_HZ = 8000; 

	final MediaRecorder recorder = new MediaRecorder();
 

	public void start(String path) throws IOException
	{
	    recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // ��������ԴΪ��˷�
	    recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); // ���������ʽ
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); // ������Ƶ�ı���
//		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//		recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();
	}

	public void stop() throws IOException
	{
		recorder.stop();
		recorder.release();
	}
	
	public double getAmplitude() {		
		if (recorder != null){			
			return  (recorder.getMaxAmplitude());		
			}		
		else			
			return 0;	
		}
}