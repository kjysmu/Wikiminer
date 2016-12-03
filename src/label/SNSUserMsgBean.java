/**
 * 
 */
package label;

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
	
	Map<String, Double> labelCategoryMap; // User-Labeled Category
	Map<String, Double> recomCategoryMap; // Recommended Category
	
	Map<String, Double> recomTotalCategoryMap; // All Sorted Recommended Category
	
	public SNSUserMsgBean(){
		
		labelCategoryMap = new HashMap<String, Double>();	
		recomCategoryMap = new HashMap<String, Double>();
		recomTotalCategoryMap = new HashMap<String, Double>();
		
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
	

}
