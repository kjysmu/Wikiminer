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
public class Gen_News {
	public static void main(String[] args){
		String newsRootDirectory = "D:\\Development\\Data\\News";
		
		//String newsRootDirectory = "D:\\Development\\result";

		for(int i=1; i<=20; i++){
			
			System.out.println( "Dataset index : " + i );
			
			File f = new File(newsRootDirectory + i+"\\WIKI-Cluster\\clusterSim_icf");
			
			f.mkdirs();
			
			//File f = new File(newsRootDirectory + i+"\\WIKI-CFICF-L2NORM");
			//File f2 = new File(newsRootDirectory + i+"\\WIKI-AFIAF-L2NORM");
			
			//f.mkdirs();
			//f2.mkdirs();
			/*
			File f = new File(newsRootDirectory + i+"\\WIKI-Cluster\\wcSimAll");
			File f2 = new File(newsRootDirectory + i+"\\WIKI-Cluster\\clusterSimAll");
			File f3 = new File(newsRootDirectory + i+"\\WIKI-Cluster\\clusterAll");
			
			f.mkdirs();
			f2.mkdirs();
			f3.mkdirs();
			 */
			//File f = new File(newsRootDirectory+"\\News" + i );
			//f.mkdirs();
			
			/*
			 * 
			 * List<File> fileList = NewsFileReader.getListOfFiles(newsRootDirectory + i);
			
			File f = new File(newsRootDirectory + i+"\\WIKI-Cluster\\wcSim");
			File f2 = new File(newsRootDirectory + i+"\\WIKI-Cluster\\clusterSim");
			File f3 = new File(newsRootDirectory + i+"\\WIKI-Cluster\\cluster");
			
			f.mkdirs();
			f2.mkdirs();
			f3.mkdirs();
			
			
			for(File f : fileList){
				
				f.delete();
				
			}
			*/
			
			
			/*
			File dir = new File(newsRootDirectory + i + "\\WIKI-CF");
			if(!dir.exists()) dir.mkdir();
			
			File dir2 = new File(newsRootDirectory + i + "\\WIKI-ICF");
			if(!dir2.exists()) dir2.mkdir();
			
			File dir3 = new File(newsRootDirectory + i + "\\WIKI-AF");
			if(!dir3.exists()) dir3.mkdir();
			
			File dir4 = new File(newsRootDirectory + i + "\\WIKI-IAF");
			if(!dir4.exists()) dir4.mkdir();
			*/
			
		}
		
	}
}
