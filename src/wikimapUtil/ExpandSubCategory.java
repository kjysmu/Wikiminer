package wikimapUtil;

import modules.Wikimap;

public class ExpandSubCategory {
	public static void main(String args[]) throws Exception{
		
		
		Wikimap wikimap = new Wikimap();
		
		wikimap.expandSubCategory(true);
		
		wikimap.exportAllMap();
		
	}

}
