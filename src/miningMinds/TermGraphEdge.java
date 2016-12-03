package miningMinds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TermGraphEdge {
	
	Double weight;
	String id;
	
	public TermGraphEdge(String term1, String term2){
		init(1.0, term1, term2);
	}
	public TermGraphEdge(Double d, String term1, String term2){
		init(d, term1, term2);		
	}
	public void init(Double d, String term1, String term2){
		this.weight = d;
		List<String> list = new ArrayList<String>();
		list.add(term1);
		list.add(term2);
		Collections.sort(list);
		String str_id = "";
		for(String str:list){
			str_id += str;
			str_id += " ";
		}
		id = str_id.trim().replaceAll(" ","#");
	}
	public String toString(){
		return "E:"+id;
	}
	
	public Double getWeigh(){
		return weight;
	}
	
	public void addWeight(){
		addWeight(1.0);
	}
	public void addWeight(Double d){
		weight += d;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TermGraphEdge other = (TermGraphEdge) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
