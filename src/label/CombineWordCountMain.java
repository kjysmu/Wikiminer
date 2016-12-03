/**
 * 
 */
package label;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kjysmu
 *
 */
public class CombineWordCountMain {
	public static void main(String[] args){
		String newsRootDirectory = "D:\\Development\\Data\\Naver News Word Counts 2012\\Komoran-v2.4";
		List<File> dirCategories = NewsFileReader.getListOfSubDirectories(newsRootDirectory);
		
		Map<String, Double> categoryNounCounts;
		Map<String, Map<String, Double>> cMap;

		for (File category : dirCategories) {
			List<File> dirSubCategories = NewsFileReader.getListOfSubDirectories(category.getAbsolutePath());
			for (File subCategory : dirSubCategories) {
				//if(!subCategory.getName().equals("IT¿œπ›")) continue;
				categoryNounCounts = new HashMap<String, Double>();
				cMap = new HashMap<String, Map<String, Double>>();
				
				System.out.println(category.getName() + "\t" + subCategory.getName());
				
				List<File> dates = NewsFileReader.getListOfFiles(subCategory.getAbsolutePath());
				for (File date : dates) {
					// TweetFunction.readWordCountFile(date.getAbsolutePath());
					
					//System.out.println(date.getName().substring(0, 8));
					//System.exit(0);
					String date_str = date.getName().substring(0, 8);
					
					if( cMap.containsKey( date_str ) ){
						cMap.put(date_str, TweetFunction3.CombineCounts( cMap.get(date_str) , TweetFunction3.readWordCountFile(date.getAbsolutePath())));
					}else{
						cMap.put(date_str, TweetFunction3.readWordCountFile(date.getAbsolutePath()));
					}
					
					//categoryNounCounts = TweetFunction3.CombineCounts(categoryNounCounts, TweetFunction3.readWordCountFile(date.getAbsolutePath()));
					
					// System.out.println (date.getAbsolutePath());
				}
				
				for(Map.Entry<String, Map<String, Double>> entry : cMap.entrySet()){
					String date_str = entry.getKey();
					Map<String, Double> wCount = entry.getValue();
					
					File dir = new File(newsRootDirectory +"\\"+ category.getName()+"\\"+subCategory.getName() +"\\WordCount");
					dir.mkdir();
					
					TweetFunction3.SaveWordCount(wCount, newsRootDirectory +"\\"+ category.getName()+"\\"+subCategory.getName() +"\\WordCount\\"+date_str+".txt");

				}
				
				//TweetFunction3.SaveWordCount(categoryNounCounts, newsRootDirectory +"\\"+ category.getName()+" "+subCategory.getName() +".txt");
				categoryNounCounts.clear();
				dates.clear();
			}
			dirSubCategories.clear();
		}
	}
}
