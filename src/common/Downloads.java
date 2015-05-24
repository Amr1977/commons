/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.Logging;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Amr
 */
public class Downloads implements Runnable{
    static private Thread downloaderThread;
    static private ArrayList<DownloadQueueEntry> downloadQueue;
    static{
        setDownloadQueue(new ArrayList<>());
        setDownloaderThread(null);
        Logging.log("Downloads Class initialized.");
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
    
    /**
     * Add the url (download source) and target fileName to download queue, 
     * sequential downloads take place immediately.
     * @param url
     * @param fileName 
     */
    public static void add(String url, String fileName){
        //TODO maybe we need another force add that overwrites the file if exists
        if (!(new File(fileName).exists())){
            DownloadQueueEntry downloadQueueEntry=new DownloadQueueEntry(url,fileName);
            getDownloadQueue().add(downloadQueueEntry);
            Logging.log("Download Queue: added["+url+","+fileName+"]");
        } else{
            Logging.log("Download Queue: dropped ["+fileName+"] already exists.");
        }
        
    }

    /**
	 * Tries to download a file from the url (if file not already existing) and save it to filePath. 
	 * @param url to download 
	 * @param filePath full destination file name to save.
	 * @throws Exception 
	 */
	public static void downloadFile(String url, String filePath) throws Exception{
		File myFile=new File(filePath);
		if (!myFile.exists()){
			try{
                            //TODO: write that file name to a transaction log, to be deleted on next start   
				Logging.log("Download start: "+url);
				FileUtils.copyURLToFile(new URL(url),myFile );
				Logging.log("Download ended: "+url);
                                //TODO: clear the transaction log
			} catch(Exception e){
                            //clean partial files
                            if (myFile.exists()){
                                myFile.delete();
                            }
			Logging.log("Error downloading/copying file: "+url);
                        throw e;
			}

		}else{
                    Logging.log("downloadFile: File already exists ["+filePath+"].");
                }
	}
        
        public static void start(){
            if (getDownloaderThread()== null){
                downloaderThread=new Thread(new Downloads(), "Downloads Thread");
		downloaderThread.start();
                Logging.log("Downloads Thread created.");
            } else{
                Logging.log("Downloads Thread already created.");
            }
        }

    /**
     * @return the downloaderThread
     */
    public static Thread getDownloaderThread() {
        return downloaderThread;
    }

    /**
     * @param aDownloaderThread the downloaderThread to set
     */
    public static void setDownloaderThread(Thread aDownloaderThread) {
        downloaderThread = aDownloaderThread;
    }
    
    public static boolean isInQueue(String fileName){
        boolean result=false;
        for(int i=0;i<getDownloadQueue().size();i++){
            if(getDownloadQueue().get(i).getFileName().toLowerCase().equals(fileName.toLowerCase())){
                result=true;
                break;
            }
        }
        return result;
    }
    
    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        while(true){
            while(getDownloadQueue().size()>0){
                DownloadQueueEntry downloadQueueEntry;
                downloadQueueEntry = new DownloadQueueEntry(
                        getDownloadQueue().get(0).getUrl(),
                        getDownloadQueue().get(0).getFileName()
                );
                  
                try {
                    Logging.log("Download Queue size: "+getDownloadQueue().size());
                    downloadFile(downloadQueueEntry.getUrl(),downloadQueueEntry.getFileName());
                    getDownloadQueue().remove(0);
                } catch (Exception ex) {
                    Logger.getLogger(Downloads.class.getName()).log(Level.SEVERE, null, ex);
                    Logging.log(ex);
                }
                  
                
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Downloads.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                    
            
        }
    }
    
    
    
}
