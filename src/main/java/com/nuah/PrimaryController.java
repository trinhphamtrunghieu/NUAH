package com.nuah;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.nuah.handler.HandleLinux;
import com.nuah.handler.HandleMac;
import com.nuah.handler.HandleWindow;
import com.nuah.handler.Handler;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class PrimaryController {

    class FileToDownload {
        String url;
        String fileName;
        String folder;
        FileToDownload(String lString, String fileString, String folderString){
            this.url = lString;
            this.fileName = fileString;
            this.folder = folderString;
        }
    }

    Handler handler;

    //normally only 1 of them should be set to true
    boolean isMac = false;
    boolean isLinux = false;
    boolean isWindow = false;

    @FXML
    Button aButton;

    @FXML
    private void activate() throws IOException {
        aButton.setDisable(true);
        assignHandler();
        handler.execute();
        String arch = System.getProperty("os.arch");
        System.out.print(arch);
    }

    private void assignHandler() {
        String os = System.getProperty("os.name");
        if (os.equals("Linux")) {
            handler = new HandleLinux();
        } else if (os.contains("Windows")) {
            handler = new HandleWindow();
        } else {
            handler = new HandleMac();
        }
    }

}
