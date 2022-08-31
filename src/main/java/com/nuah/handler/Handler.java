package com.nuah.handler;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public abstract class Handler {
    @FXML
    Text status;
    abstract void installVagant();
    abstract void installVB();
    abstract void runImage();
    final String resource = System.getProperty("user.dir") + "\\resource\\";
    final String centosBox = resource + "CentOS-7-x86_64-Vagrant-2004_01.VirtualBox.box";
    String installation = System.getProperty("user.dir") + "\\installation\\";
    String log = System.getProperty("user.dir") + "\\log\\";
    public void execute(){
        installVagant();
        installVB();
        runImage();
    }

    protected void showAlert(String tile, String contenxt) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(tile);
        errorAlert.setContentText(contenxt);
        errorAlert.showAndWait();
    }

    public void showStatus(String mes) {
        Alert infoAlert = new Alert(AlertType.INFORMATION);
        infoAlert.setHeaderText("INFO");
        infoAlert.setContentText(mes);
        infoAlert.show();       
    }

    public void assignStatusButton(Text status) {
        this.status = status;
    }

}