/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amr
 */
public class Internet {
    public static boolean connected=true;
    public static int CheckWait=500;
    
    static{
        go();
    }
    
    public static void go(){
        Thread t = new Thread() {
            @Override
            public void run() {
                while(true){
                    setConnected(getConnectionStatus());
                    try {
                        Thread.sleep(CheckWait);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Internet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        };
        t.start();
    }
    
    public static boolean getConnectionStatus() {
         try {
            //make a URL to a known source
            URL url = new URL("http://www.google.com");

            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();

            urlConnect.setConnectTimeout(1000);

                //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static boolean isConnected() {
        return connected;
    }
    
    public static void setConnected(boolean aConnected){
        connected=aConnected;
    }
    
}
