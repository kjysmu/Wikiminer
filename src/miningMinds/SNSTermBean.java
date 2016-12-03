package miningMinds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SNSTermBean {

	Double TF;
	Double IDF;
	
	String term;
	String redirectTerm;
	Boolean isRedirect;
	Boolean isHomonym;	
	Boolean isWikiCategory;	
	Boolean isWikiSubCategory;
	Boolean isWikiArticle;
	
	
	Map<String, Double> wikiCategoryMap;
	Map<String, Double> wikiSubCategoryMap;

	Map<String, Double> wikiKeywordCountMap; 
	Map<String, Double> wikiTermCountMap; 
	
	Map<String, Double> wikiArticleMap;

	//Map<String, SNSTermBean> homonymMap; // Wiki Category
	List<SNSTermBean> homonymTermList; 
	
	public SNSTermBean(){
		TF = 0.0;
		isRedirect = false;
		isHomonym = false;
		isWikiCategory = false;
		isWikiSubCategory = false;
		
		wikiCategoryMap = new HashMap<String, Double>();
		wikiKeywordCountMap = new HashMap<String, Double>();
		wikiTermCountMap = new HashMap<String, Double>();
		
		wikiSubCategoryMap = new HashMap<String, Double>();
		
		//homonymMap = new HashMap<String, SNSTermBean>();
		homonymTermList = new ArrayList<SNSTermBean>();
		
		wikiArticleMap = new HashMap<String, Double>();
		
	}
	
	public void setIDF(double value){
		IDF = value;
	}
	public Double getIDF(){
		return IDF;
	}
	public void setTF(double value){
		TF = value;
	}
	public Double getTF(){
		return TF;
	}
	
	public void setTerm(String str){
		term = str;
	}
	public String getTerm(){
		return term;
	}
	
	public void setRedirectTerm(String str){
		isRedirect = true;
		redirectTerm = str;
	}
	public String getRedirectTerm(){
		return redirectTerm;
	}
	public Boolean isRedirect(){
		return isRedirect;
	}

	
	public void setWikiCategoryMap(Map<String,Double> map){
		isWikiCategory = true;
		wikiCategoryMap.clear();
		wikiCategoryMap.putAll(map);
	}
	public Map<String,Double> getWikiCategoryMap(){
		return wikiCategoryMap;
	}
	
	
	
	public void setWikiArticleMap(Map<String,Double> map){
		isWikiArticle = true;
		wikiArticleMap.clear();
		wikiArticleMap.putAll(map);
		
		
		
		
		
		
	}
	public Map<String,Double> getWikiArticleMap(){
		return wikiArticleMap;
	}
	
	public void setWikiSubCategoryMap(Map<String,Double> map){
		isWikiSubCategory = true;
		wikiSubCategoryMap.clear();
		wikiSubCategoryMap.putAll(map);
	}
	public Map<String,Double> getWikiSubCategoryMap(){
		return wikiSubCategoryMap;
	}
	
	public void addWikiCategoryMap(String key, Double value){
		isWikiCategory = true;
		wikiCategoryMap.put(key, value);
	}
	
	public void addWikiArticleMap(String key, Double value){
		isWikiArticle = true;
		wikiArticleMap.put(key, value);
	}
	
	public Boolean isWikiCategory(){
		return isWikiCategory;
	}
	public Boolean isWikiArticle(){
		return isWikiArticle;
	}
	
	public List<SNSTermBean> getHomonymTermList(){
		return homonymTermList;
	}
	
	public void setHomonymTermList(List<SNSTermBean> list){
		isHomonym = true;
		homonymTermList.clear();
		homonymTermList.addAll(list);
	}
	
	public void addHomonymTermList(SNSTermBean bean){
		isHomonym = true;
		homonymTermList.add(bean);
	}
	public Boolean isHomonym(){
		return isHomonym;
	}
	
	
}
