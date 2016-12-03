/**
 * 
 */
package model;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import util.DoubleValueComparator;
import util.FileFunction;
import util.NewsFileReader;
import util.TermFunction;
import util.TweetFunction3;

/**
 * @author kjysmu
 *
 */
public class Gen_IDF {
	
	String UTF8_BOM = "\uFEFF";
    public String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }
    
	public Gen_IDF(){
		
	}
	public void run() throws Exception{
		
		String newsRootDirectory = "D:\\Development\\Data\\News";
		Double threshold = 0.0001;
		
		for(int i=1; i<=20; i++){
			
			String path = newsRootDirectory + i + "\\TF";
			
			System.out.println("Corpus : " + i);
			
			Map<String, Map<String,Double>> TF_Map = FileFunction.readMapStr_StrDou(path);
	    	int totalCategory = 0;
	        Map<String, Double> termCounts = new HashMap<String, Double>();
			for (Map.Entry<String, Map<String, Double>> entry : TF_Map.entrySet()) {
	        	totalCategory++;

				String key = entry.getKey();
				Map<String, Double> value = entry.getValue();
				TermFunction.RemoveStopWords(value);
				
				Map<String, Double> valueNorm = TermFunction.getNorm(value);
				
				Map<String, Double> filteredValue = new HashMap<String, Double>();
				
				for (Map.Entry<String, Double> entry2 : valueNorm.entrySet()) {
					String key2 = entry2.getKey();
					Double value2 = entry2.getValue();
					
					if(value2 >= threshold){
						filteredValue.put(key2, value2);
					}

				}
				
				filteredValue = TermFunction.getNorm(filteredValue);
				
				for (Map.Entry<String, Double> entry2 : filteredValue.entrySet()) {
					String key2 = entry2.getKey();
					Double value2 = entry2.getValue();
	        		if(termCounts.containsKey(key2))
	        			termCounts.put(key2, termCounts.get(key2) + 1.0  );
	        		else
	        			termCounts.put(key2, 1.0);
				}

			}
			
	        Map<String, Double> idf = new HashMap<String, Double>();
	        for(Map.Entry<String, Double> termCount : termCounts.entrySet()){
	        	idf.put(termCount.getKey(), Math.log( totalCategory / (double)termCount.getValue() ));
	        }
	        
	        FileFunction.writeMapStrDou(idf, newsRootDirectory + i + "//IDF//NaverIDF.txt");
				
		}


	}
}
