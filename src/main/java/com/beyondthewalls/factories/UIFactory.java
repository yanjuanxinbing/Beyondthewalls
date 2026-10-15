package com.beyondthewalls.factories;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.beyondthewalls.Main;
import com.beyondthewalls.ui.SettingsPage;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class UIFactory implements EntityFactory {
    private final SettingsPage settingsPage = new SettingsPage();
    private final Music music = FXGL.getAssetLoader().loadMusic("bgm.mp3");

    @Spawns("bg")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(FXGL.texture("bg.png"))
                .build();
    }

    @Spawns("loading")
    public Entity newLoading(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(FXGL.texture("loading.png"))
                .build();
    }

    @Spawns("start")
    public Entity newStart(SpawnData data) {
        Entity button = FXGL.entityBuilder(data)
                .view("start.png")
                .build();

        button.getViewComponent().addOnClickHandler(o -> {
            FXGL.set("level", 1);
            FXGL.set("secret1", false);
            FXGL.set("secret2", false);
            FXGL.set("secret3", false);

            FXGL.spawn("bg");
            Main.newText("open.txt");
            FXGL.runOnce(()-> {
                FXGL.setLevelFromMap("level1.tmx");
                FXGL.getAudioPlayer().stopAllMusic();
                FXGL.getAudioPlayer().loopMusic(music);
            }, Duration.ZERO);
        });

        return button;
    }

    @Spawns("load")
    public Entity newLoad(SpawnData data) {
        Entity button = FXGL.entityBuilder(data)
                .view("load.png")
                .build();

        button.getViewComponent().addOnClickHandler(o -> {
            FXGL.spawn("loading");
            FXGL.getGameTimer().runOnceAfter(() -> {
                int curLevel = FXGL.geti("level");
                String level = "level" + curLevel + ".tmx";
                FXGL.getNotificationService().pushNotification("当前进度为：level" + curLevel);
                FXGL.runOnce(() -> {
                    FXGL.setLevelFromMap(level);
                    FXGL.getAudioPlayer().stopAllMusic();
                    FXGL.getAudioPlayer().loopMusic(music);
                }, Duration.ZERO);
            }, Duration.seconds(3));
        });

        return button;
    }

    @Spawns("set")
    public Entity newSet(SpawnData data) {
        Entity button = FXGL.entityBuilder(data)
                .view("set.png")
                .build();

        button.getViewComponent().addOnClickHandler(o -> FXGL.getSceneService().pushSubScene(settingsPage));

        return button;
    }

    @Spawns("restart")
    public Entity newRestart(SpawnData data) {
        Entity button = FXGL.entityBuilder(data)
                .view("restart.png")
                .build();

        FXGL.set("nextLevel", data.get("nextLevel"));

        button.getViewComponent().addOnClickHandler(o -> {
            FXGL.spawn("loading");
            FXGL.getGameTimer().runOnceAfter(() -> FXGL.runOnce(() -> FXGL.setLevelFromMap(data.get("level")), Duration.ZERO), Duration.seconds(3));
        });

        return button;
    }

    @Spawns("title")
    public Entity newTitle(SpawnData data) {
        Text title = new Text("Beyond the Walls");

        title.setFont(Font.font("Arial", FontWeight.BOLD, 64));

        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 0,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.GOLD),
                new Stop(1, Color.ORANGE)
        );
        title.setFill(gradient);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.DARKGRAY);
        shadow.setRadius(5);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        title.setEffect(shadow);

        return FXGL.entityBuilder(data)
                .view(title)
                .build();
    }

    private void checkEnd() {
        boolean secret1 = FXGL.getb("secret1");

        if (secret1) {
            Entity end = FXGL.spawn("hideEnd");
            Main.newText("hideEnd.txt");

            end.getViewComponent().addOnClickHandler(o -> FXGL.runOnce(() -> FXGL.setLevelFromMap("ui.tmx"), Duration.ONE));
        } else {
            FXGL.setLevelFromMap("ui.tmx");
        }
    }

    @Spawns("end1")
    public Entity newEnd1(SpawnData data) {
        Entity end =  FXGL.entityBuilder()
                .view("end1.png")
                .build();

        end.getViewComponent().addOnClickHandler(o -> FXGL.runOnce(this::checkEnd, Duration.ONE));

        return end;
    }

    @Spawns("end2")
    public Entity newEnd2(SpawnData data) {
        Entity end =  FXGL.entityBuilder()
                .view("end2.png")
                .build();

        end.getViewComponent().addOnClickHandler(o -> FXGL.runOnce(this::checkEnd, Duration.ONE));

        return end;
    }

    @Spawns("end3")
    public Entity newEnd3(SpawnData data) {
        Entity end =  FXGL.entityBuilder()
                .view("end3.png")
                .build();

        end.getViewComponent().addOnClickHandler(o -> FXGL.runOnce(this::checkEnd, Duration.ONE));

        return end;
    }

    @Spawns("hideEnd")
    public Entity newHide(SpawnData data) {
        return FXGL.entityBuilder()
                .view("hideEnd.png")
                .build();
    }
}
