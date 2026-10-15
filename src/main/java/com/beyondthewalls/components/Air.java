package com.beyondthewalls.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.SensorCollisionHandler;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.beyondthewalls.utils.Direction;
import java.util.HashSet;
import java.util.Set;

public class Air extends Component {
    private final double width, height;
    private Direction direction;
    private static final int power = 200;

    /**
     * 当前处于气流区内、正在受力的实体。
     * <p>
     * 这里存 Entity 而非 Component：实体在碰撞中途被 removeFromWorld 时（例如
     * 玩家掉进水流被传送），传感器不保证回调 onCollisionEnd，直接缓存组件会留下
     * 已失效的强引用，每帧对已销毁的物理组件写速度。
     */
    private final Set<Entity> overlapping = new HashSet<>();

    public Air(double width, double height, int d) {
        this.width = width;
        this.height = height;

        switch (d) {
            case 1:
                direction = Direction.up;
                break;
            case 2:
                direction = Direction.down;
                break;
            case 3:
                direction = Direction.left;
                break;
            case 4:
                direction = Direction.right;
                break;
            default:
                // 关卡配置里写错了方向值。不抛异常终止游戏，只是该气流区不产生作用力。
                System.err.println("[BeyondtheWalls] 未知的 wind direction: " + d);
        }
    }

    @Override
    public void onAdded() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        physics.addSensor(new HitBox(BoundingShape.box(width, height)),
                new SensorCollisionHandler() {
                    @Override
                    protected void onCollisionBegin(Entity other) {
                        if (other.hasComponent(CatJump.class) || other.hasComponent(Player.class)) {
                            overlapping.add(other);
                        }
                    }

                    @Override
                    protected void onCollisionEnd(Entity other) {
                        overlapping.remove(other);
                    }
                });

        entity.addComponent(physics);
    }

    @Override
    public void onUpdate(double tpf) {
        if (overlapping.isEmpty() || direction == null) {
            return;
        }

        // 剔除已脱离世界的实体，避免传感器未回调 onCollisionEnd 造成的引用残留
        overlapping.removeIf(e -> !e.isActive());

        for (Entity e : overlapping) {
            if (e.hasComponent(Player.class)) {
                Player player = e.getComponent(Player.class);
                player.setCanMove(tpf * 2);

                switch (direction) {
                    case up:
                        player.pushY(-power * 0.5);
                        break;
                    case down:
                        player.pushY(power);
                        break;
                    case left:
                        player.pushX(-power);
                        break;
                    case right:
                        player.pushX(power);
                        break;
                }
            } else if (e.hasComponent(CatJump.class)) {
                CatJump cj = e.getComponent(CatJump.class);

                switch (direction) {
                    case up:
                        cj.pushY(-power);
                        break;
                    case down:
                        cj.pushY(power);
                        break;
                    case left:
                        cj.pushX(-power);
                        break;
                    case right:
                        cj.pushX(power);
                        break;
                }
            }
        }
    }
}
