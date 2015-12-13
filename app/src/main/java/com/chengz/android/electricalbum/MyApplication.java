package com.chengz.android.electricalbum;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * description:
 * User: stridercheng
 * Date: 2015-12-13
 * Time: 14:56
 * FIXME
 */
public class MyApplication extends Application {

    public final static String MUSICPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ElectricAlbum/music/";
    public final static String PICTUREPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ElectricAlbum/pics/";
}  
