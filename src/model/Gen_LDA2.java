package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import util.DocumentFunction;
import util.FileFunction;
import util.TermFunction;

public class Gen_LDA2 {
	
	public static void main(String args[]) throws Exception{
		
		String newsRootDirectory = "D:\\Development\\Data\\News";

		String LDAModel = "D:\\Development\\Data\\NewsX\\NaverLDAtrainingAll.txt";
		
		String LDAModel2k = "D:\\Development\\Data\\NewsX\\NaverLDAtrainingAll2k.txt";
		String LDAModel4k = "D:\\Development\\Data\\NewsX\\NaverLDAtrainingAll4k.txt";
		
		BufferedReader reader = new BufferedReader(
				   new InputStreamReader(
		                      new FileInputStream(LDAModel), "UTF-8"));
		
		BufferedWriter writer2k = new BufferedWriter(
				   new OutputStreamWriter(
		                      new FileOutputStream(LDAModel2k), "UTF-8"));
		
		BufferedWriter writer4k = new BufferedWriter(
				   new OutputStreamWriter(
		                      new FileOutputStream(LDAModel4k), "UTF-8"));
		
		String data = "";
		
		Map<String, Map<String, Double>> topicMap = new HashMap<String, Map<String, Double>>();
		
		
		int tct = 0;
		
		while ((data = reader.readLine()) != null) {
			
			
			StringTokenizer tk = new StringTokenizer(data, "\t");
			if(tk.countTokens() >= 3){
				String token1= tk.nextToken();
				String token2= tk.nextToken();
				String token3= tk.nextToken();
				
				if(tct >= 116){
					writer4k.write(tct + "\t" + token2 + "\t" + token3);
					writer4k.newLine();
						
				}else{
					writer2k.write(tct + "\t" + token2 + "\t" + token3);
					writer2k.newLine();
						
				}
				
				tct++;
				
				StringTokenizer tk2 = new StringTokenizer(token3, " ");

				Map<String, Double> topicMapSub = new HashMap<String, Double>();
				
				while( tk2.hasMoreTokens() ){
					String token3_1= tk2.nextToken();
					String token3_2= tk2.nextToken();
					token3_2 = token3_2.substring(1, token3_2.length()-1);
					
					//System.out.println("3-1:"+token3_1);
					//System.out.println("3-2:"+token3_2);
					
					topicMapSub.put(token3_1, Double.parseDouble(token3_2));
					
				} 
				
				topicMap.put(token1, topicMapSub);
				
			} // End of If
		} // End of While - DATA
		
		reader.close();
		
		BufferedReader reader2 = new BufferedReader(
				   new InputStreamReader(
		                      new FileInputStream(LDAModel), "UTF-8"));
		
		
		while ((data = reader2.readLine()) != null) {
			
			
			StringTokenizer tk = new StringTokenizer(data, "\t");
			if(tk.countTokens() >= 3){
				String token1= tk.nextToken();
				String token2= tk.nextToken();
				String token3= tk.nextToken();
				
				writer4k.write(tct + "\t" + token2 + "\t" + token3);
				writer4k.newLine();
				tct++;
				
				StringTokenizer tk2 = new StringTokenizer(token3, " ");

				Map<String, Double> topicMapSub = new HashMap<String, Double>();
				
				while( tk2.hasMoreTokens() ){
					String token3_1= tk2.nextToken();
					String token3_2= tk2.nextToken();
					token3_2 = token3_2.substring(1, token3_2.length()-1);
					
					//System.out.println("3-1:"+token3_1);
					//System.out.println("3-2:"+token3_2);
					
					topicMapSub.put(token3_1, Double.parseDouble(token3_2));
					
				} 
				
				topicMap.put(token1, topicMapSub);
				
			} // End of If
		} // End of While - DATA
		reader2.close();
		
		
		
		writer2k.close();
		writer4k.close();
		/*
		
		for(int i=1; i<=20; i++){
			
			System.out.println( "Dataset index : " + i );
			
			String pathLDA = newsRootDirectory + i + "\\LDA2\\";
			String pathTFIDF = newsRootDirectory + i + "\\TFIDF";
			
			Map<String, Map<String,Double>> TFIDF_Map = FileFunction.readMapStr_StrDou(pathTFIDF);
			
			for (Entry<String, Map<String, Double>> entry : TFIDF_Map.entrySet()) {
				String key = entry.getKey();
				
				Map<String,Double> value = TermFunction.getNorm( entry.getValue() );
				Map<String,Double> TopicDistMap = new HashMap<String, Double>();
				
				for (Entry<String, Map<String, Double>> entry2 : topicMap.entrySet()) {
					
					String key2 = entry2.getKey();
					Map<String,Double> value2 = TermFunction.getNorm( entry2.getValue() );

					Double sim = DocumentFunction.ComputeCosineSimilarity(value, value2);					
					TopicDistMap.put(key2, sim);
	
				}
				
				Map<String, Double> TopicDistMapNorm = TermFunction.getNorm(TopicDistMap);

				FileFunction.writeMapStrDou(TopicDistMapNorm, pathLDA + key + ".txt");

			}
			
			
		}
		*/
		
	}

}
