package LinClustering;

public class ClusterResultsBean {
	
	private String category;
	private Double nodeScore;
	private String cluster;
	
	public ClusterResultsBean(){
		
	}
	public void setCategory(String category){
		this.category = category;
	}
	public String getCategory(){
		return category;
	}
	public void setNodeScore(Double nodeScore){
		this.nodeScore = nodeScore;
	}
	public Double getNodeScore(){
		return nodeScore;
	}
	public void setCluster(String cluster){
		this.cluster = cluster;
	}
	public String getCluster(){
		return cluster;
	}

}
