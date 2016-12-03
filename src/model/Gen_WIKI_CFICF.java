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

import modules.Wikimap;
import util.DoubleValueComparator;
import util.FileFunction;
import util.TermFunction;

public class Gen_WIKI_CFICF {

	public Gen_WIKI_CFICF(){
		
	}
	
	public void run() throws Exception{

		System.out.println("Start-cficf");
		
		String newsRootDirectory = "D:\\Development\\DATA\\News";
		
		// Wikimap wikimap = new Wikimap();
		
		for(int i=1; i<=20; i++){
			
			System.out.println("Corpus : " + i);
			
			// String path = newsRootDirectory + i + "\\TF";
			Map<String, Double> WikiICF = new HashMap<String, Double>();
			
			//FileReader fr_WikiICF = new FileReader(newsRootDirectory + i + "\\WIKI-ICF");
			//BufferedReader br_WikiICF = new BufferedReader(fr_WikiICF);
			
			BufferedReader br_WikiICF = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(newsRootDirectory + i + "\\WIKI-ICF\\WikiICF.txt"), "UTF-8"));
			
			String line = "";
			while ((line = br_WikiICF.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "\t");
				if(token.countTokens() >= 2){
					String token1= token.nextToken();
					String token2= token.nextToken();
					WikiICF.put( token1, Double.parseDouble( token2 ) );
				}
			}			
			br_WikiICF.close();
			
			// fr_WikiICF.close();
			
			List<File> fileListWIKI = FileFunction.getListOfFiles(newsRootDirectory + i + "\\WIKI-CF");
			for (File file : fileListWIKI) {
		        Map<String, Double> WikiCFICF = new HashMap<String, Double>();

				// FileReader fr = new FileReader(file);
				// BufferedReader br = new BufferedReader(fr);
				
				BufferedReader br = new BufferedReader(
						   new InputStreamReader(
				                      new FileInputStream(file), "UTF-8"));
				
				line = "";
				while ((line = br.readLine()) != null) {
					StringTokenizer token = new StringTokenizer(line, "\t");
					if(token.countTokens() >= 2){
						String key= token.nextToken();
						String value= token.nextToken();
						
						if(Double.parseDouble(value) > 0.0001){
							if(WikiICF.containsKey(key)){
								WikiCFICF.put(key, Double.parseDouble(value) * WikiICF.get(key) );				
							}else{
								//WikiCFICF.put(key, Double.parseDouble(value) * 0.0 );				
							}
						}

					}
				}
				br.close();
				
				//Map<String, Double> WikiCFICF_Norm = TermFunction.getNorm(WikiCFICF);
				Map<String, Double> WikiCFICF_Norm = TermFunction.getNormSquare(WikiCFICF);
				
				
				FileFunction.writeMapStrDou(WikiCFICF_Norm, newsRootDirectory + i + "\\WIKI-CFICF-L2NORM\\" + file.getName());
				
			}	
		}		
		System.out.println("Complete-cficf");

	}
	
	
	
}
