package SCHype;

import modules.WikimapNew;

public class SpectralClusterMain {

	public static void main(String args[]){
		
		System.out.println("start-esa-af");
		
		String newsRootDirectory = "D:\\Development\\DATA\\News";
		
		for(int i=1; i<=20; i++){
			
			System.out.println("Corpus : " + i);
			
			SpectralCluster sc = new SpectralCluster();
			
			String filepath = newsRootDirectory + i +"\\WIKI-Cluster\\wcSim";
			sc.setHgfolder(filepath);
			sc.setIsflickr(false);
			sc.setWeighted(true);
			sc.setOutputFolder(newsRootDirectory + i +"\\WIKI-Cluster\\cluster");
			sc.doSpectralClustering();
		}
		
		
		

	}
	
	
}
