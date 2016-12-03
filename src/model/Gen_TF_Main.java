/**
 * 
 */
package model;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.NewsFileReader;
import util.TweetFunction3;

/**
 * @author kjysmu
 *
 */
public class Gen_TF_Main {
	public static void main(String[] args){
		String newsRootDirectory = "D:\\Development\\Data\\News";

		for(int i=1; i<=20; i++){
			
			System.out.println( "Dataset index : " + i );
			
			Map<String, Double> categoryNounCounts;
			Map<String, Map<String, Double>> cMap;
			
			List<File> dirCategories = NewsFileReader.getListOfSubDirectories(newsRootDirectory + i + "\\Sample" );
			
			for (File category : dirCategories) {
				
				List<File> dirSubCategories = NewsFileReader.getListOfSubDirectories(category.getAbsolutePath());
				
				for (File subCategory : dirSubCategories) {
					
					System.out.print(".");
					categoryNounCounts = new HashMap<String, Double>();
					
					
					
					List<File> dates = NewsFileReader.getListOfFiles(subCategory.getAbsolutePath());
					
					for (File date : dates) {
						
						categoryNounCounts = TweetFunction3.CombineNormCounts(categoryNounCounts, TweetFunction3.readWordCountFile(date.getAbsolutePath()));

					}

					
					
					// categoryNounCounts = TweetFunction3.readWordCountFileANSI(newsRootDirectory + i + "\\TF\\"+ category.getName()+" "+subCategory.getName() +".txt");
					
					
					File dir = new File(newsRootDirectory + i + "\\TF");
					if(!dir.exists()) dir.mkdir();
					
					File dir2 = new File(newsRootDirectory + i + "\\IDF");
					if(!dir2.exists()) dir2.mkdir();
					
					TweetFunction3.SaveNormWordCount(categoryNounCounts, newsRootDirectory + i + "\\TF\\"+ category.getName()+" "+subCategory.getName() +".txt");
					

				}
				
			}
			System.out.println();

		}
		
	}
}
