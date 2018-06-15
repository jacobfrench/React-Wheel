package com.reactwheel.game.objects;


import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


public class Wheel extends ShapeRenderer {
    private Vector2 center;
    private float radius;
    private Vector2 armPos;
    private float angle;
    private boolean isClockwise;
    private float dir;
    private Vector2 target;
    private double dist;
    private boolean gameRunning;

    private  Color BLUE = new Color(52/255f, 152/255f, 219/255f, 2f);
    private static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static final Color BACKGROUND = new Color(19/255f, 20/255f, 24/255f, 1f);
    private static final Color RED = new Color(231/255f, 76/255f, 60/255f, 1f);

    public Wheel(){
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        this.center = new Vector2(screenWidth / 2.0f, screenHeight-screenHeight/3f);
        this.radius = Gdx.graphics.getWidth() / 2.5f;
        this.armPos = new Vector2(center.x+radius/1.1f, center.y);
        this.angle = 0.0f;
        this.isClockwise = true;
        this.dir = 1.0f;
        this.target = new Vector2(center.x, center.y+radius/1.3f);
        this.dist = 0.0;
        this.gameRunning = false;
    


    }

    public void render(){
        update();
        this.begin(ShapeType.Filled);

        //outer ring
        this.setColor(BLUE);
        this.circle(center.x, center.y, radius, 100);

        //center circle
        this.setColor(BACKGROUND);
        this.circle(center.x, center.y, radius/1.1f, 100);

         //arm
         this.setColor(RED);
         this.rectLine(new Vector2(center.x,center.y), armPos, 25.0f);

        //center circle
        this.setColor(BLUE);
        this.circle(center.x, center.y, radius/1.6f, 100);
        this.circle(center.x, center.y, radius/1.6f, 100);

        renderTarget();

        this.end();

    }

    private void renderTarget(){
        this.setColor(BLUE);
        this.circle(target.x, target.y, 45.0f, 100);
        this.setColor(WHITE);
        this.circle(target.x, target.y, 30.0f, 100);
        this.setColor(RED);
        this.circle(target.x, target.y, 20.0f, 100);
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


    public void checkInput(){
        boolean touched = Gdx.input.justTouched();
        // if target successfully hit
        if(gameRunning){
            if(gameRunning && touched && targetWasHit(touched)){
                isClockwise = !isClockwise;
                Random rnd = new Random();
                float ang = 20.0f + rnd.nextFloat() * (360.0f - 20.0f);
                target = rotate(target, ang);
            }
            // if target missed
            else if(!targetWasHit(touched) && touched){
                this.gameRunning = false;
                this.angle = 0.0f;
                
            }

        }

        else if(!gameRunning ){
            if(touched){
                gameRunning = true;
                this.angle = 0.045f;
            }
        }
        
        


     
       
    }

    


    private boolean targetWasHit(boolean touched){
        this.dist = Math.abs(Math.sqrt(Math.pow((target.x - armPos.x), 2) + 
                    Math.pow((target.y - armPos.y), 2)));
        boolean isOnTarget = dist <= 45.0f*2.0f;
        

        if(isOnTarget && touched){
            return  true;
        }
        else if(!isOnTarget && touched){
            return false;
        }
        return false;
         
    }

    private void update(){
        checkInput();
        this.dir = (isClockwise) ? -1.0f : 1.0f;
        this.armPos = rotate(armPos, angle);
        
    }

    public void dispose(){
        this.dispose();
    }

}