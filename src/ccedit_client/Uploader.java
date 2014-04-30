/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccedit_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Basti
 */
public class Uploader extends Thread {

    private final String mode, computer, file, password, content;
    private String out="";
    private boolean fin = false;
    private final Object o = new Object();

    public Uploader(String mode, String computer, String file, String password, String content) {
        this.mode = mode;
        this.computer = computer;
        this.file = file;
        this.password = password;
        this.content = content;
    }

    public String getResponse() {
        synchronized (o) {
            while (!fin) {
                try {
                    o.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Uploader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return out;
    }

    @Override
    public void run() {
        String temp;
        try {
            FileInputStream fis = new FileInputStream(new File(System.getProperty("user.home") + "/yenon/CCEdit_Client/Server.cfg"));
            Properties settings = new Properties();
            settings.load(fis);
            if (settings.containsKey("IP") && settings.containsKey("Port")) {
                Socket sock = new Socket(settings.getProperty("IP"), Integer.parseInt(settings.getProperty("Port")));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                bw.write(mode + "\n");
                bw.write(computer + "\n");
                bw.write(file + "\n");
                bw.write(password + "\n");
                bw.write(content + "\n");
                bw.write("\u001A\n");
                bw.flush();
                BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                System.out.println("Fetching response");
                while (!"\u001A".equals(temp = br.readLine())) {
                    if (temp != null) {
                        if (!"".equals(out)) {
                            out = out + "\n" + temp;
                        } else {
                            out = temp;
                        }
                    }
                }
                bw.close();
                br.close();
                sock.close();
            }
        } catch (Exception ex) {
            out = "Exception: " + ex.getMessage();
        }
        fin = true;
        synchronized (o) {
            o.notify();
        }
    }
}
