package com.bradleywilcox.finaljumper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 *
 * Brad Wilcox / Michael Cha
 * CSCI 4020 Final Project
 *
 */

public class Score extends GameObject {
    private float highscore;
    private float score;
    private int counter = 0;
    private Paint paint;

    private float highScoreY;

    public Score(){
        highscore = Data.HighScore;
        score = 0;
        this.x = 1;
        this.y = 20;

        this.highScoreY = (Game.BUFFER_HEIGHT / 2) - highscore;

        paint = new Paint();
        paint.setColor(Color.WHITE);
    }


    @Override
    public void render(Canvas canvas){
        canvas.drawText("" + (int)score, x + 5, y - 1, paint);

        if(score < highscore){
            canvas.drawLine(0, highScoreY, Game.BUFFER_WIDTH, highScoreY, paint);
            canvas.drawText("High Score " + (int)highscore, 5, highScoreY - 1, paint);
        }
    }

    public void incrementScore(float score){
        this.score += score;

        if(this.score > highscore){
            Data.HighScore = this.score;

            if(highscore > 0)
                gameHigh(true);

            counter+=1;
        }
        else
        counter = 0;

        addOffsetY(score);
    }

    public void gameHigh(Boolean state)
    {
        if(state && counter==1)
        {
            Assets.playSound(2);
        }
    }

    @Override
    public void addOffsetY(float offset){
        highScoreY += offset;
    }

    public float getScore(){
        return this.score;
    }
}
