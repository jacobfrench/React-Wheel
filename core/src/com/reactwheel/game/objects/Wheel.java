package com.reactwheel.game.objects;


import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import sun.rmi.runtime.Log;


public class Wheel extends ShapeRenderer {
    private Vector2 center;
    private float radius;
    private Vector2 arm;
    private float angle;
    private boolean isClockwise;
    private float dir;
    private Vector2 target;
    private Vector2 prevTarget;
    private double dist;
    private boolean gameRunning;
    private boolean targetHit;
    private int score;
    private BitmapFont scoreFont;
    private BitmapFont messageFont;
    private boolean targetWasInRange;

    private static final Color BLUE = new Color(52/255f, 152/255f, 219/255f, 1f);
    private static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static final Color BACKGROUND = new Color(38/255f, 50/255f, 56/255f, 1f);
    private static final Color RED = new Color(231/255f, 76/255f, 60/255f, 1f);

    // animation
    private SpriteBatch batch;
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;

    private Animation<TextureRegion> explosionAnimation;
    private Texture explosionSheet;

    float stateTime;


    public Wheel(SpriteBatch batch){
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        center = new Vector2(screenWidth / 2.0f, screenHeight-screenHeight/3.0f);
        radius = Gdx.graphics.getWidth() / 2.5f;
        arm = new Vector2(center.x+radius/1.1f, center.y);
        angle = 0.0f;
        isClockwise = true;
        dir = 1.0f;
        target = new Vector2(center.x, center.y+radius/1.3f);
        dist = 0.0;
        gameRunning = false;
        this.batch = batch;
        targetHit = false;
        targetWasInRange = false;
        prevTarget = new Vector2();
        scoreFont = new BitmapFont();
        scoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                                                     Texture.TextureFilter.Linear);

        scoreFont.getData().setScale(6.0f);
        score = 0;
        messageFont = new BitmapFont();
        messageFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                                                       Texture.TextureFilter.Linear);

        messageFont.getData().setScale(6.0f);


        initAnimation();

    }



    private void initAnimation(){
        explosionSheet = new Texture("explosion.png");

        TextureRegion[][] tmp = TextureRegion.split(explosionSheet,
                                explosionSheet.getWidth()/ FRAME_COLS,
                                explosionSheet.getHeight()/ FRAME_ROWS);

        TextureRegion[] explosionFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for(int i=0; i < FRAME_ROWS; i++){
            for(int j=0; j < FRAME_COLS; j++){
                explosionFrames[index++] = tmp[i][j];
            }
        }

        explosionAnimation = new Animation<TextureRegion>(0.0055f, explosionFrames);
        stateTime = 0.0f;

    }

    public void render(){
        update();

        begin(ShapeType.Filled);

        //outer ring
        setColor(BLUE);
        circle(center.x, center.y, radius, 100);

        setColor(BACKGROUND);
        circle(center.x, center.y, radius/1.1f, 100);

        //arm
        setColor(RED);
        rectLine(new Vector2(center.x,center.y), arm, 25.0f);

        //center circles
        setColor(BLUE);
        circle(center.x, center.y, radius/1.6f, 100);
        circle(center.x, center.y, radius/1.6f, 100);

        setColor(WHITE);
        circle(center.x, center.y, radius/2.0f, 100);
        circle(center.x, center.y, radius/2.0f, 100);

        setColor(RED);
        circle(center.x, center.y, radius/2.5f, 100);
        circle(center.x, center.y, radius/2.5f, 100);

        renderTarget();
        end();

        batch.begin();
        if(targetHit){
            showExplosion();
        }

        //show score on screen
        scoreFont.draw(batch, String.valueOf(score), center.x, center.y);

        if(!gameRunning)
                messageFont.draw(batch, "Tap Screen To Play!",
                         Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/4);

        batch.end();

    }

    private void showExplosion(){
        TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, false);

        if(!explosionAnimation.isAnimationFinished(stateTime)){
            stateTime += Gdx.graphics.getDeltaTime();
            batch.draw(currentFrame, (int)prevTarget.x-256, (int)prevTarget.y-256);
            if(explosionAnimation.isAnimationFinished(stateTime)){
                targetHit = false;
                stateTime = 0.0f;
            }
        }

    }

    private void renderTarget(){
        setColor(BLUE);
        circle(target.x, target.y, 45.0f, 100);
        setColor(WHITE);
        circle(target.x, target.y, 30.0f, 100);
        setColor(RED);
        circle(target.x, target.y, 20.0f, 100);

    }

    private Vector2 rotate(Vector2 p, float theta){
        float s = (float) Math.sin(theta);
        float c = (float) Math.cos(theta);

        p.x -= center.x;
        p.y -= center.y;

        float xNew = (p.x * c -dir*p.y * s);
        float yNew = (dir*p.x * s + p.y * c);
        
        p.x = (xNew + center.x);
        p.y = (yNew + center.y);

        return p;

    }


    public void checkInput() {
        boolean touched = Gdx.input.justTouched();

        if(dist > 80.0 && dist < 90.0)
            targetWasInRange = true;




        if(gameRunning){
            if(touched && targetInRange()){
                targetWasInRange = false;
                isClockwise = !isClockwise;
                targetHit = true;
                prevTarget = new Vector2(target.x, target.y);
                Random rnd = new Random();
                float ang = 0.0f + rnd.nextFloat() * (360.0f - 0.0f);
                score++;
                target = rotate(target, ang);

            } else if(touched && !targetInRange()){
                stopGame();

            } else if(!touched && targetWasInRange && dist > 100){
                stopGame();
            }


        }else{
            if(touched) {
                startGame();

            }else{
                resetGame();

            }

        }

    }

    private boolean targetInRange(){
        float hitRange = 45.0f*2.0f;
        boolean inRange = dist <= hitRange;

        return inRange;

    }

    private void stopGame(){
        gameRunning = false;
        angle = 0.0f;
    }

    private void startGame(){
        gameRunning = true;
        angle = 0.045f;
        score = 0;
    }

    private void resetGame(){
        //reset everything when game is lost
        arm.x = center.x+radius/1.1f;
        arm.y = center.y;
        target.x = center.x;
        target.y = center.y+radius/1.3f;
        isClockwise = true;
        targetWasInRange = false;
    }



    private void update(){
        checkInput();
        dir = (isClockwise) ? -1.0f : 1.0f;
        arm = rotate(arm, angle);

        //track distance between arm and target
        dist = Math.sqrt(Math.pow((target.x - arm.x), 2) +
                Math.pow((target.y - arm.y), 2));
        
    }

    public void dispose(){
        this.dispose();

    }

}