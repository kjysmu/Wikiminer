/*
package functions;

import java.util.HashMap;
import java.util.List;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class KomoranAnalyzerTmp {

	public Komoran komoran;
	
	public KomoranAnalyzerTmp() {
		InitKomoranAnalyzer();
	}
	
	private void InitKomoranAnalyzer() {
		komoran = new Komoran("D:\\workspace\\library\\komoran-2.1.2\\models-full");
	}
	
	public HashMap<String, Double> GetNounCounts(String document, String mode) {

		HashMap<String,Double> map = new HashMap<String, Double>();
		
		List<List<Pair<String,String>>> result = komoran.analyze(document);

		for (List<Pair<String, String>> eojeolResult : result) {
			for (Pair<String, String> wordMorph : eojeolResult) {
				String noun = wordMorph.getFirst();
				String tag = wordMorph.getSecond();
				
				if( mode.equals("1") ){
					if( tag.equals("NNG") || tag.equals("NNP") ){
						if (!map.containsKey(noun))	map.put(noun, 1.0);
						else map.put(noun, map.get(noun)+1.0);
					}
				}else if( mode.equals("2") ){
					if( tag.equals("NNP") ){
						if (!map.containsKey(noun)) map.put(noun, 1.0);
						else map.put(noun, map.get(noun)+1.0);	
					}
				}else if(mode.equals("3")){
					if( tag.equals("NNP") ){
						if (!map.containsKey(noun))
							map.put(noun, 2.0);
						else
							map.put(noun, map.get(noun)+2.0);
					}else if( tag.equals("NNG") ){
						if (!map.containsKey(noun))
							map.put(noun, 1.0);
						else
							map.put(noun, map.get(noun)+1.0);
					}
				}				
			}
		}
		
		return map;
	}

}
*/
