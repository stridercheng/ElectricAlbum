package com.chengz.android.electricalbum;

import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.chengz.android.electricalbum.customview.FlowerView;
import com.chengz.android.electricalbum.customview.SlideToShowView;
import com.chengz.android.electricalbum.utils.UnzipAssets;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SlideToShowView.CooperationControlInterface {
    private SlideToShowView slideToShowView;
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
    private TextView tv_sentence;
    private float originX, originY;
    private int[] tips = new int[]{
            R.string.romantic_sentence1, R.string.romantic_sentence2,
            R.string.romantic_sentence3, R.string.romantic_sentence4,
            R.string.romantic_sentence5, R.string.romantic_sentence6,
            R.string.romantic_sentence7, R.string.romantic_sentence8,
            R.string.romantic_sentence9, R.string.romantic_sentence10
    };
    private int cursor = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowerView = (FlowerView) findViewById(R.id.flowerView);
        slideToShowView = (SlideToShowView) findViewById(R.id.slidetoshow);
        tv_sentence = (TextView) findViewById(R.id.sentence);
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

        new InitTask().execute();

        //init tv params
//        startTextAnimation();
        slideToShowView.setCooperationControlInterface(this);
    }

    private void startTextAnimation() {
        Log.e("MainActivity", "startTextAnimation--->");
        if (cursor >= tips.length) {
            cursor = 0;
        }
        tv_sentence.setText(getResources().getText(tips[cursor]));
        tv_sentence.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_tranlate));
        cursor ++;
    }

    @Override
    public void showText() {
        startTextAnimation();
    }

    class InitTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            copyResources();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            playMusic();
            slideToShowView.initData();
        }
    }

    private void copyResources() {
        try {
            UnzipAssets.unZip(this, "music.zip", MyApplication.MUSICPATH);
//            UnzipAssets.unZip(this, "pics.zip", MyApplication.PICTUREPATH);
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMusic() {
        try {

            File file = new File(MyApplication.MUSICPATH + "trace2.mp3");

            Log.e("MainActivity", file.getAbsoluteFile().toString() + "fileExists=" + file.exists());
            Uri uri = Uri.fromFile(file);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mFlowerView.recly();
    }
}
