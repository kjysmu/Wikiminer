package wikimapLoad;

import java.util.Map;
import java.util.Map.Entry;

import modules.Wikimap;
import parameter.Path;
import util.FileFunction;

public class LoadWikiLog {

	public static void main(String args[]) throws Exception{
		Wikimap wikimap = new Wikimap();		
		Map<String, String> wikiLogMap = FileFunction.readMapStrStr(Path.WIKIMAP_WIKILOG_FILEPATH);		
		for (Entry<String, String> entry : wikiLogMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			if(value.equals("superCategory")){
				wikimap.generateSuperCategory(key);
			}else if(value.equals("categoryMembers")){
				wikimap.generateCategoryMembers(key);
			}else if(value.equals("article")){
				wikimap.generateArticleContents(key);
			}
		}
		wikimap.exportAllMap();
		
		System.out.println("complete");
	}
	
}
