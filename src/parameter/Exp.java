package parameter;

public class Exp {
	
	//--------------------------New Setting---------------------------------

	public static int CorpusIndex = 1;
	
	//--------------------------Frequency High---------------------------------
	
	public static String SNSType = "Facebook"; // Facebook or Twitter
	
	//public static String approach = "wiki-cficf-cs-mix";
	//public static String approach = "tfidf-total";
	//public static String approach = "tfidf";
	public static String approach = "tfidf-lda";
	
	//New for paper
	
	public static String EXP_LEVEL = "CFICF";
	
	public static boolean isL2Norm = true;
	
	public static boolean isLDA_DOT = true;
	
	
	public boolean isTESTMODE = true;
	
	//CFICF
	//AFIAF
	//CS
	//MIX
	
	// Parameters to tune for paper work.

	//gamma
	public static double w_cficf = 0.4; 
	public static double w_afiaf = 0.3;
	public static double w_cs = 0.3;
	
	public static double subCategoryWeight_User = 0.6; // subcategory ratio - alpha
	
	public static String wikisim_ratio = "0.33 0.67"; // beta
	//public static String wikisim_ratio = "0.5 0.5"; // beta
	//public static String wikisim_ratio = "0.67 0.33"; // beta

	/*
	 * tfidf-total : baseline (total)
	 * tfidf : baseline (msg)
	 * 
	 * wiki-cf : wikipedia-CF approach
	 * wiki-cficf : wikipedia-CFICF-approach
	 * wiki-cficf-cs : wikipedia-CFICF & Cluster Approach
	 * 
	 * optional parameters
	 * -lda : use LDA Model
	 * -exp : use Expansion Model 
	 * 
	 */
	public static String resultPathAppend = approach + "-v3.00-dot-" + EXP_LEVEL; // D:\\Development\\result\\ + X
	public static String versionRemark = "";

	public static Boolean isComparisonResults = false;
	
	public static String comparisonPath1 = "tfidf-v1.0.09";
	public static String comparisonPath2 = "0.0 0.2";
	
	
	
	public static Boolean isResultAnalysis = true;

	//--------------------------Frequency Middle----------------------------------
	
	public static double msg_threshold = 0.0;
	public static double msg_threshold2 = 0.2; // threshold (RATIO)
	
	public static double subCategoryWeight_Naver = 0.0; // subcategory ratio

	public static double enrichmentWeight = 0.0;
	public static double clusterWeight = 0.5;
	//ESA
	public static double ESA_Weight = 0.2;
	
	public static double ESA_CategoryWeight = 0.0;
	public static double ESA_threshold = 0.4;

	public static int msg_maxcategory = 2;
	
	public static Boolean isWiki_NaverIDF = true;
	public static String wiki_topic_path = "WikiTopic"; // D:\\Development\\LDA\\results\\ + X
	public static String str_LDAmodel = "NaverLDAtrainingAll";

	//--------------------------Frequency Low---------------------------------
	
	public static int categoryDebugNum = 4; // # of category to be appeared regardless of threshold
	public static double WORD_FREQUENCY_THRESHOLD = 0.0001;
	public static String resultPathTestAppend = "test1"; // D:\\Development\\result\\ *Append* \\ + X

	
	public static Boolean isWikimapOfflineMode = true;
	public static Boolean isWebService = false;
	public static Boolean isEnglish = false;
	public static Boolean isWikimap = true;
	
	public static Boolean btfidf = true; // if false, only TF is used for Naver news
	public static Boolean btfidf_user = true; // if false, only TF is used for SNS msg
	
	public static Boolean isTestUser = false;
	public static String str_testUser = "100000630368349";
	
	//--------------------------Deprecated--------------------------------
	//public static Boolean isTotalTermCount = false; // If true, TotalTermCount Approach (Mr.Han) is used 
	// public static Boolean isWikiLDA = false;


}
