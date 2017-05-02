package com.bradleywilcox.finaljumper;

import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * Brad Wilcox / Michael Cha
 * CSCI 4020 Final Project
 *
 */

public class World {
    public static final int EMU_GRAVITY = 275;
    public static final int GRAVITY = 375;

    private Hero hero;
    private ArrayList<Platform> platforms;
    private ArrayList<Asteroid> asteroids;
    private Score score;

    private int offsetPosition;
    private int currentDistanceInterval;
    private int currentLevelY;
    private Random rand;

    public World(){
        hero = new Hero((Game.BUFFER_WIDTH / 2) - (Hero.WIDTH / 2), Game.BUFFER_HEIGHT - Hero.HEIGHT - Platform.HEIGHT * 4);

        platforms = new ArrayList<>(25);
        asteroids = new ArrayList<>(5);
        score = new Score();
        rand = new Random();

        generateWorld();

        offsetPosition = Game.BUFFER_HEIGHT / 2;
    }

    public void generateWorld(){
        int worldHalf = Game.BUFFER_WIDTH / 2;
        int platformHalf = Platform.WIDTH / 2;

        //starting platform
        currentLevelY = Game.BUFFER_HEIGHT - Platform.HEIGHT * 3;
        platforms.add(new Platform(worldHalf - platformHalf, currentLevelY));

        currentDistanceInterval = 50;
        currentLevelY -= currentDistanceInterval;

        for(int i = 0; i < 25; i++){
            int randX = rand.nextInt(Game.BUFFER_WIDTH - Platform.WIDTH);
            platforms.add(new Platform(randX, currentLevelY));
            currentLevelY -= currentDistanceInterval;
        }

        for(int i = 0; i < 5; i++)
            asteroids.add(i, new Asteroid(0, Game.BUFFER_HEIGHT * 2));

    }

    public void update(float delta){
        hero.update(delta);

        if(hero.getVelocityY() > 0 && !hero.isHit())
            for(Platform p : platforms)
                if(Collisions.isColliding(hero, p)) {
                    if((hero.y + hero.height * .5) <= p.y) {
                        hero.hitPlatform();
                        break;
                    }
                }

        for(Asteroid a : asteroids){
            a.update(delta);
            if(Collisions.isColliding(hero, a))
                hero.hit();
        }

        if(hero.y < offsetPosition)
            addWorldOffset(offsetPosition - hero.y);


        int notActive = 0;
        for(Platform p : platforms){
            p.update(delta);

            if(!p.getIsActive())
                notActive++;
        }
        if(notActive > 5) {
            extendTheLevel();
        }

    }

    public void render(Canvas canvas){
        hero.render(canvas);

        for(Platform p : platforms)
            p.render(canvas);

        for(Asteroid a : asteroids)
            a.render(canvas);

        score.render(canvas);
    }

    public void addWorldOffset(float offset){
        for(Platform p : platforms)
            p.addOffsetY(offset);

        for(Asteroid a : asteroids)
            a.addOffsetY(offset);

        hero.addOffsetY(offset);

        score.incrementScore(offset);

        currentLevelY += offset;
    }

    public boolean isGameOver(){
        if(hero.y + hero.height > Game.BUFFER_HEIGHT)
            return true;

        return false;
    }

    private void extendTheLevel(){
        if(currentDistanceInterval < 140)
            currentDistanceInterval += 5;

        currentLevelY -= currentDistanceInterval;

        for(Platform p : platforms){
            if(!p.getIsActive()) {
                int randX = rand.nextInt(Game.BUFFER_WIDTH - Platform.WIDTH);
                int chanceOfMoving = rand.nextInt(140 - currentDistanceInterval);
                p.setPosition(randX, currentLevelY, chanceOfMoving < 25);
                currentLevelY -= currentDistanceInterval;
            }
        }

        for(Asteroid a : asteroids){
            if(!a.getIsActive()){
                int randX = rand.nextInt(Game.BUFFER_WIDTH - Asteroid.WIDTH);
                int chanceOfSpawn = rand.nextInt(140 - currentDistanceInterval);
                if(chanceOfSpawn < 50) {
                    a.setPosition(randX, currentLevelY);
                    break;
                }
            }
        }
    }
}
