package com.doruk.dplayer.controllers;

import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.views.BaseContainer;
import javafx.application.Application.Parameters;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class NavigationController {
    private Stage stage;
    private Map<String, Controllers> controllers;
    private Stack<Controllers> views;
    private float screenSize = 0.8f;

    public NavigationController(Parameters params){
        stage = BaseContainer.getInstance().getStage();
        controllers = new HashMap<>();
        views = new Stack<>();

        controllers.put("home", new HomeController());
        controllers.put("player", null);
        controllers.put("settings", new SettingsController());

        // control the stage size ( later read the database for settings and apply them )
        this.configWindow();
        // check if there are any arguments, and launch the player if any
         Scene scene;
        if(!params.getRaw().isEmpty()) {
            controllers.put("player", new PlayerController(params));
            PlayerController playerController = (PlayerController) controllers.get("player");
            views.push(playerController);
            scene = getScene("player");
            addPlayerControllerListener(playerController);
        }
        else {  // if arguments are sent open player controller
            scene = getScene("home");
            views.push(controllers.get("home"));
        }
        addListeners();
//        scene.getRoot().setStyle("");
//        KeyCombination cntrlZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
//            @Override
//            public void handle(KeyEvent event) {
//                if(cntrlZ.match(event)){
//                    System.out.println("combo: " + cntrlZ);
//                    return;
//                }
//                System.out.println(event.getCode());
//            }
//        });
        this.stage.setScene(scene);
    }

    private void addListeners(){
        HomeController homeController = (HomeController)controllers.get("home");
        SettingsController settingsController = (SettingsController) controllers.get("settings");
        PlayerController playerController = (PlayerController) controllers.get("player");

        homeController.getPreferenceButton().setOnAction(event -> {
            displaySettingsView(settingsController);
            views.push(homeController);
        });

        addPlayerControllerListener(playerController);

        settingsController.getDoneSettings().setOnAction(event -> this.stage.setScene(views.pop().getScene()));

        // listen when the home controller finishes selecting media files/ then send the media files to PlayerController
        homeController.setOnComplete(medias -> {
            controllers.put("player", new PlayerController(medias));
            addPlayerControllerListener((PlayerController) controllers.get("player"));
            this.stage.setScene(getScene("player"));
        });
    }

    private void addPlayerControllerListener(PlayerController controller){
        if (controller == null)
            return;
        controller.getPreferenceButton().setOnAction(event -> {
            displaySettingsView((SettingsController) controllers.get("settings"));
            views.push(controller);
        });
    }

    private void displaySettingsView(SettingsController controller){
        this.stage.setScene(getScene("settings"));
        controller.scrollToBottom();
    }

    private Scene getScene(String view){
        Controllers cont = controllers.get(view);
        Scene scene = cont.getScene();
        scene.getStylesheets().add(this.getClass().getResource("/assets/dark_theme_adv.css").toString());
        return scene;
    }

    private void configWindow(){
        // set the screen size
        var bounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth((int) (bounds.getWidth() * screenSize));
        stage.setHeight((int) (bounds.getHeight() * screenSize));
        // position the window at the center
        double x = bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) * 0.4;
        stage.setX(x);
        stage.setY(y);
    }
}
