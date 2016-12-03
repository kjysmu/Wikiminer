/**
 * 
 */
package miningMinds;

import java.util.HashMap;
import java.util.Map;

// import edu.uci.ics.jung.graph.Graph;
// import edu.uci.ics.jung.graph.UndirectedSparseGraph;

import functions.*;
import util.TermFunction;

/**
 * @author kjysmu
 *
 */
public class SNSUserBean {
	String userid;
	Double similarity;
	Double msgAccuracy;
	
	Map<String, SNSUserMsgBean> msgMap;
	Map<String, Double> recomTotalCategoryMap;
	Map<String, Double> labelTotalCategoryMap;
	Boolean isRecomTotalCategoryMap;
	Boolean isLabelTotalCategoryMap;
	Boolean isMsgAccuracy;

	Map<String, Double> totalTermCountMap; // Total MSG Count for user
	
	// Graph<TermGraphNode,TermGraphEdge> termGraph;

	public SNSUserBean(){
		msgMap = new HashMap<String, SNSUserMsgBean>();
		recomTotalCategoryMap = new HashMap<String, Double>();
		labelTotalCategoryMap = new HashMap<String, Double>();
		isRecomTotalCategoryMap = false;
		isLabelTotalCategoryMap = false;
		isMsgAccuracy = false;

		totalTermCountMap = new HashMap<String, Double>();
		//termGraph = new UndirectedSparseGraph<TermGraphNode, TermGraphEdge>();
	}
	
	public void setLabelTotalCategoryMap(Map<String, Double> map){
		isLabelTotalCategoryMap = true;
		labelTotalCategoryMap = map;
		//labelTotalCategoryMap.putAll(map);
	}
	public void setRecomTotalCategoryMap(Map<String, Double> map){
		isRecomTotalCategoryMap = true;		
		recomTotalCategoryMap = map;
		
	}
	
	/*
	public void setTermGraph(Graph<TermGraphNode,TermGraphEdge> graph){
		termGraph = graph;
	}
	
	
	public Graph<TermGraphNode,TermGraphEdge> getTermGraph(){
		return termGraph;
	}
	*/

	public String getUserid(){
		return userid;
	}
	public void setUserid(String str){
		userid = str;
	}

	public Double getSimilarity(){
		return similarity;
	}

	public void setSimilarity(double sim){
		similarity = sim;
	}

	public Map<String, SNSUserMsgBean> getMsgMap(){
		return msgMap;
	}

	public void setMsgMap(Map<String, SNSUserMsgBean> map){
		msgMap.clear();
		msgMap.putAll(map);
	}
	public void addMsgMap(String id, SNSUserMsgBean userMsgBean){
		msgMap.put(id, userMsgBean);
	}

	public Map<String, Double> computeRecomTotalCategoryMap(){
		return computeRecomTotalCategoryMap(false);
	}
	public Map<String, Double> computeRecomTotalCategoryMap(boolean isWeight){
		isRecomTotalCategoryMap = false;
		return getRecomTotalCategoryMap(isWeight);
	}

	public Map<String, Double> computeLabelTotalCategoryMap(){
		return computeLabelTotalCategoryMap(false);
	}
	public Map<String, Double> computeLabelTotalCategoryMap(boolean isWeight){
		isLabelTotalCategoryMap = false;
		return getLabelTotalCategoryMap(isWeight);
	}

	public Map<String, Double> getRecomTotalCategoryMap(){
		return getRecomTotalCategoryMap(false);
	}
	public Map<String, Double> getRecomTotalCategoryMap(boolean isWeight){

		if(!isRecomTotalCategoryMap){
			recomTotalCategoryMap.clear();
			for(Map.Entry<String, SNSUserMsgBean> msgMapEntry : msgMap.entrySet()){
				SNSUserMsgBean userMsgBean = msgMapEntry.getValue();
				Map<String, Double> recomCategoryMap = userMsgBean.getRecomCategoryMap();
				for(Map.Entry<String, Double> recomCategoryMapEntry : recomCategoryMap.entrySet()){
					String key = recomCategoryMapEntry.getKey();
					Double value = recomCategoryMapEntry.getValue();
					if(isWeight) value = 1.0;
					if(recomTotalCategoryMap.containsKey(key)){
						recomTotalCategoryMap.put(key, recomTotalCategoryMap.get(key) + value);
					}else{
						recomTotalCategoryMap.put(key, value);
					}
				}
			}
			isRecomTotalCategoryMap = true;
		}

		return recomTotalCategoryMap;
	}

	public Map<String, Double> getLabelTotalCategoryMap(){
		return getLabelTotalCategoryMap(false);
	}
	public Map<String, Double> getLabelTotalCategoryMap(boolean isWeight){

		if(!isLabelTotalCategoryMap){
			labelTotalCategoryMap.clear();
			for(Map.Entry<String, SNSUserMsgBean> msgMapEntry : msgMap.entrySet()){
				SNSUserMsgBean userMsgBean = msgMapEntry.getValue();
				Map<String, Double> labelCategoryMap = userMsgBean.getLabelCategoryMap();
				for(Map.Entry<String, Double> labelCategoryMapEntry : labelCategoryMap.entrySet()){
					String key = labelCategoryMapEntry.getKey();
					Double value = labelCategoryMapEntry.getValue();
					if(isWeight) value = 1.0;
					if(labelTotalCategoryMap.containsKey(key)){
						labelTotalCategoryMap.put(key, labelTotalCategoryMap.get(key) + value);
					}else{
						labelTotalCategoryMap.put(key, value);
					}
				}
			}
			isLabelTotalCategoryMap = true;
		}

		return labelTotalCategoryMap;
	}

	public Map<String, Double> getTotalTermCount(){
		return totalTermCountMap;
	}
	
	public int getTotalTermCountNum(){
		int total = 0;
		for(Map.Entry<String, Double> entry : totalTermCountMap.entrySet()){
			total += entry.getValue().intValue();
		}
		return total;

	}
	
	public void appendTotalTermCount(Map<String, Double> map){
		totalTermCountMap = TermFunction.CombineCounts(totalTermCountMap, map);
	}

	public void setTotalTermCount(Map<String, Double> map){
		totalTermCountMap.clear();
		totalTermCountMap.putAll(map);
	}
	/*
	public Map<String, Double> getExpandedTermCount(Map<String, Double> map){
		Map<String,Double> expanedTermCountMap = new HashMap<String, Double>();
				
		for(Map.Entry<String, SNSUserMsgBean> msgMapEntry : msgMap.entrySet()){
			SNSUserMsgBean userMsgBean = msgMapEntry.getValue();
			Map<String,Double> termCountMap = userMsgBean.getTermCountMap();
			for(Map.Entry<String, Double> mapEntry : map.entrySet()){
				String key = mapEntry.getKey();
				//if(termCountMap.co)
			}
				
			

		}
		
		totalTermCountMap.clear();
		totalTermCountMap.putAll(map);
	}*/

	public Double getMsgAccuracy(){
		if(isMsgAccuracy){
			return msgAccuracy;
		}else{
			Double score = 0.0;
			Double accuracy = 0.0;
			for(Map.Entry<String, SNSUserMsgBean> msgMapEntry : msgMap.entrySet()){
				SNSUserMsgBean userMsgBean = msgMapEntry.getValue();
				Map<String, Double> labelCategoryMap = userMsgBean.getLabelCategoryMap();
				Map<String, Double> recomCategoryMap = userMsgBean.getRecomCategoryMap();
				
				if(labelCategoryMap.size() == 0){
					if(recomCategoryMap.size() == 0){
						score += 1.0;
					}
				}else if(labelCategoryMap.size() >= 1){
					if(recomCategoryMap.size() >= 1){
						Double scoreTmp = 0.0;
						Map<String, Double> totalMap = TermFunction.CombineCounts(recomCategoryMap, labelCategoryMap);
						
						for(Map.Entry<String, Double> recomCategoryMapEntry : recomCategoryMap.entrySet()){
							if( labelCategoryMap.containsKey(recomCategoryMapEntry.getKey())){
								scoreTmp += 1.0;
							}
						}
						scoreTmp = scoreTmp/totalMap.size();
						score += scoreTmp;
					}
				}
				
			}
			if(!msgMap.isEmpty()){
				accuracy = score / msgMap.size();
			}
			msgAccuracy = accuracy;
			isMsgAccuracy = true;
			return accuracy;
		}
		
	}

}
