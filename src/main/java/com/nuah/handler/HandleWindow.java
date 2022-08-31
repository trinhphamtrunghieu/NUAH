package com.nuah.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

public class HandleWindow extends Handler {

    String vagrantFile = resource + "vagrant_2.3.0_windows_%s.msi";
    String vbFileEXE = resource + "VirtualBox-6.1.36-152435-Win.exe";
    String ROOTDRIVE = Paths.get(new File(resource).getAbsolutePath()).getRoot().toString();
    String vagrantExecutable = ROOTDRIVE + "HashiCorp\\Vagrant\\bin\\vagrant.exe";
    String vbExecutable = ROOTDRIVE + "virtualbox\\VBoxManage.exe";
    boolean vagrant = false;
    boolean virtualbox = false;

    public HandleWindow(){
        File f = new File(vagrantFile);
        final String tmpRoot = Paths.get(f.getAbsolutePath()).getRoot().toString(); 
        File fV = new File(tmpRoot + "HashiCorp\\Vagrant\\bin\\vagrant.exe");
        if (fV.exists()) {
            vagrant = true;
        }
        File vbF = new File(vbExecutable);
        if (vbF.exists()) {
            virtualbox = true;
        }
    }

    @Override
    void installVagant() {
        if (!vagrant) {
            try{
                String arch = System.getProperty("os.arch");
                if (arch.equals("amd64")) {
                    vagrantFile = String.format(vagrantFile, "amd64");
                } else {
                    vagrantFile = String.format(vagrantFile, "x86");
                }
                File f = new File(vagrantFile);
                ROOTDRIVE = Paths.get(f.getAbsolutePath()).getRoot().toString();
                String logVagrant = log + "vagrant.txt";
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
                ProcessBuilder builder = new ProcessBuilder("msiexec.exe", "/a", vagrantFile, "/qr" ,"/norestart", "/L*", logVagrant, "ROOTDRIVE=" + ROOTDRIVE);
                System.out.println("Command to execute : " + builder.command().toString());
                Process p = builder.start();
                while (p.isAlive()) {
                    System.out.println("Installing vagrant....");
                    TimeUnit.SECONDS.sleep(5);
                }
                if (p.exitValue() != 0) {
                    BufferedReader errinput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    showAlert("Failed to install vagrant", errinput.readLine());
                    System.exit(0);
                }
                System.out.println("Configuring executable file");
                System.out.println("Get Installed version");
                ProcessBuilder vBuilder = new ProcessBuilder(vagrantExecutable, "-v");
                Process vProcess = vBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(vProcess.getInputStream()));
                if (vProcess.exitValue() == 0) {
                    showStatus("Installing vagrant successfully. Starting install virtualbox");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
                showAlert("Failed to install vagrant!", e.getMessage());
            }    
        }
    }

    @Override
    void installVB() {
        if (!virtualbox){
            try {
                File f = new File(vbFileEXE);
                final String logVB = log + "vb.txt";
                ProcessBuilder installBuilder = new ProcessBuilder(vbFileEXE,"--msi-log-file", logVB, "--ignore-reboot", "--msiparams", "INSTALLDIR=" + ROOTDRIVE + "virtualbox\\", "--silent");
                System.out.println("Command to execute : " + installBuilder.command());
                Process installProcess = installBuilder.start();
                while (installProcess.isAlive()) { 
                    System.out.println("Installing virtualbox......");
                    TimeUnit.SECONDS.sleep(10);
                }
                System.out.println("Instal virtualbox done!");
                ProcessBuilder vBuilder = new ProcessBuilder(vbExecutable, "-v");
                Process vProcess = vBuilder.start();
                BufferedReader vVB = new BufferedReader(new InputStreamReader(vProcess.getInputStream()));
                System.out.println("Version of virtualbox : " + vVB.readLine());
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                showAlert("Failed to install virtual box", e.getMessage());
            }
        }
    }

    @Override
    void runImage() {
        String vagrantFolder = Paths.get(new File(vagrantExecutable).getAbsolutePath()).getParent().getParent().toString();
        System.out.println("VagrantFolder: " + vagrantFolder);
        File centosFolder = new File(vagrantFolder + "\\centos7\\");
        if (centosFolder.exists()) {
            if (!centosFolder.delete()) {
                showAlert("Can not remove centos folder", "Centos folder already exists and can not be remove");
                System.exit(1);
            }
        }
        if (!centosFolder.mkdirs()) {
            showAlert("Can not create centos folder", "Centos folder can not be create");
            System.exit(1);
        }
        try {
            Path centosF = Paths.get(new File(centosBox).getAbsolutePath());
            System.out.println(centosF.getFileName().toString());
            Files.copy(centosF, 
            Paths.get(centosFolder.getAbsolutePath() + "\\" + centosF.getFileName().toString()), 
            StandardCopyOption.REPLACE_EXISTING);
            String imagePath =  vagrantFolder + "\\centos7\\CentOS-7-x86_64-Vagrant-2004_01.VirtualBox.box";
            ProcessBuilder addBoxProcessBuilder = new ProcessBuilder(vagrantExecutable, 
            "box", 
            "add",
            imagePath,
            "--name",
            "centos7"
            );
            System.out.println("Command to execute : " + addBoxProcessBuilder.command());
            addBoxProcessBuilder.start();


        } catch (IOException e) {
            showAlert("Can not copy centos box", e.getMessage());
            System.exit(1);
        }        
    }

}
