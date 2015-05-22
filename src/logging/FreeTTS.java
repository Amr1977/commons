package logging;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import de.dfki.lt.freetts.en.us.MbrolaVoice;


public class FreeTTS implements Runnable{
	public static String text; 
	public static int MAX_VOICE_QUEUE_LENGTH=3;
	private static final String VOICENAME_kevin = "kevin16";
	public static ConcurrentLinkedQueue<String> voiceLogQueue=new ConcurrentLinkedQueue<>();
	static VoiceManager voiceManager = VoiceManager.getInstance();
	static Voice voice = voiceManager.getVoice(VOICENAME_kevin);
	public static Thread freeTTS=null;
	public static boolean MUTE=false;
	
	public static void MuteToggle(){
		if (MUTE){
			MUTE=false;
			Logging.log("Assalamu alaikom, Speech ON",1);
		}else{
			Logging.log("I will MUTE, salam.",1);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MUTE=true;
		}
	}
	public FreeTTS(){

	}
	public void run() {

	}

	public void run2() {
		voice.allocate();
		while(true){
			while(! voiceLogQueue.isEmpty()){
				if (voiceLogQueue.size()>MAX_VOICE_QUEUE_LENGTH){
					voiceLogQueue.poll();
				} else{
                                    if (!MUTE){
                                        voice.speak(voiceLogQueue.poll());
                                    }else{
                                        while(! voiceLogQueue.isEmpty()){
                                            voiceLogQueue.poll();
                                        }
                                    }
					
				}
			}
			while(voiceLogQueue.isEmpty()){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	public static String say(String text) {
		voiceLogQueue.add(text);
		return text;
	}

	
	public static void init(){
		if (freeTTS==null){
			freeTTS=new Thread(new FreeTTS(),"FreeTTS Thread");
			freeTTS.start();
			FreeTTS.say("Bismillah :)");
			
		} else{
			FreeTTS.say("Alhamdulillah :)");
		}
		
	}
	
	public static void main(String[] args) {
	}



}
