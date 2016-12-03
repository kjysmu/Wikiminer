package wikipediaModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import parameter.Path;
import util.DoubleValueComparator;
import util.FileFunction;
import util.TermFunction;


public class CScoreGen {
	public static void main(String args[]) throws Exception{
		
		List<File> dates = FileFunction.getListOfFiles("D:\\Development\\WikiCluster\\all");
		String line ="";
		for(File d : dates){
			System.out.println(d.getName());
			
			FileReader fr = new FileReader ( d );
			BufferedReader br = new BufferedReader (fr);
			
			Map<String, Double> csMap = new HashMap<String, Double>(); 

			while ((line = br.readLine()) != null) {
				
				line = line.replaceAll(" > ","\t");
				
				StringTokenizer token = new StringTokenizer(line, "\t");
				
				if(token.countTokens() == 3){
					String term1 = token.nextToken();
					String term2 = token.nextToken();
					String svalue = token.nextToken();
					double value = Double.parseDouble(svalue);

					if(csMap.containsKey(term1)){
						csMap.put(term1, csMap.get(term1)+value);
					}else{
						csMap.put(term1, value);
					}
					
					if(csMap.containsKey(term2)){
						csMap.put(term2, csMap.get(term2)+value);
					}else{
						csMap.put(term2, value);
					}
				}
			}
			
			br.close();
			fr.close();
			
			csMap = TermFunction.getNorm(csMap);
			
			FileWriter fw = new FileWriter("D:\\Development\\WikiCluster\\all\\cscore\\"+d.getName());
			BufferedWriter bw = new BufferedWriter(fw);
			
			DoubleValueComparator bvc = new DoubleValueComparator(csMap);
			TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
			tMap.putAll(csMap);
			
	    	for(Map.Entry<String, Double> entry : tMap.entrySet() ){
	    		bw.write ( entry.getKey() + "\t" + entry.getValue() );
	    		bw.newLine();
	    	}
	    	
	    	bw.close();
	    	fw.close();
	    	
		}
		System.out.println("complete");

	
	}

}
