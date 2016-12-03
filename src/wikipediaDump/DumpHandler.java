package wikipediaDump;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.helpers.DefaultHandler;

import util.KomoranAnalyzer;
import util.TermFunction;

import org.xml.sax.Attributes;

public class DumpHandler extends DefaultHandler {

	public String currentTag = "";
	public String tagLevel = "";	
	public String previousTag = "";

	public String title = "";
	public String text = "";
	public String id = "";
	public String timestamp = "";
	public String ns = "";
	public Set<String> categorySet = new HashSet<String>();
	public Set<String> linkSet = new HashSet<String>();
	public Map<String, Double> termCount = new HashMap<String, Double>();
	public String termCountStr;

	public KomoranAnalyzer kom;
	public String type = "";

	public HashSet<String> set = new HashSet<String>();

	String indexDir = "wiki-dump-index-new";

	public IndexWriter writer;
	public IndexWriterConfig config;
	public int docCount;
	
	long start;
	long end;


	public DumpHandler() throws Exception{
		init();
	}
	public void init() throws Exception{
		currentTag = "";
		termCountStr = "";
		docCount = 0;
		kom = new KomoranAnalyzer();

		categorySet = new HashSet<String>();
		linkSet = new HashSet<String>();
		termCount = new HashMap<String, Double>();

		Directory dir = FSDirectory.open(new File(indexDir));
		Analyzer analyzer = new WhitespaceAnalyzer();
		config = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
		writer = new IndexWriter(dir, config);

	}

	public void startDocument(){
		System.out.println("start");
		start = System.currentTimeMillis();
	}
	public void endDocument(){

		end = System.currentTimeMillis();
		
		System.out.println("end");
		System.out.println("Indexing " + writer.numDocs() + " files took "
				+ (end - start) + " milliseconds");
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void startElement(String uri, String localName, String qname, Attributes attr){

		if(currentTag.equals("")){
			currentTag = qname;
			tagLevel = "<" + qname + ">";
		}
		else{
			tagLevel = tagLevel + "<" + qname + ">";
			currentTag = qname;
		}

		if(qname.equals("text")){
			text = "";
		}else if(qname.equals("title")){
			title = "";
		}else if(qname.equals("page")){
			text = "";
			title = "";
			id = "";
			timestamp = "";
			ns = "";
			categorySet = new HashSet<String>();
			linkSet = new HashSet<String>();
			termCount = new HashMap<String, Double>();
			termCountStr = "";
			type = "";
		}


	} 
	public void endElement(String uri, String localName, String qname){
		
		if(qname.equals("page")){		
			Document doc = new Document();

			doc.add(new StringField("id", id, Field.Store.YES));
			doc.add(new StringField("title", title, Field.Store.YES));
			doc.add(new StringField("timestamp", timestamp, Field.Store.YES));
			doc.add(new StringField("ns", ns, Field.Store.YES));
			doc.add(new StringField("type", type, Field.Store.YES));
			doc.add(new TextField("term_content", termCountStr, Field.Store.YES));
			
			for(String category : categorySet){
				doc.add(new StringField("category", category, Field.Store.YES));
			}
			
			for(String link : linkSet){
				doc.add(new StringField("link", link, Field.Store.YES));
			}
			
			for(Map.Entry<String, Double> entry : termCount.entrySet()){
				String term = entry.getKey();
				String termScore = entry.getKey() + "\t" + entry.getValue();
				
				doc.add(new StringField("term_score", termScore, Field.Store.YES));
				doc.add(new StringField("term", term, Field.Store.YES));
			}
			
			try {
				writer.addDocument(doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			docCount ++;			
			if(docCount % 10000 == 0 ) System.out.println(docCount);

		}else if(qname.equals("text")){ // Text Processing
			termCount = TermFunction.getTermCounts(text, kom);
			termCountStr = "";
			for(Map.Entry<String, Double> entry : termCount.entrySet()){
				for(int i = 0; i < entry.getValue(); i++){
					termCountStr += entry.getKey() + " ";
				}
			}
			termCountStr = termCountStr.trim();
			String pattern = "\\[\\[(.*?)\\]\\]";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(text);

			if(text.contains("#REDIRECT") || text.contains("#redirect") || text.contains("#넘겨주기") ){
				type = "RD";
			}else if(text.contains("{{동음이의}}") 
					|| text.contains("{{동명이인}}")
					|| text.contains("{{동음이의어}}")
					|| text.contains("{{Disambig}}")
					|| text.contains("{{disambiguation}}")
					){
				type = "HM";
			}else if(text.contains("{{분류 동음이의")){
				Set<String> hmset = getHomonymCategory(text.trim());
				type = "HM";
				for(String element : hmset){
					linkSet.add("분류:"+element);
				}
			}

			while (m.find()) {
				String mst = m.group();
				String keyStr = m.group().substring(2, mst.length()-2).trim();
				String keyStrNorm = articleTermNorm(keyStr);
				if(keyStrNorm.startsWith("분류:")||keyStrNorm.startsWith("Category:")){
					categorySet.add(keyStrNorm);
					type = "WC";
				}
				if(keyStrNorm.startsWith(":")) continue;
				linkSet.add(keyStrNorm);
			}
			if(type.equals("")) type = "WA";
		}
		tagLevel = tagLevel.replaceAll("<" + qname + ">", "");
	}
	
	public void characters(char[] ch, int start, int length){

		String str = new String(ch, start, length);
		if(tagLevel.equals("<mediawiki><page>")){
		}else if(tagLevel.equals("<mediawiki><page><title>")){
			title = str;
		}else if(tagLevel.equals("<mediawiki><page><revision><text>")){
			text += str;
		}else if(tagLevel.equals("<mediawiki><page><id>")){
			id = str;
		}else if(tagLevel.equals( "<mediawiki><page><revision><timestamp>" )){
			timestamp = str;
		}else if(tagLevel.equals( "<mediawiki><page><ns>" )){
			ns = str;
		}

	}
	public void ignorableWhitespace(char[] ch, int start, int length){

	}
	public String articleTermNorm(String articleTerm){
		if(articleTerm.contains("#")) articleTerm = articleTerm.substring(0, articleTerm.indexOf("#")).trim();
		if(articleTerm.contains("|")) articleTerm = articleTerm.substring(0, articleTerm.indexOf("|")).trim();
		return articleTerm;
	}
	public Set<String> getHomonymCategory(String str){
		Set<String> hmSet = new HashSet<String>();
		String keyStr = str.substring(2, str.length()-2).trim();
		String keyStrArr[] = keyStr.split("|");
		int ct = 0;
		for(String element : keyStrArr){
			if(ct!=0 && ct%2==0){
				hmSet.add(element);
			}			
			ct++;
		}
		return hmSet;
	}

}
