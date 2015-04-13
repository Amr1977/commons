package common;

import java.io.IOException;
import java.util.ArrayList;
import static logging.Logging.*;
import static logging.TextFiles.*;

public class CSV {
	public String[] header=null;
	public String[][] table=null;
	
	public CSV(String fileName) throws Exception{
		ArrayList<String> lines=load(fileName);
		if ((lines!=null)&&(lines.size()>0)){
			//Logging.log("loaded csv file.");
			header=lines.get(0).split(",");
			table=new String[lines.size()-1][header.length];
			for(int i=1; i<lines.size();i++){
				String[] terms=lines.get(i).split(",");
				for (int j=0;j<terms.length;j++){
					table[i-1][j]=terms[j];
					//Logging.log("term "+j+" = "+terms[j]);
				}
				
			}
		} else{
			log("unable to load csv file.");
		}
		
	}
	
	
	
	public String get(int rowIndex, String colHeader) throws Exception{
		String result=null;
		int colNumber=-1;
		if (rowIndex>=0 && rowIndex<table.length){
			for (int i=0; i<header.length;i++){
				if (colHeader.equalsIgnoreCase(header[i])){
					colNumber=i;
					//Logging.log("colHeader: "+colHeader+" in colNumber="+i);
					break;
				}
			}
			if (colNumber!=-1){
				result=table[rowIndex][colNumber];
			}
		}
		return result;
	}
	
}
