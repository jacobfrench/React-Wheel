package com.reactwheel.game.objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


public class Wheel extends ShapeRenderer implements InputProcessor{
    private float wheelCenterX;
    private float wheelCenterY;
    private float radius;
    private Vector2 armPos;
    private float angle;
    private boolean isClockwise;
    private float dir;

    private static final Color WHITE = new Color(236/255f, 240/255f, 241/255f, 1);
    private static final Color BACKGROUND = new Color(19/255f, 20/255f, 24/255f, 1);
    private static final Color RED = new Color(231/255f, 76/255f, 60/255f, 1);

    public Wheel(){
        this.wheelCenterX = Gdx.graphics.getWidth() / 2.0f;
        this.wheelCenterY = Gdx.graphics.getHeight() / 3.0f;
        this.radius = Gdx.graphics.getWidth() / 2.5f;
        this.armPos = new Vector2(wheelCenterX+radius, wheelCenterY);
        this.angle = 0.02f;
        this.isClockwise = true;
        this.dir = 1.0f;

    }


    public void render(){
        update();
        this.begin(ShapeType.Filled);

        //outer ring
        this.setColor(WHITE);
        this.circle(wheelCenterX, wheelCenterY, radius);

        //center circle
        this.setColor(BACKGROUND);
        this.circle(wheelCenterX, wheelCenterY, radius-50.0f);

        //center dot
        this.setColor(RED);
        this.circle(wheelCenterX, wheelCenterY, 15.0f);

        //arm
        this.setColor(RED);
        this.rectLine(new Vector2(wheelCenterX,wheelCenterY), armPos, 35.0f);
        

        this.end();



    }

    private void rotate(){
        float dir = -1.0f;
        float s = (float)Math.sin(this.angle);
        float c = (float)Math.cos(this.angle);

        armPos.x -= wheelCenterX;
        armPos.y -= wheelCenterY;

        float xNew = (armPos.x * c -dir*armPos.y * s);
        float yNew = (dir*armPos.x * s + armPos.y * c);
        
        armPos.x = (xNew + wheelCenterX);
        armPos.y = (yNew + wheelCenterY);


        
    }

    private void update(){
        if(isClockwise)
            dir = -dir;
        
        if(Gdx.input.justTouched())
			dir = -dir;

        rotate();
        
    }

 


    public void dispose(){
        this.dispose();
    }


	@Override
	public boolean keyDown(int keycode) {
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.dir = -dir;
		return true;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		return false;
	}



}