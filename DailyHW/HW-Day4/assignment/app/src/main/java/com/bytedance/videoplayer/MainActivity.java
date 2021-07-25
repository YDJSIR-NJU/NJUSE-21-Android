package com.bytedance.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bytedance.videoplayer.player.PositionRecorder;
import com.bytedance.videoplayer.player.VideoPlayerIJK;
import com.bytedance.videoplayer.player.VideoPlayerListener;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.spec.ECField;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {
    protected TextView tv_start;
    protected TextView tv_end;
    protected SeekBar seekBar;
    private Timer timer;//定时器
    private boolean isSeekbarChaning; // 互斥变量，防止进度条和定时器冲突。
    private Button buttonPlay;
    private Button buttonPause;
    private VideoView videoView;
    public int position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("VideoView");

        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(getVideoPath(R.raw.bytedance));

        initView(); // 初始化进度条
        initMediaPlayer(); // 初始化MediaPlayer

        // 隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        // 点击屏幕，暂停或者开始
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!videoView.isPlaying()) {
                    int duration = (int) videoView.getDuration();//获取视频总时间
                    seekBar.setMax(duration);//将视频总时间设置为Seekbar的最大值
                    tv_start.setText(calculateTime((int) videoView.getCurrentPosition() / 1000));//开始时间
                    tv_end.setText(calculateTime((int) (videoView.getDuration() / 1000)));//总时长
                    videoView.seekTo(PositionRecorder.position);
                    try {
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (!isSeekbarChaning) {
                                    seekBar.setProgress((int) videoView.getCurrentPosition());
                                }
                                PositionRecorder.position = videoView.getCurrentPosition();
                                Log.d("TAGHERE", String.valueOf(PositionRecorder.position / 1000));
                            }
                        }, 0, 10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    videoView.start();
                }
                else{
                    videoView.pause();
                }
            }
        });
    }

    /**
     * 横竖屏切换
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            videoView.setLayoutParams(params);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            videoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
            Log.d("VIDEOHERE", "START");
        }
        videoView.seekTo(position);
        Log.d("VIDEOHERE", "START");
        videoView.start();

    }



    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }
    
    private void initView(){
        tv_start = (TextView)findViewById(R.id.tv_start);
        tv_end = (TextView)findViewById(R.id.tv_end);
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        int duration = (int) videoView.getDuration() / 1000;//获取视频总时间
        seekBar.setMax(duration);//将视频总时间设置为Seekbar的最大值
        //绑定监听器，监听拖动到指定位置
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int duration2 = videoView.getDuration() / 1000;//获取视频总时长
                int position2 = videoView.getCurrentPosition()/1000;//获取当前播放的位置
                tv_start.setText(calculateTime(position2));//开始时间
                tv_end.setText(calculateTime(duration2));//总时长
            }
            /*
             * 通知用户已经开始一个触摸拖动手势。
             * */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekbarChaning = true;
            }
            /*
             * 当手停止拖动进度条时执行该方法
             * 首先获取拖拽进度
             * 将进度对应设置给MediaPlayer
             * */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekbarChaning = false;
                videoView.seekTo(seekBar.getProgress());//在当前位置播放
                tv_start.setText(calculateTime(videoView.getCurrentPosition() / 1000));
            }
        });


    }


    public String calculateTime(int time){
        int minute;
        int second;
        if(time > 60){
            minute = time / 60;
            second = time % 60;
            //分钟再0~9
            if(minute >= 0 && minute < 10){
                //判断秒
                if(second >= 0 && second < 10){
                    return "0"+minute+":"+"0"+second;
                }else {
                    return "0"+minute+":"+second;
                }
            }else {
                //分钟大于10再判断秒
                if(second >= 0 && second < 10){
                    return minute+":"+"0"+second;
                }else {
                    return minute+":"+second;
                }
            }
        }else if(time < 60){
            second = time;
            if(second >= 0 && second < 10){
                return "00:"+"0"+second;
            }else {
                return "00:"+ second;
            }
        }
        return null;
    }


    private void initMediaPlayer(){
        int duration2 = videoView.getDuration() / 1000;
        int position2 = videoView.getCurrentPosition();
        tv_start.setText(calculateTime(position2 / 1000));
        tv_end.setText(calculateTime(duration2));
    }

}
