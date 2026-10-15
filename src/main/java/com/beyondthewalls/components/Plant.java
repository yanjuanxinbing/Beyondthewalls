package com.beyondthewalls.components;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;

public class Plant extends Component {
    private final int id;
    private final Point2D target;
    private boolean isMoving = false;
    private static final double MOVE_SPEED = 10;

    public Plant(int id, int x2, int y2) {
        this.id = id;
        target = new Point2D(x2, y2);
    }

    public int getId() {
        return id;
    }

    @Override
    public void onAdded() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        FixtureDef td = new FixtureDef();
        td.friction(0);
        physics.setFixtureDef(td);

        CollidableComponent collidable = new CollidableComponent(true);

        entity.addComponent(physics);
        entity.addComponent(collidable);
    }

    public void move() {
        isMoving = true;
    }

    @Override
    public void onUpdate(double tpf) {
        if (!isMoving) return;

        Point2D currentPos = entity.getPosition();

        double distance = target.distance(currentPos);

        if (distance < 1) {
            PhysicsComponent physics = entity.getComponent(PhysicsComponent.class);
            physics.setLinearVelocity(0, 0);
            isMoving = false;
            return;
        }

        Vec2 direction = new Vec2(
                (target.getX() - currentPos.getX()),
                (target.getY() - currentPos.getY())
        );

        direction = direction.normalize();
        PhysicsComponent physics = entity.getComponent(PhysicsComponent.class);
        physics.setLinearVelocity(direction.mul(MOVE_SPEED).toPoint2D());
    }
}
