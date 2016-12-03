package modules;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.FSDirectory;

import parameter.Exp;

public class WikimapNew {
	
	String index = "wiki-dump-index-new";
	String invertedIndex = "wiki-dump-inverted-index-new";
	
	IndexReader reader;
    IndexSearcher searcher;
    
    IndexReader readerInv;
    IndexSearcher searcherInv;
    
	public WikimapNew() throws Exception{
		
		reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		searcher = new IndexSearcher(reader);
		
		readerInv = DirectoryReader.open(FSDirectory.open(new File(invertedIndex)));
		searcherInv = new IndexSearcher(readerInv);
		
	}
	
	public String getRedirect(String articleTerm) throws Exception{
		
		String redirectTerm = "";
		
		BooleanQuery booleanQuery = new BooleanQuery();
		Query query = new TermQuery(new Term("title", articleTerm));
		
		booleanQuery.add(query, Occur.MUST);

		TopDocs docs = searcher.search(booleanQuery, 1);
		
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
        	
            Document doc = searcher.doc(scoreDoc.doc);
            if( doc.get("type").equals("RD") ){
            	
            	redirectTerm = doc.get("link");            	
            	break;
            }
        
        }
        
		return redirectTerm;
		
	}
	
	
	
	public Set<String> getCategory(String articleTerm) throws Exception{
		return getCategory(articleTerm, false); 
	}

	public Set<String> getCategory(String articleTerm, boolean autoRedirect) throws Exception{
		
		Set<String> categorySet = new HashSet<String>();
		
		BooleanQuery booleanQuery = new BooleanQuery();
		Query query = new TermQuery(new Term("title", articleTerm));
		
		booleanQuery.add(query, Occur.MUST);

		TopDocs docs = searcher.search(booleanQuery, 1);
		
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
        	
            Document doc = searcher.doc(scoreDoc.doc);
            if( doc.get("type").equals("RD") && autoRedirect ){
            	String redirectTerm = doc.get("link");
            	
            	categorySet = getCategory(redirectTerm, false);
            	break;
            }
            
	    	String category_list[] = doc.getValues("category");
    	
	    	for(String category : category_list){
	    		categorySet.add(category.replaceFirst("분류:", "").trim());
	    	}
	    	
        }
        
		return categorySet;
		
	}
	
	public Set<String> getSubCategory(String articleCategory) throws Exception{
		
		Set<String> categorySet = new HashSet<String>();
		
		if(!articleCategory.startsWith("분류:")){
			articleCategory = "분류:"+articleCategory;
		}
		
		BooleanQuery booleanQuery = new BooleanQuery();
		Query query = new TermQuery(new Term("category", articleCategory));
		Query query2 = new TermQuery(new Term("ns", "14"));
		
		booleanQuery.add(query, Occur.MUST);
		booleanQuery.add(query2, Occur.MUST);

		TopDocs docs = searcher.search(booleanQuery, reader.numDocs());
		
		for (ScoreDoc scoreDoc : docs.scoreDocs) {
        	
            Document doc = searcher.doc(scoreDoc.doc);
            String title = doc.get("title");
            categorySet.add(title.replaceFirst("분류:", "").trim());
        
        }
		return categorySet;
		
	}

	public Set<String> getSuperCategory(String articleTerm){
		return null;
	}
	
	public Set<String> getCategoryArticle(String articleCategory) throws Exception{
		
		Set<String> articleSet = new HashSet<String>();
		
		if(!articleCategory.startsWith("분류:")){
			articleCategory = "분류:"+articleCategory;
		}
		
		BooleanQuery booleanQuery = new BooleanQuery();
		Query query = new TermQuery(new Term("category", articleCategory));
		Query query2 = new TermQuery(new Term("ns", "0"));
		
		booleanQuery.add(query, Occur.MUST);
		booleanQuery.add(query2, Occur.MUST);

		TopDocs docs = searcher.search(booleanQuery, reader.numDocs());
		
		for (ScoreDoc scoreDoc : docs.scoreDocs) {
        	
            Document doc = searcher.doc(scoreDoc.doc);
            String title = doc.get("title");
            
            articleSet.add(title.trim());
        
        }
		
		return articleSet;
		
	}
	
	
	
	public Map<String, Double> getInvArticles( String articleTerm ) throws Exception{
		
		BooleanQuery booleanQuery = new BooleanQuery();
		Query query1 = new TermQuery(new Term( "term" , articleTerm));
		booleanQuery.add(query1, BooleanClause.Occur.MUST);
		
		TotalHitCountCollector counter = new TotalHitCountCollector();
		searcherInv.search(booleanQuery, counter);
		
		if(counter.getTotalHits() == 0 ) return null;
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(counter.getTotalHits(), true);
		
		searcherInv.search(booleanQuery, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		if(hits.length == 0 ) return null;
		else{
			ScoreDoc hitsTop = hits[0];
		    int docId = hitsTop.doc;
	    	Document d = searcherInv.doc(docId);
	    	String article_score_list[] = d.getValues("article_score");
	    	
	    	Map<String, Double> articleScoreMap = new HashMap<String, Double>();
	    	
	    	for(String articleScore : article_score_list){
	    		StringTokenizer token = new StringTokenizer(articleScore, "\t");
				String article = token.nextToken();
				
				String score_str = token.nextToken();
				Double score = Double.parseDouble(score_str);
				
				articleScoreMap.put(article, score);
				
	    	}

			return articleScoreMap;
			
		   
		}
	}
	
	public Map<String, Double> getInvArticlesThreshold( String term ) throws Exception{

		BooleanQuery booleanQuery = new BooleanQuery();
		Query query1 = new TermQuery(new Term( "term" , term));
		booleanQuery.add(query1, BooleanClause.Occur.MUST);

		TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
		searcherInv.search(booleanQuery, collector);

		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		if(hits.length == 0 ) return null;
		else{
			ScoreDoc hitsTop = hits[0];
			int docId = hitsTop.doc;
			Document d = searcherInv.doc(docId);
			String article_score_list[] = d.getValues("article_score");

			Map<String, Double> articleScoreMap = new HashMap<String, Double>();

			for(String articleScore : article_score_list){
				StringTokenizer token = new StringTokenizer(articleScore, "\t");
				String article = token.nextToken();

				String score_str = token.nextToken();
				Double score = Double.parseDouble(score_str);

				if( score < Exp.ESA_threshold ) continue;

				articleScoreMap.put(article, score);

			}

			return articleScoreMap;

		}
		
		
		
	}
	
	
	
	
	

}
