package com.chengz.android.electricalbum.customview;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.chengz.android.electricalbum.R;
import com.chengz.android.electricalbum.anim.PagerTranslateFormer;
import com.chengz.android.electricalbum.utils.FileUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.util.Log;
/**
 * description:
 * User: stridercheng
 * Date: 2015-12-13
 * Time: 15:36
 * FIXME
 */
public class SlideToShowView extends FrameLayout {
    private List<ImageView> imageViewList;
    private ViewPager viewPager;
    private int currentItem = 0;
    private Context context;
    private ScheduledExecutorService scheduledExecutorService;
    private boolean isAutoPlay = true;
    private CooperationControlInterface cooperationControlInterface;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };

    public SlideToShowView(Context context) {
        super(context);
        this.context = context;
    }

    public SlideToShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SlideToShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void initData() {
        imageViewList = new ArrayList<>();
        new GetListTask().execute();

        if (isAutoPlay) {
            startPlay();
        }
    }

    public void setCooperationControlInterface(CooperationControlInterface cooperationControlInterface) {
        this.cooperationControlInterface = cooperationControlInterface;
    }

    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideTask(), 10, 10, TimeUnit.SECONDS);
    }

    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    private void initUI() {
        LayoutInflater.from(context).inflate(R.layout.layout_slideview, this, true);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        if (cooperationControlInterface != null) {
            cooperationControlInterface.showText();
        }
        viewPager.setPageTransformer(true, new PagerTranslateFormer());
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(context);
            mScroller.set(viewPager, scroller);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.e("SlideToShowView", "currentPosition=" + position);
            if (cooperationControlInterface != null) {
                cooperationControlInterface.showText();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    } else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
                default:
                    break;
            }
        }
    }


    class GetListTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            imageViewList = FileUtils.getImagesToShow(context);
            if (imageViewList == null) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean) {
                Toast.makeText(context, "获取图片失败。", Toast.LENGTH_SHORT).show();
                return;
            }
            initUI();
        }
    }

    class SlideTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewList.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViewList.get(position));
            return imageViewList.get(position);
        }
    }

    public interface CooperationControlInterface {
        void showText();
    }

}
