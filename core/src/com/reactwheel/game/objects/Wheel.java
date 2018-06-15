package com.reactwheel.game.objects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

    private  Color BLUE = new Color(52/255f, 152/255f, 219/255f, 1);
    private static final Color BACKGROUND = new Color(19/255f, 20/255f, 24/255f, 1);
    private static final Color RED = new Color(231/255f, 76/255f, 60/255f, 1);
    private static final Color GOLD = new Color(241/255f, 196/255f, 15/255f, 1);

    public Wheel(){
        this.center = new Vector2(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 3.0f);
        this.radius = Gdx.graphics.getWidth() / 2.5f;
        this.armPos = new Vector2(center.x+radius, center.y);
        this.angle = 0.03f;
        this.isClockwise = true;
        this.dir = 1.0f;
        this.target = new Vector2(center.x, center.y+radius-20.0f);
        this.dist = 0.0;

    }

    public void render(){
        update();
        this.begin(ShapeType.Filled);

        //outer ring
        this.setColor(BLUE);
        this.circle(center.x, center.y, radius, 100);

        //center circle
        this.setColor(BACKGROUND);
        this.circle(center.x, center.y, radius-50.0f, 100);

        //center dot
        this.setColor(RED);
        this.circle(center.x, center.y, 15.0f, 100);

        renderTarget();

        //arm
        this.setColor(RED);
        this.rectLine(new Vector2(center.x,center.y), armPos, 35.0f);

        
        this.end();

    }

    private void renderTarget(){
        this.setColor(GOLD);
        this.circle(target.x, target.y, 35.0f);
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
        if(touched && targetWasHit()){
            isClockwise = !isClockwise;
            Random rnd = new Random();
            float ang = 20.0f + rnd.nextFloat() * (360.0f - 20.0f);
            target = rotate(target, ang);
        }else if (touched && !targetWasHit()){
            BLUE = Color.RED; //temporary
        }
       
    }

    private boolean targetWasHit(){
        this.dist = Math.sqrt(Math.pow((target.x - armPos.x), 2) + 
                    Math.pow((target.y - armPos.y), 2));
        return (dist < 100.0f) ? true : false;
         
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