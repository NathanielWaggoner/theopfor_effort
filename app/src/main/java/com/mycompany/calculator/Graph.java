package com.mycompany.calculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Graph extends SurfaceView implements SurfaceHolder.Callback {

    // Pixels per showed cell
    int xPPC;
    int yPPC;

    int pixelOffset;

    // Starting dimensions shown to user
    int yDimension = 10;
    int xDimension = 10;

    Paint BGPaint;
    Paint paint;
    Equation equation;
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
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        equation = new Equation(context.getSharedPreferences("Equations", 0).getString("Y1", "x"));

        // Set paint for points
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);

        // Set paint for background
        BGPaint = new Paint();
        BGPaint.setStyle(Paint.Style.FILL);
        BGPaint.setColor(Color.WHITE);

        // For getting events
        setFocusable(true);

        // Setup thread
        thread = new DrawThread(holder, context, this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
    private double getXCoord(int pixel) {
        return (float)(pixel + pixelOffset) / xPPC;
    }

    // Converts y coordinate to pixels
    private int getYPixel(double y){
        return (int)y * yPPC - pixelOffset;
    }

    public void changeEquation(Equation e){
        equation = e;
    }

    public class DrawThread extends Thread{
        SurfaceHolder holder;
        Canvas canvas;
        Context context;
        Graph graph;

        int width;
        int height;
        boolean running;
        boolean alreadyDrawn;

        float points[];

        DrawThread(SurfaceHolder mHolder, Context mContext, Graph g){
            holder = mHolder;
            context = mContext;
            graph = g;
            running = false;
            width = g.getWidth();
            height = g.getHeight();
            alreadyDrawn = false;
            yPPC = height / yDimension;
            xPPC = width / xDimension;
            pixelOffset = -(width/2);
            points = new float[width*2];

            // Generate points
            for (int i = 0; i < width; i++){
                double yCoord = equation.getY(getXCoord(i));
                points[i*2] = i;
                points[i*2+1] = getYPixel((float)yCoord);
                Log.i("Point", "(" + i + ", " + getYPixel((float)yCoord) + ")");
            }
        }

        public void setRunning(boolean status){
            running = status;
        }

        public void setSurfaceSize(int w, int h){
            width = w;
            height = h;
        }

        void doDraw(Canvas canvas) {
            alreadyDrawn = true;

            // Draw background
            canvas.drawPaint(BGPaint);
            Log.i("GraphDraw", "Drew BG");

            canvas.drawLines(points, paint);
            Log.i("GraphDraw", "Points drawn");
        }

        @Override
        public void run(){
            super.run();

            while(running){
                if (!holder.getSurface().isValid())
                    continue;

                if (!alreadyDrawn) {
                    Log.i("Graph", "Drawing");
                    canvas = holder.lockCanvas();

                    if (canvas != null) {
                        doDraw(canvas);

                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
