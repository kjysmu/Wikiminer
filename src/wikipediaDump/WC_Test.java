package wikipediaDump;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.search.BooleanClause.Occur;

import util.DoubleValueComparator;


public class WC_Test {
	public static void main(String args[]) throws Exception{
		String index = "wiki-dump-index-new";
		
	    IndexReader reader;
	    IndexSearcher searcher;
	    
	    StandardAnalyzer analyzer = new StandardAnalyzer();
	    
		reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		searcher = new IndexSearcher(reader);
		
		String queryStr = "아이폰";
		
		System.out.println("Test term :" + queryStr);
		System.out.println();
		
		QueryParser parser = new QueryParser("title", new StandardAnalyzer());
		Query parsedQuery = parser.parse(queryStr);
		
		BooleanQuery booleanQuery = new BooleanQuery();
		Query query = new TermQuery(new Term("title", queryStr));
		
		booleanQuery.add(query, Occur.MUST);
		
		TopDocs docs = searcher.search(query, 10);

        for (ScoreDoc scoreDoc : docs.scoreDocs) {
        	
            Document doc = searcher.doc(scoreDoc.doc);
            
            System.out.println("TITLE : " + doc.get("title"));
            
	    	String category_list[] = doc.getValues("category");

	    	System.out.print("Category : ");
	    	for(int i=0; i<category_list.length; i++){
	    		System.out.print(category_list[i]);
	    		if(i != category_list.length-1){
		    		System.out.print(" , ");
	    		}
	    		
	    	}
	    	
            System.out.println();
            for( IndexableField f : doc.getFields() ){
            	System.out.println( f.name() + ":" + f.stringValue());
            }
            
            
            
        }
        
        

		/*
		booleanQuery.add(query1, BooleanClause.Occur.MUST);
		
		TotalHitCountCollector counter = new TotalHitCountCollector();
		
		
		searcher.search(booleanQuery, counter);
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(counter.getTotalHits(), true);
		searcher.search(booleanQuery, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
	    for(int i=0;i<hits.length;++i) {
	    	    
        TopDocs hits = searcher.search(parsedQuery, 10);
        
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
        	
            Document doc = searcher.doc(scoreDoc.doc);
            
            System.out.println("TITLE : " + doc.get("title"));
            
	    	String category_list[] = doc.getValues("category");

	    	System.out.print("Category : ");
	    	for(int i=0; i<category_list.length; i++){
	    		System.out.print(category_list[i]);
	    		if(i != category_list.length-1){
		    		System.out.print(" , ");
	    		}
	    		
	    	}
            System.out.println();
            
            
        }
        
        

	    
		*/
		
		
	

		
		/*
		
		
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
	    */

	}
}
