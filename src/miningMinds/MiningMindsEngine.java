package miningMinds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import modules.*;

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
import org.apache.lucene.document.Document;

// import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import parameter.*;
import util.DocumentFunction;
import util.DoubleValueComparator;
import util.FileFunction;
import util.TermFunction;

// import edu.uci.ics.jung.graph.Graph;
// import edu.uci.ics.jung.graph.UndirectedSparseGraph;


/**
 * @author kjysmu
 *
 */

public class MiningMindsEngine {

	int levelOfCategory = 2;
	int topKCategory = 10;

	String default_snstype = "Facebook";
	//String index = "d:/indexfile/wiki-dump-index";
	//String invertedIndex = "d:/indexfile/wiki-dump-inverted-index";

	String index = "wiki-dump-index-new";
	String invertedIndex = "wiki-dump-inverted-index-new";

	Map<String, Double> NaverIDF;

	Map<String, Double> WikiICF;
	Map<String, Double> WikiIAF;


	Map<String, Double> simMatrix;
	Map<String, Double> TopicProp;

	Map<String, Map<String, Integer>> usersLabelResults;
	Map<String, Map<String, Double>> userCategoryResult;

	Map<String, Map<String, Double>> NaverTFIDF;
	Map<String, Map<String, Double>> NaverWIKI;

	Map<String, Map<String, Double>> NaverESA_CFICF;
	Map<String, Map<String, Double>> NaverESA_AFIAF;

	Map<String, Map<String, Map<String,Double>>> NaverWIKICluster;
	Map<String, Map<String, Double>> NaverWIKIClusterSim;

	Map<String, Map<String, Double>> LDA_topicMap;
	Map<String, Double> LDA_topicDist;

	Map<String, Map<String, Double>> wikiLDA_topicMap;
	Map<String, Map<String, Double>> LDA_categoryDistMap;

	static DocumentBuilderFactory factory;
	static DocumentBuilder builder;
	static Document document;
	Wikimap wikimap;

	IndexReader reader;
	IndexSearcher searcher;

	IndexReader readerInv;
	IndexSearcher searcherInv;

	Path2 path = new Path2(1);

	public MiningMindsEngine() throws Exception{
		init(default_snstype, 1);
	}
	public MiningMindsEngine(String snstype, int corpusIndex) throws Exception{
		init(snstype, corpusIndex);
	}

	public void init(String snstype, int corpusIndex) throws Exception{
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		wikimap = new Wikimap();

		path = new Path2(corpusIndex);

		//LabeledUser labeledUser = new LabeledUser();
		//labeledUser.SaveCategoryCount(snstype);

		NaverTFIDF = new HashMap<String, Map<String, Double>>();
		NaverWIKI = new HashMap<String, Map<String, Double>>();

		NaverESA_CFICF = new HashMap<String, Map<String, Double>>();
		NaverESA_AFIAF = new HashMap<String, Map<String, Double>>();

		NaverIDF = new HashMap<String, Double>();
		WikiICF = new HashMap<String, Double>();
		WikiIAF = new HashMap<String, Double>();

		NaverWIKICluster = new HashMap<String, Map<String, Map<String, Double>>>();
		NaverWIKIClusterSim = new HashMap<String, Map<String, Double>>();

		reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		searcher = new IndexSearcher(reader);

		readerInv = DirectoryReader.open(FSDirectory.open(new File(invertedIndex)));
		searcherInv = new IndexSearcher(readerInv);

		String line = "";
		//NaverTFIDF
		List<File> fileListTFIDF = FileFunction.getListOfFiles(path.CATEGORY_MODEL_TFIDF_PATH);
		for (File file : fileListTFIDF) {
			Map<String, Double> tfidf = new HashMap<String, Double>();				
			tfidf = FileFunction.readMapStrDou(file);	
			NaverTFIDF.put(file.getName().replaceAll(".txt","").trim(), tfidf); 
		}

		//NaverWIKI
		List<File> fileListWIKI = new ArrayList<File>();
		List<File> fileListWIKICluster = new ArrayList<File>();
		List<File> fileListWIKIClusterSim = new ArrayList<File>();

		//NaverESA
		List<File> fileListESA_AFIAF = new ArrayList<File>();
		List<File> fileListESA_CFICF = new ArrayList<File>();

		if(Exp.approach.contains("icf")){
			if(Exp.subCategoryWeight_Naver != 0.0){ // Never

				// fileListWIKI = FileFunction.getListOfFiles(path.CATEGORY_MODEL_WIKISUB_CFICF_PATH + String.format("%.1f", Exp.subCategoryWeight_Naver) + "\\");		

			}else{
				if(Exp.approach.contains("cs")){
					if(Exp.isEnglish){
						//Not Yet for English
						//fileListWIKI = FileFunction.getListOfFiles(path.CATEGORY_MODEL_EN_WIKI_CFICF_PATH);
					}else{

						if(Exp.isL2Norm){

							fileListWIKI = FileFunction.getListOfFiles(path.CATEGORY_MODEL_WIKI_CFICF_L2_PATH);
							fileListESA_AFIAF = FileFunction.getListOfFiles(path.CATEGORY_MODEL_ESA_AFIAF_L2_PATH);

						}else{

							fileListWIKI = FileFunction.getListOfFiles(path.CATEGORY_MODEL_WIKI_CFICF_PATH);
							fileListESA_AFIAF = FileFunction.getListOfFiles(path.CATEGORY_MODEL_ESA_AFIAF_PATH);

						}

						fileListWIKICluster = FileFunction.getListOfFiles(path.CATEGORY_MODEL_WIKI_CFICF_CS_PATH);
						fileListWIKIClusterSim = FileFunction.getListOfFiles(path.CATEGORY_MODEL_WIKI_CFICF_CS_SIM_PATH);

					}
				}else{
					if(Exp.isEnglish){ // NEVER

						//fileListWIKI = FileFunction.getListOfFiles(path.CATEGORY_MODEL_EN_WIKI_CFICF_PATH);

					}else{

						if(Exp.isL2Norm){

							fileListWIKI = FileFunction.getListOfFiles(path.CATEGORY_MODEL_WIKI_CFICF_L2_PATH);
							fileListESA_AFIAF = FileFunction.getListOfFiles(path.CATEGORY_MODEL_ESA_AFIAF_L2_PATH);

						}else{

							fileListWIKI = FileFunction.getListOfFiles(path.CATEGORY_MODEL_WIKI_CFICF_PATH);
							fileListESA_AFIAF = FileFunction.getListOfFiles(path.CATEGORY_MODEL_ESA_AFIAF_PATH);

						}

						//fileListWIKI = FileFunction.getListOfFiles(path.CATEGORY_MODEL_WIKI_CFICF_PATH);


					}
				}
			}
		}else{
			fileListWIKI = FileFunction.getListOfFiles(path.CATEGORY_MODEL_WIKI_CF_PATH);
		}

		for (File file : fileListWIKI) {
			Map<String, Double> wmap = new HashMap<String, Double>();
			wmap = FileFunction.readMapStrDou(file);
			NaverWIKI.put(file.getName().replaceAll(".txt","").trim(), wmap  );
		}


		for (File file : fileListESA_AFIAF) {
			Map<String, Double> wmap = new HashMap<String, Double>();
			wmap = FileFunction.readMapStrDou(file);
			NaverESA_AFIAF.put(file.getName().replaceAll(".txt","").trim(), wmap  );
		}

		if(Exp.approach.contains("cs")){
			for (File file : fileListWIKICluster) {
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
				NaverWIKICluster.put(file.getName().replaceAll(".txt","").trim(), clusterMap);

			}

			for (File file : fileListWIKIClusterSim) {
				Map<String, Double> wmap = new HashMap<String, Double>();
				wmap = FileFunction.readMapStrDou(file);
				NaverWIKIClusterSim.put(file.getName().replaceAll(".txt","").trim(), wmap  );
			}

		}

		//----------- IDF Loading ----------------------------------------//

		NaverIDF = FileFunction.readMapStrDou(path.IDF_FILEPATH);

		if(Exp.subCategoryWeight_Naver != 0.0){ // NEVER
			//WikiICF = FileFunction.readMapStrDou(path.WIKI_SUB_ICF_FILEPATH);	
		}else{
			if(Exp.isEnglish){
				//WikiICF = FileFunction.readMapStrDou(path.WIKI_ICF_EN_FILEPATH);	
			}else{

				WikiICF = FileFunction.readMapStrDou(path.WIKI_ICF_FILEPATH);
				WikiIAF = FileFunction.readMapStrDou(path.WIKI_IAF_FILEPATH);

			}

		}

		//------------ NaverLDATopic Loading ------------------------------//

		BufferedReader br_lda = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(new File(path.LDA_TOPIC_FILEPATH)), "UTF8"));

		LDA_topicMap = new HashMap<String ,Map<String, Double>>();
		LDA_topicDist = new HashMap<String, Double>();

		line = "";

		Boolean isFstLine = true;

		while(true){

			line = br_lda.readLine();
			if(line == null) break;

			if(isFstLine){
				line = FileFunction.removeUTF8BOM(line);
				isFstLine = false;
			}

			Map<String, Double> tmap = new HashMap<String, Double>();

			StringTokenizer token = new StringTokenizer(line, "\t");
			String topicName = "";
			if(token.countTokens() == 3){
				String token1= token.nextToken();
				topicName = token1;
				String token2= token.nextToken();
				LDA_topicDist.put(topicName, Double.parseDouble(token2));
				String token3= token.nextToken().trim();
				StringTokenizer tk = new StringTokenizer(token3, " ");
				while(tk.hasMoreTokens()){
					String tk1 = tk.nextToken();
					String tk2 = tk.nextToken();
					double tk2_num = Double.parseDouble( tk2.substring(1, tk2.length()-1) );
					tmap.put(tk1, tk2_num);
				}
				LDA_topicMap.put("Topic"+topicName, getTFIDF( TermFunction.getNorm(tmap) ) );
			}
		}



		br_lda.close();




		//------------ NaverLDACategoryModel Loading ---------------//

		LDA_categoryDistMap = new HashMap<String, Map<String, Double> >();
		LDA_categoryDistMap = FileFunction.readMapStr_StrDou(path.CATEGORY_MODEL_LDA_PATH);


		//System.out.println("size:" + LDA_categoryDistMap.size());




	} // End of Initialization


	public void setCorpusIndex ( int index ){

		path = new Path2( index );


	}

	public void exportWikimap() throws Exception{

		wikimap.exportAllMap();

	}

	public Map<String, Double> getCategoryList( SNSUserBean userBean ) throws Exception {
		Map<String, Double> termCounts = userBean.getTotalTermCount();


		Map<String, Double> similarities = new HashMap<String, Double>();		
		for(Map.Entry<String, Map<String,Double>> NaverTFIDFEntry : NaverTFIDF.entrySet() ){
			if(Exp.btfidf_user){
				Map<String, Double> user_tfidf = getTFIDF(termCounts);
				similarities.put(NaverTFIDFEntry.getKey(), getCosineSimilarity(user_tfidf, NaverTFIDFEntry.getValue()) );
			}else{
				similarities.put(NaverTFIDFEntry.getKey(), getCosineSimilarity(termCounts, NaverTFIDFEntry.getValue()) );
			}
		}
		return similarities;
	}

	public Map<String, Double> getCategoryList( SNSUserMsgBean userMsgBean, SNSUserBean userBean ) throws Exception {

		Map<String, Double> similarities = new HashMap<String, Double>();

		Map<String, Double> similaritiesCFICF = new HashMap<String, Double>();
		Map<String, Double> similaritiesAFIAF = new HashMap<String, Double>();
		Map<String, Double> similaritiesCS = new HashMap<String, Double>();

		Map<String, Double> totalInvCategoryMap = new HashMap<String, Double>();
		Map<String, Double> totalInvArticleMap = new HashMap<String, Double>();

		Map<String, Double> topsimilarities = new HashMap<String, Double>();
		Map<String, Double> termCounts = userMsgBean.getTermCountMap();

		termCounts = TermFunction.getNorm(termCounts);
		userMsgBean.setTermFreqMap(termCounts);

		/*---------------------------------------------------------------------------
		 * Step.1  Calculate Category Similarity
		 *  : Calculate the similarity based on the approaches below.
		 *  [Approaches]
		 *  	1. tfidf
		 *  	2. tfidf-lda
		 *  	3. wiki
		 *  	4. wiki-lda
		 *  
		 *---------------------------------------------------------------------------
		 */

		if(Exp.approach.contains("tfidf")){
			if(Exp.approach.contains("lda")){

				Map<String, Double> map_feature = new HashMap<String, Double>();

				for(Map.Entry<String, Map<String,Double>> LDA_topicMapEntry : LDA_topicMap.entrySet()){


					if(Exp.isLDA_DOT){
						Double sim = getDotSimilarity( getTFIDF( TermFunction.getNorm(termCounts)) , LDA_topicMapEntry.getValue());
						map_feature.put(LDA_topicMapEntry.getKey().replaceAll("Topic", "").trim(), sim );

					}else{
						Double sim = getCosineSimilarity( getTFIDF( TermFunction.getNorm(termCounts)) , LDA_topicMapEntry.getValue());
						map_feature.put(LDA_topicMapEntry.getKey().replaceAll("Topic", "").trim(), sim );

					}





					//Double sim = getCosineSimilarity( termCounts , LDA_topicMapEntry.getValue());

					//System.out.println( LDA_topicMapEntry.getKey() );


				}

				for(Map.Entry<String, Map<String,Double>> LDA_distEntry : LDA_categoryDistMap.entrySet() ){

					Double sim = getCosineSimilarity( map_feature, LDA_distEntry.getValue() );

					if(sim.isNaN()){
						/*
						System.out.println( "map-feature" );

						for(Map.Entry<String, Double> entry : map_feature.entrySet()){
							System.out.println( entry.getKey() + " : " + entry.getValue());
						}

						System.out.println( "lda-feature" );

						for(Map.Entry<String, Double> entry : LDA_distEntry.getValue().entrySet()){
							System.out.println( entry.getKey() + " : " + entry.getValue());
						}
						 */

						sim = 0.0;

						//System.exit(0);


					}



					similarities.put(LDA_distEntry.getKey(), sim );
				}




			}else{
				for(Map.Entry<String, Map<String,Double>> NaverTFIDFEntry : NaverTFIDF.entrySet() ){
					if(Exp.btfidf_user){
						Map<String, Double> user_tfidf = getTFIDF(termCounts);
						similarities.put(NaverTFIDFEntry.getKey(), getCosineSimilarity(user_tfidf, NaverTFIDFEntry.getValue()) );
					}else{
						similarities.put(NaverTFIDFEntry.getKey(), getCosineSimilarity(termCounts, NaverTFIDFEntry.getValue()) );
					}
				}
			}
		}else if(Exp.approach.contains("wiki")){
			Map<String, Double> wikiCategory = new HashMap<String,Double>();


			for (Map.Entry<String, Double> termcount : termCounts.entrySet())
			{
				SNSTermBean termBean = new SNSTermBean();
				Double term_idf = -1.0;
				String term = termcount.getKey();
				termBean.setTerm(term);
				Double TF = termcount.getValue();
				termBean.setTF(TF);

				if(Exp.isWikimap){
					Map<String, Double> InvArticleMap = new HashMap<String, Double>();

					String wikiTerm = term;

					if(wikimap.isRedirect(term)){
						String term_rd = wikimap.getRedirect(term);
						termBean.setRedirectTerm(term_rd);

						if( NaverIDF.get(term) == null && NaverIDF.get(term_rd) == null  ){
							term_idf = -1.0;
						}else{
							if( NaverIDF.get(term) != null ) term_idf = NaverIDF.get(term);
							if( NaverIDF.get(term_rd) != null ) term_idf = NaverIDF.get(term_rd);
						}
						wikiTerm = term_rd;
					}else{
						if( NaverIDF.get(term) == null ){
							term_idf = -1.0;
						}else{
							term_idf = NaverIDF.get(term);
						}
					}

					termBean.setIDF(term_idf);

					String maxHomonym = "";
					double maxhsim = 0.5;
					boolean isHom = false;

					if( wikimap.isHomonym(wikiTerm) && termCounts.size() >= 2 ){

						isHom = true;

						if( wikiTerm.length() >= 2 ){	

							Set<String> set = wikimap.getHomonym(wikiTerm);
							for(String homonym : set){

								Map<String,Double> hmap = wikimap.getArticleTermCounts(homonym);
								//Map<String,Double> hmap = wikimap.getArticleKeywords(homonym);

								if(!hmap.isEmpty()){

									double hsim = DocumentFunction.ComputeCosineSimilarity(getTFIDF(hmap), getTFIDF(termCounts));

									if( hsim > maxhsim ){

										maxhsim= hsim;
										maxHomonym = homonym;	
										//wikiTerm = maxHomonym;
									}

								}
							}
							if(!maxHomonym.equals("")){
								//System.out.println(wikiTerm + ":" + maxHomonym + "-" + maxhsim);
								wikiTerm = maxHomonym;

							}	
						}	
					}

					if( (wikimap.hasCategory(wikiTerm)&&!isHom) || (isHom&&!maxHomonym.equals("")&&wikimap.hasCategory(wikiTerm))  ){
						/*---------------------------------WikiCategory Model------------------------------------*/
						Set<String> categoryList = wikimap.getCategory(wikiTerm);
						for(String category : categoryList){
							if(wikiCategory.containsKey(category)){
								if( Exp.isWiki_NaverIDF ){
									//wikiCategory.put(category, wikiCategory.get(category) + term_idf);
									if(term_idf == -1.0){
										wikiCategory.put(category, wikiCategory.get(category)  + 1.0);

									}else{
										wikiCategory.put(category, wikiCategory.get(category) + term_idf);
									}
									termBean.addWikiCategoryMap(category, term_idf);
								}
								else {
									wikiCategory.put(category, wikiCategory.get(category) + 1.0);
									termBean.addWikiCategoryMap(category, 1.0);
								}
							}else{
								if( Exp.isWiki_NaverIDF ) {
									//wikiCategory.put(category, term_idf);
									if(term_idf == -1.0){
										wikiCategory.put(category, 1.0);
									}else{
										wikiCategory.put(category, term_idf);
									}
									termBean.addWikiCategoryMap(category, term_idf) ;
								}
								else {
									wikiCategory.put(category, 1.0);
									termBean.addWikiCategoryMap(category, 1.0);
								}
							}
						}
					}

					userMsgBean.addTermList(termBean);

					Map<String, Double> invmap = wikimap.getInvArticle(term);

					for(Map.Entry<String, Double> entry : invmap.entrySet()){
						String key = entry.getKey();
						Double value = entry.getValue();

						if(term_idf == -1.0){
							term_idf = 1.0;
						}

						if(!key.contains(":")){
							InvArticleMap.put(key, value * term_idf );	
						}

					}

					/*
					if( invmap != null  ){
						InvArticleMap.putAll(invmap);
					}else{
					}
					 */

					termBean.setWikiArticleMap(InvArticleMap);


					totalInvArticleMap = TermFunction.CombineCounts(totalInvArticleMap, InvArticleMap);

				}else{

				}

			}//End of For-TermCount

			Map<String, Double> wikiCategoryMap = new HashMap<String, Double>();
			Map<String, Double> wikiArticleMap = new HashMap<String, Double>();


			if(Exp.isL2Norm){
				wikiArticleMap = TermFunction.getNormSquare(totalInvArticleMap);
				wikiCategoryMap = TermFunction.getNormSquare(wikiCategory);

			}else{
				wikiArticleMap = TermFunction.getNorm(totalInvArticleMap);
				wikiCategoryMap = TermFunction.getNorm(wikiCategory);

			}


			wikiCategoryMap = getWikiTCICF(wikiCategoryMap);
			wikiArticleMap = getWikiAFIAF(wikiArticleMap);



			/*
			if(Exp.isL2Norm){
				wikiArticleMap = TermFunction.getNormSquare(totalInvArticleMap);
				wikiCategoryMap = TermFunction.getNormSquare(wikiCategory);

			}else{
				wikiArticleMap = TermFunction.getNorm(totalInvArticleMap);
				wikiCategoryMap = TermFunction.getNorm(wikiCategory);

			}*/







			/*--------------Wiki-SubCategory Model--------------------*/
			if(Exp.subCategoryWeight_User != 0.0){
				Map<String, Double> subCategoryMap = new HashMap<String, Double>();

				if(Exp.isWikimap){
					subCategoryMap = getSubCategoryMap2(wikiCategoryMap);
				}else{
					subCategoryMap = getSubCategoryMap(wikiCategoryMap);

				}

				if(subCategoryMap != null && !subCategoryMap.isEmpty() ){
					subCategoryMap = getWikiTCICF(subCategoryMap);
					userMsgBean.setWikiSubCategoryMap(subCategoryMap);
					wikiCategoryMap = TermFunction.CombineCountsWeight(wikiCategoryMap, subCategoryMap, 1.0 - Exp.subCategoryWeight_User, Exp.subCategoryWeight_User);
				}else{

				}
			}


			userMsgBean.setWikiCategoryMap(TermFunction.getNorm(wikiCategory));
			userMsgBean.setWikiTotalCategoryMap(wikiCategoryMap);
			userMsgBean.setWikiTotalArticleMap(totalInvArticleMap);

			if( Exp.approach.contains("lda") ){
				Map<String, Double> map_feature = new HashMap<String, Double>();
				for(Map.Entry<String, Map<String,Double>> wikiLDA_topicEntry : wikiLDA_topicMap.entrySet()){
					Double sim = getCosineSimilarity(wikiCategoryMap , wikiLDA_topicEntry.getValue());
					map_feature.put(wikiLDA_topicEntry.getKey(), sim );
				} 
				for(Map.Entry<String, Map<String,Double>> wikiLDA_distEntry : LDA_categoryDistMap.entrySet() ){
					similarities.put(wikiLDA_distEntry.getKey(), getCosineSimilarity(map_feature, wikiLDA_distEntry.getValue()));
				}
			}else{

				if(Exp.EXP_LEVEL.contains("CFICF")){
					for(Map.Entry<String, Map<String,Double>> NaverWIKI_entry : NaverWIKI.entrySet() ){
						similarities.put(NaverWIKI_entry.getKey(), getCosineSimilarity( wikiCategoryMap , NaverWIKI_entry.getValue()) );
					}

				}
				if(Exp.EXP_LEVEL.contains("AFIAF")){
					for(Map.Entry<String, Map<String,Double>> NaverESA_AFIAF_entry : NaverESA_AFIAF.entrySet() ){
						similarities.put(NaverESA_AFIAF_entry.getKey(), getCosineSimilarity( wikiArticleMap , NaverESA_AFIAF_entry.getValue()) );
					}
				}
				if(Exp.EXP_LEVEL.contains("CS")){
					for(Map.Entry<String, Map<String, Map<String,Double>>> NaverWIKICluster_entry : NaverWIKICluster.entrySet() ){
						similarities.put(NaverWIKICluster_entry.getKey(), getClusterSimilarity( wikiCategoryMap , NaverWIKICluster_entry.getValue(), NaverWIKICluster_entry.getKey() ));
					}
				}
				if(Exp.EXP_LEVEL.contains("MIX")){

					for(Map.Entry<String, Map<String,Double>> NaverWIKI_entry : NaverWIKI.entrySet() ){
						similaritiesCFICF.put(NaverWIKI_entry.getKey(), getCosineSimilarity( wikiCategoryMap , NaverWIKI_entry.getValue()) );
					}

					for(Map.Entry<String, Map<String,Double>> NaverESA_AFIAF_entry : NaverESA_AFIAF.entrySet() ){
						similaritiesAFIAF.put(NaverESA_AFIAF_entry.getKey(), getCosineSimilarity( wikiArticleMap , NaverESA_AFIAF_entry.getValue()) );
					}

					for(Map.Entry<String, Map<String, Map<String,Double>>> NaverWIKICluster_entry : NaverWIKICluster.entrySet() ){
						similaritiesCS.put(NaverWIKICluster_entry.getKey(), getClusterSimilarity( wikiCategoryMap , NaverWIKICluster_entry.getValue(), NaverWIKICluster_entry.getKey() ));
					}

					similarities = TermFunction.CombineCountsWeight(similaritiesCFICF, similaritiesAFIAF, similaritiesCS, Exp.w_cficf, Exp.w_afiaf, Exp.w_cs);

				}




				/*
				if( Exp.approach.contains("cs") ){
					Map<String, Double> similarities1 = new HashMap<String, Double>();
					Map<String, Double> similarities2 = new HashMap<String, Double>();

					Map<String, Double> similaritiesInv = new HashMap<String, Double>();

					for(Map.Entry<String, Map<String,Double>> NaverWIKI_entry : NaverWIKI.entrySet() ){
						similarities1.put(NaverWIKI_entry.getKey(), getCosineSimilarity( wikiCategoryMap , NaverWIKI_entry.getValue()) );
					}

					for(Map.Entry<String, Map<String, Map<String,Double>>> NaverWIKICluster_entry : NaverWIKICluster.entrySet() ){
						similarities2.put(NaverWIKICluster_entry.getKey(), getClusterSimilarity( wikiCategoryMap , NaverWIKICluster_entry.getValue(), NaverWIKICluster_entry.getKey() ));
					}

					similarities = TermFunction.CombineCountsWeight(similarities1, similarities2, 1.0 - Exp.clusterWeight, Exp.clusterWeight);

					if( Exp.approach.contains("esa") ){

						for(Map.Entry<String, Map<String,Double>> NaverESA_AFIAF_entry : NaverESA_AFIAF.entrySet() ){
							similaritiesInv.put(NaverESA_AFIAF_entry.getKey(), getCosineSimilarity( totalInvArticleMap , NaverESA_AFIAF_entry.getValue()) );
						}

						similarities = TermFunction.CombineCountsWeight(similarities, similaritiesInv, 1.0 - Exp.ESA_Weight, Exp.ESA_Weight);
					}					
				}else{
					for(Map.Entry<String, Map<String,Double>> NaverWIKI_entry : NaverWIKI.entrySet() ){
						similarities.put(NaverWIKI_entry.getKey(), getCosineSimilarity( wikiCategoryMap , NaverWIKI_entry.getValue()) );
					}
				}
				 */

			}
		}//End of If-Wiki


		DoubleValueComparator bvc = new DoubleValueComparator(similarities);
		TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
		tMap.putAll(similarities);



		userMsgBean.setRecomTotalCategoryMap(similarities);

		/*---------------------------------------------------------------------------
		 * Step.2  Threshold Modeling
		 *  : Filter out the categories whose scores are lower than threshold
		 *  
		 *---------------------------------------------------------------------------
		 */

		double maxsim = 0.0;
		double totalsim = 0.0;

		for(Map.Entry<String, Double> similarity : tMap.entrySet() ){
			if( !(similarity.getValue() == 0 || similarity.getValue() == null || isNaN(similarity.getValue())) ){
				totalsim += similarity.getValue();
			}
			if( maxsim < similarity.getValue()) maxsim = similarity.getValue();
		}
		if(totalsim == 0 || isNaN(totalsim)){
			topsimilarities.clear();
			return topsimilarities;
		}

		Iterator<Map.Entry<String,Double>> iter = tMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String,Double> entry = iter.next();
			if(entry.getValue() == 0 || entry.getValue() == null || isNaN(entry.getValue()) ) iter.remove();
			else{
				double score = entry.getValue();
				double score2 = score / totalsim;
				if( score < Exp.msg_threshold ){
					iter.remove();
				}else if( score2 < Exp.msg_threshold2 ){
					iter.remove();
				}
			}
		}

		if(similarities.isEmpty()){
			topsimilarities.clear();
		}else{
			int count = 0;
			for(Map.Entry<String, Double> similarity : tMap.entrySet() ){
				topsimilarities.put(similarity.getKey(), similarity.getValue());
				count ++;
				if(Exp.msg_maxcategory == 0){
				}else if(count >= Exp.msg_maxcategory) break;
			}
		}



		return topsimilarities;
	}

	public double getDotSimilarity(Map<String, Double> map1 , Map<String, Double> map2 ){
		if(map1.isEmpty() || map2.isEmpty()) return 0.0;
		else{

			//Map<String, Double> freq1 = TermFunction.getNorm(map1);
			//Map<String, Double> freq2 = TermFunction.getNorm(map2);

			double dotProduct = 0.0;
			for(Map.Entry<String, Double> termFrequency1 : map1.entrySet()){
				if(map2.containsKey(termFrequency1.getKey())){
					dotProduct += termFrequency1.getValue() * map2.get(termFrequency1.getKey());
				}
			}

			return dotProduct;
		}
	}



	public double getCosineSimilarity(Map<String, Double> map1 , Map<String, Double> map2 ){
		if(map1.isEmpty() || map2.isEmpty()) return 0.0;
		else{

			//Map<String, Double> freq1 = TermFunction.getNorm(map1);
			//Map<String, Double> freq2 = TermFunction.getNorm(map2);

			double sim = DocumentFunction.ComputeCosineSimilarity( map1, map2 );
			return sim;
		}
	}

	public double getClusterSimilarity(Map<String, Double> map , Map<String, Map<String,Double>> clusterMap , String key ){
		if(map.isEmpty() || clusterMap.isEmpty()) return 0.0;
		else{
			map = TermFunction.getNorm(map);
			Map<String, Double> clusterSimMap = NaverWIKIClusterSim.get(key);
			Double totalsim = 0.0;

			for(Map.Entry<String, Map<String,Double>> entry : clusterMap.entrySet() ){

				String clusterKey = entry.getKey();
				Double weight = clusterSimMap.get(clusterKey);

				Map<String,Double> mapValue = entry.getValue();
				Map<String,Double> mapValueNorm = TermFunction.getNorm(mapValue);

				double sim = DocumentFunction.ComputeCosineSimilarity( map , mapValueNorm );
				totalsim += sim*weight;

			}

			return totalsim;

		}
	}


	public Map<String, Double> getSubCategoryMap2(Map<String, Double> map1 ) throws Exception{
		Map<String, Double> subCategoryMap = new HashMap<String, Double>();
		for (Entry<String, Double> entry : map1.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();

			Set<String> subCategoryList = wikimap.getSubCategory(key);

			for(String subCategory : subCategoryList){

				if(subCategoryMap.containsKey(subCategory)){
					subCategoryMap.put(subCategory, subCategoryMap.get(subCategory) + value);
				}else{
					subCategoryMap.put(subCategory, value);
				}

			}
		}

		return TermFunction.getNorm(subCategoryMap);	
	}

	public Map<String, Double> getSubCategoryMap(Map<String, Double> map1 ) throws Exception{
		Map<String, Double> subCategoryMap = new HashMap<String, Double>();
		Document d;

		try{

			for (Entry<String, Double> entry : map1.entrySet()) {
				String key = entry.getKey();
				Double value = entry.getValue();
				String keyStr;

				if(Exp.isEnglish){
					keyStr = "Category:"+key;
				}else{
					keyStr = "분류:"+key;
				}

				BooleanQuery booleanQuery = new BooleanQuery();

				Query query1 = new TermQuery(new Term( "category" , keyStr));
				Query query2 = new TermQuery(new Term( "ns" , "14"));


				booleanQuery.add(query1, BooleanClause.Occur.MUST);
				booleanQuery.add(query2, BooleanClause.Occur.MUST);

				TotalHitCountCollector counter = new TotalHitCountCollector();
				searcher.search(booleanQuery, counter);

				if(counter.getTotalHits() == 0 ) {
					continue;
				}
				TopScoreDocCollector collector = TopScoreDocCollector.create(counter.getTotalHits(), true);
				searcher.search(booleanQuery, collector);
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				if(hits.length == 0 ){
					continue;
				}
				else{
					for(ScoreDoc sdoc : hits){
						int docId = sdoc.doc;
						Document document = searcher.doc(docId);
						String subCategory = document.get("title");

						if(Exp.isEnglish){
							subCategory = subCategory.replaceAll("Category:", "");
						}else{
							subCategory = subCategory.replaceAll("분류:", "");
						}

						if(subCategoryMap.containsKey(subCategory)){
							subCategoryMap.put(subCategory, subCategoryMap.get(subCategory) + value);
						}else{
							subCategoryMap.put(subCategory, value);
						}
					}
				}

			}

		}catch(Exception e){
			//e.printStackTrace();
		}

		return TermFunction.getNorm(subCategoryMap);	
	}


	//------------------------------------------------------------------------------//

	public Map<String, Double> getNaverIDF (){
		return NaverIDF;
	}
	public Map<String, Double> getNaverTFIDF(String name){
		return NaverTFIDF.get(name);
	}
	public Map<String, Double> getTFIDF (Map<String, Double> map){
		return DocumentFunction.GetTFIDF(map, NaverIDF);
	}
	public Map<String, Double> getWikiTCICF (Map<String, Double> map){
		return DocumentFunction.GetTFIDF(map, WikiICF);
	}
	public Map<String, Double> getWikiAFIAF (Map<String, Double> map){
		return DocumentFunction.GetTFIDF(map, WikiIAF);
	}

	boolean isNaN(double x) {return x != x;}

	public static double logB(double x, double base) {
		return Math.log(x) / Math.log(base);
	}

	public Document getLuceneDocument( String term, String parameter ) throws Exception{

		BooleanQuery booleanQuery = new BooleanQuery();
		Query query1 = new TermQuery(new Term( parameter , term));
		booleanQuery.add(query1, BooleanClause.Occur.MUST);

		TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
		searcher.search(booleanQuery, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		if(hits.length == 0 ) return null;
		else{
			ScoreDoc hitsTop = hits[0];

			int docId = hitsTop.doc;

			Document d = searcher.doc(docId);
			return d;
		}
	}



}
