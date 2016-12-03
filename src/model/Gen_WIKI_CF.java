package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import modules.Wikimap;
import modules.WikimapNew;

//import parameter.Path;

import util.FileFunction;
import util.TermFunction;


// import modules.*;

public class Gen_WIKI_CF {
	public Gen_WIKI_CF(){
		
	}
	public void run() throws Exception{
		
		System.out.println("start-cf");
		String newsRootDirectory = "D:\\Development\\DATA\\News";
		
		WikimapNew wikimap = new WikimapNew();
		
		for(int i=1; i<=20; i++){
			
			System.out.println("Corpus : " + i);
			
			// String path = newsRootDirectory + i + "\\TF";
			
			String sourcePath = newsRootDirectory + i + "\\TFIDF";
			String targetPath = newsRootDirectory + i + "\\WIKI-CF\\";
			
			Map<String, Map<String,Double>> TFIDF_Map = FileFunction.readMapStr_StrDou(sourcePath);
			Map<String, Map<String,Double>> WIKI_CF_Map = new HashMap<String, Map<String, Double>>();
			
			for (Entry<String, Map<String, Double>> entry : TFIDF_Map.entrySet()) {
				String key = entry.getKey();
				//System.out.println("Loading "+key);
				Map<String,Double> categoryMap = new HashMap<String,Double>();
				
				Map<String, Double> value = entry.getValue();
				for (Entry<String, Double> entry2 : value.entrySet()) {
					String key2 = entry2.getKey();
					Double value2 = entry2.getValue();
					// TFIDF to WIKI-Category conversion method -----------------------
					
					//List<String> categoryList = wikimap.getCategory(key2);
					Set<String> categoryList = wikimap.getCategory(key2);
					
					for( String str : categoryList ){
						//if(str.contains("년")) continue;
						if(categoryMap.containsKey(str)){
							categoryMap.put(str, categoryMap.get(str) + value2);
						}else{
							categoryMap.put(str, value2);
						}
					}
					//  -------------------------------------------------------
				}
				WIKI_CF_Map.put(key, categoryMap);
			}
			FileFunction.writeMapStr_StrDou(WIKI_CF_Map, targetPath);
			
			
		}
		System.out.println("complete-cf");
		
	}
		
		
		
	
}
