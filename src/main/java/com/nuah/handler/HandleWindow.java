package com.nuah.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HandleWindow extends Handler {

    String vagrantFile = resource + "vagrant_2.3.0_windows_%s.msi";
    final String ROOTDRIVE = System.getenv("ProgramFiles") + "\\";
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
            System.out.println(vagrantFile);
            ProcessBuilder builder = new ProcessBuilder("msiexec.exe", "/a", vagrantFile, "/norestart", "/L*", log + "vagrant.txt",
            "INSTALLDIR=" + installation);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
              line = r.readLine();
              if (line == null) {
                break;
              }
              System.out.println(line);
            }
        } catch (IOException e) {
            showAlert("Failed to install vagrant!", e.getLocalizedMessage());
        }
    }

    @Override
    void checkVB() {
        // TODO Auto-generated method stub
        
    }
}
