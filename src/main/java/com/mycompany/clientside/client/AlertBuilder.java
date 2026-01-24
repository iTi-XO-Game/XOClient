package com.mycompany.clientside.client;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.controllers.AlertController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AlertBuilder {
    private String title = "";
    private String subTitle = "";
    private String acceptText = "";
    private String cancelText = "";
    private Runnable onAccept = () -> {};
    private Runnable onCancel = () -> {};
    private boolean isCancellable = false;
    private boolean isDanger = false;

    public AlertBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public AlertBuilder setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public AlertBuilder setAcceptText(String acceptText) {
        this.acceptText = acceptText;
        return this;
    }

    public AlertBuilder setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public AlertBuilder setOnAccept(Runnable onAccept) {
        this.onAccept = onAccept;
        return this;
    }

    public AlertBuilder setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    public AlertBuilder setCancellable(boolean isCancellable) {
        this.isCancellable = isCancellable;
        return this;
    }
    public AlertBuilder setDanger(boolean isDanger) {
        this.isDanger = isDanger;
        return this;
    }

    public AlertBuilder() {
    }

    public void show() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource(Screens.ALERT_DIALOG + ".fxml"));
                Parent root = loader.load();
                AlertController controller = getAlertController(loader);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);

                stage.initStyle(StageStyle.TRANSPARENT);

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);

                stage.setOnCloseRequest(e -> {
                    if (!controller.actionWasTaken) {
                        if (isCancellable) {
                            controller.cancel.run();
                        } else {
                            controller.accept.run();
                        }
                    }
                });

                stage.show();

            } catch (IOException e) {

            }
        });
    }

    private AlertController getAlertController(FXMLLoader loader) {
        AlertController controller = loader.getController();

        controller.setTitle(title);
        controller.setSubTitle(subTitle);
        controller.setAcceptText(acceptText);
        controller.setOnAccept(onAccept) ;

        if (isCancellable) {
            controller.setCancelText(cancelText);
            controller.setOnCancel(onCancel);
        } else {
            controller.hideCancelButton();
        }
        if (isDanger) {
            controller.seDangerButton();
        }
        return controller;
    }

}
