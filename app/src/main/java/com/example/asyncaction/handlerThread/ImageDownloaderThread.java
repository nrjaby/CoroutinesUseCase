package com.example.asyncaction.handlerThread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.InputStream;


public class ImageDownloaderThread implements Runnable {

    private final int threadNo;
    private final Handler handler;
    public String imageUrl;

    public static final String TAG = "ImageDownloaderThread";


    public ImageDownloaderThread(int threadNo, String imageUrl, Handler handler) {
        this.threadNo = threadNo;
        this.handler = handler;
        this.imageUrl = imageUrl;
    }

    @Override
    public void run() {
        Log.i(TAG, "Starting Thread : " + threadNo);
        getBitmap(imageUrl);
        sendMessage(threadNo, "Thread Completed");
        Log.i(TAG, "Thread Completed " + threadNo);
    }

    public void sendMessage(int what, String msg) {
        Message message = handler.obtainMessage(what, msg);
        message.sendToTarget();
    }

    private void getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            InputStream input = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
