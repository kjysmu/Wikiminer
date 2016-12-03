package wikimapUtil;

import modules.Wikimap;
import util.KomoranAnalyzer;

public class WikiCountMain {
	public static void main(String argsp[]) throws Exception{
		Wikimap wikimap = new Wikimap();
		KomoranAnalyzer kom = new KomoranAnalyzer();
		wikimap.getWikiCount(true, kom);
		wikimap.exportAllMap();
	}

}
