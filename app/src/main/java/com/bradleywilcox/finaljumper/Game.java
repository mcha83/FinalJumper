package com.bradleywilcox.finaljumper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 *
 * Brad Wilcox / Michael Cha
 * CSCI 4020 Final Project
 *
 */

public class Game extends SurfaceView implements Runnable {

    public enum GameState{
        RUNNING,
        GAME_OVER,
        PAUSE
    }

    public static final int BUFFER_WIDTH = 320;
    public static final int BUFFER_HEIGHT = 480;

    private Canvas gameCanvas;
    private Bitmap frameBuffer;
    private Thread gameThread = null;
    private SurfaceHolder holder;
    private volatile boolean running = false;
    private FPSCounter fps;
    private int counter = 0;

    private GameState gameState;
    private World world;
    private Rect background;
    private Rect pause;

    private Context context;

    private Button buttLose1, buttLose2;

    public Game(Context context, Bitmap buffer) {
        super(context);
        this.context = context;
        frameBuffer = buffer;
        gameCanvas = new Canvas(frameBuffer);
        holder = getHolder();
        fps = new FPSCounter();

        background = new Rect(0, 0, BUFFER_WIDTH, BUFFER_HEIGHT);
        pause = new Rect(BUFFER_WIDTH - 64, 0, BUFFER_WIDTH - 64 + 64, 64);

        gameState = GameState.RUNNING;
        world = new World();
    }

    @Override
    public void run() {
        Rect rect = new Rect();
        long startTime = System.nanoTime();

        while (running) {
            if (!holder.getSurface().isValid())
                continue;

            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;

            startTime = System.nanoTime();

            update(deltaTime);
            render(deltaTime);


            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(rect);
            canvas.drawBitmap(frameBuffer, null, rect, null);
            holder.unlockCanvasAndPost(canvas);

            fps.logFrame();
        }
    }

    public void update(float deltaTime) {
        if(gameState == GameState.RUNNING)
            world.update(deltaTime);

        if(world.isGameOver()) {
            gameState = GameState.GAME_OVER;
            Data.saveHighScore();
        }

        if(InputHandler.touchX > BUFFER_WIDTH - 64)
            if(InputHandler.touchY < 64)
                if(gameState == GameState.RUNNING)
                    gameState = GameState.PAUSE;
                else if(gameState == GameState.PAUSE)
                    gameState = GameState.RUNNING;

        InputHandler.resetTouch();
    }

    public void render(float deltaTime) {
        //background
        gameCanvas.drawBitmap(Assets.background, null, background, null);

        //game world
        world.render(gameCanvas);

        //menu
        if(gameState != GameState.PAUSE)
            gameCanvas.drawBitmap(Assets.pauseButton, null, pause, null);
        else
            gameCanvas.drawBitmap(Assets.playButton, null, pause, null);

        if (gameState == GameState.GAME_OVER) {
            gameLost(true);
            counter +=1;
        }
        else
            counter = 0;

    }

    public void gameLost(boolean state)
    {
        if(state && counter ==1)
        {
            Assets.playSound(3);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    runPopup2();
                }
            });
        }
    }

    public void runPopup2() {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.lose, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        buttLose1 = (Button) popupView.findViewById(R.id.buttonOver);
        buttLose2 = (Button) popupView.findViewById(R.id.buttonOver2);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        buttLose1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                popupWindow.dismiss();
            }
        });

        buttLose2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){

                Intent intent = new Intent(context, Start.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

                context.startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }


    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                gameThread.join();
                break;
            } catch (InterruptedException e) {

            }
        }
    }



}
