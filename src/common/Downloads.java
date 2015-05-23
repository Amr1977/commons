/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.ArrayList;

/**
 *
 * @author Amr
 */
public class Downloads {
    static private ArrayList<DownloadQueueEntry> downloadQueue;
    static{
        setDownloadQueue(new ArrayList<>());
    }

    /**
     * @return the downloadQueue
     */
    public static ArrayList<DownloadQueueEntry> getDownloadQueue() {
        return downloadQueue;
    }

    /**
     * @param aDownloadQueue the downloadQueue to set
     */
    public static void setDownloadQueue(ArrayList<DownloadQueueEntry> aDownloadQueue) {
        downloadQueue = aDownloadQueue;
    }
    
    
    
}
