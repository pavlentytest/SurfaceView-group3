package com.samsung.myitschool.myapplication;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    MyThread myThread; // наш поток прорисовки

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
// вызывается когда surfaceview появляется на экране
        myThread = new MyThread(getHolder());
        myThread.setRunning(true);
        myThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
// когда меняется surfaceview
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
// onDestroy()
        boolean retry = true;
        myThread.setRunning(false);
        while(retry) {
            try {
                myThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
}
