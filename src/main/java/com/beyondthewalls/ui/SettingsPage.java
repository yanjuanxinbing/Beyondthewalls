package com.beyondthewalls.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SettingsPage extends SubScene {
    private static final double STAGE_WIDTH  = 960;
    private static final double STAGE_HEIGHT = 640;
    private static final double PANEL_WIDTH  = 600;
    private static final double PANEL_HEIGHT = 400;
    private static final double PANEL_X = (STAGE_WIDTH  - PANEL_WIDTH)  / 2;
    private static final double PANEL_Y = (STAGE_HEIGHT - PANEL_HEIGHT) / 2;

    public SettingsPage() {

        var root = new Pane();
        root.setPrefSize(STAGE_WIDTH, STAGE_HEIGHT);

        // 设置SubScene的内容
        getContentRoot().getChildren().setAll(root);

        initUI(root);
    }

    private void initUI(Pane root) {


        VBox panel = new VBox(30);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(50));
        panel.setPrefSize(PANEL_WIDTH, PANEL_HEIGHT);
        panel.setLayoutX(PANEL_X);
        panel.setLayoutY(PANEL_Y);
        panel.setStyle("-fx-background-color: #FFF1B6; -fx-background-radius: 20;"); // 背景色改为 #f9fff8

        // 标题
        Text title = new Text("游戏设置");
        title.setFont(Font.font("Microsoft YaHei", 36));
        title.setFill(Color.web("#5db277"));

        //音乐与音效控制 - FXGL 21中获取音量设置的方式可能不同
        HBox musicControl = createSliderControl("音乐音量", FXGL.getSettings().getGlobalMusicVolume());
        HBox soundControl = createSliderControl("音效音量", FXGL.getSettings().getGlobalSoundVolume());

        // 按钮容器
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);

        //保存按钮
        Button saveButton = new Button("保存设置");
        saveButton.setFont(Font.font("Microsoft YaHei", 18));
        saveButton.setStyle("-fx-background-color: #c8f4ae; -fx-text-fill:#5db277 ;");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #5db277; -fx-text-fill: #c8f4ae;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color: #c8f4ae; -fx-text-fill: #5db277;"));
        saveButton.setOnAction(e -> {
            FXGL.getSettings().setGlobalMusicVolume(((Slider) musicControl.getUserData()).getValue());
            FXGL.getSettings().setGlobalSoundVolume(((Slider) soundControl.getUserData()).getValue());

            // FXGL 21中关闭子场景的方式
            FXGL.getSceneService().popSubScene();
        });

        //返回游戏按钮
        Button backButton = new Button("返回游戏");
        backButton.setFont(Font.font("Microsoft YaHei", 18));
        backButton.setStyle("-fx-background-color: #c8f4ae; -fx-text-fill:#5db277 ;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #5db277; -fx-text-fill: #c8f4ae;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #c8f4ae; -fx-text-fill: #5db277;"));
        backButton.setOnAction(e -> {
            // 直接返回游戏，不保存设置
            FXGL.getSceneService().popSubScene();
        });

        buttonContainer.getChildren().addAll(saveButton, backButton);

        panel.getChildren().addAll(title, musicControl, soundControl, buttonContainer);
        root.getChildren().add(panel);
    }

    private HBox createSliderControl(String labelText, double initialValue) {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);

        Slider slider = new Slider(0, 1, initialValue);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.25);
        slider.setBlockIncrement(0.1);
        slider.setPrefWidth(260);

        // 清除可能存在的默认样式
        slider.getStylesheets().clear();

        // 使用lookup方法设置渐变样式
        slider.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                // 等待皮肤加载完成后设置样式
                javafx.application.Platform.runLater(() -> {
                    javafx.scene.Node track = slider.lookup(".track");
                    javafx.scene.Node thumb = slider.lookup(".thumb");

                    if (track != null) {
                        // 设置轨道为绿色到#ffe67d的渐变
                        track.setStyle("-fx-background-color: linear-gradient(to right, #FFF1B6, #5db277); " +
                                "-fx-background-radius: 5; " +
                                "-fx-pref-height: 8;");
                    }
                    if (thumb != null) {
                        // 设置滑块按钮样式
                        thumb.setStyle("-fx-background-color: #5db277; " +
                                "-fx-background-radius: 10; " +
                                "-fx-pref-height: 20; " +
                                "-fx-pref-width: 20;");
                    }
                });
            }
        });

        container.setUserData(slider);

        Label label = new Label(labelText);
        label.setFont(Font.font("Microsoft YaHei", 18));
        label.setTextFill(Color.web("#5db277")); // 标签颜色改为绿色
        label.setMinWidth(120);

        Label valueLabel = new Label(String.format("%.0f%%", initialValue * 100));
        valueLabel.setFont(Font.font("Microsoft YaHei", 16));
        valueLabel.setTextFill(Color.web("#5db277")); // 数值颜色改为绿色
        valueLabel.setMinWidth(50);

        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                valueLabel.setText(String.format("%.0f%%", newVal.doubleValue() * 100)));

        container.getChildren().addAll(label, slider, valueLabel);
        return container;
    }
}