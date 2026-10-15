package com.beyondthewalls.components;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.beyondthewalls.utils.Status;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;

public class Player extends Component {
    private PhysicsComponent physics;
    private final int powerX, powerY;
    private final SimpleObjectProperty<Status> status;
    private double canJump = 0;
    private double canMove = 0;
    private final Sound duang = FXGL.getAssetLoader().loadSound("duang.wav");

    public Player(boolean isGirl) {
        powerX = isGirl ? 96 : 144;
        powerY = isGirl ? -300 : -250;
        status = new SimpleObjectProperty<>(Status.waiting);
    }

    @Override
    public void onAdded() {
        physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        entity.addComponent(physics);
        entity.addComponent(new CollidableComponent(true));
    }

    @Override
    public void onUpdate(double tpf) {
        double velocityX = physics.getVelocityX();
        double velocityY = physics.getVelocityY();

        if (Math.abs(velocityY) > 0.01) {
            status.set(Status.jumping);

            if (velocityX > 0) {
                entity.setScaleX(-1);
            } else {
                entity.setScaleX(1);
            }
        }
        else if (Math.abs(velocityX) > 0.01) {
            status.set(Status.moving);

            if (velocityX > 0) {
                entity.setScaleX(-1);
            } else {
                entity.setScaleX(1);
            }
        }
        else {
            status.set(Status.waiting);
        }

        if (canJump > 0) {
            canJump -= tpf;
            if (canJump < 0) {
                canJump = 0;
            }
        }

        if (canMove > 0) {
            canMove -= tpf;
            if (canMove < 0) {
                canMove = 0;
            }
        }
    }

    public void stop() {
        physics.setVelocityX(0);
    }

    private boolean canMove() {
        return status.get() != Status.jumping || canMove > 0;
    }

    private boolean canJump() {
        return status.get() != Status.jumping || canJump > 0;
    }

    public void tryLeft() {
        if (canMove()) {
            physics.setVelocityX(-powerX);
        }
    }

    public void tryRight() {
        if (canMove()) {
            physics.setVelocityX(powerX);
        }
    }

    public void tryJump() {
        if (canJump()) {
            physics.setVelocityY(powerY);
        }
    }

    public void pushX(double power) {
        physics.setVelocityX(power);
    }

    public void pushY(double power) {
        physics.setVelocityY(power);
    }

    public void translate(Point2D target) {
        entity.removeFromWorld();
        if (powerX == 96) {
            FXGL.spawn("girlWithPin", target);
        } else {
            FXGL.spawn("cat", target);
        }
    }

    public SimpleObjectProperty<Status> statusProperty() {
        return status;
    }

    public void setOnBed() {
        canJump = 0.15;
        canMove = 0.5;
        FXGL.getAudioPlayer().playSound(duang);
    }

    public void setCanMove(double time) {
        canMove += time;
    }
}
