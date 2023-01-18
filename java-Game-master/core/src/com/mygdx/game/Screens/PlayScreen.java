package com.mygdx.game.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Game;
import com.mygdx.game.Sprites.Enemy;
import com.mygdx.game.Sprites.Ghost;
import com.mygdx.game.Sprites.Knight;
import com.mygdx.game.Tools.WorldCreator;
import org.graalvm.util.GuardedAnnotationAccess;
import space.earlygrey.shapedrawer.ShapeDrawer;
import sun.jvm.hotspot.runtime.ppc64.PPC64CurrentFrameGuess;

public class PlayScreen implements Screen {
    private Game game;//game variable
    private Texture texture;//variable  for texture
    private OrthographicCamera gamecamera;//our game camera
    private TextureAtlas atlas;//atlas holding all our textures
    //private float elapsedTime = 0f;
    private Viewport gameport;//viewport used for camera
    private TmxMapLoader mapLoader;//used for the tilemap
    private TiledMap map;//makes new tilemap
    private OrthogonalTiledMapRenderer renderer;//makes a renderer for map
    Knight player;//main character calling
    private Ghost ghost;//calls ghost
    private Sprite sprite;//makes a sprite
    private int dead;
    //all ghosts
    private Ghost ghost1;
    private Ghost ghost2;
    private Ghost ghost3;
    private Ghost ghost4;
    private Ghost ghost5;
    private Ghost ghost6;
    private Ghost ghost7;
    private Ghost ghost8;
    private Ghost ghost9;
    private Ghost ghost10;
    private Ghost ghost11;
    private Ghost ghost12;
    private Ghost ghost13;
    private Ghost ghost14;
    private Ghost ghost15;
    private Ghost ghost16;
    private Ghost ghost17;
    private Ghost ghost18;
    private Ghost ghost19;
    private Ghost ghost20;
    private Ghost ghost21;
    //textures for hearts
    private Texture heart;
    private Sprite healthHeart1;
    private Sprite healthHeart2;
    private Sprite healthHeart3;
    private Sprite StartScreen;
    private Ghost[] ghostarray;//array that holds all enemies
    private char screen;//used for start/main/end screen
    //loads different screens
    private Texture mainscreen;
    private Texture lossScreen;
    private Sprite LossScreen;
    private Texture winScreen;
    private Sprite WinScreen;
    private Music music;

    //box2d
    private World world;//new world
    private Box2DDebugRenderer box2DDebugRenderer;//used to render the box2d bodies

    public PlayScreen(Game game){
        //sdas
        this.game = game;
        music = Game.manager.get("bg.mp3",Music.class);
        music.setLooping(true);
        music.play();
        screen = 't';//for title screen
        //loading in and setting location / sizes for sprites / textures
        texture = new Texture(Gdx.files.internal("Background.png"));
        mainscreen = new Texture(Gdx.files.internal("MAINSCREEN.png"));
        lossScreen = new Texture(Gdx.files.internal("loser.png"));
        LossScreen = new Sprite(lossScreen,0,0,1280,1100);
        LossScreen.setSize(12.8f, 11f);
        LossScreen.setPosition(-1,-2.5f);
        winScreen = new Texture(Gdx.files.internal("WINNER.png"));
        WinScreen = new Sprite(winScreen,0,0,1280,1100);
        WinScreen.setSize(12.8f, 11f);
        WinScreen.setPosition(-1,-2.5f);
        StartScreen = new Sprite(mainscreen,0,0,1280,1100);
        sprite = new Sprite(texture,0,-50,928,793);
        heart = new Texture(Gdx.files.internal("Heart.png"));
        healthHeart1 = new Sprite(heart,0,0,20,20);
        healthHeart2 = new Sprite(heart,0,0,20,20);
        healthHeart3 = new Sprite(heart,0,0,20,20);
        healthHeart1.setSize(0.2f, 0.2f);
        healthHeart2.setSize(0.2f, 0.2f);
        healthHeart3.setSize(0.2f, 0.2f);
        healthHeart1.setPosition(0.1f,4.6f);
        healthHeart2.setPosition(0.2f,4.6f);
        healthHeart3.setPosition(0.4f,4.6f);
        sprite.setSize(2000/Game.PPM,900/Game.PPM);
        //sets up camera and all of rendering
        gamecamera = new OrthographicCamera();//game camera that follows player
        gameport = new StretchViewport(800/Game.PPM,480/Game.PPM,gamecamera);//used for camera
        world = new World(new Vector2(0,-10), true);//used to set the gravity in the world
        box2DDebugRenderer = new Box2DDebugRenderer();//makes new renderer for box2d objects
        atlas = new TextureAtlas(Gdx.files.internal("KnightR.atlas"));//loads in atlas
        mapLoader = new TmxMapLoader();//used to load in tilemap
        map = mapLoader.load("MainTile.tmx");//tilemap
        renderer = new OrthogonalTiledMapRenderer(map,1/Game.PPM);//makes renderer to render tilemap
        gamecamera.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0);//sets gamecamera position
        player = new Knight(this);//calls player
        //calls all the ghosts
        ghost = new Ghost(this, 501f, 50);
        ghost2 = new Ghost(this, 1510f, 250);
        ghost3 = new Ghost(this, 2730f, 350);
        ghost4 = new Ghost(this, 2710f, 160);
        ghost5 = new Ghost(this, 3200f, 50);//
        ghost6 = new Ghost(this, 3660f, 80);
        ghost7 = new Ghost(this, 3350f, 26);
        ghost8 = new Ghost(this, 5400, 303);
        ghost9 = new Ghost(this, 5370f, 80);
        ghost10 = new Ghost(this, 5730f, 20);
        ghost11 = new Ghost(this, 6300f, 100);
        ghost12 = new Ghost(this, 6600f, 100);
        ghost13 = new Ghost(this, 8200f, 20);
        ghost14 = new Ghost(this, 8710f, 20);
        ghost15 = new Ghost(this, 9230f, 20);
        ghost16 = new Ghost(this, 9720f, 20);
        ghost17 = new Ghost(this, 10700f, 140);
        ghost18 = new Ghost(this, 11760f, 250);
        ghost19 = new Ghost(this, 12400f, 150);
        ghost20= new Ghost(this, 12380f, 355);
        ghost21= new Ghost(this, 12720f, 355);
        ghostarray = new Ghost[]{ghost,ghost2,ghost3,ghost4,ghost5,ghost6,ghost7,ghost8,ghost9,ghost10,ghost11,ghost12,ghost13,ghost14,ghost15,ghost16,ghost17,ghost18,ghost19,ghost20,ghost21};

        new WorldCreator(this);
    }
    @Override
    public void show() {

    }
    public void handleInput(float deltaTime) {
        //checks for jumping
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)&& !player.jumped)
            player.body.applyLinearImpulse(new Vector2(0, 6f), player.body.getWorldCenter(), true);
        //checks for sword swing
        if(!player.swing) {
            //checks which direction
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2)
                player.body.applyLinearImpulse(new Vector2(0.5f, 0), player.body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2)
                player.body.applyLinearImpulse(new Vector2(-0.5f, 0), player.body.getWorldCenter(), true);
        }
        //for title screen
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && screen == 't'){
            screen = 'g';

        }


    }


    public void update(float deltaTime){
        //checks for inputs
        handleInput(deltaTime);
        world.step(1/60f,6,2);
        //updates player
        player.update(deltaTime);
        //updates all the ghosts
        ghost.update(deltaTime, 5f , 6.5f, player);
        ghost2.update(deltaTime,15f,23.4f,player);
        ghost3.update(deltaTime, 27f , 28.4f, player);
        ghost4.update(deltaTime , 26.6f,30.1f,player);
        ghost5.update(deltaTime,31.5f,33.5f,player);
        ghost6.update(deltaTime, 35f , 39f, player);
        ghost7.update(deltaTime , 37f,37.5f,player);
        ghost8.update(deltaTime,53.1f,58.9f,player);
        ghost9.update(deltaTime, 52.5f , 55f, player);
        ghost10.update(deltaTime , 57f,59.8f,player);
        ghost11.update(deltaTime,60.9f,64f,player);
        ghost12.update(deltaTime,65f,70f,player);
        ghost13.update(deltaTime,81.8f,85f,player);
        ghost14.update(deltaTime,88f,90f,player);
        ghost15.update(deltaTime,93f,95.3f,player);
        ghost16.update(deltaTime,98f,100f,player);
        ghost17.update(deltaTime,106f,113f,player);
        ghost18.update(deltaTime,116f,121f,player);
        ghost19.update(deltaTime,123.5f,145.5f,player);
        ghost20.update(deltaTime,123.3f,125f,player);
        ghost21.update(deltaTime,126.4f,127.8f,player);
        //updates camera
        gamecamera.update();
        renderer.setView(gamecamera);
        gamecamera.position.x = player.body.getPosition().x;
        //checks for hp
        hearts();



    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public void hearts(){
        //sets the hearts
        healthHeart1.setPosition(gamecamera.position.x-4,4.6f);
        healthHeart2.setPosition(gamecamera.position.x-3.8f,4.6f);
        healthHeart3.setPosition(gamecamera.position.x-3.6f,4.6f);

    }

    @Override
    public void render(float delta) {
        //checks if you have won the game
        for (int i = 0; i < ghostarray.length; i++) {
            if (!ghostarray[i].alive) {
                dead ++;
            }
        }
        System.out.println(dead);
        System.out.println(ghostarray.length);
        if(dead == ghostarray.length-1)
            screen = 'w';
        else
            dead = 0;
        //if the player is currently in the main game
        if(screen == 'g') {
            if(player.hearts <= 0){
                screen = 'l';
            }
            update(delta);
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            game.batch.begin();
            game.batch.setProjectionMatrix(gamecamera.combined);
            renderer.render();
            //used to see box2d objects
            //box2DDebugRenderer.render(world, gamecamera.combined);
            //decides how many hearts to draw
            if (player.hearts >= 1) {
                healthHeart1.draw(game.batch);
            }
            if (player.hearts >= 2) {
                healthHeart2.draw(game.batch);
            }
            if (player.hearts >= 3) {
                healthHeart3.draw(game.batch);
            }
            //draws all alive ghosts
            for (int i = 0; i < ghostarray.length; i++) {
                if (ghostarray[i].alive || ghostarray[i].State == 'd')
                    ghostarray[i].draw(game.batch);
            }
            //draws player
            player.draw(game.batch);
            game.batch.end();
        }
        //used to draw title screen
        else if(screen == 't'){
            handleInput(delta);
            game.batch.begin();
            StartScreen.draw(game.batch);
            game.batch.end();


        }
        //used to draw lose screen
        else if(screen == 'l'){
            game.batch.begin();
            LossScreen.draw(game.batch);
            game.batch.end();
        }
        //used to draw win screen
        else if(screen == 'w'){
            game.batch.begin();
            WinScreen.draw(game.batch);
            game.batch.end();
        }

    }
    @Override
    public void resize(int width, int height) {
        gameport.update(width,height);//used to resize gameport.

    }
    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {//good coders dispose to save memory!!!
        atlas.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();

    }
}
