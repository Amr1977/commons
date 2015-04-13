package logging;


//import i4view.I4ViewTest;

import static logging.Logging.*;
//import static logging.Logging.timeStamp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;

public class TextFiles {

	//read text file into ArrayList of strings

	
	
	
	
	/**
	 * This method reads file lines into ArrayList, 
	 * @param fileName	the file to read from
	 * @return ArrayList of lines read
	 * @author Amr Lotfy
	 */
	public static ArrayList<String> load(String fileName){ 
		ArrayList<String> lines=new ArrayList<String>();
                log("Load: "+fileName);
		if ((fileName!=null)&&(new File(fileName).exists())){
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(fileName));
				try {
					String line;

					while ((line = br.readLine()) != null) {
						// process the line.
						//add to value list
						lines.add(line);
					}
				} catch (IOException e) {
					Logging.log(e.toString());
				}
				try {
					br.close();
				} catch (IOException e) {
					Logging.log(e.toString());
				}
			} catch (FileNotFoundException e) {
				Logging.log(e.toString());
			}
		} else{
			log("file not exist: "+fileName);
		}
		log("lines returned: "+lines.size());
		return lines;
	}

	/**
	 * Load the specified file into one continues string.
	 * @param fileName
	 * @return
	 */
	public static String loadString(String fileName){
		String result="";
		if (new File(fileName).exists()){
			ArrayList<String> lines=load(fileName);
			for (String line:lines){
				result+=line+"\n";
			}
		}
		return result;
	}

	//write ArrayList of strings into a text file

	/**
	 * writes an ArrayList of strings  into a file, each list element is a line
	 * @param lines list of strings to be written to file
	 * @param fileName file to save lines
	 * @author Amr Lotfy
	 * 
	 * @throws IOException 
	 * 
	 */

	public static void save(ArrayList<String> lines, String fileName) throws IOException{

		if ((lines==null)||("".equals(fileName))){
			return;
		}
		FileWriter f0 = new FileWriter(fileName);

		String newLine = System.getProperty("line.separator");


		for( String line : lines)
		{
			f0.write( line + newLine);
		}
		f0.close();

	}

	public static void save(String fName, String... lines) throws IOException{
		save(Paths.get(fName),lines);
	}

	public static void save(Path fName, String... lines) throws IOException{
		String fileName=fName.toString();
		if ((lines==null)||("".equals(fileName))){
			return;
		}
		FileWriter f0 = new FileWriter(fileName,true);

		String newLine = System.getProperty("line.separator");


		for( String line : lines)
		{
			f0.write( line + newLine);
		}
		f0.close();

	}
	
	public static void OverwriteSave(String fName, String... lines){
		if ((lines==null)||("".equals(fName))){
			return;
		}
		FileWriter f0;
		try {
			f0 = new FileWriter(fName,false);
			String newLine = System.getProperty("line.separator");
			for( String line : lines)
			{
				try {
					f0.write( line + newLine);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			f0.close();
		} catch (IOException e) {
			e.printStackTrace();
		}



	}



	public static void save(List<Integer> lines, String fileName) throws IOException {
		if ((lines==null)||("".equals(fileName))){
			return;
		}
		FileWriter f0 = new FileWriter(fileName);

		String newLine = System.getProperty("line.separator");


		for( Integer line : lines)
		{
			f0.write( line.toString() + newLine);
		}
		f0.close();




	}



	public static void save(Integer[] lines, String fileName) throws IOException {

		if ((lines==null)||("".equals(fileName))){
			return;
		}
		FileWriter f0 = new FileWriter(fileName);

		String newLine = System.getProperty("line.separator");


		for( Integer line : lines)
		{
			f0.write( line.toString() + newLine);
		}
		f0.close();
	}


	public static byte[] readBytes(Path fileName) throws IOException{
		return Files.readAllBytes(fileName);
	}

	public static void writeBytes(byte[] bytes,String fileName) throws IOException{
		File file = new File(fileName);
		try (FileOutputStream fop = new FileOutputStream(file)) {
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			fop.write(bytes);
			fop.flush();
			fop.close();
		}catch (IOException e) {
			Logging.log(e.toString());
		}


	}

	public static TreeMap<String,String> parseCFG(String cfgFileName) throws IOException{
		final String COMMENT =";"; 
		TreeMap<String,String> result=new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		ArrayList<String> lines =load(cfgFileName);
		int i = 1 ;
		String[] terms;
		for (String line: lines){
			terms=line.split(COMMENT);

			if (terms.length>=1){
				//split on first occurrence of '='
				terms=terms[0].split("=",2);
				if (terms.length==2){
					result.put(terms[0], terms[1]);
				} else {
					// log("CFG file ["+cfgFileName+"] line #"+i+" ignored.");
				}
			}
			i++;
		}
		//TODO test case: no ; in line
		return result;
	}




//	public static void extract(String dest) throws URISyntaxException, IOException{
//		File destDir=new File(dest);
//
//		if (! destDir.exists()){
//			if (!destDir.mkdir()){
//				Logging.log("Failed to create directory: "+dest);
//			}
//			CodeSource codeSource =  I4ViewTest.class.getProtectionDomain().getCodeSource();
//			File jarFile = new File(codeSource.getLocation().toURI().getPath());
//			Logging.log("Jar file path:"+jarFile.getAbsolutePath());
//			JarHelper jh=new JarHelper();
//			Logging.log("calling extracting module ...");
//			jh.setVerbose(true);
//			jh.unjarDir(jarFile, destDir);
//			Logging.log("extract module done.");
//		}
//
//
//
//	}

	public static boolean compare(String fileA,String fileB){
		boolean result=false;
		try {
			Logging.log("Comparing: ["+fileA+"] and ["+fileB+"]");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Logging.log(e1.toString());
		}
		File file1 = new File(fileA);
        File file2 = new File(fileB);
        try {
			result = FileUtils.contentEquals(file1, file2);//contentEquals(file1, file2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logging.log(e.toString());
		}
        if (result){
        	try {
				Logging.log("Comparing Match.");
			} catch (Exception e) {
				Logging.log(e.toString());
			}
        } else{
        	try {
				Logging.log("Comparing mismatch.");
			} catch (Exception e) {
				Logging.log(e.toString());
			}
        }
		return result;
	}

	public static String getStartLocation(){
		String result;
			result=System.getProperty("user.dir")+File.separator;//TextFiles.class.getProtectionDomain().getCodeSource().getLocation().getPath().toLowerCase().substring(1);
                        //log("start Location: "+result);
			
                        /*
                        File jarFile=new File(result);
			if (result.contains(".jar")){
				result=result.substring(0,result.length()-4);
				while(!(result.substring(result.length()-1).equals("\\")||result.substring(result.length()-1).equals("/"))){
					result=result.substring(0, result.length()-1);
				}
			} else{
				Logging.log("not running from a jar file.");
			}
                        log("start Location: "+result);
                        */
		return (result);
	}

	/**
	 * Literally Replaces all occurrences of oldString with newString in sourceString using String.replaceAll() method !
	 * @param sourceString
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static String replaceAll(String sourceString, String oldString,String newString ){
		return sourceString.replaceAll(Matcher.quoteReplacement(oldString), Matcher.quoteReplacement(newString));
	}
	
	public static String makeLink(String... fileNames){
		String result="";
		int i=1;
		for (String fileName: fileNames){
			result+="<a href=\"file///"+fileName+"\" download>"+ i++ +"</a> ";
		}
		return result;
	}
	
	public static String JAR_BUILD=getBuildDate();
	
	public static String getBuildDate(){
		String result="";
		result=TextFiles.class.getProtectionDomain().getCodeSource().getLocation().getPath().toLowerCase().substring(1);
		if (result.indexOf(".jar")!=-1){
			result=timeStamp.format((new File(result).lastModified()));
		} else{
			result=getTimeStamp();
		}
		return result;
	}	
	
	
	public static void main(String[] args) throws IOException{
		System.out.println(compare("d:/downloads/Exported-701.xml","C:/SiLKTestDemo/data/exports/FTS/export.xml"));
	}
	

}
