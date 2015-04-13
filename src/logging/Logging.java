package logging;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;



/**
 * 
 * @author Amr Lotfy
 *
 */
public class Logging {
	private static Path logFile=Paths.get("log.txt");
	public static ConcurrentLinkedQueue<String> diskLogQueue=new ConcurrentLinkedQueue<>();
	public static ConcurrentLinkedQueue<String> webLogQueue=new ConcurrentLinkedQueue<>();
	public static Boolean showLogMsg=true;
	public static boolean saveToFile=true;
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	public static SimpleDateFormat timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
	public static SimpleDateFormat dateStamp = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat dbReadDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	public static SimpleDateFormat postDateFormat = new SimpleDateFormat("M/d/yyyy hh:mm:ss aaa");
	// 3/24/2015 5:12:03 PM
	public static Path getLogFile() {
		return logFile;
	}

	
	
	public static void setLogFile(Path logFile) {
		Logging.logFile = logFile;
		
	}
	
	public static void setLogFile(String logFile) {
		//TODO create folder if not exist
		Logging.logFile = Paths.get(logFile);
		Logging.logFile.getParent().toFile().mkdirs();
		
	} 

	/**
	 * yyyyMMdd
	 * 
	 * @return
	 */
	public static String getDateStamp(){
		return dateStamp.format(new Date());
	}
	
	/**
	 * yyyyMMdd-HHmmssS
	 * 
	 * @return
	 */
	public static String getTimeStamp(){
		return timeStamp.format(new Date());
	}
	
	public static boolean getShowLogMsg() {
		return showLogMsg;
	}

	public static void setShowLogMsg(boolean showLogMsg) {
		Logging.showLogMsg = showLogMsg;
	}
	
	public static void logMsgOn(){
		Logging.showLogMsg=true;
	}
	public static void logMsgOff(){
		Logging.showLogMsg=false;
	}

	public Logging(String logFilePath){
		Logging.logMsgOn();
		logFile= Paths.get(logFilePath) ;
	}
	
	/**
	 * 
	 * @param logMessage the message to log
	 * @param flags if equals 1 then FreeTTS will pronounce the message
	 * @return
	 */
	public static String log(String logMessage,int flags){
		if (flags==1){
			if (FreeTTS.freeTTS==null){
				FreeTTS.init();
			}
			FreeTTS.say(logMessage);
		}
		return log(logMessage);
	}
	
	public static String log(String logMessage){
		if (! "".equals(logMessage)){
			logMessage="["+dateFormat.format(new Date())+"] "+logMessage;
		}
		if (Logging.getShowLogMsg()){
			System.out.println(logMessage);//just for now
		}
		if (saveToFile){
			diskLogQueue.add(logMessage);
			webLogQueue.add(logMessage);
			while(! diskLogQueue.isEmpty()){
				try {
					TextFiles.save(logFile,diskLogQueue.poll());
				} catch (IOException e) {
					System.out.println("Error saving log to file"+logFile);
				}
			}
		} else{
			System.out.println("Logging: saveToFile off");
		}
		return logMessage;
	}
	/**
	 * Displays and logs the content of a text file.
	 * @param fileName a text file to include in the log and view on console
	 */
	public static ArrayList<String> logLines(String fileName){
		if ((fileName!=null)&&(fileName.contains(".txt")) ){
			ArrayList<String> lines=TextFiles.load(fileName);
			if (lines!=null){
				for(String s: lines){
					Logging.log(s);
				}
			}
			return lines;
		} else{
			return null;
		}
	}
	public static void log(Exception e){
		 StackTraceElement[]  ste=e.getStackTrace();
		 log("Exception occured, Stack trace: ",1);
		 for(StackTraceElement i:ste){
			 log(i.toString());
			 e.printStackTrace();
		 }
	}
public static  void main(String[] args){
	System.out.println(getTimeStamp());
	
}

}
