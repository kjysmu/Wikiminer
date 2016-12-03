/**
 * 
 */
package label;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author kjysmu
 *
 */
public class CorpusGenerator {
	
	public static void copyFile( File from, File to ) throws IOException {
	    Files.copy( from.toPath(), to.toPath() );
	}
	
	public static void main(String[] args) throws IOException{
		String newsRootDirectory2012 = "D:\\Development\\Data\\Naver News Word Counts 2012\\Komoran-v2.4";
		String newsRootDirectory2013 = "D:\\Development\\Data\\Naver News Word Counts 2013\\Komoran-v2.4";
		
		List<File> dirCategories2012 = NewsFileReader.getListOfSubDirectories(newsRootDirectory2012);
		List<File> dirCategories2013 = NewsFileReader.getListOfSubDirectories(newsRootDirectory2013);
		
		List<String> dateList = new ArrayList<String>();
		
		for (File category : dirCategories2012) {
			String categoryStr = category.getName();
			
			
			List<File> dirSubCategories = NewsFileReader.getListOfSubDirectories(category.getAbsolutePath());
			for (File subCategory : dirSubCategories) {
				String subCategoryStr = subCategory.getName();
				
				String WCPath = subCategory.getAbsolutePath() + "\\WordCount";
				
				String WCPath2 = WCPath.replaceAll("2012", "2013");
				
				List<File> dates = NewsFileReader.getListOfFiles(WCPath);
				List<File> dates2 = NewsFileReader.getListOfFiles(WCPath2);
				
				//Set<String> set = new HashSet<String>();
				
				System.out.println( categoryStr +" " + subCategoryStr) ;
				
				dateList = new ArrayList<String>();
				
				for (File date : dates) {
					//set.add(date.getName());
					dateList.add(date.getName());
					
				}
				
				for (File date : dates2) {
					//set.add(date.getName());
					dateList.add(date.getName());
					
				}
				
				int list_size = dateList.size();
				int sample_size = 100;
				
				for(int i=1; i<=20; i++){
				
					Collections.shuffle(dateList);
					int date_ct = 0;
					
					
					for(String dateStr : dateList){
						
						if(dateStr.startsWith("2012")){
							
							copyFile(new File(WCPath + "\\" + dateStr),
									new File("D:\\Development\\Data\\News" + i + "\\Sample\\"+categoryStr+"\\"+subCategoryStr+"\\"+dateStr));
						
						}else if(dateStr.startsWith("2013")){
							
							copyFile(new File(WCPath2 + "\\" + dateStr),
									new File("D:\\Development\\Data\\News" + i + "\\Sample\\"+categoryStr+"\\"+subCategoryStr+"\\"+dateStr));
							
						}
						
						date_ct++;
						if(date_ct >= sample_size) break;
					}
					
				}
								
				dates.clear();
				dates2.clear();
				
			}
			dirSubCategories.clear();
		}
	}
}
