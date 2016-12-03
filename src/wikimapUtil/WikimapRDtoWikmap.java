package wikimapUtil;

// Insert RD key to Wikimap as ( RD key TAB NONE_RD )
import java.util.Map;
import java.util.Map.Entry;

import parameter.*;
import util.FileFunction;
import modules.*;



public class WikimapRDtoWikmap {
	
	public static void main(String args[]) throws Exception{
		Wikimap wikimap = new Wikimap();		
		Map<String, String> rdmap = FileFunction.readMapStrStr(Path.WIKIMAP_REDIRECT_FILEPATH);		
		for (Entry<String, String> entry : rdmap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			wikimap.putWCMap(key, "NONE_RD");			
		}
		wikimap.exportAllMap();
		
		System.out.println("complete");
	}

}
