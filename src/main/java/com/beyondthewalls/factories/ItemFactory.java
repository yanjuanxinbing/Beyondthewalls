package com.beyondthewalls.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.beyondthewalls.components.*;
import com.beyondthewalls.utils.Type;

public class ItemFactory implements EntityFactory {

    @Spawns("barrier")
    public Entity newBarrier(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.barrier)
                .bbox(BoundingShape.box(width, height))
                .with(new Barrier())
                .build();
    }

    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return  FXGL.entityBuilder(data)
                .type(Type.wall)
                .bbox(BoundingShape.box(width, height))
                .with(new Item())
                .build();
    }

    @Spawns("box")
    public Entity newBox(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.box)
                .bbox(BoundingShape.box(width, height))
                .with(new Box())
                .build();
    }

    @Spawns("bed")
    public Entity newBed(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.bed)
                .bbox(BoundingShape.box(width, height))
                .with(new Bed())
                .build();
    }

    @Spawns("hairpin")
    public Entity newHairpin(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.hairpin)
                .bbox(BoundingShape.box(width, height))
                .with(new Item())
                .build();
    }

    @Spawns("treasure")
    public Entity newTreasure(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.treasure)
                .bbox(BoundingShape.box(width, height))
                .with(new Item())
                .build();
    }

    @Spawns("pressurePlate")
    public Entity newPress(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());
        int id = Integer.parseInt(data.get("id").toString());

        return FXGL.entityBuilder(data)
                .type(Type.pressurePlate)
                .with(new Press(width, height, id))
                .build();
    }

    @Spawns("end")
    public Entity newEnd(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.end)
                .with(new End(width, height))
                .build();
    }

    @Spawns("pressPlatform")
    public Entity newPlant(SpawnData data) {
        int id = Integer.parseInt(data.get("id").toString());
        int len = Integer.parseInt(data.get("len").toString());

        Plant plant = new Plant(id, data.get("tx"), data.get("ty"));

        return FXGL.entityBuilder(data)
                .type(Type.pressPlatform)
                .viewWithBBox("plant" + len + ".png")
                .with(plant)
                .build();
    }

    @Spawns("key")
    public Entity newKey(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.key)
                .bbox(BoundingShape.box(width, height))
                .with(new Item())
                .build();
    }

    @Spawns("lock")
    public Entity newLock(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.lock)
                .bbox(BoundingShape.box(width, height))
                .with(new Lock())
                .build();
    }

    @Spawns("secret")
    public Entity newSecret(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());
        int id = Integer.parseInt(data.get("id").toString());

        return FXGL.entityBuilder(data)
                .type(Type.secret)
                .bbox(BoundingShape.box(width, height))
                .with(new Secret(id))
                .build();
    }
}
