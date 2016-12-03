package parameter;

public class Path {
	
	public static String STOPWORD_FILEPATH = "D:\\Development\\Data\\Naver News\\StopWords.txt";
	
	public static String TWITTER_STOPWORD_FILEPATH ="D:\\Development\\Data\\Naver News\\TwitterStopWords.txt";
	public static String WORD_DICTIONARY_FILEPATH = "D:\\Development\\Data\\Naver News\\WordDictionary.dat";

	//----------------- label path -----------------------------------------------------------------//


	public static String FACEBOOK_LABEL_RESULT_PATH = "D:\\Development\\DATA\\LabelResult\\LabelResultFB_final.txt";
	public static String FACEBOOK_LABEL_RESULT_TERMCOUNT_PATH = "D:\\Development\\DATA\\LabelResult\\TermCounts_FB_final.txt";
	
	public static String TWITTER_LABEL_RESULT_PATH = "D:\\Development\\Data\\LabelResult\\LabelResult.txt";
	public static String TWITTER_LABEL_RESULT_TERMCOUNT_PATH = "D:\\Development\\Data\\LabelResult\\TermCounts_TW.txt";

	//public static String FACEBOOK_LABEL_RESULT_COUNT_PATH = "D:\\Development\\Data\\LabelResult\\LabelResultCountFB.txt";
	//public static String TWITTER_LABEL_RESULT_COUNT_PATH = "D:\\Development\\Data\\LabelResult\\LabelResultCount.txt";

	//----------------- category model path -----------------------------------------------------------------//

	// NEW
	
	public static String CATEGORY_MODEL_WIKI_CFICF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-CFICF\\";
	public static String CATEGORY_MODEL_WIKI_CF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-CF\\";
	
	public static String CATEGORY_MODEL_ESA_AFIAF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-AFIAF\\";
	public static String CATEGORY_MODEL_ESA_AF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-AF\\";
	
	public static String CATEGORY_MODEL_TFIDF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\TFIDF\\";
	
	public static String CATEGORY_MODEL_LDA_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\LDA\\";	
	public static String LDA_TOPIC_FILEPATH = "D:\\Development\\DATA\\NewsX\\NaverLDAtrainingAll.txt";

	public static String CATEGORY_MODEL_WIKI_CFICF_CS_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-Cluster\\cluster\\";
	public static String CATEGORY_MODEL_WIKI_CFICF_CS_SIM_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-Cluster\\clusterSim\\";
	
	public static String WIKI_ICF_FILEPATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-ICF\\WikiICF.txt";
	
	public static String IDF_FILEPATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\IDF\\NaverIDF.txt";
	
	//----------------- wikimap path -----------------------------------------------------------------//
	
	public static String WIKIMAP_ARTICLE_CATEGORY_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleCategory.txt"; // WikiTerm to WikiCategory
	public static String WIKIMAP_REDIRECT_FILEPATH = "D:\\Development\\wikimap\\wikimap_redirect.txt"; // WikiTerm to WikiTerm(RD)
	public static String WIKIMAP_HOMONYM_FILEPATH = "D:\\Development\\wikimap\\wikimap_homonym.txt"; // WikiTerm to WikiTerm(HM)

	public static String WIKIMAP_CATEGORY_SUPERCATEGORY_FILEPATH = "D:\\Development\\wikimap\\wikimap_categorySupercategory.txt"; // WikiCategory to WikiCategory
	public static String WIKIMAP_CATEGORY_SUBCATEGORY_FILEPATH = "D:\\Development\\wikimap\\wikimap_categorySubcategory.txt"; // WikiCategory to WikiCategory
	public static String WIKIMAP_CATEGORY_ARTICLE_FILEPATH = "D:\\Development\\wikimap\\wikimap_categoryArticle.txt"; // WikiCategory to WikiCategory

	public static String WIKIMAP_ARTICLE_RAWDATA_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleRawdata.txt"; // WikiTerm to Raw WikiArticle Text
	public static String WIKIMAP_ARTICLE_TERMCOUNTS_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleTermCounts.txt"; // WikiArticle with TermCount (Parser dependent)
	public static String WIKIMAP_ARTICLE_KEYWORDS_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleKeywords.txt"; // WikiArticle with KeyCount
	
	public static String WIKIMAP_WIKILOG_FILEPATH = "D:\\Development\\wikimap\\wikilog.txt"; // Log Info
	
	public static String WIKIMAP_ESA_FILEPATH = "D:\\Development\\wikimap\\wikimap_esa.txt"; // WikiArticle with KeyCount
	
	
	
	//----------------- results path -----------------------------------------------------------------//

	public static String RESULT_PATH = "D:\\Development\\result\\News"+Exp.CorpusIndex+"\\";
	
	public static String RESULT_COMPARISON_FILEPATH = "D:\\Development\\result\\News"+Exp.CorpusIndex+"\\" + Exp.comparisonPath1 + "\\" + Exp.comparisonPath2 + "\\recomCategoryResults.txt";
	
	public static String TEST_PATH = "D:\\Development\\test\\";
	
	
	
	
	
	
	//public static String WIKIPEDIA_PATH = "D:\\Development\\Data\\Wikipedia\\";
	//public static String WIKIPEDIA_ALL_TITLES_FILEPATH = "D:\\Development\\Data\\Wikipedia\\kowiki-20140706-all-titles-in-ns0.dat";
	
	//public static String NEWS_TERMCOUNT_DIRECTORYPATH = "D:\\Development\\Data\\Naver News Word Counts\\";
	//public static String NEWS_DIRECTORYPATH = "D:\\Development\\Data\\Naver News\\";
	
	//public static String KOMORAN2_MODEL_PATH = "D:\\workspace\\library\\komoran-2.0\\models";
	//public static String KOMORAN2_DIC_USER_FILEPATH = "D:\\workspace\\library\\komoran-2.0\\models\\dic.user";
	//public static String KOMORAN2_MECAB_PATH = "D:\\workspace\\library\\komoran-2.0\\models\\mecab\\";
	
	//public static String KOMORAN_MODEL_PATH = "D:\\project\\library\\datas";
	//public static String STANFORD_MODEL_PATH = "D:\\project\\library\\stanford-postagger\\models\\english-left3words-distsim.tagger";
	
	
	
	//public static String WIKI_ICF_CS_PATH = "D:\\Development\\NaverCategoryModel\\IDF\\WikiICF_CS\\";
	
	//public static String CATEGORY_MODEL_LDA_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\LDA\\CategoryModel\\"+Exp.str_LDAmodel+".txt";
	
	/*
	public static String CATEGORY_MODEL_WIKI_CFICF_PATH = "D:\\Development\\NaverCategoryModel\\WikiCFICF\\";
	public static String CATEGORY_MODEL_WIKI_CF_PATH = "D:\\Development\\NaverCategoryModel\\WikiCF\\";
	
	public static String CATEGORY_MODEL_ESA_CFICF_PATH = "D:\\Development\\NaverCategoryModel\\ESA_CFICF\\";
	public static String CATEGORY_MODEL_ESA_CF_PATH = "D:\\Development\\NaverCategoryModel\\ESA_CF\\";
	public static String CATEGORY_MODEL_ESA_AFIAF_PATH = "D:\\Development\\NaverCategoryModel\\ESA_AFIAF\\";
	public static String CATEGORY_MODEL_ESA_AF_PATH = "D:\\Development\\NaverCategoryModel\\ESA_AF\\";
	
	public static String CATEGORY_MODEL_TFIDF_PATH = "D:\\Development\\NaverCategoryModel\\TFIDF\\";
	public static String CATEGORY_MODEL_LDA_PATH = "D:\\Development\\NaverCategoryModel\\LDA\\CategoryModel\\"+Exp.str_LDAmodel+".txt";
	
	*/
	
	//ClusterMethod
	
	//public static String CATEGORY_MODEL_WIKI_CFICF_CSICF_PATH = "D:\\Development\\NaverCategoryModel\\WikiCFICF_CSICF\\";
	
	//public static String LDA_TOPIC_WIKI_PATH = "D:\\Development\\NaverCategoryModel\\LDA\\LDATopicWIKI\\"+ Exp.wiki_topic_path;
	
	//public static String CATEGORY_MODEL_WIKISUB_CFICF_PATH = "D:\\Development\\NaverCategoryModel\\WikiCFICF\\subcategory_cficf\\";
	//public static String CATEGORY_MODEL_WIKISUB_CF_PATH = "D:\\Development\\NaverCategoryModel\\WikiCFICF\\subcategory\\";
	
	
	//public static String WIKI_SUB_ICF_FILEPATH = "D:\\Development\\NaverCategoryModel\\IDF\\WikiSubICF.txt";
	
	//public static String ESA_IAF_FILEPATH = "D:\\Development\\NaverCategoryModel\\IDF\\ESA_IAF.txt";
	
	//public static String ESA_ICF_FILEPATH = "D:\\Development\\NaverCategoryModel\\IDF\\ESA_ICF.txt";
	
	
	
	//English
	
	/*
	public static String CATEGORY_MODEL_EN_WIKI_CFICF_PATH = "D:\\Development\\NaverCategoryModel_EN\\WikiCFICF\\";
	public static String CATEGORY_MODEL_EN_WIKI_CF_PATH = "D:\\Development\\NaverCategoryModel_EN\\WikiCF\\";
	public static String CATEGORY_MODEL_EN_TFIDF_PATH = "D:\\Development\\NaverCategoryModel_EN\\TFIDF\\";
	public static String CATEGORY_MODEL_EN_LDA_PATH = "D:\\Development\\NaverCategoryModel_EN\\LDA\\CategoryModel\\"+Exp.str_LDAmodel+".txt";

	public static String WIKI_ICF_EN_FILEPATH = "D:\\Development\\NaverCategoryModel_EN\\IDF\\WikiICF.txt";
	public static String WIKI_SUB_ICF_EN_FILEPATH = "D:\\Development\\NaverCategoryModel_EN\\IDF\\WikiSubICF.txt";
	
	public static String NAVER_CATEGORY_EN_FILEPATH = "D:\\Development\\NaverCategoryModel_EN\\NaverCategoryEnMap.txt";
	*/
	


	
	
	
	//----------------- wikiCluster path -----------------------------------------------------------------//

	//public static String WIKI_CLUSTER_PATH = "D:\\Development\\WikiCluster\\all\\";
	//public static String WIKI_CLUSTER_RESULTS_PATH = "D:\\Development\\WikiCluster\\all\\results\\";

	//------------------------------------------Deprecated----------------------------------------------
	

	//public static String CATEGORY_MODEL_WIKI_SUB_CFICF_PATH = "D:\\Development\\NaverCategoryModel\\WikiCFICF\\subcategory_cficf\\";

	//public static String NEWS_TERMCOUNT_DIRECTORYPATH = "D:\\Development\\Data\\Naver News Word Counts Komoran\\";
	//public static String NEWS_TERMCOUNT_DIRECTORYPATH_KOMORAN = "D:\\Development\\Data\\Naver News Word Counts Komoran\\";
	//public static String NEWS_TERMCOUNT_DIRECTORYPATH_KOMORAN2 = "D:\\Development\\Data\\Naver News Word Counts Komoran2\\";
		
	
	//public static String USER_ID_MAP_FILEPATH = "D:\\Development\\Data\\Twitter\\UserID\\UserIDMap.txt";
	//public static String USER_TWITTER_TERMCOUNT_DIRECTORYPATH = "D:\\Development\\Data\\Facebook\\Term Counts Komoran";
	
	//public static String NaverLDApath = "D:\\Development\\LDA\\results\\" +Exp.str_LDAmodel+ ".txt";
	//public static String NaverLDACategoryModelpath = "D:\\Development\\LDA\\model_new\\"+Exp.str_LDAmodel+".txt";
	//public static String TFIDF_DIRECTORYPATH = "D:\\Development\\TFIDF\\";

	//public static String WIKIMAP_TermCategoryMapPath = "D:\\Development\\wikimap\\wikimap_ct.txt"; // WikiCategory to WikiCategory
	//public static String LDA_CategoryModelPath = "D:\\Development\\NaverCategoryModel\\LDA\\CategoryModel\\"+Exp.str_LDAmodel+".txt";

	//public static String WikiCFICF_CategoryModelPath = "D:\\Development\\NaverCategoryModel\\WikiCFICF\\";
	//public static String WIKI_CategoryModelPath = "D:\\Development\\NaverCategoryModel\\WikiCF\\";
	//public static String TFIDF_CategoryModelPath = "D:\\Development\\NaverCategoryModel\\TFIDF\\";
	//public static String resultComparisonFilePath = "D:\\Development\\result\\" + Exp.comparisonPath1 + "\\" + Exp.comparisonPath2 + "\\recomCategoryResults.txt";

}
