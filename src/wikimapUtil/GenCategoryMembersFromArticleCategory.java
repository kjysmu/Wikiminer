package wikimapUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import modules.*;

public class GenCategoryMembersFromArticleCategory{
	public static void main(String args[]) throws Exception{
		Boolean isShowProgress = true;
		Wikimap wikimap = new Wikimap();
		Map<String, String> articleCategoryMap =  wikimap.getArticleCategoryMap();
		wikimap.clearCategoryArticleMap();
		wikimap.clearCategorySubCategoryMap();
		
		HashSet<String> hs = new HashSet<String>();		
		for (Map.Entry<String, String> entry : articleCategoryMap.entrySet()) {
			String valueStr = entry.getValue();
			if(!valueStr.contains("NONE")){
				String valueArr[] = valueStr.split("\\[&]");
				for(String str : valueArr){
					hs.add(str);
				}
			}
			
		}
		Iterator<String> itr = hs.iterator();
		int total_progress = hs.size();
		int progress = 0;
		while(itr.hasNext()) {
			String curStr = itr.next();
			wikimap.generateCategoryMembers(curStr);
			progress++;
			if( isShowProgress && progress % 50 == 0 ) System.out.println(progress + "/" + total_progress + "\t" + String.format("%.4f", progress/(double)total_progress));
		}
		
		wikimap.exportAllMap();
		
		
		
		
	}
}
