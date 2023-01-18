package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.Screens.PlayScreen;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class Ghost extends Enemy {


    private float stateTime;//used for animations

    //texture regions for all animations
    private Animation<TextureRegion> ghostRun;
    private Animation<TextureRegion> ghostRunOther;
    private Animation<TextureRegion> ghostSwing;
    private Animation<TextureRegion> ghostSwingOther;
    private Animation<TextureRegion> death;
    private Animation<TextureRegion> deathOther;
    ///


    private Array<TextureRegion> frames;//texture regions for frames
    private boolean runningRight = true;//used to see if running right
    public int hitCooldown = 0;//doesnt spam swinging for enemy
    public boolean alive;//to see if alive
    public char State = 'r';//to see if running
    private int deathTimer = 50;//death timer for animation
    public boolean turn;//to see what direction hes turning



    public Ghost(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        turn = false;//hes not turned
        alive = true;//hes alive originally
        frames = new Array<TextureRegion>();

        //all loops cycle through pics in texture atlas and adds to texture  region for respective animation texture regions.
        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bigrun"), i * 140, 0, 120, 90));//i * x for each loop , x being the space between each picture.
        ghostRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bigrunother"), i * 140, 0, 120, 90));
        ghostRunOther = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 0; i < 7; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("enemySwing"), i * 140, 0, 135, 124));
        ghostSwing = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 0; i < 7; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Swingother1"), i * 140, 0, 135, 124));
        ghostSwingOther = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 0; i < 6; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bigded"), i * 100, 0, 90,85));
        death = new Animation<TextureRegion>(0.3f, frames);
        frames.clear();

        for (int i = 0; i < 6; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bigdedother"), i * 100, 0, 90, 85));
        deathOther = new Animation<TextureRegion>(0.3f, frames);
        frames.clear();

        stateTime = 0;

        setBounds(getX(), getY(), 170 / Game.PPM, 124 / Game.PPM);
    }

    public void update(float deltaTime, float x, float y , Knight knight) {
        stateTime += deltaTime;
        if (State == 'r') {//if ghost is moving right
            if (runningRight) {
                setRegion(ghostRun.getKeyFrame(stateTime, true));//set texture region to his right run sprties
            } else {
                setRegion(ghostRunOther.getKeyFrame(stateTime, true));//otherwise its left
            }

            //does the same thing for Swinging and death
        } else if (State == 's') {
            if (runningRight) {
                setRegion(ghostSwing.getKeyFrame(stateTime, true));
            } else {
                setRegion(ghostSwingOther.getKeyFrame(stateTime, true));
            }
        } else if (State == 'd') {
            deathTimer-=1;
            if (runningRight) {
                setRegion(death.getKeyFrame(stateTime,true));
            } else {
                setRegion(deathOther.getKeyFrame(stateTime,true));
            }
        }
        if (alive) {//if hes alive
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);//set his position of pic
            if (hitCooldown == 75)//hit cooldown so he cant spam
                State = 'r';//his state is running

            if (hitCooldown != 0) {
                hitCooldown--;//when the cooldown is 0 he can hit again
            }
            if (State == 'r') {//if hes alive and moving right
                move(x, y);//call move and his collision
                collide(knight);
            }
        }
        if(deathTimer ==0){
            State = 'n';//if hes dead loop through no animation texture region
        }
    }

    public void collide(Knight knight){
        //makes his hitbox
        Rectangle ghostBox = new Rectangle(getX(), getY(), 150 / Game.PPM, 124 / Game.PPM);
        ghostBox.setPosition(getX(), getY());
        //if the ghosthitbox overlaps with the players hitbox
        if(ghostBox.overlaps(knight.playerBox) && hitCooldown ==0){
            State = 's';//hes swinging
            if(knight.playerBox.getX()  < getX()+75/Game.PPM) {//if he hits player knock player back
                knight.body.applyLinearImpulse(new Vector2(-5, 2), knight.body.getWorldCenter(), true);
            }
            else {//same thing but knock backs other side
                knight.body.applyLinearImpulse(new Vector2(5, 2), knight.body.getWorldCenter(), true);
            }
            hitCooldown = 100;
            knight.hearts -=1;//subtract one health



        }
        if(knight.swingBox.overlaps(ghostBox) && knight.currentState.equals(Knight.State.Swing)){//if ghost gets hit
            alive = false;//he dies
            world.destroyBody(body);//destroy box2d object
            body = null;//destroys box2d object
            State = 'd';//state is dead
        }

    }
    @Override
    protected void defineEnemy(float x, float y) {
        BodyDef bodyDef = new BodyDef();//makes body for enemy
        FixtureDef fixtureDef = new FixtureDef();//makes fixture for enemy
        CircleShape shape = new CircleShape();//makes a circle
        bodyDef.position.set(x / Game.PPM, y / Game.PPM);//used to set the position of the body
        bodyDef.type = BodyDef.BodyType.DynamicBody;//makes it a dynamic body so it has the perks of a dynamic body
        body = world.createBody(bodyDef);//creates body
        shape.setRadius(50 / Game.PPM);//sets the radius so its bigger
        fixtureDef.shape = shape;//makes it the shape
        body.createFixture(fixtureDef);//creates the fixture
        }



    public void move(float start, float finish) {
        body.setLinearVelocity(velocity);//by default moving
        //if he hits the finish turn left until he hits start and then turn again and repeat....
        if (body.getPosition().x >= finish && !turn || body.getPosition().x <= start && !turn) {
            turn = true;
            if(runningRight){
                runningRight = false;
            }
            else{
                runningRight = true;
            }
            reverseVelocity(true, false);
            body.setLinearVelocity(velocity);

        }
        if(body.getPosition().x >= start && runningRight|| body.getPosition().x <= finish && !runningRight){
            turn = false;
        }

    }
}
