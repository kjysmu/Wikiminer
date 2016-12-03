/**
 * 
 */
package modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import parameter.*;
import util.DoubleValueComparator;
import util.KomoranAnalyzer;
import util.TermFunction;

/**
 * @author kjysmu
 *
 */
public class Wikimap {

	Map<String, String> articleCategoryMap; // Article-Category Map
	Map<String, String> redirectMap; // Term-Redirect Map
	Map<String, String> homonymMap; // Term-Homonym Map
	//Map<String, String> ctmap; // Category-superCategory Map
	
	Map<String, String> categorySuperCategoryMap; // Category-superCategory Map
	Map<String, String> categorySubCategoryMap; // Category-superCategory Map
	Map<String, String> categoryArticleMap; // Category-superCategory Map
	
	Map<String, String> articleRawdataMap; // Term-RawArticle Map
	Map<String, String> articleTermCountsMap; // Article-TermCount Map
	Map<String, String> articleKeywordsMap; // Article-KeyCount Map
	
	Map<String, String> invertArticleMap; // Article-KeyCount Map

	
	
	HashSet<String> hslog;
	//KomoranAnalyzer kom;
	
	WikimapNew wikimapNew;

	String wikiStr = "http://ko.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=";
	String wikiStr_CategoryMember = "https://ko.wikipedia.org/w/api.php?action=query&format=xml&list=categorymembers&cmlimit=500&cmtitle=";

	String wikiStr_English = "http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=";
	String wikiStr_CategoryMember_English = "https://en.wikipedia.org/w/api.php?action=query&format=xml&list=categorymembers&cmlimit=500&cmtitle=";

	// https://ko.wikipedia.org/w/api.php?action=query&format=xml&imlimit=10&generator=categorymembers&gcmtitle=%EB%B6%84%EB%A5%98:%EC%86%8C%ED%96%89%EC%84%B1&gcmnamespace=0&gcmlimit=500
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	Document document;
	Boolean isOfflineMode;
	
	int added_subCat = 0;
	int added_artCat = 0;

	public Wikimap() throws Exception{
		init(true); // Default
	}
	public Wikimap(Boolean isLoadMap) throws Exception{
		init(isLoadMap);
	}
	public void init(Boolean isLoadMap) throws Exception{
		//kom = new KomoranAnalyzer();
		
		isOfflineMode = Exp.isWikimapOfflineMode;
		
		hslog = new HashSet<String>();

		wikimapNew = new WikimapNew();
		
		articleCategoryMap = new HashMap<String, String>();
		redirectMap = new HashMap<String, String>();
		homonymMap = new HashMap<String, String>();
		
		//ctmap = new HashMap<String, String>();
		
		categorySuperCategoryMap = new HashMap<String, String>();; 
		categorySubCategoryMap = new HashMap<String, String>();; 
		categoryArticleMap = new HashMap<String, String>();; 
		
		articleRawdataMap = new HashMap<String, String>();
		articleTermCountsMap = new HashMap<String, String>();
		articleKeywordsMap = new HashMap<String, String>();
		
		invertArticleMap = new HashMap<String, String>();
		
		
		if(!Exp.isWebService){
			if(isLoadMap){
				homonymMap.putAll(loadMap(Path.WIKIMAP_HOMONYM_FILEPATH));
				redirectMap.putAll(loadMap(Path.WIKIMAP_REDIRECT_FILEPATH));
				articleCategoryMap.putAll(loadMap(Path.WIKIMAP_ARTICLE_CATEGORY_FILEPATH));
				//ctmap.putAll(LoadMap(TermCategoryMapPath));
				
				categorySuperCategoryMap.putAll(loadMap(Path.WIKIMAP_CATEGORY_SUPERCATEGORY_FILEPATH));
				categorySubCategoryMap.putAll(loadMap(Path.WIKIMAP_CATEGORY_SUBCATEGORY_FILEPATH));
				categoryArticleMap.putAll(loadMap(Path.WIKIMAP_CATEGORY_ARTICLE_FILEPATH));
				
				articleRawdataMap.putAll(loadMap(Path.WIKIMAP_ARTICLE_RAWDATA_FILEPATH));
				articleTermCountsMap.putAll(loadMap(Path.WIKIMAP_ARTICLE_TERMCOUNTS_FILEPATH));
				articleKeywordsMap.putAll(loadMap(Path.WIKIMAP_ARTICLE_KEYWORDS_FILEPATH));
				
				invertArticleMap.putAll(loadMap(Path.WIKIMAP_ESA_FILEPATH));
			}
		}
		
		
		factory = DocumentBuilderFactory.newInstance();
		
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		
		builder = factory.newDocumentBuilder();

	}
	public Map<String, String> loadMap(String path) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		File f = new File(path);
		if(f.exists()){
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String data = "";
			while ((data = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(data, "\t");
				if(st.countTokens() == 2){
					String tk1 = st.nextToken();
					String tk2 = st.nextToken();
					map.put( termNorm(tk1), tk2);
				}
			}
			br.close();
			fr.close();
		}
		return map;
	}

	public void exportAllMap() throws Exception{
		exportMap(articleCategoryMap, Path.WIKIMAP_ARTICLE_CATEGORY_FILEPATH);
		exportMap(redirectMap, Path.WIKIMAP_REDIRECT_FILEPATH);
		exportMap(homonymMap, Path.WIKIMAP_HOMONYM_FILEPATH);
		//ExportMap(ctmap, TermCategoryMapPath);
		
		exportMap(categorySuperCategoryMap, Path.WIKIMAP_CATEGORY_SUPERCATEGORY_FILEPATH);
		exportMap(categorySubCategoryMap, Path.WIKIMAP_CATEGORY_SUBCATEGORY_FILEPATH);
		exportMap(categoryArticleMap, Path.WIKIMAP_CATEGORY_ARTICLE_FILEPATH);
		
		exportMap(articleRawdataMap, Path.WIKIMAP_ARTICLE_RAWDATA_FILEPATH);
		exportMap(articleTermCountsMap, Path.WIKIMAP_ARTICLE_TERMCOUNTS_FILEPATH);
		exportMap(articleKeywordsMap, Path.WIKIMAP_ARTICLE_KEYWORDS_FILEPATH);

		exportMap(invertArticleMap, Path.WIKIMAP_ESA_FILEPATH);
		
		if(!hslog.isEmpty()) exportSet(hslog, Path.WIKIMAP_WIKILOG_FILEPATH);

	}

	public void exportMap(Map<String, String> map, String path) throws Exception{
		FileWriter fw = new FileWriter(path);
		BufferedWriter bw = new BufferedWriter(fw);
		for(Map.Entry<String, String> entry : map.entrySet() ){
			bw.write(entry.getKey() + "\t" + entry.getValue());
			bw.newLine();
		}
		bw.close();
		fw.close();
	}
	public void exportSet(HashSet<String> hs, String path) throws Exception{
		FileWriter fw = new FileWriter(path);
		BufferedWriter bw = new BufferedWriter(fw);
		Iterator<String> itr = hs.iterator();
		while(itr.hasNext()) {
			String curStr = itr.next();
			bw.write(curStr);
			bw.newLine();
		}
		bw.close();
		fw.close();
	}

	public String termNorm(String articleTerm){
		if(articleTerm.contains("#")) articleTerm = articleTerm.substring(0, articleTerm.indexOf("#")).trim();
		if(articleTerm.contains("|")) articleTerm = articleTerm.substring(0, articleTerm.indexOf("|")).trim();
		return articleTerm;
	}
	
	
	public String getRedirect(String articleTerm){
		String rd_str = "";
		if(!isOfflineMode) generateArticleContents(articleTerm);	
		if( redirectMap.containsKey(articleTerm)){
			rd_str = redirectMap.get(articleTerm);
		}else{
			rd_str = null;
		}
		return rd_str;
	}
	
	public Set<String> getCategory(String articleTerm) throws Exception{
		Set<String> categoryList = new HashSet<String>();
		
		String keyStr = articleTerm;
		
		
		
		if(!isOfflineMode) generateArticleContents(articleTerm);	
		
		if( redirectMap.containsKey(articleTerm) ) keyStr = redirectMap.get(keyStr);
		
		if( articleCategoryMap.containsKey(keyStr)){
			String valueStr = articleCategoryMap.get(keyStr);
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				if( !str.contains("NONE") ) categoryList.add(str);
			}
			
			
			return categoryList;

		}else{
			
			categoryList = wikimapNew.getCategory(articleTerm, true);
			
			String categoryStr = "";
			
			int setSize = categoryList.size();
			int ct_set = 0;
			for(String str : categoryList){
				categoryStr += str;
				ct_set ++;
				if(ct_set < setSize){
					categoryStr += "[&]";
				}
			}
			if(categoryStr.equals("")){
				articleCategoryMap.put(articleTerm, "NONE");
					
			}else{
				articleCategoryMap.put(articleTerm, categoryStr);	
			}	
			
			return categoryList;
		}
		
	}

	
	
	public Set<String> getSubCategory(String categoryTerm) throws Exception{
		Set<String> subCategoryList = new HashSet<String>();
		
		if(!isOfflineMode) generateCategoryMembers(categoryTerm);
		
		if( categorySubCategoryMap.containsKey(categoryTerm)){
			String valueStr = categorySubCategoryMap.get(categoryTerm);
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				if(!str.equals("NONE")) subCategoryList.add(str);
			}
			
			return subCategoryList;

			
		}else{

			subCategoryList = wikimapNew.getSubCategory(categoryTerm);
			String subCategoryStr = "";
			
			int setSize = subCategoryList.size();
			int ct_set = 0;
			for(String str : subCategoryList){
				subCategoryStr += str;
				ct_set ++;
				if(ct_set < setSize){
					subCategoryStr += "[&]";
				}
			}
			
			if(subCategoryStr.equals("")){
				categorySubCategoryMap.put(categoryTerm, "NONE");
					
			}else{
				categorySubCategoryMap.put(categoryTerm, subCategoryStr);
				
			}
			return subCategoryList;	
			
		}
		
	}
	
	public List<String> getSuperCategory(String categoryTerm){
		List<String> superCategoryList = new ArrayList<String>();
		if(!isOfflineMode) generateSuperCategory(categoryTerm);	
		if( categorySuperCategoryMap.containsKey(categoryTerm)){
			String valueStr = categorySuperCategoryMap.get(categoryTerm);
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				if(!str.equals("NONE")) superCategoryList.add(str);
			}
		}
		return superCategoryList;
	}
	
	public Set<String> getCategoryArticle(String categoryTerm) throws Exception{
		Set<String> categoryArticleList = new HashSet<String>();
		if(!isOfflineMode) generateCategoryMembers(categoryTerm);	
		if( categoryArticleMap.containsKey(categoryTerm)){
			String valueStr = categoryArticleMap.get(categoryTerm);
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				if(!str.equals("NONE")) categoryArticleList.add(str);
			}
			
			return categoryArticleList;

		}else{
			
			categoryArticleList = wikimapNew.getCategoryArticle(categoryTerm);
			String categoryArticleStr = "";
			
			int setSize = categoryArticleList.size();
			int ct_set = 0;
			for(String str : categoryArticleList){
				categoryArticleStr += str;
				ct_set ++;
				if(ct_set < setSize){
					categoryArticleStr += "[&]";
				}
			}
			
			if(categoryArticleStr.equals("")){
				categoryArticleMap.put(categoryTerm, "NONE");
					
			}else{
				categoryArticleMap.put(categoryTerm, categoryArticleStr);
				
			}
			return categoryArticleList;	
			
			
		}
		
		
		
	}
	
	public Set<String> getHomonym(String articleTerm){
		Set<String> homonymList = new HashSet<String>();
		if(!isOfflineMode) generateArticleContents(articleTerm);	
		if( homonymMap.containsKey(articleTerm)){
			String valueStr = homonymMap.get(articleTerm);
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				if(!str.equals("NONE")) homonymList.add(str);
			}
		}
		return homonymList;
	}
	
	public Boolean isHomonym(String articleTerm){
		Boolean b = false;
		if(!isOfflineMode) generateArticleContents(articleTerm);
		if(homonymMap.containsKey(articleTerm)) b = true;
		else b = false;
		return b;
	}
	public Boolean isRedirect(String articleTerm){
		Boolean b = false;
		if(!isOfflineMode) generateArticleContents(articleTerm);
		if(redirectMap.containsKey(articleTerm)) b = true;
		else b = false;
		return b;
	}
	public Boolean hasSubcategory(String categoryTerm){
		Boolean b = false;
		if(!isOfflineMode) generateCategoryMembers(categoryTerm);
		if(categorySubCategoryMap.containsKey(categoryTerm)) b = true;
		else b = false;
		return b;
	}
	public Boolean hasSupercategory(String categoryTerm){
		Boolean b = false;
		if(!isOfflineMode) generateSuperCategory(categoryTerm);
		if(categorySuperCategoryMap.containsKey(categoryTerm)) b = true;
		else b = false;
		return b;
	}
	public Boolean hasCategory(String articleTerm){
		Boolean b = false;
		if(!isOfflineMode) generateArticleContents(articleTerm);
		if(articleCategoryMap.containsKey(articleTerm)){
			if( !articleCategoryMap.get(articleTerm).contains("NONE")){
				b=true;
			}
		}
		return b;
	}
	
	
	public Map<String, Double> getInvArticle( String articleTerm ) throws Exception{
		
		Map<String, Double> invMap = new HashMap<String, Double>();
		if(invertArticleMap.containsKey(articleTerm)){
			
			if( !invertArticleMap.get(articleTerm).equals("NONE") ){
				invMap = convertTermCountMap(invertArticleMap.get(articleTerm));	
			}
			
			return invMap;

		}else{
			
			invMap = wikimapNew.getInvArticlesThreshold(articleTerm);
			
			if(invMap != null){
				
				invertArticleMap.put(articleTerm, TermFunction.convertMapSDToStr(invMap, "%.8f"));
					
			}else{
				
				invertArticleMap.put(articleTerm, "NONE");
				
			}
			
			return invMap;

		}
		
		
		
	}

	
	
	public Map<String, Double> getArticleTermCounts(String articleTerm){
		Map<String, Double> termCounts = new HashMap<String, Double>();
		if(articleTermCountsMap.containsKey(articleTerm)) termCounts = convertTermCountMap(articleTermCountsMap.get(articleTerm));
		return termCounts;
	}
	
	public Map<String, Double> getArticleKeywords(String articleTerm){
		Map<String, Double> keywords = new HashMap<String, Double>();
		if(articleKeywordsMap.containsKey(articleTerm)) keywords = convertTermCountMap(articleKeywordsMap.get(articleTerm));
		return keywords;
	}
	
	// Add category values in CTMAP into CTMAP 
	public void expandSuperCategory(Boolean isShowProgress){
		HashSet<String> hs = new HashSet<String>();	
		for (Map.Entry<String, String> entry : categorySuperCategoryMap.entrySet()) {
			String valueStr = entry.getValue();
			if( valueStr.equals("NONE") ){
				continue;
			}
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				hs.add(str);
			}
		}
		Iterator<String> itr = hs.iterator();
		int total_progress = hs.size();
		int progress = 0;
		while(itr.hasNext()) {
			String curStr = itr.next();
			generateSuperCategory(curStr);
			progress++;
			if( isShowProgress && progress % 50 == 0 ) System.out.println(progress + "/" + total_progress + "\t" + String.format("%.4f", progress/(double)total_progress));
		}
	}
	public void expandSubCategory(Boolean isShowProgress){
		HashSet<String> hs = new HashSet<String>();	
		for (Map.Entry<String, String> entry : categorySubCategoryMap.entrySet()) {
			String valueStr = entry.getValue();
			if( valueStr.equals("NONE") ){
				continue;
			}
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				hs.add(str);
			}
		}
		Iterator<String> itr = hs.iterator();
		int total_progress = hs.size();
		int progress = 0;
		while(itr.hasNext()) {
			String curStr = itr.next();
			generateCategoryMembers(curStr);
			progress++;
			if( isShowProgress && progress % 50 == 0 ) System.out.println(progress + "/" + total_progress + "\t" + String.format("%.4f", progress/(double)total_progress));
		}
	}
	
	// Add CategoryTerm into CTMap
	public void generateSuperCategory(String categoryTerm){
		generateSuperCategory(categoryTerm , 1);
	}

	public void generateSuperCategory(String categoryTerm, int categoryDepth){
		try{
			String superCategoryStr = "";
			String categoryNormTerm = termNorm(categoryTerm);
			if(!categorySuperCategoryMap.containsKey(categoryNormTerm)){
				document = builder.parse(wikiStr + "분류:" + categoryNormTerm.replaceAll(" ", "_"));
				NodeList nl = document.getElementsByTagName("rev");
				if ( nl.getLength() >= 1 ){
					
					String wikiText = nl.item(0).getTextContent();
					String pattern = "\\[\\[(.*?)\\]\\]";
					Pattern r = Pattern.compile(pattern);
					Matcher m = r.matcher(wikiText);
					
					while (m.find()) {
						String mst = m.group();
						if(mst.contains("[분류:")){
							mst = mst.replaceAll("분류:","").replaceAll("]", "").replaceAll("\\[", "").trim();
							mst = termNorm ( mst );
							
							if(!superCategoryStr.equals("")){
								superCategoryStr += "[&]";
							}
							superCategoryStr += mst;
						}								
					}	
				}
				if(superCategoryStr.equals("")){
					categorySuperCategoryMap.put(categoryNormTerm, "NONE");
				}
				else {
					categorySuperCategoryMap.put(categoryNormTerm, superCategoryStr);
					
					if( categoryDepth >= 2 ){
						categoryDepth = categoryDepth - 1;
						String superCategoryArr[] = superCategoryStr.split("\\[&]");
						for(String superCategoryTerm : superCategoryArr){
							generateCategoryMembers( superCategoryTerm , categoryDepth );
						}
					}
					
				}
			}
		}catch(Exception e){
			System.out.println("Exception occured for the term : " + categoryTerm);
			hslog.add(categoryTerm +"\t"+"superCategory");
			e.printStackTrace();
			try{
				if(!Exp.isWebService) {
					exportMap(categorySuperCategoryMap, Path.WIKIMAP_CATEGORY_SUPERCATEGORY_FILEPATH);

				}

				
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}
	public void generateCategoryMembers(String categoryTerm){
		generateCategoryMembers(categoryTerm, 1);
	}
	public void generateCategoryMembers(String categoryTerm, int categoryDepth){
		try{
			if(!categorySubCategoryMap.containsKey(categoryTerm) && !categoryArticleMap.containsKey(categoryTerm)){
				String subCategoryStr = ""; // To store wcmap category
				String categoryArticleStr = ""; // To store wcmap category

				DocumentBuilderFactory factory;
				DocumentBuilder builder;
				Document document;
				
				factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
				
				String categoryNormTerm = termNorm(categoryTerm);
				String parseStr = "";
				if(Exp.isEnglish){
					parseStr = wikiStr_CategoryMember_English + "Category:" + categoryNormTerm.replaceAll(" ", "_");					

				}else{
					String urlkey = URLEncoder.encode(categoryNormTerm, "UTF-8");
					String categorykey = URLEncoder.encode("분류", "UTF-8");
					parseStr = wikiStr_CategoryMember + categorykey + ":" + urlkey.replaceAll(" ", "_");					
				}

				document = builder.parse(parseStr);
				
				//String urlkey = URLEncoder.encode(articleNormTerm, "UTF-8");
				//String parseStr = wikiStr + urlkey.replaceAll(" ", "_");
				//document = builder.parse(parseStr);

				NodeList nl = document.getElementsByTagName("cm");

				if ( nl.getLength() >= 1 ){
					for(int i = 0; i< nl.getLength(); i++){
						NamedNodeMap attrs = nl.item(i).getAttributes();
						for(int j = 0 ; j<attrs.getLength() ; j++) {
							Attr attribute = (Attr)attrs.item(j);
							if(attribute.getName().equals("title")){
								String member = attribute.getValue();
								if(member.contains("분류:") || member.contains("Category:")){
									if(!subCategoryStr.equals("")){
										subCategoryStr += "[&]";
									}
									if(Exp.isEnglish){
										subCategoryStr += termNorm( member.replaceAll("Category:", "") );
									}else{
										subCategoryStr += termNorm( member.replaceAll("분류:", "") );
									}
									
								}else{
									if(!categoryArticleStr.equals("")){
										categoryArticleStr += "[&]";
									}
									categoryArticleStr += termNorm( member );																		
								}
							}
						}
					}
				}
				
				if(subCategoryStr.equals("")){
					categorySubCategoryMap.put(categoryNormTerm, "NONE");
				}
				else {
					//System.out.println("subStr : " + subCategoryStr);
					categorySubCategoryMap.put(categoryNormTerm, subCategoryStr);
					
					if( categoryDepth >= 2 ){
						categoryDepth = categoryDepth - 1;
						String subCategoryArr[] = subCategoryStr.split("\\[&]");
						for(String subCategoryTerm : subCategoryArr){
							generateCategoryMembers( subCategoryTerm , categoryDepth );
						}
					}
					
				}
				if(categoryArticleStr.equals("")){
					categoryArticleMap.put(categoryNormTerm, "NONE");
				}
				else {
					categoryArticleMap.put(categoryNormTerm, categoryArticleStr);
				}
				
			}
			
		}catch(Exception e){
			System.out.println("Exception occured for the term : " + categoryTerm);
			hslog.add(categoryTerm +"\t"+"categoryMembers");
			e.printStackTrace();
			try{
				if(!Exp.isWebService) {
				
					exportMap(categorySubCategoryMap, Path.WIKIMAP_CATEGORY_SUBCATEGORY_FILEPATH);
					exportMap(categoryArticleMap , Path.WIKIMAP_CATEGORY_ARTICLE_FILEPATH);
					
				}
				
				
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
		
	}

	// Add Term into Wikimap
	public void generateArticleContents(String articleTerm){
		try{
			String term_tmp = ""; // To store Original or Redirected term for wcmap
			String ct_tmp = ""; // To store wcmap category

			String articleNormTerm= termNorm(articleTerm);

			if(redirectMap.containsKey(articleNormTerm)){
				String rstr = redirectMap.get(articleNormTerm);
				articleNormTerm = termNorm(rstr);
			}
			
			if(articleCategoryMap.containsKey(articleNormTerm) || homonymMap.containsKey(articleNormTerm)){
			}else{
				Boolean isHM = false;
				term_tmp = articleNormTerm;
				String parseStr = "";
				String urlkey = "";
				if(Exp.isEnglish == true){
					parseStr = wikiStr_English + articleNormTerm.replaceAll(" ", "_");
				}else{
					urlkey = URLEncoder.encode(articleNormTerm, "utf-8");
					parseStr = wikiStr + urlkey.replaceAll(" ", "_");

				}

				document = builder.parse(parseStr);

				NodeList nl = document.getElementsByTagName("rev");
				if ( nl.getLength() >= 1 ){
					String wikiText = nl.item(0).getTextContent();
					
					//System.out.println(wikiText);
					
					String pattern = "\\[\\[(.*?)\\]\\]";
					Pattern r = Pattern.compile(pattern);
					Matcher m = r.matcher(wikiText);
					Boolean isDup = false;
					if(wikiText.contains("REDIRECT") || wikiText.contains("넘겨주기")){
						m.find();
						String redirectTerm = m.group().replaceAll("]", "").replaceAll("\\[", "").trim();
						if( !redirectMap.containsKey(articleNormTerm) ){
							redirectMap.put(articleNormTerm, redirectTerm);
						}
						if( !articleCategoryMap.containsKey(articleNormTerm)){
							articleCategoryMap.put(term_tmp, "NONE_RD");
						}
						
						String redirectNormTerm = termNorm( redirectTerm );
						term_tmp = redirectNormTerm;

						if(articleCategoryMap.containsKey(redirectNormTerm) || homonymMap.containsKey(redirectNormTerm)){
							if( homonymMap.containsKey(redirectNormTerm) ) isHM = true;
							isDup = true;
						}
						else{
							
							
							if(Exp.isEnglish){
								parseStr = wikiStr_English + redirectNormTerm.replaceAll(" ", "_");

							}else{
								urlkey = URLEncoder.encode(redirectNormTerm, "utf-8");
								parseStr = wikiStr + urlkey.replaceAll(" ", "_");		
							}
							
							
							document = builder.parse(parseStr);
							
							
							
							nl = document.getElementsByTagName("rev");
							if ( nl.getLength() >= 1 ){
								wikiText = nl.item(0).getTextContent();
								m = r.matcher(wikiText);
							}
						}
					}
					if(isDup){}
					else if(wikiText.contains("{{동음이의}}") || wikiText.contains("{{disambiguation}}")  ){
						String hmstr = "";
						int hmct = 0;
						while (m.find()) {
							String mst = m.group();
							String keyStr = m.group().substring(2, mst.length()-2).trim();
							if(hmct != 0) hmstr += "[&]";
							hmstr += keyStr;
							//addMap(keyStr);
							hmct ++;
						}
						if( !homonymMap.containsKey(term_tmp) ){
							homonymMap.put(term_tmp, hmstr);
						}
						isHM = true;
					}else{
						while (m.find()) {
							String mst = m.group();
							
							if(Exp.isEnglish){
								if(mst.contains("[Category:")){
									mst = mst.replaceAll("Category:","").replaceAll("]", "").replaceAll("\\[", "").trim();
									mst = termNorm ( mst );
									if(!ct_tmp.equals("")){
										ct_tmp += "[&]";
									}
									ct_tmp += mst;
								}	
							}else{
								if(mst.contains("[분류:")){
									mst = mst.replaceAll("분류:","").replaceAll("]", "").replaceAll("\\[", "").trim();
									mst = termNorm ( mst );
									if(!ct_tmp.equals("")){
										ct_tmp += "[&]";
									}
									ct_tmp += mst;
								}	
							}
						
						}
					}
				}

				if(!articleCategoryMap.containsKey(term_tmp)){
					if(ct_tmp.equals("")){
						if(isHM){
							articleCategoryMap.put(term_tmp, "NONE_HM");
						}else{
							articleCategoryMap.put(term_tmp, "NONE");
						}
					}
					else {
						articleCategoryMap.put(term_tmp, ct_tmp);
					}
				}
			}
		}catch(Exception e){
			System.out.println("Exception occured for the term : " + articleTerm);
			hslog.add(articleTerm +"\t"+"article");

			e.printStackTrace();
			try{
				
				if(!Exp.isWebService) exportAllMap();
				
			}catch(Exception e2){
				e2.printStackTrace();
			}

		}

	}
	public Map<String, Double> convertTermCountMap(String str){
		
		Map<String,Double> termCounts = new HashMap<String,Double>();
		
		if(!str.equals("NONE")){
			String valueArr[] = str.split("\\[&]");
			for(String value : valueArr){
				String valueArr2[] = value.split("\\[#]");
				
				String term = valueArr2[0];
				String count = valueArr2[1];
				
				termCounts.put(term, Double.parseDouble(count));
			}
		}
	
		return termCounts;
	}
	
	public String convertTermCountStr(Map<String, Double> termCounts){
		
		String valueStr = "";
		for (Map.Entry<String, Double> termCount : termCounts.entrySet()) {
			String termCountKey = termCount.getKey();
			int termCountValue = termCount.getValue().intValue();
			
			String termCountStr = termCountKey + "[#]" + termCountValue;

			if(!valueStr.equals("")) valueStr += "[&]";
			valueStr += termCountStr;
		}
		
		return valueStr;
	}

	public void getWikiCount(Boolean isShowProgress, KomoranAnalyzer kom){
		
		int total_progress = articleCategoryMap.size();
		int progress = 0;
		for (Map.Entry<String, String> entry : articleCategoryMap.entrySet()) {
			
			progress++;
			if(progress % 100 == 0 && isShowProgress) System.out.println(progress + "/" + total_progress + "\t" + String.format("%.4f", progress/(double)total_progress));
				
			String keyStr = entry.getKey();
			if(!articleRawdataMap.containsKey(keyStr)){
				try{
					
					String parseStr = "";
					String urlkey = "";
					if(Exp.isEnglish == true){
						parseStr = wikiStr_English + keyStr.replaceAll(" ", "_");
					}else{
						urlkey = URLEncoder.encode(keyStr, "utf-8");
						parseStr = wikiStr + urlkey.replaceAll(" ", "_");

					}

					document = builder.parse(parseStr);
					
					//document = builder.parse(wikiStr + keyStr);
					
					
					NodeList nl = document.getElementsByTagName("rev");
					
					if ( nl.getLength() >= 1 ){
						String wikiText = nl.item(0).getTextContent();
						String wikiTextNorm = wikiText.replaceAll("[\\s]", " "); // Replace tab to space
						
						if(!articleRawdataMap.containsKey(keyStr)){
							if(wikiTextNorm.trim().equals("")) articleRawdataMap.put(keyStr, "NONE");
							else articleRawdataMap.put(keyStr, wikiTextNorm);
						}
						
						if(!articleTermCountsMap.containsKey(keyStr)){
							
							Map<String,Double> termCounts = TermFunction.getTermCounts(wikiText, kom);
							
							DoubleValueComparator bvc = new DoubleValueComparator(termCounts);
							TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
							tMap.putAll(termCounts);
							String atmap_valueStr = convertTermCountStr(tMap);
							
							if(atmap_valueStr.trim().equals("")) articleTermCountsMap.put(keyStr, "NONE");
							else articleTermCountsMap.put(keyStr, atmap_valueStr);
						}else{
							
						}
						
						if(!articleKeywordsMap.containsKey(keyStr)){
							String pattern = "\\[\\[(.*?)\\]\\]";
							Pattern r = Pattern.compile(pattern);
							Matcher m = r.matcher(wikiText);
							
							Map<String,Double> keywordCounts = new HashMap<String,Double>();
							
							while (m.find()) {
								String mst = m.group();
								if(!mst.contains("[분류:")){
									mst = mst.replaceAll("]", "").replaceAll("\\[", "").trim();
									mst = termNorm ( mst );
									if(keywordCounts.containsKey(mst)){
										keywordCounts.put(mst, keywordCounts.get(mst) + 1.0);
									}else{
										keywordCounts.put(mst, 1.0);
									}
								}
							}
							
							DoubleValueComparator bvc = new DoubleValueComparator(keywordCounts);
							TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
							tMap.putAll(keywordCounts);
							String akmap_valueStr = convertTermCountStr(tMap);
							
							if(akmap_valueStr.trim().equals("")) articleKeywordsMap.put(keyStr, "NONE");
							else articleKeywordsMap.put(keyStr, akmap_valueStr);	
						}else{
							
						}
					}
				}
				catch(Exception e){
					System.out.println("Exception occured for the term : " + keyStr);
					hslog.add(keyStr + "\t" + "article");
					e.printStackTrace();
					
					try{
						
						if(!Exp.isWebService) exportAllMap();
						
					}catch(Exception e2){
						e2.printStackTrace();
					}
					break;
				}
			}else{
			
			}
			
		}
	}
	
	
	public Map<String,String> getRedirectMap(){
		return redirectMap;
	}
	public Map<String,String> getHomonymMap(){
		return homonymMap;
	}
	public Map<String,String> getArticleCategoryMap(){
		return articleCategoryMap;
	}
	public Map<String,String> getCategorySubCategoryMap(){
		return categorySubCategoryMap;
	}
	public Map<String,String> getCategorySuperCategoryMap(){
		return categorySuperCategoryMap;
	}
	public Map<String,String> getCategoryArticleMap(){
		return categoryArticleMap;
	}
	public Map<String,String> getArticleKeywordsMap(){
		return articleKeywordsMap;
	}
	public Map<String,String> getArticleRawdataMap(){
		return articleRawdataMap;
	}
	public Map<String,String> getArticleTermCountsMap(){
		return articleTermCountsMap;
	}
	
////
	
	public void clearRedirectMap(){
		redirectMap.clear();
	}
	public void clearHomonymMap(){
		homonymMap.clear();
	}
	public void clearArticleCategoryMap(){
		articleCategoryMap.clear();
	}
	public void clearCategorySubCategoryMap(){
		categorySubCategoryMap.clear();
	}
	public void clearCategorySuperCategoryMap(){
		categorySuperCategoryMap.clear();
	}
	public void clearCategoryArticleMap(){
		categoryArticleMap.clear();
	}
	public void clearArticleKeywordsMap(){
		articleKeywordsMap.clear();
	}
	public void clearArticleRawdataMap(){
		articleRawdataMap.clear();
	}
	public void clearArticleTermCountsMap(){
		articleTermCountsMap.clear();
	}
	
	// ---------------------------------------Rarely used---------------------------------------

	
	public void putWCMap(String key, String value){
		articleCategoryMap.put(key, value);
	}
	public void putRDMap(String key, String value){
		redirectMap.put(key, value);
	}
	
	public void printAdded(){
		System.out.println("new artCat : "+added_artCat);
		System.out.println("new subCat : "+added_artCat);
	}
	
	
	// ---------------------------------------Deprecated---------------------------------------
	
	//Check whether key is contained in XX Map or not
	/*
	public Boolean isRDMap(String key){
		Boolean b = false;
		if(redirectMap.containsKey(key)) b = true;
		else b = false;
		
		return b;
	}
	public Boolean isHMMap(String key){
		Boolean b = false;
		if(homonymMap.containsKey(key)) b = true;
		else b = false;
		
		return b;
	}
	public Boolean isWCMap(String key){
		Boolean b = false;
		if(articleCategoryMap.containsKey(key)) b = true;
		else b = false;
		
		return b;
	}

	public Boolean isWCMapNone(String key){
		Boolean b = true;
		if(articleCategoryMap.containsKey(key)){
			if( (!articleCategoryMap.get(key).equals("NONE")) && (!articleCategoryMap.get(key).equals("NONE_HM")) ){
				b=false;
			}
		}
		return b;
	}

	public HashSet<String> getHMValue(String key){
		HashSet<String> hs = new HashSet<String>();
		if( homonymMap.containsKey(key)){
			String valueStr = homonymMap.get(key);
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				hs.add(str);
			}
		}
		return hs;
	}
	public HashSet<String> getWCValue(String key){
		HashSet<String> hs = new HashSet<String>();
		if( articleCategoryMap.containsKey(key)){
			String valueStr = articleCategoryMap.get(key);
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				hs.add(str);
			}
		}
		return hs;
	}
	public String getRDValue(String key){
		String rd_str = "";
		if( redirectMap.containsKey(key)){
			rd_str = redirectMap.get(key);
		}else{
			rd_str = null;
		}
		return rd_str;
	}
	
		// Add category values in WCMAP into CTMAP 
	public void addWCValueToCTMap(Boolean isShowProgress){
		HashSet<String> hs = new HashSet<String>();		
		for (Map.Entry<String, String> entry : articleCategoryMap.entrySet()) {
			String valueStr = entry.getValue();
			
			if( valueStr.equals("NONE") || valueStr.equals("NONE_HM") ){
				continue;
			}

			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				hs.add(str);
			}
		}
		Iterator<String> itr = hs.iterator();
		int total_progress = hs.size();
		int progress = 0;
		while(itr.hasNext()) {
			String curStr = itr.next();
			generateSuperCategory(curStr);
			
			progress++;
			if( isShowProgress && progress % 50 == 0 ) System.out.println(progress + "/" + total_progress + "\t" + String.format("%.4f", progress/(double)total_progress));
		}
	}
	
		
	// Add homonym terms in HMMAP into WIKIMAP
	public void addHMValueToMap(Boolean isShowProgress){
		HashSet<String> hs = new HashSet<String>();		
		for (Map.Entry<String, String> entry : homonymMap.entrySet()) {
			String valueStr = entry.getValue();
			String valueArr[] = valueStr.split("\\[&]");
			for(String str : valueArr){
				hs.add(str);
			}			
		}
		Iterator<String> itr = hs.iterator();
		int total_progress = hs.size();
		int progress = 0;
		while(itr.hasNext()) {
			String curStr = itr.next();
			generateArticleContents(curStr);
			progress++;
			if( isShowProgress && progress % 50 == 0 ) System.out.println(progress + "/" + total_progress + "\t" + String.format("%.4f", progress/(double)total_progress));
		}
	}
	
	*/
	
	
	//parseStr = new String(parseStr.getBytes("utf-8"), "euc-kr");
	/*
	InputStream is = null;
	InputStreamReader isr = null;
	
	is = new URL(parseStr).openStream();
	isr = new InputStreamReader(is, "utf-8");
	
	StringBuffer sb = new StringBuffer(); int c;
	while ((c = isr.read()) != -1) {sb.append((char) c);}
	isr.close(); is.close();
	
	document = builder.parse(new InputSource(new StringReader(sb.toString())));
	*/
	
}
