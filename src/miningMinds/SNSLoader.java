/**
 * 
 */
package miningMinds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import parameter.*;
import util.DocumentFunction;
import util.DoubleValueComparator;
import util.FileFunction;
import util.KomoranAnalyzer;
import util.TermFunction;

// import edu.uci.ics.jung.graph.Graph;
// import edu.uci.ics.jung.graph.UndirectedSparseGraph;


/**
 * @author kjysmu
 *
 */
public class SNSLoader {
	
	String path_sns;
	String path_tc;
	Double averageSimilarity;
	Double averageMsgAccuracy;

	Map<String, SNSUserBean> userMap;
	Map<String, String> msgTermCountsStrMap;
	
	String SNS;
	Boolean isTermCountData;

	public SNSLoader(String SNStype, Boolean isTermCountData){
		SNS = SNStype;
		this.isTermCountData = isTermCountData;
		
		if(SNStype.equals("Facebook")){
			
			
			path_sns = Path.FACEBOOK_LABEL_RESULT_PATH;
			path_tc = Path.FACEBOOK_LABEL_RESULT_TERMCOUNT_PATH;
			
			
		}else if(SNStype.equals("Twitter")){
			path_sns = Path.TWITTER_LABEL_RESULT_PATH;
			path_tc = Path.TWITTER_LABEL_RESULT_TERMCOUNT_PATH;
		}
		init();
	}
	
	public void init(){
		msgTermCountsStrMap = new HashMap<String, String>();
		userMap = new HashMap<String, SNSUserBean>();
		SNSUserBean userBean = new SNSUserBean();
		KomoranAnalyzer kom = null;
		
		try{
			if(isTermCountData){
			
				BufferedReader br_TermCounts = new BufferedReader(
						   new InputStreamReader(
				                      new FileInputStream(path_tc), "UTF-8"));
				
				String lineTC = "";
				Boolean isFstLine = true;
				
				while (true) {
					lineTC = br_TermCounts.readLine();
					if (lineTC == null) break;
					
					if(isFstLine){
						lineTC = FileFunction.removeUTF8BOM(lineTC);
						isFstLine = false;
					}
					
					
					StringTokenizer stringTokenizer = new StringTokenizer(lineTC, "\t");
					if (stringTokenizer.countTokens() == 2) {
						String token1 = stringTokenizer.nextToken();
						String token2 = stringTokenizer.nextToken();
						msgTermCountsStrMap.put(token1, token2);
					}
				}
				br_TermCounts.close();
				
				
				//fr_TermCounts.close();
			}else{
				kom = new KomoranAnalyzer();
			}

			
			BufferedReader br = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(path_sns), "UTF-8"));
			
			String line = "";
			Boolean isFst = true;
			Boolean isUID = false;
			String userID;
			
			Boolean isFstLine = true;
			
			// Graph<TermGraphNode,TermGraphEdge> graph = new UndirectedSparseGraph<TermGraphNode, TermGraphEdge>();

			while(true){
				line = br.readLine();
				if( line == null ){
					if(isUID){
						// userBean.setTermGraph(graph);
						
						userMap.put(userBean.getUserid(), userBean);
						// Add previous data 
					}
					break;
				}
				
				if(isFstLine){
					line = FileFunction.removeUTF8BOM(line);
					isFstLine = false;
				}
				
				if( line.startsWith("// UserID")  ){
					isUID = true;
					if( isFst ){
						isFst = false;
					}else{
						// userBean.setTermGraph(graph);
						
						userMap.put(userBean.getUserid(), userBean);
						
					} // Add previous data
					userBean = new SNSUserBean();
					// graph = new UndirectedSparseGraph<TermGraphNode, TermGraphEdge>();
					
					userID = line.substring(11);
					userBean.setUserid(userID);
					
				}else{
					StringTokenizer st = new StringTokenizer(line,"\t");
					
					int stcount = st.countTokens();
					if(stcount == 7){
						
						Map<String, Double> msgTermCounts = new HashMap<String,Double>();
						
						SNSUserMsgBean userMsgBean = new SNSUserMsgBean();
						
						String tokenid = st.nextToken();
						String token = st.nextToken();
						String msg = token.replaceAll("[\\s]", " ");
	
						String t_time = st.nextToken();
						String t_category1 = st.nextToken().replaceAll("/", "").replaceAll(" ", "");
						String t_category2 = st.nextToken().replaceAll("/", "").replaceAll(" ", "");
						String t_category3 = st.nextToken().replaceAll("/", "").replaceAll(" ", "");
						String t_category4 = st.nextToken().replaceAll("/", "").replaceAll(" ", "");
	
						userMsgBean.setMsgid(tokenid);
						userMsgBean.setTime(t_time);
						userMsgBean.setMsg(msg);
						
						if(!t_category1.equals("NONE") && !t_category2.equals("NONE")){
							userMsgBean.addLabelCategoryMap(t_category1 + " " + t_category2);
						}
						if(!t_category3.equals("NONE") && !t_category4.equals("NONE")){
							userMsgBean.addLabelCategoryMap(t_category3+ " " + t_category4);
						}
						
						if(isTermCountData){
							msgTermCounts = TermFunction.convertTermCountMap(msgTermCountsStrMap.get(tokenid));

						}else{
							msgTermCounts = TermFunction.getTermCounts(msg, kom);
							msgTermCountsStrMap.put(tokenid, TermFunction.convertTermCountStr(msgTermCounts));
						}
						
						
						
						userBean.appendTotalTermCount(msgTermCounts);						
						userMsgBean.setTermCountMap(msgTermCounts);
						userBean.addMsgMap(tokenid, userMsgBean);
			
					}//end of stcount IF

				}

			}
			
			br.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void exportTermCountData() throws Exception{
		
		
		BufferedWriter bw = new BufferedWriter(
				   new OutputStreamWriter(
		                      new FileOutputStream(path_tc), "UTF-8"));
		
		for(Map.Entry<String, String> msgTermCountsStrMapEntry : msgTermCountsStrMap.entrySet()){
			bw.write(msgTermCountsStrMapEntry.getKey() + "\t" + msgTermCountsStrMapEntry.getValue());
			bw.newLine();
		}
		
		bw.close();
		
	}
	
	public Map<String, SNSUserBean> getUserMap(){
		return userMap;
	}
	
	public void setUserMap(Map<String, SNSUserBean> map){
		
		userMap.clear();
		userMap.putAll(map);
		
	}
	
	
	
	
	public void generateResultHTML(int corpusIndex) throws Exception{
		
		Map<String, String> comparisonResultMap = new HashMap<String, String>();
		
		if(Exp.isComparisonResults){
			FileReader fr_comp = new FileReader(new File("D:\\Development\\result\\News"+corpusIndex+"\\" + Exp.comparisonPath1 + "\\" + Exp.comparisonPath2 + "\\recomCategoryResults.txt"));
			BufferedReader br_comp = new BufferedReader(fr_comp);
			
			String line_comp = "";
			
			
			while (true) {
				line_comp = br_comp.readLine();
				if (line_comp == null) break;
				StringTokenizer stringTokenizer = new StringTokenizer(line_comp, "\t");
				if (stringTokenizer.countTokens() == 2) {
					String key = stringTokenizer.nextToken();
					String value = stringTokenizer.nextToken();
					comparisonResultMap.put(key, value);
				}
			}
			br_comp.close();
			fr_comp.close();
		}
		
		for (Entry<String, SNSUserBean> userMapEntry : userMap.entrySet()) {
			String userid = userMapEntry.getKey();
			SNSUserBean userBean = userMapEntry.getValue();
			
			File dir = new File("D:\\Development\\result\\News"+corpusIndex+"\\", Exp.resultPathAppend);
			if(!dir.exists()) dir.mkdir();
			File dir2 = new File(dir.getCanonicalPath(), Exp.resultPathTestAppend);
			if(!dir2.exists()) dir2.mkdir();
			
			File dir_page = new File(dir2.getCanonicalPath(), "resultPage" );
			if(!dir_page.exists()) dir_page.mkdir();
			
			FileWriter fw_res = new FileWriter( dir_page.getCanonicalPath()+"\\" +userBean.getUserid()+".html" );
			BufferedWriter writer_res = new BufferedWriter(fw_res);
			
			writer_res.write("<html>\n");
			writer_res.write("<head>\n");
			writer_res.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
			writer_res.write("<title>Recommendation Result</title>\n");
			writer_res.write("</head>\n");
			writer_res.write("<body>\n");
			
			Map<String, SNSUserMsgBean> msgMap = userBean.getMsgMap();
			
			for (Entry<String, SNSUserMsgBean> msgMapEntry : msgMap.entrySet()) {
				String msgMapKey = msgMapEntry.getKey();
				SNSUserMsgBean msgMapValue = msgMapEntry.getValue();
				Map<String, Double> msgTermCounts = msgMapValue.getTermCountMap();
				Map<String, Double> labelCategoryMap = msgMapValue.getLabelCategoryMap();
				Map<String, Double> recomCategoryMap = msgMapValue.getRecomCategoryMap();
				Map<String, Double> recomTotalCategoryMap = msgMapValue.getRecomTotalCategoryMap();
				
				Map<String, Double> msgWikiCategoryMap = msgMapValue.getWikiCategoryMap();
				Map<String, Double> msgWikiSubCategoryMap = msgMapValue.getWikiSubCategoryMap();
				Map<String, Double> msgWikiTotalCategoryMap = msgMapValue.getWikiTotalCategoryMap();
				
				Map<String, Double> msgWikiTotalArticleMap = msgMapValue.getWikiTotalArticleMap();
				
				List<SNSTermBean> termList = msgMapValue.getTermList();
				
				writer_res.write("<table width=\"1000\" height=\"100\" border=\"1\">\n");
				writer_res.write("<tr>\n");
				writer_res.write("<td height=\"10\" colspan=\"3\">" + "Msg ID : "+ msgMapValue.getMsgid() + "</td>\n");
				writer_res.write("</tr>\n");
				
				
				/*-------------------------------------------------------------------------------------------------------*/
				// RAW MSG
				writer_res.write("<tr>\n");
				writer_res.write("<td height=\"60\" colspan=\"3\">" + msgMapValue.getMsg() + "</td>\n");
				writer_res.write("</tr>\n");
				
				/*-------------------------------------------------------------------------------------------------------*/
				// Result Analysis Detail 
				if(Exp.approach.contains("wiki") && Exp.isResultAnalysis){
					for(SNSTermBean termBean : termList){
						writer_res.write("<tr>\n");
						writer_res.write("<td width=\"300\"><small>\n");
						writer_res.write("<b>term</b> : "+termBean.getTerm() + "</small></td><td colspan=\"2\"><small>");
						if(termBean.isRedirect()) writer_res.write("<b>rediretedTerm</b> : "+termBean.getRedirectTerm() +" ");
						writer_res.write("<b>tf</b> : "+ String.format("%.2f", termBean.getTF()) + " ");
						writer_res.write("<b>idf</b> : "+ String.format("%.2f", termBean.getIDF()) + " ");
						if(termBean.isHomonym()){
							List<SNSTermBean> homonymTermList = termBean.getHomonymTermList();
							writer_res.write("<b>homonymTerms</b> : ");
							String hmlist_str = "";
							for(SNSTermBean homonymTermBean : homonymTermList){
								if(!hmlist_str.equals("")) hmlist_str +=" , ";;
								hmlist_str += homonymTermBean.getTerm();
							}
							writer_res.write( hmlist_str + " \n");
						}
						if(termBean.isWikiCategory()){
							Map<String, Double> termWikiCategoryMap = termBean.getWikiCategoryMap();
							writer_res.write("<b>wikiCategory</b> : ");
							String wclist_str = "";
							for (Entry<String, Double> entry : termWikiCategoryMap.entrySet()) {
								String key = entry.getKey();
								Double value = entry.getValue();
								if(!wclist_str.equals("")) wclist_str +=" , ";;
								wclist_str += key +"(" + String.format("%.2f", value) + ")";
							}
							writer_res.write( wclist_str + "\n");
						}
						if(termBean.isWikiArticle()){
							Map<String, Double> termWikiArticleMap = termBean.getWikiArticleMap();
							DoubleValueComparator dvc = new DoubleValueComparator(termWikiArticleMap);
							TreeMap<String, Double> tmap = new TreeMap<String, Double>(dvc);
							tmap.putAll(termWikiArticleMap);
							
							writer_res.write("<b>wikiArticle(top5)</b> : ");
							String walist_str = "";
							int wa_ct = 0;
							for (Entry<String, Double> entry : tmap.entrySet()) {
								wa_ct++;
								if(wa_ct > 5) break;
								
								String key = entry.getKey();
								Double value = entry.getValue();
								if(!walist_str.equals("")) walist_str +=" , ";;
								walist_str += key +"(" + String.format("%.2f", value) + ")";
							}
							writer_res.write( walist_str + "\n");
						}
						
						writer_res.write("</small></td>\n");
						writer_res.write("</tr>\n");
					}
				}else{
					/*-------------------------------------------------------------------------------------------------------*/
					// TermCounts Results
					writer_res.write("<tr>\n");
					writer_res.write("<td colspan=\"3\">");
					
					for(Map.Entry<String, Double> msgTermCount : msgTermCounts.entrySet()){
						writer_res.write(msgTermCount.getKey() + "(" + String.format("%.2f",msgTermCount.getValue()) +")"+ " ");
					}
					
					writer_res.write("</td>\n");
					writer_res.write("</tr>\n");
				}
				
	
				/*-------------------------------------------------------------------------------------------------------*/
				// WikiCategory (MSG) Results
				if(Exp.approach.contains("wiki")){
					
					writer_res.write("<tr>\n");
					writer_res.write("<td colspan=\"3\"><small>");
					DoubleValueComparator doubleValueComparator2 = new DoubleValueComparator(msgWikiTotalArticleMap);
					TreeMap<String, Double> treeMap2 = new TreeMap<String, Double>(doubleValueComparator2);
					treeMap2.putAll(msgWikiTotalArticleMap);
					writer_res.write("<b>TotalWikiArticle List(Top10)</b> : ");
					
					int twa_ct = 0;
					for(Map.Entry<String, Double> entry : treeMap2.entrySet()){
						twa_ct++;
						if(twa_ct > 10) break;

						if(entry.getValue() <= 0.00 ) writer_res.write("<strike>");
						writer_res.write(entry.getKey() + "(" + String.format("%.2f",entry.getValue()) +")"+ " ");
						if(entry.getValue() <= 0.00 ) writer_res.write("</strike>");
					}
					writer_res.write("</small></td>\n");
					writer_res.write("</tr>\n");
					
					writer_res.write("<tr>\n");
					writer_res.write("<td colspan=\"3\"><small>");
					DoubleValueComparator doubleValueComparator = new DoubleValueComparator(msgWikiCategoryMap);
					TreeMap<String, Double> treeMap = new TreeMap<String, Double>(doubleValueComparator);
					treeMap.putAll(msgWikiCategoryMap);
					writer_res.write("<b>WikiCategory List</b> : ");
					for(Map.Entry<String, Double> entry : treeMap.entrySet()){
						if(entry.getValue() <= 0.00 ) writer_res.write("<strike>");
						writer_res.write(entry.getKey() + "(" + String.format("%.2f",entry.getValue()) +")"+ " ");
						if(entry.getValue() <= 0.00 ) writer_res.write("</strike>");
					}
					writer_res.write("</small></td>\n");
					writer_res.write("</tr>\n");
					
					if(Exp.subCategoryWeight_User != 0.0){
						
						writer_res.write("<tr>\n");
						writer_res.write("<td colspan=\"3\"><small>");
						doubleValueComparator = new DoubleValueComparator(msgWikiSubCategoryMap);
						treeMap = new TreeMap<String, Double>(doubleValueComparator);
						treeMap.putAll(msgWikiSubCategoryMap);
						writer_res.write("<b>WikiSubCategory List</b> : ");
						for(Map.Entry<String, Double> entry : treeMap.entrySet()){
							if(entry.getValue() <= 0.00 ) writer_res.write("<strike>");
							writer_res.write(entry.getKey() + "(" + String.format("%.2f",entry.getValue()) +")"+ " ");
							if(entry.getValue() <= 0.00 ) writer_res.write("</strike>");
						}
						writer_res.write("</small></td>\n");
						writer_res.write("</tr>\n");
						
						writer_res.write("<tr>\n");
						writer_res.write("<td colspan=\"3\"><small>");
						doubleValueComparator = new DoubleValueComparator(msgWikiTotalCategoryMap);
						treeMap = new TreeMap<String, Double>(doubleValueComparator);
						treeMap.putAll(msgWikiTotalCategoryMap);
						writer_res.write("<b>WikiTotalCategory List</b> : ");
						for(Map.Entry<String, Double> entry : treeMap.entrySet()){
							if(entry.getValue() <= 0.00 ) writer_res.write("<strike>");
							writer_res.write(entry.getKey() + "(" + String.format("%.2f",entry.getValue()) +")"+ " ");
							if(entry.getValue() <= 0.00 ) writer_res.write("</strike>");
						}
						writer_res.write("</small></td>\n");
						writer_res.write("</tr>\n");
						
					}
				
				}
				
				/*-------------------------------------------------------------------------------------------------------*/
				// User-Labeled Category
				writer_res.write("<tr>\n");
				writer_res.write("<td width=\"200\" height=\"60\">");
				for(Map.Entry<String, Double> labelCategoryMapEntry : labelCategoryMap.entrySet()){
					writer_res.write("<p>"+ labelCategoryMapEntry.getKey() +"</p>");
				}
				writer_res.write("</td>\n");
				
				// Recommended Category
				writer_res.write("<td width=\"450\">");
				DoubleValueComparator bvc = new DoubleValueComparator(recomTotalCategoryMap);
				TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
				tMap.putAll(recomTotalCategoryMap);
				
				int ct = 0;
				for(Map.Entry<String, Double> TCMapEntry : tMap.entrySet()){
					if( ct >= Exp.categoryDebugNum ) break;
					if(recomCategoryMap.containsKey(TCMapEntry.getKey())){
						if( labelCategoryMap.containsKey(TCMapEntry.getKey()) ){
							writer_res.write("<font color=\"#0066FF\"><strong>\n");
							writer_res.write("<p>"+ TCMapEntry.getKey() + " ("+ String.format("%.6f",TCMapEntry.getValue())+  ")"+"</p>\n");
							writer_res.write("</strong></font>\n");
						}else{
							writer_res.write("<font color=\"#CC0000\"><strong>\n");
							writer_res.write("<p>"+ TCMapEntry.getKey() + " ("+ String.format("%.6f",TCMapEntry.getValue())+  ")"+"</p>\n");
							writer_res.write("</strong></font>\n");
						}
					}else{
						if( labelCategoryMap.containsKey(TCMapEntry.getKey()) ){
							writer_res.write("<font color=\"#0066FF\"><strike>\n");
							writer_res.write("<p>"+ TCMapEntry.getKey() + " ("+ String.format("%.6f",TCMapEntry.getValue())+  ")"+"</p>\n");
							writer_res.write("</strike></font>\n");
						}else{
							writer_res.write("<font color=\"#CC0000\"><strike>\n");
							writer_res.write("<p>"+ TCMapEntry.getKey() + " ("+ String.format("%.6f",TCMapEntry.getValue())+  ")"+"</p>\n");
							writer_res.write("</strike></font>\n");
						}
					}
					ct++;
				}
				writer_res.write("</td>\n");
				
				if(Exp.isComparisonResults){
					
					writer_res.write("<td width=\"450\" bgcolor=\"#BBBBBB\">");
					String compCategoryResultStr = comparisonResultMap.get(msgMapValue.getMsgid());
					Map<String, Double> compCategoryResultMap = TermFunction.convertTermCountMap(compCategoryResultStr);

					bvc = new DoubleValueComparator(compCategoryResultMap);
					tMap = new TreeMap<String, Double>(bvc);
					tMap.putAll(compCategoryResultMap);
					
					for(Map.Entry<String, Double> TCMapEntry : tMap.entrySet()){
						if( labelCategoryMap.containsKey(TCMapEntry.getKey()) ){
							writer_res.write("<font color=\"#0066FF\"><strike>\n");
							writer_res.write("<p>"+ TCMapEntry.getKey() + " ("+ String.format("%.6f",TCMapEntry.getValue())+  ")"+"</p>\n");
							writer_res.write("</strike></font>\n");
						}else{
							writer_res.write("<font color=\"#CC0000\"><strike>\n");
							writer_res.write("<p>"+ TCMapEntry.getKey() + " ("+ String.format("%.6f",TCMapEntry.getValue())+  ")"+"</p>\n");
							writer_res.write("</strike></font>\n");
						}
					}
					writer_res.write("</td>\n");
				}
				
				writer_res.write("</tr>\n");
				writer_res.write("</table>\n");
				writer_res.write("<br />\n");
				
			}
			
			writer_res.write("<tr>\n");
			writer_res.write("<td height=\"60\" colspan=\"2\">" + "Similarity:"+ String.format("%.4f", userBean.getSimilarity()) + "</td>\n");
			writer_res.write("</tr>\n");
			writer_res.write("</table>\n");
			writer_res.write("</body>\n");
			writer_res.write("</html>\n");
			
			writer_res.close();
			fw_res.close();
			
		}
	}
	
	public void generateResultFile(int corpusIndex) throws Exception{
		
		File dir = new File("D:\\Development\\result\\News"+corpusIndex+"\\", Exp.resultPathAppend);
		
		
		
		if(!dir.exists()) dir.mkdir();
		File dir2 = new File(dir.getCanonicalPath(), Exp.resultPathTestAppend);
		if(!dir2.exists()) dir2.mkdir();
		
		FileWriter fw_res = new FileWriter(dir2.getCanonicalPath() +"\\similarity.txt");
		BufferedWriter writer_res = new BufferedWriter(fw_res);
		
		
		double totalSim = 0.0;
		
		
		Map<Integer, Double> topKmap = new HashMap<Integer, Double>();
		Map<Integer, Double> topKmapNorm = new HashMap<Integer, Double>();
	
		Map<Integer, Double> topKmap2 = new HashMap<Integer, Double>();
		Map<Integer, Double> topKmapNorm2 = new HashMap<Integer, Double>();
	
		
		for (Entry<String, SNSUserBean> userMapEntry : userMap.entrySet()) {
			String userid = userMapEntry.getKey();
			SNSUserBean userBean = userMapEntry.getValue();
			double sim = userBean.getSimilarity();
			
			FileWriter fw_res_recom = new FileWriter(dir2.getCanonicalPath() +"\\recom_results_"+userid+".txt");
			BufferedWriter writer_res_recom = new BufferedWriter(fw_res_recom);
			
			FileWriter fw_res_label = new FileWriter(dir2.getCanonicalPath() +"\\label_results_"+userid+".txt");
			BufferedWriter writer_res_label = new BufferedWriter(fw_res_label);
			
			totalSim+=sim;
			writer_res.write(String.format("%.4f", sim));
			writer_res.newLine();
			
			Map<String, Double> recomTotalCategoryMap = userBean.getRecomTotalCategoryMap();
			Map<String, Double> labelTotalCategoryMap = userBean.getLabelTotalCategoryMap();
			
			recomTotalCategoryMap = TermFunction.getNorm(recomTotalCategoryMap);
			labelTotalCategoryMap = TermFunction.getNorm(labelTotalCategoryMap);

			DoubleValueComparator bvc = new DoubleValueComparator(recomTotalCategoryMap);
			TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
			tMap.putAll(recomTotalCategoryMap);
			
			for (Entry<String, Double> recomEntry : tMap.entrySet()) {
				
				writer_res_recom.write(recomEntry.getKey() + "\t" + String.format("%.8f", recomEntry.getValue() ));
				writer_res_recom.newLine();
				
			}
			
			DoubleValueComparator bvc2 = new DoubleValueComparator(labelTotalCategoryMap);
			TreeMap<String, Double> tMap2 = new TreeMap<String, Double>(bvc2);
			tMap2.putAll(labelTotalCategoryMap);
			
			int ct = 1;
			Map<String, Double> map = new HashMap<String, Double>();
			Map<String, Double> umap = new HashMap<String, Double>();

			for (Entry<String, Double> labelEntry : tMap2.entrySet()) {

				map.put(labelEntry.getKey(), labelEntry.getValue());
				
				umap.put(labelEntry.getKey(), 1.0);
				
				Map<String, Double> map2 = new HashMap<String, Double>();
				Map<String, Double> umap2 = new HashMap<String, Double>();
				
				
				int r_ct = 1;
				for (Entry<String, Double> recomEntry : tMap.entrySet()) {
					
					if( r_ct > ct ) break;
					map2.put(recomEntry.getKey(), recomEntry.getValue());
					
					umap2.put(recomEntry.getKey(), 1.0);
					
					
					r_ct ++;
				}
				
				Double cossim =getCosineSimilarity(map, map2);
				Double cossim2 =getCosineSimilarity(umap, umap2);
				
				System.out.println(ct+":"+cossim);
				System.out.println(ct+":"+cossim2);
				System.out.println();
				
				
				if(topKmap.containsKey( ct )){
					topKmap.put(ct, topKmap.get(ct) + cossim );
				}else{
					topKmap.put(ct, cossim);
				}
				
				if(topKmap2.containsKey( ct )){
					topKmap2.put(ct, topKmap2.get(ct) + cossim2 );
				}else{
					topKmap2.put(ct, cossim2);
				}

				writer_res_label.write(labelEntry.getKey() + "\t" +  String.format("%.8f", labelEntry.getValue() )   );
				writer_res_label.newLine();
				
				ct++;

			}
			
			if(ct <= 20){
				
				for(int xct= ct; xct <=20; xct++){
					
					Map<String, Double> map2 = new HashMap<String, Double>();
					Map<String, Double> umap2 = new HashMap<String, Double>();
					
					int r_ct = 1;
					for (Entry<String, Double> recomEntry : tMap.entrySet()) {
						
						if( r_ct > xct ) break;
						map2.put(recomEntry.getKey(), recomEntry.getValue());
						
						umap2.put(recomEntry.getKey(), 1.0);
						
						
						r_ct ++;
					}
					
					Double cossim =getCosineSimilarity(map, map2);
					Double cossim2 =getCosineSimilarity(umap, umap2);
					
					
					if(topKmap.containsKey( xct )){
						topKmap.put(xct, topKmap.get(xct) + cossim );
					}else{
						topKmap.put(xct, cossim);
					}
					
					if(topKmap2.containsKey( xct )){
						topKmap2.put(xct, topKmap2.get(xct) + cossim2 );
					}else{
						topKmap2.put(xct, cossim2);
					}
					
					
				}
				
				
			}
			
		
			writer_res_recom.close();
			fw_res_recom.close();
			
			writer_res_label.close();
			fw_res_label.close();
			

		}
		
		for (Entry<Integer, Double> topKEntry2 : topKmap2.entrySet()) {
			int key = topKEntry2.getKey();
			Double value = topKEntry2.getValue();
			
			topKmapNorm2.put(key, value / (double)userMap.size()  );
			
		}
		
		
		for (Entry<Integer, Double> topKEntry : topKmap.entrySet()) {
			int key = topKEntry.getKey();
			Double value = topKEntry.getValue();
			
			topKmapNorm.put(key, value / (double)userMap.size()  );
			
		}
		
		
		FileWriter fw_res_topk = new FileWriter(dir2.getCanonicalPath() +"\\topk.txt");
		BufferedWriter writer_res_topk = new BufferedWriter(fw_res_topk);
		
		FileWriter fw_res_topk2 = new FileWriter(dir2.getCanonicalPath() +"\\topk2.txt");
		BufferedWriter writer_res_topk2 = new BufferedWriter(fw_res_topk2);
		
		
		for(int i=1; i<=20; i++){
			
			Double score = topKmapNorm.get(i);
			
			writer_res_topk.write(i+"\t"+  String.format("%.8f", score ) );
			writer_res_topk.newLine();
			
			Double score2 = topKmapNorm2.get(i);
			
			writer_res_topk2.write(i+"\t"+  String.format("%.8f", score2 ) );
			writer_res_topk2.newLine();
			
		}
		
		writer_res_topk.close();
		writer_res_topk2.close();

		writer_res.newLine();
		double avrSim = totalSim / userMap.size();
		writer_res.write(String.format("%.4f", avrSim ));
		
		writer_res.close();
		fw_res.close();

		averageSimilarity = avrSim;
	}
	
	public void generateMsgAccuracyResultFile(int corpusIndex) throws Exception{
		
		File dir = new File("D:\\Development\\result\\News"+corpusIndex+"\\", Exp.resultPathAppend);
		
		
		if(!dir.exists()) dir.mkdir();
		File dir2 = new File(dir.getCanonicalPath(), Exp.resultPathTestAppend);
		if(!dir2.exists()) dir2.mkdir();
		
		FileWriter fw_res = new FileWriter(dir2.getCanonicalPath() +"\\msg_accuracy.txt");
		BufferedWriter writer_res = new BufferedWriter(fw_res);
		
		double totalAcc = 0.0;
		for (Entry<String, SNSUserBean> userMapEntry : userMap.entrySet()) {
			String userid = userMapEntry.getKey();
			SNSUserBean userBean = userMapEntry.getValue();
			double accuracy = userBean.getMsgAccuracy();
			totalAcc+=accuracy;
			writer_res.write(String.format("%.4f", accuracy));
			writer_res.newLine();	
		}
		writer_res.newLine();
		double avrAcc = totalAcc / userMap.size();
		writer_res.write(String.format("%.4f", avrAcc ));
		
		writer_res.close();
		fw_res.close();
		
		averageMsgAccuracy = avrAcc;
		
	}
	
	
	public void generateRecomCategoryResultFile(int corpusIndex) throws Exception{
		
		File dir = new File("D:\\Development\\result\\News"+corpusIndex+"\\", Exp.resultPathAppend);
		if(!dir.exists()) dir.mkdir();
		File dir2 = new File(dir.getCanonicalPath(), Exp.resultPathTestAppend);
		if(!dir2.exists()) dir2.mkdir();
		
		FileWriter fw_res = new FileWriter(dir2.getCanonicalPath() +"\\recomCategoryResults.txt");
		BufferedWriter writer_res = new BufferedWriter(fw_res);
		for (Entry<String, SNSUserBean> userMapEntry : userMap.entrySet()) {
			SNSUserBean userBean = userMapEntry.getValue();
			Map<String, SNSUserMsgBean> userMsgBean = userBean.getMsgMap();
			for (Entry<String, SNSUserMsgBean> entry : userMsgBean.entrySet()) {
				String key = entry.getKey();
				SNSUserMsgBean value = entry.getValue();
				Map<String, Double> recomCategoryMap = value.getRecomCategoryMap();
				
				
				String recomCategoryStr = TermFunction.convertMapSDToStr(recomCategoryMap, "%.6f");
				
				
				if(recomCategoryStr.trim().equals("")) recomCategoryStr = "NONE";
				writer_res.write(key + "\t" + recomCategoryStr);
				writer_res.newLine();
			}
		}		
		writer_res.close();
		fw_res.close();
		
	}
	
	public void generateParameterFile( int corpusIndex) throws Exception{
		
		File dir = new File("D:\\Development\\result\\News"+corpusIndex+"\\", Exp.resultPathAppend);
		if(!dir.exists()) dir.mkdir();
		File dir2 = new File(dir.getCanonicalPath(), Exp.resultPathTestAppend);
		if(!dir2.exists()) dir2.mkdir();
		
		FileWriter fw_res = new FileWriter(dir2.getCanonicalPath() +"\\parameter.txt");
		BufferedWriter writer_res = new BufferedWriter(fw_res);
		
		writer_res.write("SNSType : "+Exp.SNSType);
		writer_res.newLine();
		writer_res.write("approach : "+Exp.approach);
		writer_res.newLine();
		writer_res.write("msg_threshold : "+Exp.msg_threshold);
		writer_res.newLine();
		writer_res.write("msg_threshold2 : "+Exp.msg_threshold2);
		writer_res.newLine();
		writer_res.write("msg_maxcategory : "+Exp.msg_maxcategory);
		writer_res.newLine();
		writer_res.write("isWikiIDF : "+Exp.isWiki_NaverIDF);
		writer_res.newLine();
		writer_res.write("wiki_topic_path : "+Exp.wiki_topic_path);
		writer_res.newLine();
		writer_res.write("str_LDAmodel : "+Exp.str_LDAmodel);
		writer_res.newLine();
		writer_res.write("WORD_FREQUENCY_THRESHOLD : "+ String.format("%.8f", Exp.WORD_FREQUENCY_THRESHOLD) );
		writer_res.newLine();
		
		writer_res.write("a1 : "+ String.format("%.4f", 1.0 - Exp.subCategoryWeight_User) );
		writer_res.newLine();
		writer_res.write("a2 : "+ String.format("%.4f", Exp.subCategoryWeight_User) );
		writer_res.newLine();
		
		
		
		writer_res.write("b (ratio) : "+ Exp.wikisim_ratio );
		writer_res.newLine();
		
		
		
		writer_res.write("r1 : "+ String.format("%.4f", Exp.w_cficf) );
		writer_res.newLine();
		writer_res.write("r2 : "+ String.format("%.4f", Exp.w_afiaf) );
		writer_res.newLine();
		writer_res.write("r3 : "+ String.format("%.4f", Exp.w_cs) );
		writer_res.newLine();
		
		writer_res.newLine();
		writer_res.write("Version REMARK : "+ Exp.versionRemark );
		writer_res.newLine();
		
		writer_res.close();
		fw_res.close();
	}
	
	public double getAverageSimilarity(){
		return averageSimilarity;
	}
	public double getAverageMsgAccuracy(){
		return averageMsgAccuracy;
	}
	
	public double getCosineSimilarity(Map<String, Double> map1 , Map<String, Double> map2 ){
		if(map1.isEmpty() || map2.isEmpty()) return 0.0;
		else{
			Map<String, Double> freq1 = TermFunction.getNorm(map1);
			Map<String, Double> freq2 = TermFunction.getNorm(map2);
			double sim = DocumentFunction.ComputeCosineSimilarity( freq1, freq2 );
			return sim;
		}
	}


	
	
	
	
}
