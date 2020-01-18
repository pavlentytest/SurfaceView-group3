package com.samsung.myitschool.myapplication;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.Random;

public class MyThread extends Thread {

    // частота обновления экранае
    private final int REDRAW_TIME=100;
    // время анимации
    private final int ANIMATION_TIME=1500;
    //  флажок запущен ли поток
    private boolean flag;
    // время начала анимации
    private long startTime;
    // предыдущее время перерисовки
    private long prevRedrawTime;
    private Paint paint;
    // переменная для интерполирования
    private ArgbEvaluator argbEvaluator;
    // указатель на holder  для получения canvas
    private SurfaceHolder surfaceHolder;

    MyThread(SurfaceHolder h) {
        surfaceHolder = h;
        flag = false;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        argbEvaluator = new ArgbEvaluator();
    }

    public long getTime() {
        return System.nanoTime()/1000; // mkrsec
    }

    public void setRunning(boolean running) {
        flag = running;
        prevRedrawTime = getTime();
    }


    @Override
    public void run() {
        Canvas canvas;
        startTime = getTime();
        while(flag) {
            long currentTime = getTime();
            long elapsedTime = currentTime - prevRedrawTime;
            if(elapsedTime < REDRAW_TIME) {
                continue;
            }
            canvas = null;
            // получаем результат canvas
            canvas = surfaceHolder.lockCanvas();

            draw(canvas);

            // очищаем canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
            // обновляем время
            prevRedrawTime = getTime();
        }
    }

    public void draw(Canvas canvas) {
        long currTime = getTime() - startTime;
        // высота и ширина canvas
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        // фон
        canvas.drawColor(Color.BLACK);
        int centerX = width/2;
        int centerY = height/2;
        // максимальный размер радиуса
        float maxRadius = Math.min(width,height)/2;
        // шаг для интерполирования
        float fraction = (float)(currTime%ANIMATION_TIME)/ANIMATION_TIME;
        // цвет в какой-то момент времени
        Random r = new Random();
        int color = Color.argb(r.nextInt(255),r.nextInt(255),r.nextInt(255),r.nextInt(255));
        //int color = (int)argbEvaluator.evaluate(fraction,Color.RED,Color.BLACK);
        paint.setColor(color);
        canvas.drawCircle(centerX,centerY,maxRadius*fraction,paint);
    }

}

