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

import util.FileFunction;


public class ClusterWikiCategoryUtil {

	public static void main(String args[]) throws Exception{
		
		List<File> dates = FileFunction.getListOfFiles("D://Development//NaverCategoryModel//WikiCFICF");
		
		List<File> listSim = FileFunction.getListOfFiles("D://Development//WikiCluster//all");

		for(File d : listSim){
			String line = "";
			Map<String, Double> tcount = new HashMap<String, Double>();
			Map<String, Double> simMap = new HashMap<String, Double>();

			FileReader fr2 = new FileReader ( "D://Development//NaverCategoryModel//WikiCFICF//" + d.getName() );
			BufferedReader br2 = new BufferedReader (fr2);
			while ((line = br2.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "\t");
				String term = token.nextToken();
				
				if(term.contains("년")) continue;
				
				String svalue = token.nextToken();

				double value = Double.parseDouble(svalue);
				tcount.put(term.replaceAll(" ", "_"), value);
			}
			br2.close();
			fr2.close();
			
			FileReader fr = new FileReader ( d );
			BufferedReader br = new BufferedReader (fr);
			while ((line = br.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, " ");
				if(token.countTokens() == 3){
					String term1 = token.nextToken();
					String term2 = token.nextToken();
					String value = token.nextToken();
					
					if(tcount.containsKey(term1) && tcount.containsKey(term2) ){
						simMap.put(term1+" "+term2, Double.parseDouble(value));
					}
					
				}
			}
			br.close();
			fr.close();
			
			FileWriter fw2 = new FileWriter ( new File("D:\\Development\\WikiCluster\\all\\"+d.getName()) );
			BufferedWriter bw2 = new BufferedWriter (fw2);
			
			for(Map.Entry<String, Double> simMapEntry : simMap.entrySet()){
				bw2.write( simMapEntry.getKey() + " " + String.format("%.4f", simMapEntry.getValue()) );
				bw2.newLine();
			}
			bw2.close();
			fw2.close();

		}
		
	}
	
}
