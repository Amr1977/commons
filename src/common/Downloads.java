/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.Logging;
import logging.TextFiles;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Amr
 */
public class Downloads implements Runnable{
    static private Thread downloaderThread;
    static private ArrayList<String> downloadQueue;
    static private TreeMap<String,String> downloadTreeMap=new TreeMap(String.CASE_INSENSITIVE_ORDER);
    static private File toDelete;
    static{
        setDownloadQueue(new ArrayList<>());
        //setDownloadTreeMap(new TreeMap(String.CASE_INSENSITIVE_ORDER));
        setDownloaderThread(null);
        Logging.log("Downloads Class initialized.");
        //create a folder to hold file names of files being downloaded, 
        //each one of these files contains the full path of a file that is being downloaded now 
        // so next time when the application loads it wipes all incomplete downlads
        toDelete=new File("todelete");
        if (toDelete.mkdirs()){
            Logging.log("created transaction folder.");
        }
        if (toDelete.isDirectory()) {
            for (String fileName : toDelete.list()) {
                try {
                    String fullFileName = toDelete.getAbsolutePath() + File.separator + fileName;
                    Logging.log("Incomplete download file found in : " + fullFileName);
                    String fileToDelete = TextFiles.loadString(fullFileName);
                    if (!fileToDelete.isEmpty()){
                        //get rid of line feed
                        fileToDelete=fileToDelete.substring(0, fileToDelete.length()-1);
                    }
                    Logging.log("Deleting incomplete download file found in : " + fileToDelete);
                    try {
                        if (new File(fileToDelete).delete()) {
                            Logging.log("Deleted: " + fileToDelete);
                        }
                        if (new File(fullFileName).delete()) {
                            Logging.log("Deleted: " + fullFileName);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Downloads.class.getName()).log(Level.SEVERE, null, ex);
                        Logging.log(ex);
                    }
                } catch (Exception e) {
                    Logging.log(e);
                }

            }
        }
        
    }

    /**
     * @return the downloadQueue
     */
    public static ArrayList<String> getDownloadQueue() {
        return downloadQueue;
    }

    /**
     * @param aDownloadQueue the downloadQueue to set
     */
    public static void setDownloadQueue(ArrayList<String> aDownloadQueue) {
        downloadQueue = aDownloadQueue;
    }
    
    /**
     * Add the url (download source) and target fileName to download queue, 
     * sequential downloads take place immediately.
     * @param url
     * @param fileName 
     */
    public static void add(String url, String fileName,boolean inQueueHead){
        //TODO maybe we need another force add that overwrites the file if exists
        if (isInQueue(fileName)){
            Logging.log("Download Queue["+getDownloadQueue().size()+"]: Skipped ["+fileName+"] already in Queue.");
            return;
        }
        if (!(new File(fileName).exists())){
            if (inQueueHead){
                //insertion in the head
                getDownloadQueue().add(0,fileName);
            }else{
                //insertion at the end
                getDownloadQueue().add(fileName);
            }
            
            getDownloadTreeMap().put(fileName, url);
            Logging.log("Download Queue["+getDownloadQueue().size()+"]: added["+url+","+fileName+"]");
        } else{
            Logging.log("Download Queue["+getDownloadQueue().size()+"]: dropped ["+fileName+"] already exists.");
        }
        
    }

    /**
     * Tries to download a file from the url (if file not already existing) and
     * save it to filePath.
     *
     * @param url to download
     * @param filePath full destination file name to save.
     * @throws Exception
     */
    public static void downloadFile(String url, String filePath) throws Exception {
        File myFile = new File(filePath);
        if (!myFile.exists()) {
            try {
                //TODO: write that file name to a transaction log, to be deleted on next start   
                Logging.log("Download start: " + url);

                //create a temp file with contents that equals the name of the file being downladed
                File tempFile = File.createTempFile("Del-", ".txt", toDelete);
                TextFiles.save(tempFile.getAbsolutePath(), filePath);
                

                FileUtils.copyURLToFile(new URL(url), myFile);
                //if reached here without any exception then we can safely delete the temp file
                tempFile.delete();
                Logging.log("Download ended: " + url);
                //TODO: clear the transaction log
            } catch (Exception e) {
                //clean partial files
                if (myFile.exists()) {
                    myFile.delete();
                }
                Logging.log("Error downloading/copying file: " + url);
                throw e;
            }

        } else {
            Logging.log("downloadFile: File already exists [" + filePath + "].");
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
        
        if (getDownloadTreeMap().get(fileName)==null){
            return false;
        } else{
            return true;
        }
        /*
        boolean result=false;
        for(int i=0;i<getDownloadQueue().size();i++){
            if(getDownloadQueue().get(i).getFileName().toLowerCase().equals(fileName.toLowerCase())){
                result=true;
                break;
            }
        }
        return result;
        
        */
    }

    /**
     * @return the downloadTreeMap
     */
    public static TreeMap<String,String> getDownloadTreeMap() {
        if (downloadTreeMap==null){
            downloadTreeMap=new TreeMap(String.CASE_INSENSITIVE_ORDER);
        }
        return downloadTreeMap;
    }

    /**
     * @param aDownloadTreeMap the downloadTreeMap to set
     */
    public static void setDownloadTreeMap(TreeMap<String,String> aDownloadTreeMap) {
        downloadTreeMap = aDownloadTreeMap;
    }
    
    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        while(true){
            while(getDownloadQueue().size()>0){
                
                  
                try {
                    Logging.log("Download Queue size: "+getDownloadQueue().size());
                    String fileName=getDownloadQueue().get(0);
                    downloadFile(getDownloadTreeMap().get(fileName),fileName);
                    
                    //TODO find a way to delete that entry and not first entry
                    getDownloadQueue().remove(fileName);
                    getDownloadTreeMap().remove(fileName);
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
