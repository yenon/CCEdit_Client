/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ccedit_client;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Basti
 */
public class LoadHelper extends Thread{
    private final LoaderView lv;
    public LoadHelper(LoaderView _lv){
        lv=_lv;
    }
    
    @Override
    public void run(){
        boolean ok;
        if(!new File(System.getProperty("user.home") + "/yenon/CCEdit_Client").isDirectory()){
            if(!new File(System.getProperty("user.home") + "/yenon/CCEdit_Client").mkdirs()){
                ErrorFrame.main("Could not create /yenon/CCEdit_Client in your user-home");
                lv.setVisible(false);
            }
        }
        if(!new File(System.getProperty("user.home") + "/yenon/CCEdit_Client/CCEdit.cfg").isFile()){
            lv.setLoadingStatus(5,"Waiting for user input...");
            InputServerdata is = new InputServerdata();
            is.setVisible(true);
            is.waitForFinish();
        }
        lv.setLoadingStatus(10,"Downloading command file...");
    }
}
