package wikipediaDump;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
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

import util.DoubleValueComparator;


public class ESA_Test {
	public static void main(String args[]) throws Exception{
		String invertedIndex = "wiki-dump-inverted-index-new";
		
	    IndexReader reader;
	    IndexSearcher searcher;
	    
		reader = DirectoryReader.open(FSDirectory.open(new File(invertedIndex)));
		searcher = new IndexSearcher(reader);
		
		String test = "카카오톡";
		
		System.out.println("Test term :" + test);
		System.out.println();
		
		BooleanQuery booleanQuery = new BooleanQuery();
		Query query1 = new TermQuery(new Term("term", test));
		booleanQuery.add(query1, BooleanClause.Occur.MUST);
		
		TotalHitCountCollector counter = new TotalHitCountCollector();
		searcher.search(booleanQuery, counter);
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(counter.getTotalHits(), true);
		searcher.search(booleanQuery, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
	    for(int i=0;i<hits.length;++i) {
	    	int docId = hits[i].doc;
	    	double docScore = hits[i].score;

	    	Document d = searcher.doc(docId);
	    	
	    	String article_score_list[] = d.getValues("article_score");
	    	
	    	Map<String, Double> articleScoreMap = new HashMap<String, Double>();
	    	
	    	for(String articleScore : article_score_list){
	    		StringTokenizer token = new StringTokenizer(articleScore, "\t");
				String article = token.nextToken();
				
				String score_str = token.nextToken();
				Double score = Double.parseDouble(score_str);
				
				articleScoreMap.put(article, score);
				
	    	}
	    	
	    	DoubleValueComparator bvc = new DoubleValueComparator(articleScoreMap);
			TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
			tMap.putAll(articleScoreMap);
			
	    	for(Map.Entry<String, Double> entry : tMap.entrySet() ){
	    		
	    		System.out.println("WikiArticle : " + entry.getKey());
	    		System.out.println("WikiArticle Score : " + entry.getValue());
	    		System.out.println();
	    		
	    		
	    	}
	    	
	    	
	    }

	}
}
