package wikimapLoad;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import modules.Wikimap;
import parameter.Path;
import util.FileFunction;


public class LoadNaverWikiSubCategory {
	public static void main(String args[]) throws Exception{
		Wikimap wikimap = new Wikimap();
		Map<String, Map<String, Double>> categoryMap = FileFunction.readMapStr_StrDou(Path.CATEGORY_MODEL_WIKI_CFICF_PATH);
		for (Entry<String, Map<String, Double>> entry : categoryMap.entrySet()) {
			String key = entry.getKey();
			System.out.println("Loading "+key);
			Map<String, Double> value = entry.getValue();
			for (Entry<String, Double> entry2 : value.entrySet()) {
				String key2 = entry2.getKey();
				Set<String> subCategoryList = wikimap.getSubCategory(key2);
				for(String str : subCategoryList){
					wikimap.generateCategoryMembers(str);
				}
			}
		}
		wikimap.exportAllMap();
		System.out.println("Complete");
		
	}
}
