package com.example.aishw.techdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder mRecorder = null;
    public Boolean working;
    public VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


    }


    public void start(View view) {


        videoView=(VideoView)findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.windchimes);
        if (mRecorder == null) {
            working=true;
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while(working) {
                    double amp;
                    String show = "a";
                    amp = getAmplitude();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    show = String.valueOf(amp);
                    if(amp>10000 && !videoView.isPlaying()) {
//                    videoView.seekTo((int) (videoView.getCurrentPosition()+amp*100));
                        videoView.start();
                    }
                    if(videoView.getCurrentPosition()==videoView.getDuration() || working==false) {
                        videoView.pause();
                        videoView.seekTo(0);
                    }
//                    if (show != null)
//                        text.setText(show);
                    Log.i("Amplitude",show);
                }
            }
        });

    }

    public void stop(View view) {
        if (mRecorder != null) {
            working=false;
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return  mRecorder.getMaxAmplitude();
        else
            return 0;
    }
}
