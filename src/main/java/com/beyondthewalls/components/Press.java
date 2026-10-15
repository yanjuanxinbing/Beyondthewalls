package com.beyondthewalls.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.SensorCollisionHandler;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.beyondthewalls.utils.Type;

import java.util.Optional;

public class Press extends Component {
    private final double width, height;
    private final int id;

    public Press(double width, double height, int id) {
        this.width = width;
        this.height = height;
        this.id = id;
    }

    /**
     * 按 id 在当前关卡中查找对应的植物。
     * <p>
     * 刻意不使用静态注册表：静态表无法随关卡切换重置，会让压力板推上一个关卡
     * 遗留的、已脱离游戏世界的实体。此处每次从世界实时解析，天然免疫关卡切换，
     * 且找不到目标时静默跳过而不是抛 NPE。
     */
    private Optional<Plant> findPlant() {
        return FXGL.getGameWorld().getEntitiesByType(Type.pressPlatform).stream()
                .map(e -> e.getComponentOptional(Plant.class))
                .flatMap(Optional::stream)
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public void onAdded() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);
        physics.addSensor(new HitBox(BoundingShape.box(width, height)),
                new SensorCollisionHandler() {
                    @Override
                    protected void onCollisionBegin(Entity other) {
                        findPlant().ifPresent(Plant::move);
                    }
                });

        entity.addComponent(physics);
    }
}
