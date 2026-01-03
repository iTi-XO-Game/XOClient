/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.clientside.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author lenovo
 */
@FunctionalInterface
public interface ClientCallback {
    
    public void onSuccess(String responseJson);
    
    default public void onFailure(String errorMessage) {
        if (!errorMessage.isBlank()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An Error Ocurred");
                alert.setHeaderText(errorMessage);
                alert.showAndWait();
            });
        }
    }
}
