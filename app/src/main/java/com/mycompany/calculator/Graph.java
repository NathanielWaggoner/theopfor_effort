package com.mycompany.calculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Graph extends View {
    Canvas graph;
    Paint paint;
    Equation equation;

    int width;
    int height;
    int xPPC;
    int yPPC;
    int pixelOffset;

    Graph(Equation e, View v, Context context) {
        super(context);

        if (v == null)
            throw new NullPointerException("Graph's view cannot be null");

        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        graph = new Canvas(bitmap);
        width = graph.getWidth();
        height = graph.getHeight();
        equation = e;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }

    // Convert pixels to X Coordinate
    public double getXCoord(int pixel) {
        return (float)(pixel + pixelOffset) / xPPC;
    }

    // Converts y coordinate to pixels
    public int getYPixel(double y){
        return (int)y * yPPC - pixelOffset;
    }
}
