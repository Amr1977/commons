package test;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import logging.FreeTTS;
import logging.Logging;
import static logging.Logging.*;
import static logging.TextFiles.*;
//import common.Sikuliz;




public class BaseTest   {
	/**
	 * A map containing string variables
	 */
	public static TreeMap<String,String> varTable=new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	public static boolean concurrentHighlight=false;
	
	public static  ArrayList<String> passTCs = new ArrayList<>();
	public static   ArrayList<String> failTCs = new ArrayList<>();
	public static   ArrayList<String> warnings = new ArrayList<>();
	
	public static int testRunNum = 0;
	public static int testRunSuccess = 0;
	public static int testRunFail = 0;
	
	
	/**
	 * substitute any variable in this string with its value from varTable
	 * @param token a string where to search for variables.
	 * @return string after substituting all variables with their values
	 */
	public static String expand(String token){
		String result=null;
		boolean altered=false;
		do{
			altered=false;
			for(String entry:varTable.keySet()){
				if (token.indexOf(entry)>=0){
					log("substituting ["+entry+"] in ["+token+"]");
					String substitutionValue=varTable.get(entry);
					String newToken=replaceAll(token,entry, substitutionValue);//TODO BUG !!
					token=newToken;
					log("After substitution  ["+newToken+"]");
					altered=true;
				}
			}
		}while(altered);//precaution for multilevel substitution
		result=token;
		
		return result;
	}
//	public static Sikuliz getSikuliChild() {
//		return sikuliChild;
//	}
//
//	public static void setSikuliChild(Sikuliz sikuliChild) {
//		BaseTest.sikuliChild = sikuliChild;
//	}

//	public static Screen getScreen() {
//		return screen;
//	}
//
//	public static void setScreen(Screen screen) {
//		BaseTest.screen = screen;
//	}

//	public TreeMap<String, String[]> getStateMap() {
//		return stateMap;
//	}

//	public void setStateMap(TreeMap<String, String[]> stateMap) {
//		this.stateMap = stateMap;
//	}

	public TreeMap<String, String> getServerMap() {
		return serverMap;
	}

	public void setServerMap(TreeMap<String, String> serverMap) {
		this.serverMap = serverMap;
	}

	

	//static public Screen screen;
	//public TreeMap<String,String[]> stateMap=new TreeMap<String,String[]>(String.CASE_INSENSITIVE_ORDER);
	public static TreeMap<String,String> serverMap=new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
	public BaseTest() throws Exception{
		
		
		
		
	}
	
	
	
	public static void executer(String... parameters) throws IOException{
		
		Process process = new ProcessBuilder(parameters).start();
		
	}
	
	
	public static void summary() throws IOException, InterruptedException,	AWTException {
		log("");
		log(FreeTTS.say("Summary:"));
		if (testRunSuccess > 0) {
			log("");
			log("Passed tests:");
			log("");
			for (int i = 0; i < passTCs.size(); i++) {
				log(passTCs.get(i));
			}
		}
		if (testRunFail > 0) {
			log("");
			log("Failed tests:");
			log("");
			for (int i = 0; i < failTCs.size(); i++) {
				log(failTCs.get(i));
			}
		}
		log("");
		log(FreeTTS.say("Test count: " + testRunNum));
		log(FreeTTS.say("Pass count: " + testRunSuccess));
		log(FreeTTS.say("Fail count: " + testRunFail));
		log("");
		if (warnings.size() > 0) {
			log("");
			log("Warnings: "+warnings.size());
			log("");
			for (int i = 0; i < warnings.size(); i++) {
				log(warnings.get(i));
			}
		}
		log("");
		log("End of test run.");
		failTCs.clear();
		passTCs.clear();
		warnings.clear();
		log("log file saved in: "+makeLink(getLogFile().toString()));
		
	}
	
	
	
	
}
