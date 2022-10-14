package com.example.asyncaction.handlerThread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageDownLoader implements Handler.Callback {

    public static final String TAG = "ImageDownLoader";
    private Handler handler;
    int curCount = 0;
    String url1 = "YOUR_IMAGE_URL1";
    String url2 = "YOUR_IMAGE_URL2";
    float totalCount = 50F;

    int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    private void imageRequest() {
        for (int i = 0; i < totalCount; i++) {
            String imageUrl = (i % 2 == 0) ? url1 : url2;
            executor.execute(new ImageDownloaderThread(i, imageUrl, new Handler(this)));
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        curCount++;
        float per = (curCount / totalCount) * 100;
        if (per < 100)
            Log.i(TAG, "Downloaded [" + curCount + "/" + (int) totalCount + "]");
        else
            Log.i(TAG, "All images downloaded.");
        return true;
    }
}
