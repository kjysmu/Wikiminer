package modules;

import java.util.Set;

public class WikimapTest {
	public static void main(String args[]) throws Exception{
		
		WikimapNew wikimap = new WikimapNew();
		
		Set<String> set = wikimap.getCategory("클라우드 서비스", true);
		for(String category: set){
			//System.out.println(category);
		}
		
		Set<String> set2 = wikimap.getSubCategory("야구");
		for(String category: set2){
			//System.out.println(category);
		}
		
		Set<String> set3 = wikimap.getCategoryArticle("야구");
		for(String category: set3){
			System.out.println(category);
		}
		

		
		
		
		
		
		
		
		
	}

}
