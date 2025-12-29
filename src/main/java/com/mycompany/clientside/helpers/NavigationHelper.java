/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.helpers;

import java.util.Stack;
import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Depogramming
 */
public class NavigationHelper {

    private static Stack<Parent> history = new Stack<>();

    public static void navigateTo(Stage stage, Parent nextRoot) {
        Parent currentRoot = stage.getScene().getRoot();
        history.push(currentRoot);

        // 1. Prepare the new root (invisible at first)
        nextRoot.setOpacity(0);

        // 2. Fade Out the current screen
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            // 3. Once faded out, swap the roots
            stage.getScene().setRoot(nextRoot);

            // 4. Fade In the new screen
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), nextRoot);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    public static void goBack(Stage stage) {
        if (!history.isEmpty()) {
            Parent previousRoot = history.pop();
            // You can apply the same Fade logic here to go back
            stage.getScene().setRoot(previousRoot);
        }
    }
}
