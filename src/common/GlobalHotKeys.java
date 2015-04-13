package common;

import java.io.IOException;
import java.util.TreeMap;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.sun.speech.freetts.FreeTTS;
import static logging.Logging.*;

public class GlobalHotKeys implements HotkeyListener{
	public static TreeMap<String,Integer> keyNamesMap=new TreeMap<String,Integer>();
	public static final int KEY_F1=112;
	public static final int KEY_F2=113;
	public static final int KEY_F3=114;
	public static final int KEY_F4=115;
	public static final int KEY_F5=116;
	public static final int KEY_F6=117;
	public static final int KEY_F7=118;
	public static final int KEY_F8=119;
	public static final int KEY_F9=120;
	public static final int KEY_F10=121;
	public static final int KEY_F11=122;
	public static final int KEY_F12=123;
	
	public static final int KEY_NUM_0=96;
	public static final int KEY_NUM_1=97;
	public static final int KEY_NUM_2=98;
	public static final int KEY_NUM_3=99;
	public static final int KEY_NUM_4=100;
	public static final int KEY_NUM_5=101;
	public static final int KEY_NUM_6=102;
	public static final int KEY_NUM_7=103;
	public static final int KEY_NUM_8=104;
	public static final int KEY_NUM_9=105;
	public static final int KEY_NUM_STAR=106;
	public static final int KEY_NUM_PLUS=107;
	public static final int KEY_NUM_MINUS=109;
	public static final int KEY_NUM_SLASH=111;
	
	public static final int KEY_LEFT_ARROW=37;
	public static final int KEY_RIGHT_ARROW=39;
	public static final int KEY_UP_ARROW=38;
	public static final int KEY_DOWN_ARROW=40;
	
	public static final int KEY_HOME=36;
	public static final int KEY_INSERT=45;
	public static final int KEY_PAGE_UP=33;
	public static final int KEY_PAGE_DOWN=34;
	public static final int KEY_ESC=27;
	public static final int KEY_BACKSPACE=8;
	public static final int KEY_TILD=192;
	public static final int KEY_0=48;
	public static final int KEY_1=49;
	public static final int KEY_2=50;
	public static final int KEY_3=51;
	public static final int KEY_4=52;
	public static final int KEY_5=53;
	public static final int KEY_6=54;
	public static final int KEY_7=55;
	public static final int KEY_8=56;
	public static final int KEY_9=57;
	public static final int KEY_MINUS=189;
	public static final int KEY_PLUS=187;
	public static final int KEY_A=65;
	public static final int KEY_B=66;
	public static final int KEY_C=67;
	public static final int KEY_D=68;
	public static final int KEY_E=69;
	public static final int KEY_F=70;
	public static final int KEY_G=71;
	public static final int KEY_H=72;
	public static final int KEY_I=73;
	public static final int KEY_J=74;
	public static final int KEY_K=75;
	public static final int KEY_L=76;
	public static final int KEY_M=77;
	public static final int KEY_N=78;
	public static final int KEY_O=79;
	public static final int KEY_P=80;
	public static final int KEY_Q=81;
	public static final int KEY_R=82;
	public static final int KEY_S=83;
	public static final int KEY_T=84;
	public static final int KEY_U=85;
	public static final int KEY_V=86;
	public static final int KEY_W=87;
	public static final int KEY_X=88;
	public static final int KEY_Y=89;
	public static final int KEY_Z=90;
	public static final int KEY_COLON=186;
	public static final int KEY_LESS_THAN=188;
	public static final int KEY_GREATER_THAN=190;
	public static final int KEY_SLASH=191;
	
	
	
/**
 * Hook these global ctrl+alt hotkeys
 * 
 */
	public  GlobalHotKeys(int...keys){
		for(int i:keys){
			JIntellitype.getInstance().registerHotKey(i, JIntellitype.MOD_CONTROL+JIntellitype.MOD_ALT, i);
		}
		JIntellitype.getInstance().addHotKeyListener(this);
		//add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				try {
					log("ShutdownHook: cleanup...",1);
					JIntellitype.getInstance().cleanUp();
				} catch (Exception e) {
					log(e);
				}
			}
		});
		
	}
	
	public static IKeyHandler keyHandler=null;
	
	public  GlobalHotKeys(IKeyHandler ikh){
		if (ikh==null){
			log("No key handler installed.");
			return;
		}
		keyHandler=ikh;
		for(int i:ikh.keysToRegister()){
			JIntellitype.getInstance().registerHotKey(i, JIntellitype.MOD_CONTROL+JIntellitype.MOD_ALT, i);
			log("installed keyhook for: Ctrl+Alt+"+i);
		}
		JIntellitype.getInstance().addHotKeyListener(this);
		//add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				try {
					log("ShutdownHook: cleanup...",1);
					JIntellitype.getInstance().cleanUp();
				} catch (Exception e) {
					log(e);
				}
			}
		});
	}
	@Override
	public void onHotKey(int arg0){
		if (keyHandler==null){
			return;
		}
		keyHandler.actOnKey(arg0);
	}
//	{
//		System.out.println("You pressed: CTRL+ALT+ "+arg0);
//		if (arg0==KEY_Q){
//			JIntellitype.getInstance().cleanUp();
//			System.exit(0);
//		}
//	}

	
	

}
