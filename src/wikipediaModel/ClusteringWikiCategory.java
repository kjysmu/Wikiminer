package wikipediaModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import modules.WikimapNew;
import util.FileFunction;

public class ClusteringWikiCategory {
	public static void main(String args[]) throws Exception{

		String newsRootDirectory = "D:\\Development\\DATA\\News";

		Map<String, Double> simMap = new HashMap<String, Double>();

		
		for(int i=1; i<=20; i++){
			
			System.out.println("Corpus : " + i);
			
			List<File> dates = FileFunction.getListOfFiles(newsRootDirectory+i+"\\WIKI-CFICF");
			//Map<String, Double> simMap = new HashMap<String, Double>();

			WikiCategorySim wcs = new WikiCategorySim();
			for(File d : dates){

				System.out.println(d.getName());

				BufferedReader br = new BufferedReader(
						   new InputStreamReader(
				                      new FileInputStream(d), "UTF-8"));
				
				
				BufferedWriter bw2 = new BufferedWriter(
						   new OutputStreamWriter(
				                      new FileOutputStream(newsRootDirectory+i+"\\WIKI-Cluster\\wcSim\\"+d.getName()), "UTF-8"));
				
				String line = "";
				Map<String, Double> tcount = new HashMap<String, Double>();

				while ((line = br.readLine()) != null) {
					StringTokenizer token = new StringTokenizer(line, "\t");
					String term = token.nextToken();
					String svalue = token.nextToken();

					double value = Double.parseDouble(svalue);
					//tcount.put(term, value);
					
					if(value >= 0.0002) tcount.put(term, value);

				}
				int ct = 0;
				System.out.println("size : " + tcount.size());
				int tcount_size = tcount.size() * tcount.size();
				double tmp = 0.0;
				HashSet<String> set = new HashSet<String>();

				for(Map.Entry<String, Double> termCount1 : tcount.entrySet()){
					set.add(termCount1.getKey());
					for(Map.Entry<String, Double> termCount2 : tcount.entrySet()){
						ct++;

						if(set.contains(termCount2.getKey())) continue;
						
						if( !termCount1.getKey().equals( termCount2.getKey() )){
							Double sim = 0.0;
							String key1 = termCount1.getKey().replaceAll(" ", "_") +" "+ termCount2.getKey().replaceAll(" ", "_");
							String key2 = termCount2.getKey().replaceAll(" ", "_") +" "+ termCount1.getKey().replaceAll(" ", "_");
							
							if(simMap.containsKey(key1)){
								sim = simMap.get(key1);
							}else if(simMap.containsKey(key2)){
								sim = simMap.get(key2);
							}else{
								sim = wcs.getSimilarity(termCount1.getKey(), termCount2.getKey());
								
								simMap.put(key1, sim);
								simMap.put(key2, sim);
								
							}
							
							if(sim >= 0.0001){
								bw2.write(termCount1.getKey().replaceAll(" ", "_") +" "+ termCount2.getKey().replaceAll(" ", "_") + " " + String.format("%.4f", sim));
								bw2.newLine();
							}
							double progress = ct / (double) tcount_size ;
							if(progress - tmp >= 0.02 ){
								tmp = tmp + 0.02;
								System.out.print(">");
							}
						}
					}
				}
				System.out.println();
				br.close();
				bw2.close();
			}	
		}
		
		System.out.println("complete");
	
	}

}
