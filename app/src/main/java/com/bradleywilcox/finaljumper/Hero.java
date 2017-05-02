package com.bradleywilcox.finaljumper;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 *
 * Brad Wilcox / Michael Cha
 * CSCI 4020 Final Project
 *
 */

public class Hero extends GameObject{

    private static final int JUMP_VELOCITY = -300;
    private static final int MOVE_VELOCITY = 500;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private PointF velocity;
    private RectF drawingRect;

    private boolean isHit;

    public Hero(int x, int y) {
        this.width = WIDTH;
        this.height = HEIGHT;

        velocity = new PointF(0, 0);

        this.x = x;
        this.y = y;

        drawingRect = new RectF(x, y, x + width, x + height);
        isHit = false;
    }

    @Override
    public void update(float delta) {

        if(isHit)
            velocity.x = 0;
        else
            velocity.x = InputHandler.accel * MOVE_VELOCITY * delta;

            velocity.y += InputHandler.IsEmulator ? World.EMU_GRAVITY * delta : World.GRAVITY * delta;

        x += velocity.x * delta;
        y += velocity.y * delta;

        if(x > Game.BUFFER_WIDTH)
            x = 0;
        else if(x + WIDTH < 0)
            x = Game.BUFFER_WIDTH;

        // "try" to simulate the feel of the accelerometer control on the device
        if(InputHandler.IsEmulator)
            InputHandler.accel -= InputHandler.accel * .9f * delta;
    }

    @Override
    public void render(Canvas canvas) {

        drawingRect.set(x, y, x + width, y + height);
        if(velocity.y < 0)
            canvas.drawBitmap(Assets.heroJump, null, drawingRect, null);
        else
            canvas.drawBitmap(Assets.heroStand, null, drawingRect, null);

    }

    public void hitPlatform() {
        velocity.y = JUMP_VELOCITY;
        Assets.playSound(1);
    }

    public float getVelocityY(){
        return this.velocity.y;
    }

    public void hit(){
        if(!isHit){
            isHit = true;
            velocity.y = 0;
            Assets.playSound(4);
        }
    }

    public boolean isHit(){
        return isHit;
    }

}
