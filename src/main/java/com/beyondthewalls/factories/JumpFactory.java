package com.beyondthewalls.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.beyondthewalls.components.*;
import com.beyondthewalls.utils.Type;

public class JumpFactory implements EntityFactory {
    @Spawns("lift")
    public Entity newLift(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.lift)
                .bbox(BoundingShape.box(width, height))
                .with(new Bat())
                .build();
    }

    @Spawns("patrolPlatform")
    public Entity newPatrolPlatform(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.patrolPlatform)
                .bbox(BoundingShape.box(width, height))
                .with(new Plant2())
                .build();
    }

    /**
     * 与 {@link #newPatrolPlatform} 行为完全相同，区别只在于带贴图。
     * <p>
     * TODO 二者本应合并：level5 中 23 个 patrolPlatform 在 Tiled 里都配了
     * {@code len} 属性（2/3/4），但本 spawn 既没读 {@code len} 也没有 view，
     * 导致这些平台在游戏中完全不可见。合并前需先确认它们应有的碰撞尺寸。
     */
    @Spawns("patrolPlatformVisible")
    public Entity newPatrolPlatformVisible(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(Type.patrolPlatform)
                .viewWithBBox("plant2.png")
                .with(new Plant2())
                .build();
    }

    @Spawns("spike")
    public Entity newSpike(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());

        return FXGL.entityBuilder(data)
                .type(Type.hazard)
                .bbox(BoundingShape.box(width, height))
                .collidable()
                .build();
    }

    @Spawns("water")
    public Entity newWater(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());
        int tx = Integer.parseInt((data.get("tx").toString()));
        int ty = Integer.parseInt((data.get("ty").toString()));

        return FXGL.entityBuilder(data)
                .type(Type.water)
                .bbox(BoundingShape.box(width, height))
                .with(new Water(tx, ty))
                .build();
    }

    @Spawns("air")
    public Entity newAir(SpawnData data) {
        double width = Double.parseDouble(data.get("width").toString());
        double height = Double.parseDouble(data.get("height").toString());
        int d = data.get("direction");

        return FXGL.entityBuilder(data)
                .with(new Air(width, height, d))
                .build();
    }

    @Spawns("energy")
    public Entity newEnergy(SpawnData data) {
        return FXGL.entityBuilder(data)
                .with(new Energy())
                .build();
    }
}
