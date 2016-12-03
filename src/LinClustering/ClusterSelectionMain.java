package LinClustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import parameter.Path;
import parameter.Path2;
import util.DocumentFunction;
import util.FileFunction;
import util.TermFunction;

public class ClusterSelectionMain {
	
	public static void main(String args[]) throws Exception{
		
		String newsRootDirectory = "D:\\Development\\DATA\\News";
		
		Path2 path = new Path2(1);
		
		for(int i=1; i<=20; i++){
			
			path = new Path2(i);
			
			System.out.println("Corpus : " + i);
			
			String path1 = newsRootDirectory + i +"\\WIKI-Cluster\\wcSim\\";
			String path2 = newsRootDirectory + i +"\\WIKI-Cluster\\cluster\\";
			
			String pathSim = newsRootDirectory + i +"\\WIKI-Cluster\\clusterSim_icf\\";
			
			String sourcePath = newsRootDirectory + i + "\\WIKI-CFICF\\";
			
			//String clusterpath = "D:\\Development\\NaverCategoryModel\\WikiCFICF_CS\\cluster\\0.5 0.5\\";
			//String path2 = "D:\\Development\\WikiCluster\\all\\0.5 0.5\\results\\";

			List<File> fileListWIKI = FileFunction.getListOfFiles(path2);
			
			Map<String,Double> WikiICF = FileFunction.readMapStrDou(path.WIKI_ICF_FILEPATH);

			
			
			String line = "";
			
			Map<String, Map<String,Double>> map = FileFunction.readMapStr_StrDou(sourcePath);

			for (File file : fileListWIKI) {
				
				Map<String,Double> wikiCFICF = map.get( file.getName().replaceAll(".txt", "") );
				
				System.out.println(file.getName());
				//System.out.println(wikiCFICF.size());

				FileReader fr = new FileReader ( file );
				BufferedReader br = new BufferedReader (fr);
				
				Map<String, Map<String,Double>> clusterMap = new HashMap<String, Map<String,Double>>();
				Map<String,Double> categoryMap = new HashMap<String,Double>();
				String clusterTmp = "";
				Boolean isFst = true;
				
				while (true) {
					line = br.readLine();
					if( line == null ){
						clusterMap.put(clusterTmp, categoryMap);
						break;
					}
					StringTokenizer token = new StringTokenizer(line, "\t");
					if(token.countTokens() == 3){
						String category = token.nextToken().replaceAll("_", " ");
						String nodeScore = token.nextToken();
						String cluster = token.nextToken();
						if(isFst){
							clusterTmp = cluster;
							isFst = false;
							categoryMap = new HashMap<String,Double>();
						}
						
						if(clusterTmp.equals(cluster)){
							categoryMap.put(category, Double.parseDouble(nodeScore));
							//System.out.println(category);
						}else{
							clusterMap.put(clusterTmp, categoryMap);
							clusterTmp = cluster;
							categoryMap = new HashMap<String,Double>();
							categoryMap.put(category, Double.parseDouble(nodeScore));
						}
					}
				}
				
				br.close();
				fr.close();
				
				Map<String, Double> clusterSimMap = new HashMap<String, Double>();
				Map<String, Double> wikiCFICF_CS = new HashMap<String, Double>();
				
				for(Map.Entry<String, Map<String,Double>> entry : clusterMap.entrySet() ){
					String clusterKey = entry.getKey();
					
					Map<String,Double> mapValue = entry.getValue();
					
					Map<String,Double> mapValueNorm = TermFunction.getNorm(mapValue);

					double sim = DocumentFunction.ComputeCosineSimilarity( wikiCFICF , DocumentFunction.GetTFIDF(mapValueNorm, WikiICF) );
					
					clusterSimMap.put(clusterKey, sim);
					
					/*
					for(Map.Entry<String, Double> entry2 : mapValueNorm.entrySet() ){
						wikiCFICF_CS.put(entry2.getKey(), wikiCFICF.get(entry2.getKey()) * sim * entry2.getValue() );						
					}
					*/
					
				}
				//wikiCFICF_CS = TermFunction.getNorm(wikiCFICF_CS);
				
				//FileFunction.writeMapStrDou(wikiCFICF_CS, clusterpath + file.getName());

				FileFunction.writeMapStrDou(clusterSimMap, pathSim + file.getName());
		
		}
		


		
		}
		
	}

}
