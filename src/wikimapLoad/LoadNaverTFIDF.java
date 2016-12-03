package wikimapLoad;

import java.util.Map;
import java.util.Map.Entry;

import modules.Wikimap;
import parameter.Path;
import util.FileFunction;
import util.KomoranAnalyzer;


public class LoadNaverTFIDF {
	public static void main(String args[]) throws Exception{
		Wikimap wikimap = new Wikimap();
		KomoranAnalyzer kom = new KomoranAnalyzer();
		Map<String, Map<String, Double>> categoryMap = FileFunction.readMapStr_StrDou(Path.CATEGORY_MODEL_TFIDF_PATH);
		for (Entry<String, Map<String, Double>> entry : categoryMap.entrySet()) {
			String key = entry.getKey();
			System.out.println("Loading "+key);
			Map<String, Double> value = entry.getValue();
			for (Entry<String, Double> entry2 : value.entrySet()) {
				String key2 = entry2.getKey();
				wikimap.generateArticleContents(key2);
			}
		}
		wikimap.exportAllMap();
		
		//wikimap.getWikiCount(true, kom);
		System.out.println("Complete");
		
	}

}
