package com.mycompany.clientside.client;

import javafx.scene.control.Alert;

public class MyAlert
{
    public static void show(Alert.AlertType type, String title,String header,String content)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.show();
    }
}
