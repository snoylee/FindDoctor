package com.guangyi.finddoctor.utils;

import java.io.IOException;

import android.annotation.TargetApi;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class AudioRecorder {
	private static int SAMPLE_RATE_IN_HZ = 8000;
	private MediaRecorder recorder ;
		
	public AudioRecorder()
	{
		if(recorder==null)
		{
			recorder=new MediaRecorder();
		}
	}

	public void start(String path) throws IOException {
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置输入源为麦克风
		recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); // 设置输出格式
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); // 设置音频的编码
		// recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();
	}

	public void stop() throws IOException {
		recorder.stop();
		recorder.release();
	}
	
	public void reset() throws IOException {
		recorder.reset();
	}
	
	public void release() throws IOException {
		recorder.release();
	}

	public double getAmplitude() {
		if (recorder != null) {
			return (recorder.getMaxAmplitude());
		} else
			return 0;
	}
}