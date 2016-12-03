package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;


import util.DoubleValueComparator;
import util.FileFunction;
import util.TermFunction;

public class Gen_TFIDF {
	public Gen_TFIDF(){

	}
	public void run() throws Exception{
		
		// String path = "D:\\Development\\DATA\\Naver News Word Counts\\Komoran-v2.1.2";
		Double threshold = 0.0001;
		String newsRootDirectory = "D:\\Development\\Data\\News";
		
		for(int i=1; i<=20; i++){
			
			String path = newsRootDirectory + i + "\\TF";
			
			System.out.println("Corpus : " + i);
			
			Map<String, Map<String,Double>> TF_Map = FileFunction.readMapStr_StrDou(path);
			
			
			Map<String, Double> NaverICF = new HashMap<String, Double>();
			
			NaverICF = FileFunction.readMapStrDou(newsRootDirectory + i + "\\IDF\\NaverIDF.txt");
			
			for (Entry<String, Map<String, Double>> entry : TF_Map.entrySet()) {
				String key = entry.getKey();
				
		        Map<String, Double> tfidf = new HashMap<String, Double>();
				
				Map<String,Double> value = TermFunction.getNorm( entry.getValue() );
				
				Map<String, Double> filteredValue = new HashMap<String, Double>();
				
				for (Entry<String, Double> entry2 : value.entrySet()) {
					String key2 = entry2.getKey();
					Double value2 = entry2.getValue();
					if(value2 >= threshold){
						filteredValue.put(key2, value2);
					}
				}
				
				filteredValue = TermFunction.getNorm(filteredValue);
				
				for (Entry<String, Double> entry2 : filteredValue.entrySet()) {
					String key2 = entry2.getKey();
					Double value2 = entry2.getValue();
		        	if(NaverICF.containsKey(key2)){
		        		tfidf.put(key2, value2 * NaverICF.get(key2) );
		        	}
		        	
				}
				
				tfidf = TermFunction.getNorm( tfidf );
				
				//tfidf = TermFunction.getNormSquare( tfidf );
				
				// FileWriter fw = new FileWriter(newsRootDirectory + i + "\\TFIDF\\" + key + ".txt");
				// BufferedWriter bw = new BufferedWriter(fw);
				BufferedWriter bw = new BufferedWriter(
						   new OutputStreamWriter(
				                      new FileOutputStream(newsRootDirectory + i + "\\TFIDF\\" + key + ".txt"), "UTF-8"));
				
				
				DoubleValueComparator bvc = new DoubleValueComparator(tfidf);
				TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
				tMap.putAll(tfidf);
				
		    	for(Map.Entry<String, Double> entry2 : tMap.entrySet() ){
		    		if(entry2.getValue() != 0.0){
			    		bw.write ( entry2.getKey() + "\t" + String.format("%.8f" , entry2.getValue()) );
			    		bw.newLine();	
		    		}
		    	}
		    	
		    	bw.close();		    	
		    	//fw.close();
			
			}
			
		}

	}
}
