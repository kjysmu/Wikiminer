package wikimapLoad;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import miningMinds.SNSLoader;
import miningMinds.SNSUserBean;
import miningMinds.SNSUserMsgBean;
import modules.Wikimap;
import modules.WikimapNew;
import parameter.Path;
import util.TermFunction;

public class LoadSNS {
	public static void main(String args[]) throws Exception{

		int debug_max = 5;
		int debug_ct = 0;

		System.out.println("Start");
		SNSLoader sloader = new SNSLoader("Facebook", true);

		Wikimap wikimap = new Wikimap();
		WikimapNew wikimapNew = new WikimapNew();

		System.out.println("SNSLoader Init Complete");
		
		Map<String, String> invMap = new HashMap<String, String>();

		Map<String, SNSUserBean> userMap = sloader.getUserMap();
		for (Map.Entry<String, SNSUserBean> userMapEntry : userMap.entrySet()){
			
			String userID = userMapEntry.getKey();
			SNSUserBean userBean = userMapEntry.getValue();
			
			System.out.println("userid:" + userID);

			Map<String, SNSUserMsgBean> msgMap = userBean.getMsgMap();

			for (Map.Entry<String, SNSUserMsgBean> msgMapEntry : msgMap.entrySet()){
				String msgID = msgMapEntry.getKey();
				SNSUserMsgBean userMsgBean = msgMapEntry.getValue();
				String msg = userMsgBean.getMsg();
				Map<String, Double> tcount = userMsgBean.getTermCountMap();
				
				for(Map.Entry<String, Double> entry : tcount.entrySet()){
					
					String key = entry.getKey();
					
					String invStr = "";
			    	
					Map<String, Double> articleScoreMap = new HashMap<String, Double>();
					
					if(invMap.containsKey(key)) continue;
					
			    	articleScoreMap = wikimapNew.getInvArticlesThreshold(key);
			    	
			    	if(articleScoreMap != null){
			    		
			    		invStr = TermFunction.convertMapSDToStr(articleScoreMap, "%.8f");
			    		
			    	}else{
			    		
			    	}
					
					if(invStr.trim().equals("")){
						invMap.put(key, "NONE");
					}else{
						
						invMap.put(key, invStr);

					}
					
					/*
					 * String redkey = wikimapNew.getRedirect(key);
					
					if(!redkey.equals("")){
						wikimap.putRDMap(key, redkey);
						
						Set<String> categoryList = wikimap.getCategory(redkey);
						
						for(String category : categoryList){
							
							Set<String> subcategoryList = wikimap.getSubCategory(category);	
						}				
					}*/
		
				}
			}
		}
		
		wikimap.exportMap(invMap, Path.WIKIMAP_ESA_FILEPATH);
		
		//wikimap.printAdded();
		//wikimap.exportAllMap();
		//wikimap.getWikiCount(true, kom);
		
		System.out.println("Complete");
		
		
	}
}
