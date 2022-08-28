package com.nuah.handler;

import javafx.scene.control.Alert;

public abstract class Handler {
    abstract void checkVagant();
    abstract void installVagant();
    abstract void checkVB();
    final String resource = System.getProperty("user.dir") + "\\resource\\";
    String installation = System.getProperty("user.dir") + "\\installation\\";
    String log = System.getProperty("user.dir") + "\\log\\";
    public void execute(){
        checkVagant();
        installVagant();
        checkVB();
    }

    protected void showAlert(String tile, String contenxt) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(tile);
        errorAlert.setContentText(contenxt);
        errorAlert.showAndWait();
    }
}
