package parameter;

public class Path2 {
	
	int CorpusIndex;
	
	public String STOPWORD_FILEPATH = "D:\\Development\\Data\\Naver News\\StopWords.txt";
	
	public String TWITTER_STOPWORD_FILEPATH ="D:\\Development\\Data\\Naver News\\TwitterStopWords.txt";
	public String WORD_DICTIONARY_FILEPATH = "D:\\Development\\Data\\Naver News\\WordDictionary.dat";

	//----------------- label path -----------------------------------------------------------------//


	public String FACEBOOK_LABEL_RESULT_PATH = "D:\\Development\\DATA\\LabelResult\\LabelResultFB_final.txt";
	public String FACEBOOK_LABEL_RESULT_TERMCOUNT_PATH = "D:\\Development\\DATA\\LabelResult\\TermCounts_FB_final.txt";
	
	public String TWITTER_LABEL_RESULT_PATH = "D:\\Development\\Data\\LabelResult\\LabelResult.txt";
	public String TWITTER_LABEL_RESULT_TERMCOUNT_PATH = "D:\\Development\\Data\\LabelResult\\TermCounts_TW.txt";

	//public String FACEBOOK_LABEL_RESULT_COUNT_PATH = "D:\\Development\\Data\\LabelResult\\LabelResultCountFB.txt";
	//public String TWITTER_LABEL_RESULT_COUNT_PATH = "D:\\Development\\Data\\LabelResult\\LabelResultCount.txt";

	//----------------- category model path -----------------------------------------------------------------//

	// NEW
	
	public String CATEGORY_MODEL_WIKI_CFICF_L2_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-CFICF-L2NORM\\";
	
	public String CATEGORY_MODEL_WIKI_CFICF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-CFICF\\";
	public String CATEGORY_MODEL_WIKI_CF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-CF\\";
	
	public String CATEGORY_MODEL_ESA_AFIAF_L2_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-AFIAF-L2NORM\\";
	
	public String CATEGORY_MODEL_ESA_AFIAF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-AFIAF\\";
	public String CATEGORY_MODEL_ESA_AF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-AF\\";
	
	public String CATEGORY_MODEL_TFIDF_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\TFIDF\\";
	
	public String CATEGORY_MODEL_LDA_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\LDA\\";	
	public String LDA_TOPIC_FILEPATH = "D:\\Development\\DATA\\NewsX\\NaverLDAtrainingAll.txt";

	public String CATEGORY_MODEL_WIKI_CFICF_CS_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-Cluster\\cluster\\";
	public String CATEGORY_MODEL_WIKI_CFICF_CS_SIM_PATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-Cluster\\clusterSim\\";
	
	public String WIKI_ICF_FILEPATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-ICF\\WikiICF.txt";
	public String WIKI_IAF_FILEPATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\WIKI-IAF\\WikiIAF.txt";
	
	
	
	public String IDF_FILEPATH = "D:\\Development\\DATA\\News"+Exp.CorpusIndex+"\\IDF\\NaverIDF.txt";
	
	//----------------- wikimap path -----------------------------------------------------------------//
	
	public String WIKIMAP_ARTICLE_CATEGORY_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleCategory.txt"; // WikiTerm to WikiCategory
	public String WIKIMAP_REDIRECT_FILEPATH = "D:\\Development\\wikimap\\wikimap_redirect.txt"; // WikiTerm to WikiTerm(RD)
	public String WIKIMAP_HOMONYM_FILEPATH = "D:\\Development\\wikimap\\wikimap_homonym.txt"; // WikiTerm to WikiTerm(HM)

	public String WIKIMAP_CATEGORY_SUPERCATEGORY_FILEPATH = "D:\\Development\\wikimap\\wikimap_categorySupercategory.txt"; // WikiCategory to WikiCategory
	public String WIKIMAP_CATEGORY_SUBCATEGORY_FILEPATH = "D:\\Development\\wikimap\\wikimap_categorySubcategory.txt"; // WikiCategory to WikiCategory
	public String WIKIMAP_CATEGORY_ARTICLE_FILEPATH = "D:\\Development\\wikimap\\wikimap_categoryArticle.txt"; // WikiCategory to WikiCategory

	public String WIKIMAP_ARTICLE_RAWDATA_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleRawdata.txt"; // WikiTerm to Raw WikiArticle Text
	public String WIKIMAP_ARTICLE_TERMCOUNTS_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleTermCounts.txt"; // WikiArticle with TermCount (Parser dependent)
	public String WIKIMAP_ARTICLE_KEYWORDS_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleKeywords.txt"; // WikiArticle with KeyCount
	
	public String WIKIMAP_WIKILOG_FILEPATH = "D:\\Development\\wikimap\\wikilog.txt"; // Log Info
	
	public String WIKIMAP_ESA_FILEPATH = "D:\\Development\\wikimap\\wikimap_esa.txt"; // WikiArticle with KeyCount
	
	
	
	//----------------- results path -----------------------------------------------------------------//

	public String RESULT_PATH = "D:\\Development\\result\\News"+Exp.CorpusIndex+"\\";
	
	public String RESULT_COMPARISON_FILEPATH = "D:\\Development\\result\\News"+Exp.CorpusIndex+"\\" + Exp.comparisonPath1 + "\\" + Exp.comparisonPath2 + "\\recomCategoryResults.txt";
	
	public String TEST_PATH = "D:\\Development\\test\\";
	
	
	
	
	
	
	
	public Path2(int corpusIndex){
		
		init(corpusIndex);
		
	}
	
	void init(int index){
		
		STOPWORD_FILEPATH = "D:\\Development\\Data\\Naver News\\StopWords.txt";
		
		TWITTER_STOPWORD_FILEPATH ="D:\\Development\\Data\\Naver News\\TwitterStopWords.txt";
		WORD_DICTIONARY_FILEPATH = "D:\\Development\\Data\\Naver News\\WordDictionary.dat";

		//----------------- label path -----------------------------------------------------------------//

		FACEBOOK_LABEL_RESULT_PATH = "D:\\Development\\DATA\\LabelResult\\LabelResultFB_final.txt";
		FACEBOOK_LABEL_RESULT_TERMCOUNT_PATH = "D:\\Development\\DATA\\LabelResult\\TermCounts_FB_final.txt";
		
		TWITTER_LABEL_RESULT_PATH = "D:\\Development\\Data\\LabelResult\\LabelResult.txt";
		TWITTER_LABEL_RESULT_TERMCOUNT_PATH = "D:\\Development\\Data\\LabelResult\\TermCounts_TW.txt";

		//----------------- category model path -----------------------------------------------------------------//

		// NEW
		
		CATEGORY_MODEL_WIKI_CFICF_PATH = "D:\\Development\\DATA\\News"+index+"\\WIKI-CFICF\\";
		CATEGORY_MODEL_WIKI_CF_PATH = "D:\\Development\\DATA\\News"+index+"\\WIKI-CF\\";
		
		CATEGORY_MODEL_ESA_AFIAF_PATH = "D:\\Development\\DATA\\News"+index+"\\WIKI-AFIAF\\";
		CATEGORY_MODEL_ESA_AF_PATH = "D:\\Development\\DATA\\News"+index+"\\WIKI-AF\\";
		
		CATEGORY_MODEL_TFIDF_PATH = "D:\\Development\\DATA\\News"+index+"\\TFIDF\\";
		
		CATEGORY_MODEL_LDA_PATH = "D:\\Development\\DATA\\News"+index+"\\LDA2\\";	
		LDA_TOPIC_FILEPATH = "D:\\Development\\DATA\\NewsX\\NaverLDAtrainingAll.txt";

		CATEGORY_MODEL_WIKI_CFICF_CS_PATH = "D:\\Development\\DATA\\News"+index+"\\WIKI-Cluster\\cluster\\";
		CATEGORY_MODEL_WIKI_CFICF_CS_SIM_PATH = "D:\\Development\\DATA\\News"+index+"\\WIKI-Cluster\\clusterSim\\";
		
		WIKI_ICF_FILEPATH = "D:\\Development\\DATA\\News"+index+"\\WIKI-ICF\\WikiICF.txt";
		
		IDF_FILEPATH = "D:\\Development\\DATA\\News"+index+"\\IDF\\NaverIDF.txt";
		
		//----------------- wikimap path -----------------------------------------------------------------//
		
		WIKIMAP_ARTICLE_CATEGORY_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleCategory.txt"; // WikiTerm to WikiCategory
		WIKIMAP_REDIRECT_FILEPATH = "D:\\Development\\wikimap\\wikimap_redirect.txt"; // WikiTerm to WikiTerm(RD)
		WIKIMAP_HOMONYM_FILEPATH = "D:\\Development\\wikimap\\wikimap_homonym.txt"; // WikiTerm to WikiTerm(HM)

		WIKIMAP_CATEGORY_SUPERCATEGORY_FILEPATH = "D:\\Development\\wikimap\\wikimap_categorySupercategory.txt"; // WikiCategory to WikiCategory
		WIKIMAP_CATEGORY_SUBCATEGORY_FILEPATH = "D:\\Development\\wikimap\\wikimap_categorySubcategory.txt"; // WikiCategory to WikiCategory
		WIKIMAP_CATEGORY_ARTICLE_FILEPATH = "D:\\Development\\wikimap\\wikimap_categoryArticle.txt"; // WikiCategory to WikiCategory

		WIKIMAP_ARTICLE_RAWDATA_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleRawdata.txt"; // WikiTerm to Raw WikiArticle Text
		WIKIMAP_ARTICLE_TERMCOUNTS_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleTermCounts.txt"; // WikiArticle with TermCount (Parser dependent)
		WIKIMAP_ARTICLE_KEYWORDS_FILEPATH = "D:\\Development\\wikimap\\wikimap_articleKeywords.txt"; // WikiArticle with KeyCount
		
		WIKIMAP_WIKILOG_FILEPATH = "D:\\Development\\wikimap\\wikilog.txt"; // Log Info
		
		WIKIMAP_ESA_FILEPATH = "D:\\Development\\wikimap\\wikimap_esa.txt"; // WikiArticle with KeyCount
		
		//----------------- results path -----------------------------------------------------------------//

		RESULT_PATH = "D:\\Development\\result\\News"+index+"\\";
		
		RESULT_COMPARISON_FILEPATH = "D:\\Development\\result\\News"+index+"\\" + Exp.comparisonPath1 + "\\" + Exp.comparisonPath2 + "\\recomCategoryResults.txt";
		
		TEST_PATH = "D:\\Development\\test\\";
		
	}
	
}
