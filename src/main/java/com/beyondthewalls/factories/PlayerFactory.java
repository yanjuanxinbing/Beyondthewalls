package com.beyondthewalls.factories;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.beyondthewalls.components.CatJump;
import com.beyondthewalls.components.Player;
import com.beyondthewalls.utils.Type;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class PlayerFactory implements EntityFactory {
    private final Sound catSound = FXGL.getAssetLoader().loadSound("cat.wav");

    /**
     * 构建女孩实体。
     * <p>
     * 两个 spawn 仅精灵图不同，故抽成带前缀的公共方法：
     * <ul>
     *   <li>{@code girlNoPin} — 尚未拾取蝴蝶发卡（第 1 关）</li>
     *   <li>{@code girlWithPin} — 已将发卡别回发间（第 2 关起）</li>
     * </ul>
     * 两者共用 {@link Type#girl}，因为玩法上完全一致。
     */
    private Entity buildGirl(SpawnData data, String spritePrefix) {
        AnimationChannel wait = new AnimationChannel(
                FXGL.image(spritePrefix + "wait.png"),
                4,
                148 / 4,
                64,
                Duration.seconds(2),
                0,
                3
        );

        AnimationChannel run = new AnimationChannel(
                FXGL.image(spritePrefix + "run.png"),
                4,
                152 / 4,
                64,
                Duration.ONE,
                0,
                3
        );

        AnimationChannel jump = new AnimationChannel(
                FXGL.image(spritePrefix + "jump.png"),
                1,
                41,
                64,
                Duration.ZERO,
                0,
                0
        );

        AnimatedTexture at = new AnimatedTexture(wait);
        at.loop();

        Player player = new Player(true);
        player.statusProperty().addListener((o, p, newStatus) -> {
            switch (newStatus) {
                case waiting:
                    at.loopAnimationChannel(wait);
                    break;

                case moving:
                    at.loopAnimationChannel(run);
                    break;

                case jumping:
                    at.loopAnimationChannel(jump);
                    break;
            }
        });

        return FXGL.entityBuilder(data)
                .type(Type.girl)
                .view(at)
                .bbox(new HitBox(
                        new Point2D(4.5, 4),
                        BoundingShape.box(28, 56)
                ))
                .with(player)
                .build();
    }

    @Spawns("girlNoPin")
    public Entity newGirlNoPin(SpawnData data) {
        return buildGirl(data, "girl1");
    }

    @Spawns("girlWithPin")
    public Entity newGirlWithPin(SpawnData data) {
        return buildGirl(data, "girl2");
    }

    @Spawns("cat")
    public Entity newCat(SpawnData data) {
        AnimationChannel wait = new AnimationChannel(
                FXGL.image("cat1wait.png"),
                4,
                116/4,
                32,
                Duration.seconds(2),
                0,
                3
        );

        AnimationChannel run = new AnimationChannel(
                FXGL.image("cat1run.png"),
                3,
                96/3,
                32,
                Duration.ONE,
                0,
                2
        );

        AnimationChannel jump = new AnimationChannel(
                FXGL.image("cat1jump.png"),
                1,
                34,
                32,
                Duration.ZERO,
                0,
                0
        );

        AnimatedTexture at = new AnimatedTexture(wait);
        at.loop();

        Player player = new Player(false);
        player.statusProperty().addListener((o, p, newStatus) -> {
            switch (newStatus) {
                case waiting:
                    at.loopAnimationChannel(wait);
                    break;

                case moving:
                    at.loopAnimationChannel(run);
                    break;

                case jumping:
                    at.loopAnimationChannel(jump);
                    FXGL.getAudioPlayer().playSound(catSound);
                    break;
            }
        });

        return FXGL.entityBuilder(data)
                .type(Type.cat)
                .bbox(new HitBox(new Point2D(1.5, 3),
                        BoundingShape.circle(13)))
                .view(at)
                .with(player)
                .build();
    }

    @Spawns("catJumper")
    public Entity newCatJumper(SpawnData data) {
        AnimationChannel wait = new AnimationChannel(
                FXGL.image("cat1wait.png"),
                4,
                116/4,
                32,
                Duration.seconds(2),
                0,
                3
        );

        AnimationChannel jumpBegin = new AnimationChannel(
                FXGL.image("cat1jumpBegin.png"),
                1,
                34,
                32,
                Duration.ZERO,
                0,
                0
        );

        AnimationChannel inAir = new AnimationChannel(
                FXGL.image("cat1jump.png"),
                1,
                34,
                32,
                Duration.ZERO,
                0,
                0
        );

        AnimatedTexture at = new AnimatedTexture(wait);
        at.loop();

        CatJump cj = new CatJump();
        cj.statusProperty().addListener((o, p, newStatus) -> {
            switch (newStatus) {
                case waiting:
                    at.loopAnimationChannel(wait);
                    FXGL.getGameScene().getViewport().shakeTranslational(10);
                    break;
                case jumping:
                    FXGL.getAudioPlayer().playSound(catSound);
                    at.loopAnimationChannel(inAir);
                    break;
                case jumpBegin:
                    at.loopAnimationChannel(jumpBegin);
                    break;
            }
        });

        Entity cat2 = FXGL.entityBuilder(data)
                .type(Type.catJumper)
                .bbox(new HitBox(new Point2D(1.5, 3),
                        BoundingShape.circle(13)))
                .view(at)
                .with(cj)
                .build();

        FXGL.getGameScene().getViewport().bindToEntity(cat2, 192, 512);

        return cat2;
    }
}
