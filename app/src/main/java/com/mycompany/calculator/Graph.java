package com.mycompany.calculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Graph extends SurfaceView implements SurfaceHolder.Callback {
    Paint paint;
    Equation equation;

    int xPPC;
    int yPPC;
    int pixelOffset;
    DrawThread thread;
    Context context;

    public Graph(Context mContext) {
        super(mContext);
        context = mContext;
        init();
    }

    public Graph(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        context = mContext;
        init();
    }

    public Graph(Context mContext, AttributeSet attrs, int defStyle) {
        super(mContext, attrs, defStyle);
        context = mContext;
        init();
    }

    public void init(){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.id.GraphView);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new DrawThread(holder, getContext(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setRunning(false);
        boolean retry = true;

        while (retry) {

            try{
                thread.join();
                retry = false;
            }
            catch (Exception e){
                Log.v("Exception occured", e.getMessage());
            }
        }
    }

    // Convert pixels to X Coordinate
    public double getXCoord(int pixel) {
        return (float)(pixel + pixelOffset) / xPPC;
    }

    // Converts y coordinate to pixels
    public int getYPixel(double y){
        return (int)y * yPPC - pixelOffset;
    }

    public void changeEquation(Equation e){
        equation = e;
    }

    public class DrawThread extends Thread{
        boolean running;
        SurfaceHolder holder;
        Canvas canvas;
        Context context;
        Graph graph;
        int width;
        int height;

        DrawThread(SurfaceHolder mHolder, Context mContext, Graph g){
            holder = mHolder;
            context = mContext;
            graph = g;
            running = false;
            width = g.getWidth();
            height = g.getHeight();
        }

        void setRunning(boolean status){
            running = status;
        }

        void setSurfaceSize(int w, int h){
            width = w;
            height = h;
        }

        void doDraw(Canvas canvas) {
            canvas.drawPaint(paint);
        }

        @Override
        public void run(){
            super.run();

            while(running){
                canvas = holder.lockCanvas();

                if(canvas != null){
                    doDraw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
