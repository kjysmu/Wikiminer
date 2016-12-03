package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import modules.*;
import parameter.*;
import util.DoubleValueComparator;
import util.FileFunction;

// Input : NaverTFIDF
// Output : WikiIDF

public class Gen_WIKI_IAF {
	
	public Gen_WIKI_IAF(){
		
	}

	public void run() throws Exception{
		
		System.out.println("Start-esa-iaf");
		
		String newsRootDirectory = "D:\\Development\\DATA\\News";
		
		for(int i=1; i<=20; i++){
			
			System.out.println("Corpus : " + i);

			List<File> fileListWIKI = FileFunction.getListOfFiles(newsRootDirectory + i + "\\WIKI-AF");
			
			int totalCategory = 0;
	        
			Map<String, Double> termCounts = new HashMap<String, Double>();
	        Map<String, Double> idf = new HashMap<String, Double>();

			for (File file : fileListWIKI) {
				totalCategory++;
				
				BufferedReader br = new BufferedReader(
						   new InputStreamReader(
				                      new FileInputStream(file), "UTF-8"));
				
				
				String line = "";
				
				while ((line = br.readLine()) != null) {
					StringTokenizer token = new StringTokenizer(line, "\t");
					if(token.countTokens() >= 2){
						String key= token.nextToken();
						String value= token.nextToken();
						
						if(Double.parseDouble(value) >= 0.00001 ){
							if(termCounts.containsKey(key))
			        			termCounts.put(key, termCounts.get(key) + 1.0  );
			        		else
			        			termCounts.put(key, 1.0);
						}
						
					}
				}
				br.close();

			}
			
		    for(Map.Entry<String, Double> termCount : termCounts.entrySet()){
	        	idf.put(termCount.getKey(), Math.log( totalCategory / (double)termCount.getValue() )); 	
	        }
	        
		    FileFunction.writeMapStrDou(idf, newsRootDirectory + i + "\\WIKI-IAF\\WikiIAF.txt");
		
		}

    	
		System.out.println("Complete-esa-iaf");

    	
	}
	
}
