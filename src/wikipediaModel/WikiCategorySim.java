package wikipediaModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import modules.Wikimap;
import modules.WikimapNew;
import parameter.Path;
import util.DoubleValueComparator;
import util.FileFunction;


public class WikiCategorySim {
	
	Wikimap wikimap;
	//WikimapNew wikimap;
	
	Double w1;
	Double w2;
	
	public WikiCategorySim() throws Exception{
		wikimap = new Wikimap();
		
		w1 = 0.33;
		w2 = 0.67;
		
		//w1 = 0.67;
		//w2 = 0.33;
		
	}
	
	public void setSubWeight(Double d){
		w1 = 1 - d;
		w2 = d;
	}
	public void run() throws Exception{
		
		List<File> fileListWIKI = FileFunction.getListOfFiles(Path.TEST_PATH);
		for (File file : fileListWIKI) {
	        Map<String, Double> WikiCategoryScoreMap = new HashMap<String, Double>();
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			Boolean isFirst = true;
			String inputCategory = "";
			while ((line = br.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "\t");
				if(token.countTokens() >= 2){
					String category= token.nextToken();
					String value= token.nextToken();
					
					if(isFirst){
						inputCategory = category;
						isFirst = false;
					}
					
					WikiCategoryScoreMap.put( category, getSimilarity(inputCategory , category) );
					
				}
			}
			br.close();
			fr.close();
			
			FileWriter fw = new FileWriter(Path.TEST_PATH +"results\\"+ file.getName().replaceAll(".txt", "") +" "+ String.format("%.1f", w2) +".txt");
			BufferedWriter bw = new BufferedWriter(fw);
			
			DoubleValueComparator bvc = new DoubleValueComparator(WikiCategoryScoreMap);
			TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
			tMap.putAll(WikiCategoryScoreMap);
			
	    	for(Map.Entry<String, Double> entry : tMap.entrySet() ){
	    		bw.write ( entry.getKey() + "\t" + String.format("%.4f", entry.getValue()) );
	    		bw.newLine();
	    	}
	    	
	    	bw.close();
	    	fw.close();
			
		}
		
		System.out.println("finished");
		
	}
	
	public Double getSimilarity(String category1, String category2 ) throws Exception{
		
		// Step 1. calculate category score
		Double categoryScore = categorySim(category1, category2);
		
		// Step 2. calculate subCategory score
		Set<String> subCategory1 = wikimap.getSubCategory(category1);
		Set<String> subCategory2 = wikimap.getSubCategory(category2);
		
		Double totalSubScore = 0.0;
		int totalSubCount = subCategory1.size() * subCategory2.size();
		
		for(String sc1 : subCategory1 ){
			for(String sc2 : subCategory2 ){
				totalSubScore += categorySim(sc1, sc2);
			}
		}
		Double averageSubScore;

		if(totalSubCount == 0){
			averageSubScore = 0.0;
		}else{
			averageSubScore = totalSubScore / (double)totalSubCount;
		}

		// Step 3. combination
		
		Double finalScore = (w1*categoryScore) + (w2*averageSubScore);
		return finalScore;

		
	}
	public Double categorySim(String category1, String category2 ) throws Exception{
		
		Set<String> categoryArticleList1 =wikimap.getCategoryArticle(category1);
		Set<String> categoryArticleList2 =wikimap.getCategoryArticle(category2);
		int coCount = 0;
		
		int csize1 = categoryArticleList1.size();
		int csize2 = categoryArticleList2.size();
		if ( csize1==0 || csize2==0 ){
			return 0.0;
		}else{
			for(String article : categoryArticleList2){
				if(categoryArticleList1.contains(article)){
					coCount++;
				}
			}
			Double score = (double)coCount / (Math.sqrt(categoryArticleList1.size()) * Math.sqrt(categoryArticleList2.size()))  ;
			return score;
		}
		
		
		
	}

}
