package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

/**
 * @author Jonghyun Han
 *
 */

public class NewsFileReader {
	public static List<File> getListOfSubDirectories(String path) {
		File dir = new File(path);
		File[] listDir = dir.listFiles();
		List<File> subDirectories = new ArrayList<File>();
		
		for (File file : listDir) {
			if (file.isDirectory()) {
				subDirectories.add(file);
			}
		}
		
		return subDirectories;
	}

	public static List<File> getListOfFiles(String path) {
		File dir = new File(path);
		File[] listFiles = dir.listFiles();
		List<File> files = new ArrayList<File>();
		
		for (File file : listFiles) {
			if (file.isFile()) {
				files.add(file);
			}
		}
		
		return files;
	}
	
	public static String getNewsContent(String filename) {
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fr);
			
			String fileContent = "", data = "";
			
			while ((data = reader.readLine()) != null) {
				if (data.startsWith("//") || data.trim().isEmpty())
					continue;
				
				fileContent += data + "\r\n";
			}			
			reader.close();
			fr.close();
			
			return fileContent;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static ArrayList<String> getEachNewsContent(String filename) {
		
		try {
			ArrayList<String> newsList = new ArrayList<String>();
			
			FileReader fr = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fr);
			
			String fileContent = "", data = "";
			
			int ct = 0;
			boolean isStart = false;
			
			while ((data = reader.readLine()) != null) {
				
				if(data.isEmpty()){
					ct++;
				}else{
					ct = 0;
					isStart= true;
					fileContent += data + "\r\n";
				}
				
				if(ct >= 2){
					if(isStart){
						newsList.add(fileContent.replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s.]"," "));
						fileContent = "";
						ct = 0;
						isStart = false;
					}else{
						
					}
				}
				
			}			
			reader.close();
			fr.close();

			return newsList;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}
