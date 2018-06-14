package com.reactwheel.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.reactwheel.game.objects.Wheel;

public class ReactWheelGame extends ApplicationAdapter {
	
	private Wheel wheel;

	@Override
	public void create () {
		System.out.println("Creating Game...");
		wheel = new Wheel();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(19/255f, 20/255f, 24/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		wheel.render();
		
	}
	
	@Override
	public void dispose () {
		wheel.dispose();
	}
}
