package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Game;
import com.mygdx.game.Screens.PlayScreen;

public class WorldCreator {
    public WorldCreator(PlayScreen screen){
        //used to create world.
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;


        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            //defines the world
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX()+rect.getWidth()/2) / Game.PPM,(rect.getY()+rect.getHeight()/2)/Game.PPM);
            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth()/2/Game.PPM, rect.getHeight()/2/Game.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
            ///
        }



    }
}
