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

import parameter.Path;
import util.DoubleValueComparator;
import util.FileFunction;
import util.TermFunction;

public class Gen_WIKI_AFIAF {
	
	public Gen_WIKI_AFIAF(){
		
	}
	
	public void run() throws Exception{

		System.out.println("Start-esa-afiaf");
		
		String newsRootDirectory = "D:\\Development\\DATA\\News";
		
		for(int i=1; i<=20; i++){
			System.out.println("Corpus : " + i);

			Map<String, Double> WikiIAF = new HashMap<String, Double>();
			
			BufferedReader br_WikiIAF = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(newsRootDirectory + i + "\\WIKI-IAF\\WikiIAF.txt"), "UTF-8"));
			
			
			String line = "";
			while ((line = br_WikiIAF.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "\t");
				if(token.countTokens() >= 2){
					String token1= token.nextToken();
					String token2= token.nextToken();
					WikiIAF.put( token1, Double.parseDouble( token2 ) );
				}
			}			
			br_WikiIAF.close();
			
			List<File> fileListWIKI = FileFunction.getListOfFiles(newsRootDirectory + i + "\\WIKI-AF");

			for (File file : fileListWIKI) {
		        Map<String, Double> WikiAFIAF = new HashMap<String, Double>();

				BufferedReader br = new BufferedReader(
						   new InputStreamReader(
				                      new FileInputStream(file), "UTF-8"));
				
				line = "";
				while ((line = br.readLine()) != null) {
					StringTokenizer token = new StringTokenizer(line, "\t");
					if(token.countTokens() >= 2){
						String key= token.nextToken();
						String value= token.nextToken();
						
						if(Double.parseDouble(value) > 0.00001 ){
							if(WikiIAF.containsKey(key)){
								WikiAFIAF.put(key, Double.parseDouble(value) * WikiIAF.get(key) );				
							}else{
								//WikiCFICF.put(key, Double.parseDouble(value) * 0.0 );				
							}
						}

					}
				}
				br.close();
				
				//Map<String, Double> WikiAFIAF_Norm = TermFunction.getNorm(WikiAFIAF);
				
				Map<String, Double> WikiAFIAF_Norm = TermFunction.getNormSquare(WikiAFIAF);
				
				FileFunction.writeMapStrDou(WikiAFIAF_Norm, newsRootDirectory + i + "\\WIKI-AFIAF-L2NORM\\" + file.getName());
					
			}
			
		}
		
		System.out.println("Complete-esa-afiaf");
	
	}


	
}
