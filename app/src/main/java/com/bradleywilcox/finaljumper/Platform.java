package com.bradleywilcox.finaljumper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 *
 * Brad Wilcox / Michael Cha
 * CSCI 4020 Final Project
 *
 */

public class Platform extends GameObject {

    private Paint paint;

    public Platform(int x, int y) {
        this.width = 100;
        this.height = 10;

        paint = new Paint();
        paint.setColor(Color.WHITE);

        this.x = x;
        this.y = y;
    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

}