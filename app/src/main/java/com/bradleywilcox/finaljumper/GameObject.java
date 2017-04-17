package com.bradleywilcox.finaljumper;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by Brad on 4/17/2017.
 */

public class GameObject {
    protected float x, y;
    protected int width, height;

    private PointF point2D;

    public GameObject(){
        point2D = new PointF();
    }

    public void update(float delta){

    }

    public void render(Canvas canvas){

    }

    public PointF lowerLeft(){
        point2D.set(x, y);
        return point2D;
    }

    public PointF lowerRight(){
        point2D.set(x + width, y);
        return point2D;
    }

    public PointF upperLeft(){
        point2D.set(x, y + height);
        return point2D;
    }

    public PointF upperRight(){
        point2D.set(x + width, y + height);
        return point2D;
    }
}
