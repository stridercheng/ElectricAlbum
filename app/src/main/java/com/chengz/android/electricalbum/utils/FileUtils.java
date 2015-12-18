package com.chengz.android.electricalbum.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.chengz.android.electricalbum.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
/**
 * description:
 * User: stridercheng
 * Date: 2015-12-13
 * Time: 15:50
 * FIXME
 */
public class FileUtils {

    public static List<ImageView> getImagesToShow(Context context) {
        List<ImageView> imageViewList = null;
        try {
            imageViewList = new ArrayList<>();

            File file = new File(MyApplication.PICTUREPATH);
            Log.e("FileUtils", "path =" + MyApplication.PICTUREPATH);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];
                if (tempFile.getName().endsWith("jpg") || tempFile.getName().endsWith("png")) {
                    // read picture;
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(tempFile));

                    ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);



                    imageView.setImageBitmap(bitmap);

                    imageViewList.add(imageView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageViewList;
    }
}  
