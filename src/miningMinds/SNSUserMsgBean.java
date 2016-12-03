/**
 * 
 */
package miningMinds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author kjysmu
 *
 */
public class SNSUserMsgBean {
	String msgid;
	String msg;
	String time;
	
	//Map<String , SNSTermBean> termMap;
	List<SNSTermBean> termList;
	List<SNSTermBean> termExpList;

	Map<String, Double> termCountMap; // Term counts for msg
	
	Map<String, Double> termFreqMap; // Term counts for msg
	Map<String, Double> termExpFreqMap; // Term counts for msg
	
	Map<String, Double> labelCategoryMap; // User-Labeled Category
	Map<String, Double> recomCategoryMap; // Recommended Category
	
	Map<String, Double> recomTotalCategoryMap; // All Sorted Recommended Category
	Map<String, Double> wikiCategoryMap; // Wiki Category
	Map<String, Double> wikiSubCategoryMap; // Wiki Category
	Map<String, Double> wikiTotalCategoryMap; // Wiki Category
	
	Map<String, Double> wikiArticleMap; // Wiki Article
	Map<String, Double> wikiTotalArticleMap; // Wiki Article
	
	public SNSUserMsgBean(){
		//termMap = new HashMap<String, SNSTermBean>();
		termList = new ArrayList<SNSTermBean>();
		termExpList = new ArrayList<SNSTermBean>();
		
		termCountMap = new HashMap<String, Double>();
		
		termFreqMap = new HashMap<String, Double>();
		termExpFreqMap = new HashMap<String, Double>();
		
		labelCategoryMap = new HashMap<String, Double>();	
		recomCategoryMap = new HashMap<String, Double>();
		recomTotalCategoryMap = new HashMap<String, Double>();
		
		wikiCategoryMap = new HashMap<String, Double>();
		wikiSubCategoryMap = new HashMap<String, Double>();
		wikiTotalCategoryMap = new HashMap<String, Double>();
		
		wikiArticleMap = new HashMap<String, Double>();
		wikiTotalArticleMap = new HashMap<String, Double>();		
	}
	
	public String getMsgid(){
		return msgid;
	}
	public void setMsgid(String str){
		msgid = str;
	}
	
	public String getMsg(){
		return msg;
	}
	public void setMsg(String str){
		msg = str;
	}
	
	public String getTime(){
		return time;
	}
	public void setTime(String str){
		time = str;
	}
	
	public Map<String, Double> getTermCountMap(){
		return termCountMap;
	}
	public void setTermCountMap(Map<String, Double> map){
		termCountMap.clear();
		termCountMap.putAll(map);
	}
	
	public Map<String, Double> getTermFreqMap(){
		return termFreqMap;
	}
	public void setTermFreqMap(Map<String, Double> map){
		termFreqMap.clear();
		termFreqMap.putAll(map);
	}
	
	public Map<String, Double> getTermExpFreqMap(){
		return termExpFreqMap;
	}
	public void setTermExpFreqMap(Map<String, Double> map){
		termExpFreqMap.clear();
		termExpFreqMap.putAll(map);
	}
	
	//User label category map
	public Map<String, Double> getLabelCategoryMap(){
		return labelCategoryMap;
	}
	public void setLabelCategoryMap(Map<String, Double> map){
		labelCategoryMap.clear();
		labelCategoryMap.putAll(map);
	}
	public void addLabelCategoryMap(String key){
		labelCategoryMap.put(key, 1.0);
	}
	public void addLabelCategoryMap(String key, Double value){
		labelCategoryMap.put(key, value);
	}
	
	//Recom category map
	public Map<String, Double> getRecomCategoryMap(){
		return recomCategoryMap;
	}
	
	public void setRecomCategoryMap(Map<String, Double> map){
		recomCategoryMap.clear();
		recomCategoryMap.putAll(map);
	}
	public void addRecomCategoryMap(String key, Double value){
		recomCategoryMap.put(key, value);
	}
	
	//Recom Total category map
	public Map<String, Double> getRecomTotalCategoryMap(){
		return recomTotalCategoryMap;
	}
	
	public void setRecomTotalCategoryMap(Map<String, Double> map){
		recomTotalCategoryMap.clear();
		recomTotalCategoryMap.putAll(map);
	}
	
	// WikiCategoryMap for MSG
	public Map<String, Double> getWikiCategoryMap(){
		return wikiCategoryMap;
	}
	
	public void setWikiCategoryMap(Map<String, Double> map){
		wikiCategoryMap.clear();
		wikiCategoryMap.putAll(map);
	}
	
	public Map<String, Double> getWikiArticleMap(){
		return wikiArticleMap;
	}
	
	public void setWikiArticleMap(Map<String, Double> map){
		wikiArticleMap.clear();
		wikiArticleMap.putAll(map);
	}
	
	
	public Map<String, Double> getWikiSubCategoryMap(){
		return wikiSubCategoryMap;
	}
	
	public void setWikiSubCategoryMap(Map<String, Double> map){
		wikiSubCategoryMap.clear();
		wikiSubCategoryMap.putAll(map);
	}
	
	public Map<String, Double> getWikiTotalCategoryMap(){
		return wikiTotalCategoryMap;
	}
	
	public void setWikiTotalCategoryMap(Map<String, Double> map){
		wikiTotalCategoryMap.clear();
		wikiTotalCategoryMap.putAll(map);
	}
	
	
	public Map<String, Double> getWikiTotalArticleMap(){
		return wikiTotalArticleMap;
	}
	
	public void setWikiTotalArticleMap(Map<String, Double> map){
		wikiTotalArticleMap.clear();
		wikiTotalArticleMap.putAll(map);
	}
	
	
	public List<SNSTermBean> getTermList(){
		return termList;
	}
	
	public List<SNSTermBean> getTermExpList(){
		return termExpList;
	}
	
	public void setTermList(List<SNSTermBean> list){
		termList.clear();
		termList.addAll(list);
	}
	
	public void addTermList(SNSTermBean bean){
		termList.add(bean);
	}
	
	public void addTermExpList(SNSTermBean bean){
		termExpList.add(bean);
	}
	
	public void setTermExpList(List<SNSTermBean> list){
		termExpList.clear();
		termExpList.addAll(list);
	}
	
	
	// Term Bean
	/*
	public Map<String, SNSTermBean> getTermMap(){
		return termMap;
	}
	
	public void setTermMap(Map<String, SNSTermBean> map){
		termMap.clear();
		termMap.putAll(map);
	}
	*/
	// Term List

	

}
