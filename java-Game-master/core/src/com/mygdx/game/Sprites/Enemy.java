package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;

public abstract class Enemy extends Sprite {//abstract because it will never be called or edited. can be used to make more enemies.
    protected World world;
    protected PlayScreen screen;
    public Body body;
    public Vector2 velocity;
    public Enemy(PlayScreen screen, float x , float y){
        this.world = screen.getWorld();
        this.screen = screen;
        defineEnemy(x , y);//defines ghost as enemy
        setPosition(x,y);//sets position
        velocity = new Vector2(2,0);//velocity for their movment
    }
    protected abstract void defineEnemy(float x , float y);

    public void reverseVelocity(boolean x, boolean y){//switches the x velocity
        if(x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }
}
