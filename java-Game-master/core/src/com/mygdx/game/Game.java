//GAME.java
//ALI GHOSN JADEN VARGEHSE
//2d sidescroller run around map and try to reach the end. kill all enemies to win.
package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Screens.PlayScreen;


public class Game extends com.badlogic.gdx.Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
	public SpriteBatch batch;//sprite batch to draw
	public static AssetManager manager;//for music

	@Override
	public void create() {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("bg.mp3", Music.class);//loads in background music
		manager.finishLoading();
		setScreen(new PlayScreen(this));//sets the screen

	}

	@Override
	public void render() {
		super.render();
	}


	@Override
	public void dispose() {

	}
}

