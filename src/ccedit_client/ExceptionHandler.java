/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ccedit_client;

/**
 *
 * @author Game
 */
public class ExceptionHandler {
    public static void handle(boolean stop,Exception ex){
        String out;
        StackTraceElement ste[];
        int i=0;
        out=ex.getMessage()+"\n";
        ste=ex.getStackTrace();
        while(i<ste.length){
            out=out+ste[i].toString()+"\n";
        }
        ErrorFrame.main(out,stop);
    }
}
