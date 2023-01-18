package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.Screens.PlayScreen;
public class Knight extends Sprite {
    //enum to see what player is doing
    public enum State {
        Falling,
        Jumping,
        Standing,
        Running,
        Swing,
    }
    //used for player state
    public State currentState;
    public State previousState;
    //makes world
    public World world;
    //box2d body
    public Body body;
    //loads in animations and textures
    private TextureRegion knightStand;
    private Animation<TextureRegion> knightRun;
    private Animation<TextureRegion> knightJump;
    private Animation<TextureRegion> knightSwing;
    //animation timer
    private float stateTimer;
    //direction check
    public boolean runningRight;
    //hit check
    public boolean swing;
    //swing timer
    int oldaction = 0;
    //hitbox for hit and player
    public Rectangle playerBox;
    public Rectangle swingBox;
    //used to single jump
    public boolean jumped = false;

    //character health
    public int hearts = 3;


    //constructor
    public Knight(PlayScreen screen) {
        //calling the atlas
        super(screen.getAtlas().findRegion("swing"));
        //gets world
        this.world = screen.getWorld();
        //sets states
        currentState = State.Standing;
        previousState = State.Standing;
        stateTimer = 0;
        //he is running right
        runningRight = true;
        //not swinging
        swing = false;
        //loads in all animations using atlas
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 9; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 255, 55, 74));;
        }
        knightRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 1; i <9; i++)
            frames.add(new TextureRegion(getTexture(),i*80,510,55,74));
        knightJump = new Animation<TextureRegion>(0.05f,frames);
        frames.clear();


        for (int i = 1; i < 6; i++) {
               frames.add(new TextureRegion(getTexture(), i * 80, 0, 58, 74));
        }
        knightSwing = new Animation<TextureRegion>(0.07f, frames);
        frames.clear();



        defineKnight();
        knightStand = new TextureRegion(getTexture(), 0, 255, 55, 74);
        setBounds(0, 0, 50 / Game.PPM, 74 / Game.PPM);
        setRegion(knightStand);
    }

    public void update(float deltaTime) {
        //changes position
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(deltaTime));
        //hitboxes
        box();
    }
    public void box(){
        //player position
        playerBox = new Rectangle(getX() , getY() , 55/Game.PPM , 74/Game.PPM);
        playerBox.setPosition(getX() , getY());
        //checks for direction to set swing box on a side
        if (runningRight) {
            swingBox = new Rectangle(getX()+(55/Game.PPM), getY(), 100/Game.PPM , 74/Game.PPM);
            swingBox.setPosition(getX()+(55/Game.PPM) , getY());

        }
        else{
            swingBox = new Rectangle(getX()-(100/Game.PPM), getY(), 100/Game.PPM , 74/Game.PPM);
            swingBox.setPosition(getX()-(100/Game.PPM) , getY());

        }
    }


    public TextureRegion getFrame(float deltaTime) {
        //chooses which texture region to use
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case Jumping://if he jumps his jumped is true till he lands
                jumped = true;
                region = knightJump.getKeyFrame(stateTimer);
                break;
            case Running:
                jumped = false;
                region = knightRun.getKeyFrame(stateTimer,true);
                break;
            case Swing:
                jumped = false;
                if(oldaction < 40) {
                    swing = true;
                }
                else {
                    oldaction = 0;
                    swing = false;
                }
                    region = knightSwing.getKeyFrame(stateTimer);
                    oldaction +=1;
                break;
            case Falling:
            case Standing:
            default://standing by default
                region = knightStand;
                jumped = false;
                break;
        }
        //checks to see if he is now running left
        if((body.getLinearVelocity().x < 0 || !runningRight)&& !region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        }
        else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        //statetimer to equal does our current state equal prev state if it does then statetimer + deltatime else set to 0 because it must of transitioned to another state to set to 0
        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }



    public State getState() {
        //checks what he us doing based on input
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)){
            swing = true;
            return State.Swing;
        }
        else if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y <0 && previousState == State.Jumping))//if his y is not changed
            return State.Jumping;
        else if(body.getLinearVelocity().y < 0)// if his y velocity is negative
            return State.Falling;
        else if(body.getLinearVelocity().x !=0 && !swing)//if his x changed
            return State.Running;
        else if(swing)
            return State.Swing;
        else
            return State.Standing;

    }

    public void defineKnight(){ //used for box2d physics
        BodyDef bodyDef = new BodyDef();//makes box2d body
        bodyDef.position.set(70/ Game.PPM,70/Game.PPM);//sets position for character
        bodyDef.type = BodyDef.BodyType.DynamicBody;//makes it a dynamic body
        body = world.createBody(bodyDef);//creates the body in the world
        FixtureDef fixtureDef = new FixtureDef();//makes a fixture
        CircleShape shape = new CircleShape();//makes it a circle
        shape.setRadius(15/Game.PPM);//set radius to 5
        fixtureDef.shape = shape;//sets the circle to shape
        body.createFixture(fixtureDef);//creates the circle

    }
}