package LinClustering;

import java.io.File;
import java.util.List;

import SCHype.SpectralCluster;
import parameter.Path;
import functions.FileFunction;

public class LinClusteringMain {
	
	public static void main(String args[]) throws Exception{
	
		String newsRootDirectory = "D:\\Development\\DATA\\News";	
		for(int i=1; i<=1; i++){
			
			System.out.println("Corpus : " + i);
			
			String path1 = newsRootDirectory + i +"\\WIKI-Cluster\\wcSimAll\\";
			String path2 = newsRootDirectory + i +"\\WIKI-Cluster\\clusterAll\\";

			LinLogLayout lin = new LinLogLayout();
			
			List<File> fileListWIKI = FileFunction.getListOfFiles(path1);

			for (File file : fileListWIKI) {
				
				System.out.println(file.getName());
				lin.run("2", path1 + file.getName() , path2 + file.getName() );
			
			}
			
		}
		
		System.out.println("complete!");
		
		/*
		LinLogLayout lin = new LinLogLayout();
		
		List<File> fileListWIKI = FileFunction.getListOfFiles(Path.WIKI_CLUSTER_PATH);

		for (File file : fileListWIKI) {
			
			System.out.println(file.getName());
			lin.run("2", Path.WIKI_CLUSTER_PATH + file.getName() , Path.WIKI_CLUSTER_RESULTS_PATH + file.getName() );
		
		}
		System.out.println("complete!");
		*/
		
	}

}
