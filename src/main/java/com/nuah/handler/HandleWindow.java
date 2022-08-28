package com.nuah.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class HandleWindow extends Handler {

    String vagrantFile = resource + "vagrant_2.3.0_windows_%s.msi";
    String ROOTDRIVE;
    @Override
    void checkVagant() {
        // TODO Auto-generated method stub    
    }

    @Override
    void installVagant() {
        try{
            String arch = System.getProperty("os.arch");
            if (arch.equals("amd64")) {
                vagrantFile = String.format(vagrantFile, "amd64");
            } else {
                vagrantFile = String.format(vagrantFile, "x86");
            }
            File f = new File(vagrantFile);
            ROOTDRIVE = Paths.get(f.getAbsolutePath()).getRoot().toString();
            log = log + "vagrant.txt";
            if (installation.contains(" ")) {
                installation = "`\"" + installation + "`\"";
            }
            if (log.contains(" ")) {
                log = "'" + log + "'";
            }
            if (vagrantFile.contains(" ")) {
                vagrantFile = "'" + vagrantFile + "'";
            }
            System.out.println(vagrantFile);
            System.out.println("Log dir : " + log);
            System.out.println("INSTALLDIR : " + installation);
            ProcessBuilder builder = new ProcessBuilder("msiexec.exe", "/i", vagrantFile, "/qr" ,"/norestart", "/L*", log, "INSTALLDIR=" + ROOTDRIVE);
            System.out.println("Command to execute : " + builder.command().toString());
            Process p = builder.start();
            ProcMon monitor = new ProcMon(p);
            while (!monitor.isComplete()) {
                System.out.println("Installing vagant......");
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            showAlert("Failed to install vagrant!", e.getLocalizedMessage());
        }
    }

    @Override
    void checkVB() {
        // TODO Auto-generated method stub
        
    }
}
