package wikipediaDump;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.*;

import org.xml.sax.*;

public class WikiDumpParser {
	public static void main(String args[]){
		
		String path = "D:\\Development\\Wikipedia\\kowiki-latest-pages-articles.xml";
		File xmlFile = new File(path);
		process(xmlFile);
	}
	private static void process(File file){
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser parser = null;
		
		spf.setNamespaceAware(true);
		spf.setValidating(true);
		try{
			parser=spf.newSAXParser();
		}catch(SAXException e){
			e.printStackTrace(System.err);
			System.exit(1);
		}catch(ParserConfigurationException e){
			e.printStackTrace(System.err);
			System.exit(1);
		}
		
		System.out.println("start");
		try{
			DumpHandler handler = new DumpHandler();
			parser.parse(file, handler);
		}catch(Exception e){
			e.printStackTrace(System.err);
		}
		
	}
	

}
