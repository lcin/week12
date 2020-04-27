package com.android.uoso.week12.activity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.uoso.week12.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActVideo extends AppCompatActivity {

    @BindView(R.id.video_view)
    VideoView videoView;
    //网络视频地址
    private static final String URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_speed)
    ImageView ivSpeed;
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.iv_screen)
    ImageView ivScreen;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    public static final int SPACE_TIME = 10 * 1000;//快进、
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    private boolean isShow = true;//是否显示操作进度栏
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10) {
                //刷新进度条
                int currentPosition = videoView.getCurrentPosition();
                int duration = videoView.getDuration();
                int bufferPercentage = videoView.getBufferPercentage(); //视频缓冲的百分比 50%
                //计算视频缓冲的时间点
                int secondProgress = (int) (((double) duration / 100) * bufferPercentage);
                seekBar.setSecondaryProgress(secondProgress); //设置缓冲进度
                seekBar.setProgress(currentPosition); //设置视频当前播放进度
                tvDuration.setText(formatTime(duration));
                tvCurrent.setText(formatTime(currentPosition));
                handler.sendEmptyMessageDelayed(10, 500);//循环发送消息，通知刷新
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_video);
        ButterKnife.bind(this);
        //设置网络视频地址
        videoView.setVideoURI(Uri.parse(URL));
        //视频准备完成
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //获取视频总长度
                int duration = videoView.getDuration();
                seekBar.setMax(duration);
                handler.sendEmptyMessage(10);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //触控进度条结束
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                videoView.seekTo(progress);
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    //隐藏
                    isShow = false;
                    llBar.setVisibility(View.GONE);
                    ivScreen.setVisibility(View.GONE);
                } else {
                    //显示
                    isShow = true;
                    llBar.setVisibility(View.VISIBLE);
                    ivScreen.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    /**
     * 格式化时间
     */
    private String formatTime(int msec) {
        DecimalFormat decimalFormat = new DecimalFormat("00");

        int sec = msec / 1000; //05  15
        String time = "";
        if (sec < 60) {//一分钟以内
            time = "00:" + decimalFormat.format(sec);
        } else if (sec < 3600) {//一分钟以上，一个小时以内
            int min = sec / 60; // 128/60  02:08;
            sec = sec % 60;
            time = decimalFormat.format(min) + ":" + decimalFormat.format(sec);
        } else {
            int hour = sec / 3600;//7278 /3600 = 02:01:18   7278/60 = 121...18
            int min = (sec % 3600) / 60;
            sec = sec % 60;
            time = decimalFormat.format(hour) + ":" + decimalFormat.format(min) + ":" + decimalFormat.format(sec);
        }
        return time;
    }

    @OnClick({R.id.iv_back, R.id.iv_play, R.id.iv_speed, R.id.iv_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                back();
                break;
            case R.id.iv_play:
                play();
                break;
            case R.id.iv_speed:
                speed();
                break;
            case R.id.iv_screen:
                break;
        }
    }

    /**
     * 快退
     */
    private void back() {
        int currentPosition = videoView.getCurrentPosition();
        int target = currentPosition - SPACE_TIME;
        if (target < 0) {
            target = 0;
        }
        videoView.seekTo(target);
    }

    /**
     * 快进
     */
    private void speed() {
        //获取当前视频播放的时间点（毫秒）
        int currentPosition = videoView.getCurrentPosition();
        int duration = videoView.getDuration();
        //计算快进后的时间点
        int target = currentPosition + SPACE_TIME;
        if (target > duration) {
            target = duration;
        }
        videoView.seekTo(target);
    }


    /**
     * 播放或暂停
     */
    private void play() {
        if (videoView.isPlaying()) {
            //暂停
            videoView.pause();
            ivPlay.setImageResource(R.mipmap.ic_play);
        } else {
            //播放
            videoView.start();
            ivPlay.setImageResource(R.mipmap.ic_stop);
        }
    }
}
