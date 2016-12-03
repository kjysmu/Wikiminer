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

import org.omg.CORBA.SystemException;

import modules.WikimapNew;
import util.FileFunction;

public class ClusteringWikiCategoryFast {
	public static void main(String args[]) throws Exception{

		String newsRootDirectory = "D:\\Development\\DATA\\News";
		
		String wcSimDir = "D:\\Development\\Wikipedia\\0.33 0.67";
		String oldCFICFDir = "D:\\Development\\NaverCategoryModel\\WikiCFICF";
		
		List<File> wcSimList = FileFunction.getListOfFiles(wcSimDir);
		List<File> ocList = FileFunction.getListOfFiles(oldCFICFDir);
		
		
		
		
		Map<String, Double> simMap = new HashMap<String, Double>();
		
		//Map<String, Map<String, Double>> simMap2 = new HashMap<String, Map<String, Double>>();
		
		
		
		Map<String, Double> simHistoryMap = new HashMap<String, Double>();

		Map<String, Map<String,Double>> wcMap = FileFunction.readMapStr_StrDou(oldCFICFDir);
		
		System.out.println("History Loading");
		
		
		
		for(File f : wcSimList){
			
			BufferedReader brWC = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(f), "UTF-8"));
			
			String line = "";
			
			boolean isFirst = true;
			
			while ((line = brWC.readLine()) != null) {
				
				if(isFirst){
					line = FileFunction.removeUTF8BOM(line);
					isFirst = false;
				}
				
				
				StringTokenizer token = new StringTokenizer(line, " ");
				if(token.countTokens() == 3){
					String wc1 = token.nextToken();
					String wc2 = token.nextToken();
					String score = token.nextToken();
					
					simHistoryMap.put(wc1+" "+wc2, Double.parseDouble(score));
					simHistoryMap.put(wc2+" "+wc1, Double.parseDouble(score));
					
				}
			}
			
			brWC.close();
		
		}
		System.out.println("History Loading Complete");
		System.out.println(simHistoryMap.size());
		System.out.println("History Loading2");
		
		/*
		for(File f : ocList){
			Map<String, Double> tcount = new HashMap<String, Double>();
			BufferedReader brWC = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(f), "UTF-8"));
			
			String line = "";
			
			boolean isFirst = true;
			
			while ((line = brWC.readLine()) != null) {
				
				if(isFirst){
					line = FileFunction.removeUTF8BOM(line);
					isFirst = false;
				}
				
				
				StringTokenizer token = new StringTokenizer(line, "\t");
				if(token.countTokens() == 2){
					String token1 = token.nextToken();
					String token2 = token.nextToken();
					
					tcount.put(token1, Double.parseDouble(token2));
				}
			}
			
			brWC.close();
			HashSet<String> set = new HashSet<String>();

			for(Map.Entry<String, Double> termCount1 : tcount.entrySet()){
				set.add(termCount1.getKey());
				for(Map.Entry<String, Double> termCount2 : tcount.entrySet()){
					if(set.contains(termCount2.getKey())) continue;
					
					String key1 = termCount1.getKey().replaceAll(" ", "_") +" "+ termCount2.getKey().replaceAll(" ", "_");
					String key2 = termCount2.getKey().replaceAll(" ", "_") +" "+ termCount1.getKey().replaceAll(" ", "_");
					
					if(!simHistoryMap.containsKey(key1)){
						simHistoryMap.put(key1, 0.0);
						simHistoryMap.put(key2, 0.0);	
					}
				}
			}
		}
		*/
		
		System.out.println("History Loading Complete2");
		
		System.out.println(simHistoryMap.size());
		
		boolean isStart = false;
		
		
		for(int i=1; i<=20; i++){
			
			System.out.println("Corpus : " + i);
			
			List<File> dates = FileFunction.getListOfFiles(newsRootDirectory+i+"\\WIKI-CFICF");
			
			//Map<String, Double> simMap = new HashMap<String, Double>();

			WikiCategorySim wcs = new WikiCategorySim();
			for(File d : dates){

				System.out.println(d.getName());
				
				
				
				if(!isStart){
					if( d.getName().equals("스포츠 해외축구.txt") ){
						isStart = true;
					}else{
						continue;
					}
				}
				
				/*
				if(simMap2.containsKey(d.getName())){
					
					simMap = new HashMap<String, Double>();
					simMap.putAll(simMap2.get(d.getName()));
					
				}else{
					
					simMap = new HashMap<String, Double>();

				}*/
				
				
				
				Map<String, Double> wc = wcMap.get(d.getName().replaceAll(".txt", ""));
				System.out.println(wc.size());
				
				
				
				BufferedReader br = new BufferedReader(
						   new InputStreamReader(
				                      new FileInputStream(d), "UTF-8"));
				
				
				BufferedWriter bw2 = new BufferedWriter(
						   new OutputStreamWriter(
				                      new FileOutputStream(newsRootDirectory+i+"\\WIKI-Cluster\\wcSimAll\\"+d.getName()), "UTF-8"));
				
				String line = "";
				
				Map<String, Double> tcount = new HashMap<String, Double>();

				boolean isFirst = true;
				while ((line = br.readLine()) != null) {
					
					if(isFirst){
						line = FileFunction.removeUTF8BOM(line);
						isFirst = false;
					}
					
					StringTokenizer token = new StringTokenizer(line, "\t");
					String term = token.nextToken();
					String svalue = token.nextToken();

					double value = Double.parseDouble(svalue);
					tcount.put(term, value);
					
					//if(value >= 0.0002) tcount.put(term, value);

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
							
							if(simHistoryMap.containsKey(key1)){
								sim = simHistoryMap.get(key1);
								
								//System.out.println("1:" + sim);

							}else{
								
								if( wc.containsKey(termCount1.getKey()) &&  wc.containsKey(termCount2.getKey())){
									sim = 0.0;
									//System.out.println("x:" + sim);

								}else{
									if(simMap.containsKey(key1)){
										sim = simMap.get(key1);
										
										//System.out.println("2:" + sim);

									}else{
										sim = wcs.getSimilarity(termCount1.getKey(), termCount2.getKey());
										
										//System.out.println("3:" + sim);
										
										simMap.put(key1, sim);
										simMap.put(key2, sim);
									}
								}
							}
							
							/*
							if(simMap.containsKey(key1)){
								sim = simMap.get(key1);
							}else if(simMap.containsKey(key2)){
								sim = simMap.get(key2);
							}else{
								sim = wcs.getSimilarity(termCount1.getKey(), termCount2.getKey());
								
								simMap.put(key1, sim);
								simMap.put(key2, sim);
								
							}*/
							
							
							if(sim >= 0.0001){
								//bw2.write(termCount1.getKey().replaceAll(" ", "_") +" "+ termCount2.getKey().replaceAll(" ", "_") + " " + String.format("%.4f", sim));
								
								bw2.write(key1 + " " + String.format("%.4f", sim));
								bw2.newLine();
								
							}
							double progress = ct / (double) tcount_size ;
							
							if(progress - tmp >= 0.05 ){
								tmp = tmp + 0.05;
								
								System.out.print(">");
							}
						}
					}
				}
				System.out.println();
				br.close();
				bw2.close();
				
				
				
				//simMap2.put(d.getName(), simMap);
				
			}	
		}
		
		System.out.println("complete");
	
	}

}
