package com.websocket.websocket;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2015-10-27.
        */
public class MyVideo {
    public Bitmap image;
    public String title;
    public String writer;
    public String video_path;
    public String thumbnail_path;

    public MyVideo(Bitmap mImage, String mTitle, String mWriter, String mVideo_path, String mThumbnail_path){
        image = mImage;
        title = mTitle;
        writer = mWriter;
        video_path = mVideo_path;
        thumbnail_path = mThumbnail_path;
    }

}
