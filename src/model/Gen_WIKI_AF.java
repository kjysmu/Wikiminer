package model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.FSDirectory;

import parameter.Path;
import util.FileFunction;
import util.TermFunction;
import modules.*;

public class Gen_WIKI_AF {

	public Gen_WIKI_AF() throws Exception{
		
		
	}
	public void run() throws Exception{
		
		System.out.println("start-esa-af");
		
		String newsRootDirectory = "D:\\Development\\DATA\\News";
		
		WikimapNew wikimap = new WikimapNew();
		
		for(int i=1; i<=20; i++){
			
			System.out.println("Corpus : " + i);
			
			String sourcePath = newsRootDirectory + i + "\\TFIDF";
			String targetPath = newsRootDirectory + i + "\\WIKI-AF\\";
			
			Map<String, Map<String,Double>> TFIDF_Map = FileFunction.readMapStr_StrDou(sourcePath);
			Map<String, Map<String,Double>> ESA_AF_Map = new HashMap<String, Map<String, Double>>();
			
			for (Entry<String, Map<String, Double>> entry : TFIDF_Map.entrySet()) {
				String key = entry.getKey();
				
				System.out.print(".");
				
		    	Map<String, Double> totalArticleScoreMap = new HashMap<String, Double>();
				Map<String, Double> value = entry.getValue();
				
				for (Entry<String, Double> entry2 : value.entrySet()) {
					String key2 = entry2.getKey();
					Double value2 = entry2.getValue();
					if( value2 <= 0.0001 ) continue;

			    	Map<String, Double> articleScoreMap = new HashMap<String, Double>();
			    	articleScoreMap = wikimap.getInvArticles(key2);
			    	
			    	
			    	if(articleScoreMap == null) continue;
			    	totalArticleScoreMap = TermFunction.CombineCounts(totalArticleScoreMap, articleScoreMap, value2);
					
				}
				totalArticleScoreMap = TermFunction.getNorm(totalArticleScoreMap);
				ESA_AF_Map.put(key, totalArticleScoreMap);
			}
			System.out.println("");

			FileFunction.writeMapStr_StrDou(ESA_AF_Map, targetPath);
			
		}
		
		System.out.println("complete-esa-af");
		
		
	}
	

		
		
		
	
}
