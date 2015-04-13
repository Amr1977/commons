package common;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static logging.Logging.*;



public class Timer {
public static ArrayList<TimerNode> timersList=new ArrayList<>();

public static void start(String timerId){
	try {
		log("Timer started: "+timerId);
	} catch (Exception e) {
		log(e.toString());
	}
	TimerNode node=new TimerNode();
	node.timerID=timerId;
	node.time = System.currentTimeMillis( );
	
		timersList.add(node);
	
	
}

public static TimerNode getNode(String timerId){
	if (!"".equals(timerId)){
		for(TimerNode node : timersList){
			if (node.timerID.equals(timerId)){
				return node;
			}
		}
	}
	return null;
	
}

public static long getTime(String timerId){
	TimerNode node=Timer.getNode(timerId);
	if (node==null){
		return 0;
		
	}
	Long result=System.currentTimeMillis( )- node.time;
	return result;
}
public static long end(String timerId){
	
	TimerNode node=Timer.getNode(timerId);
	if (node==null){
		return 0;
		
	}
	Long result=System.currentTimeMillis( )- node.time;
	Timer.timersList.remove(node);
	try {
		log("Timer stopped: "+timerId+" , recorded time(ms): "+result);
	} catch (Exception e) {
		log(e.toString());
	}
	return result;
	
	
}

public static void remove(String timerId){
	
	Timer.timersList.remove(Timer.getNode(timerId));
}

public static void clear(){
	timersList.clear();
}


public static boolean isLeapYear(Integer year){
	/*
	 * http://en.wikipedia.org/wiki/Leap_year
	 * 
	 * 
	 * if year is not divisible by 4 then common year
		else if year is not divisible by 100 then leap year
		else if year is not divisible by 400 then common year
		else leap year
	 */
	boolean result=false;
	final boolean LEAP_YEAR=true;
	final boolean COMMON_YEAR=false;
	if (!((year % 4)==0)){
		result=COMMON_YEAR;
	} else if (!((year % 100)==0)){
		result=LEAP_YEAR;
	}else if (!((year % 400)==0)){
		result=COMMON_YEAR;
	}else {
		result=LEAP_YEAR;
	}
	
	
	return result;
}

public static boolean isLeapYearTest(){
	boolean result=true;
	int[] leapYears=new int[]{
			1804,
			1808,
			1812,
			1816,
			1820,
			1824,
			1828,
			1832,
			1836,
			1840,
			1844,
			1848,
			1852,
			1856,
			1860,
			1864,
			1868,
			1872,
			1876,
			1880,
			1884,
			1888,
			1892,
			1896,
			1904,
			1908,
			1912,
			1916,
			1920,
			1924,
			1928,
			1932,
			1936,
			1940,
			1944,
			1948,
			1952,
			1956,
			1960,
			1964,
			1968,
			1972,
			1976,
			1980,
			1984,
			1988,
			1992,
			1996,
			2000,
			2004,
			2008,
			2012,
			2016,
			2020,
			2024,
			2028,
			2032,
			2036,
			2040,
			2044,
			2048,
			2052,
			2056,
			2060,
			2064,
			2068,
			2072,
			2076,
			2080,
			2084,
			2088,
			2092,
			2096,
			2104,
			2108,
			2112,
			2116,
			2120,
			2124,
			2128,
			2132,
			2136,
			2140,
			2144,
			2148,
			2152,
			2156,
			2160,
			2164,
			2168,
			2172,
			2176,
			2180,
			2184,
			2188,
			2192,
			2196,
			2204,
			2208,
			2212,
			2216,
			2220,
			2224,
			2228,
			2232,
			2236,
			2240,
			2244,
			2248,
			2252,
			2256,
			2260,
			2264,
			2268,
			2272,
			2276,
			2280,
			2284,
			2288,
			2292,
			2296,
			2304,
			2308,
			2312,
			2316,
			2320,
			2324,
			2328,
			2332,
			2336,
			2340,
			2344,
			2348,
			2352,
			2356,
			2360,
			2364,
			2368,
			2372,
			2376,
			2380,
			2384,
			2388,
			2392,
			2396,
			2400,
	};
	for (int i=0;i<leapYears.length;i++){
		System.out.println(leapYears[i]+" : "+(isLeapYear(leapYears[i])? "**Leap" : "Common"));
		result=(result && isLeapYear(leapYears[i]));
	}
	
	System.out.println("Test Result: "+(result? "Success": "Fail"));
	return result;
}

public static void main(String[] args){
	isLeapYearTest();
	File timestamps=new File("c:/100000-random-timestamps.txt");
	for (int i=1;i<=100000;i++){
		//generate and save time stamp
	}
	
}
}
