package com.chengz.android.electricalbum;

import android.media.MediaPlayer;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.chengz.android.electricalbum.customview.FlowerView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    ImageView iv_background;
    private int screenWidth, screenHeight;
    private float density;

    private FlowerView mFlowerView;

    private Timer myTimer;
    private TimerTask mTask;
    private static final int FLOWER_BLOCK = 1;
    private MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            mFlowerView.inva();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_background = (ImageView) findViewById(R.id.weddingPicture);
        mFlowerView = (FlowerView) findViewById(R.id.flowerView);
        initData();
    }

    private void initData() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        density = metrics.density;

        mFlowerView.setWH(screenWidth, screenHeight, density);
        mFlowerView.loadFlower();
        mFlowerView.addRect();

        myTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = FLOWER_BLOCK;
                mHandler.sendMessage(msg);
            }
        };

        myTimer.schedule(mTask, 3000, 10);

        playMusic();
    }

    private void playMusic() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.trace2);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mFlowerView.recly();
    }
}
