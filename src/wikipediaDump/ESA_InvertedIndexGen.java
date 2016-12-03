package wikipediaDump;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class ESA_InvertedIndexGen {
	
	public static void main(String args[]) throws Exception{
		
		String index = "wiki-dump-index-new";
		String invertedIndex = "wiki-dump-inverted-index-new";
		IndexWriter writer;
		IndexWriterConfig config;
		
	    IndexReader reader;
	    IndexSearcher searcher;
	    
		Directory dir = FSDirectory.open(new File(invertedIndex));
		Analyzer analyzer = new WhitespaceAnalyzer();
		
		config = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
		writer = new IndexWriter(dir, config);
	    
	    
	    Set<String> termSet = new HashSet<String>();
	    Map<String, Map<String,Double>> invMap = new HashMap<String, Map<String,Double>>();
	    
	    Map<String, Integer> DF_Map = new HashMap<String, Integer>();
	    
		reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		
		searcher = new IndexSearcher(reader);
		analyzer = new WhitespaceAnalyzer();
		
		int maxDoc = reader.maxDoc();
		System.out.println("termSet Loading");

		for (int i=0; i<reader.maxDoc(); i++) {
		    if (reader.hasDeletions() ) continue;
		    Document doc = reader.document(i);
	    	String[] termList = doc.getValues("term");
	    	for(String term : termList){
	    		termSet.add(term);
	    	}
		}
		System.out.println("termSet Loading Complete");
		System.out.println("DFMap Loading");
		for(String term : termSet){
			BooleanQuery booleanQuery = new BooleanQuery();
			Query query1 = new TermQuery(new Term("term", term));
			booleanQuery.add(query1, BooleanClause.Occur.MUST);
			TotalHitCountCollector counter = new TotalHitCountCollector();
			searcher.search(booleanQuery, counter);
			int totalhit = counter.getTotalHits();
			DF_Map.put(term, totalhit);
		}
		
		System.out.println("DFMap Loading Complete");
		System.out.println("InvMap Loading");

		for (int i=0; i<reader.maxDoc(); i++) {
		    if (reader.hasDeletions() ) continue;
		    
		    if(i % 10000 == 0 ) System.out.println(i);
		    
		    Document doc = reader.document(i);
		    String title = doc.get("title");
	    	String[] termScoreList = doc.getValues("term_score");
	    	String[] termList = doc.getValues("term");
	    	Double sum_totalscore = 0.0;
	    	
	    	for(String termScore : termScoreList){
	    		StringTokenizer token = new StringTokenizer(termScore, "\t");
				String term = token.nextToken();
				String score_str = token.nextToken();
				Double tscore = Double.parseDouble(score_str);
	    		int df = DF_Map.get(term);
	    		Double totalscore = ( 1 + Math.log (tscore)) * Math.log( maxDoc / (double)df );
	    		sum_totalscore += (totalscore*totalscore);
	    		if(invMap.containsKey(term)){
	    			Map<String,Double> wmap = invMap.get(term);
		    		wmap.put(title, totalscore);
		    		invMap.put(term, wmap);
	    		}else{
	    			Map<String,Double> wmap = new HashMap<String,Double>();
		    		wmap.put(title, totalscore);
		    		invMap.put(term, wmap);
	    		}
	    		
	    	}
	    	
	    	Double denominator = Math.sqrt(sum_totalscore);
	    	if(denominator != 0.0){
		    	for(String term : termList){
		    		
		    		Map<String,Double> wmap = invMap.get(term);
		    		wmap.put(title, wmap.get(title) / denominator );
		    		invMap.put(term, wmap);
		    		
		    	}
	    	}
	    			
		}
		System.out.println("InvMap Loading Complete");
		System.out.println("Indexing");

		
		for(Map.Entry<String, Map<String,Double>> invMapEntry : invMap.entrySet() ){
			
			Document doc = new Document();
			
			String term = invMapEntry.getKey();
			doc.add(new StringField("term", term, Field.Store.YES));
			
			Map<String,Double> wmap = invMapEntry.getValue();
			for(Map.Entry<String, Double> wmapEntry : wmap.entrySet() ){
				String article = wmapEntry.getKey();
				String articleScore = wmapEntry.getKey() + "\t" + String.format("%.8f",wmapEntry.getValue() );
				
				doc.add(new StringField("article", article, Field.Store.YES));
				doc.add(new StringField("article_score", articleScore, Field.Store.YES));
			}
			
			writer.addDocument(doc);

		}
		writer.close();
		System.out.println("Indexing complete");

	}

}
