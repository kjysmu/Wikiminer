/**
 * 
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author Jonghyun Han
 *
 */
public class TweetFunction3 {
	
    public static String removeUTF8BOM(String s) {
    	String UTF8_BOM = "\uFEFF";
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }
	
	
	public static Map<String, Integer> SortMap(Map<String, Integer> map) {
		IntValueComparator bvc =  new IntValueComparator(map);
		TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
		sortedMap.putAll(map);
		
		return sortedMap;
	}
	
	public static Map<String, Double> SortDoubleMap(Map<String, Double> map) {
		DoubleValueComparator bvc =  new DoubleValueComparator(map);
		TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(bvc);
		sortedMap.putAll(map);
		
		return sortedMap;
	}
	
	public static void AddCounts(Map<String, Integer> terms, Map<String, Integer> addedTerms) {
		if (terms == null)
			terms = new HashMap<String, Integer>();
		
		for (Map.Entry<String, Integer> entry : addedTerms.entrySet()) {
			String key = entry.getKey();
			
			if (terms.containsKey(key)) {
				terms.put(key, terms.get(key) + entry.getValue());
			}
			else {
				terms.put(key, entry.getValue());
			}
		}
	}
	
	public static Map<String, Double> CombineCounts(Map<String, Double> source, Map<String, Double> target) {
		Map<String, Double> combinedCounts = new HashMap<String, Double>();
		
		combinedCounts.putAll(source);
		for (Map.Entry<String, Double> entry : target.entrySet()) {
			String key = entry.getKey();
			
			if (source.containsKey(key)) {
				combinedCounts.put(key, source.get(key) + entry.getValue());
			}
			else {
				combinedCounts.put(key, entry.getValue());
			}
		}
		
		return combinedCounts;
	}
	public static Map<String, Double> CombineNormCounts(Map<String, Double> source, Map<String, Double> target) {
		Map<String, Double> combinedCounts = new HashMap<String, Double>();
		
		combinedCounts.putAll(source);
		for (Map.Entry<String, Double> entry : target.entrySet()) {
			String key = entry.getKey();
			
			if (source.containsKey(key)) {
				combinedCounts.put(key, source.get(key) + entry.getValue());
			}
			else {
				combinedCounts.put(key, entry.getValue());
			}
		}
		
		return combinedCounts;
	}
	
	
	public static String ExtractUserID(String tweets, Map<String, Integer> mentionCounts) {
		if (mentionCounts == null)
			mentionCounts = new HashMap<String, Integer>();
		
		int index = tweets.indexOf('@');
		while (index < tweets.length() && index != -1) {			
			String userid = "@";

			while (true) {
				if (index == tweets.length()-1)
					break;

				char c = tweets.charAt(++index);
				if ((c >= 'a' && c <= 'z') || 
					(c >= 'A' && c <= 'Z') ||
					(c >= '0' && c <= '9') ||
					(c == '_') ) {
					userid += c;
				}
				else
					break;
			}
			
			if (userid.length() != 1) {
				if (mentionCounts.containsKey(userid))
					mentionCounts.put(userid, mentionCounts.get(userid)+1);
				else
					mentionCounts.put(userid, 1);
			}

			if (index == tweets.length()-1)
				break;
			
			index = tweets.indexOf('@', index);
		}
		
		String removedIDTweets = tweets;
		
		for (Map.Entry<String, Integer> entry : mentionCounts.entrySet()) {
			removedIDTweets = removedIDTweets.replace(entry.getKey(), "");
		}
		
		return removedIDTweets;
	}
	
	private static Map<String, Double> ExtractURL(String tweets, String protocol) {
		Map<String, Double> urls = new HashMap<String, Double>();
		
		int index = tweets.indexOf(protocol);
		while (index < tweets.length() && index != -1) {			
			String url = "";

			int indexOfWhiteSpace = tweets.indexOf(' ', index+1);
			if (indexOfWhiteSpace > tweets.indexOf('\r', index+1) && tweets.indexOf('\r', index+1) != -1)
				indexOfWhiteSpace = tweets.indexOf('\r', index+1);
			if (indexOfWhiteSpace > tweets.indexOf('\n', index+1) && tweets.indexOf('\n', index+1) != -1)
				indexOfWhiteSpace = tweets.indexOf('\n', index+1);
			if (indexOfWhiteSpace > tweets.indexOf('\t', index+1) && tweets.indexOf('\t', index+1) != -1)
				indexOfWhiteSpace = tweets.indexOf('\t', index+1);

			if (indexOfWhiteSpace < 0)
				indexOfWhiteSpace = tweets.length();
			
			url = tweets.substring(index, indexOfWhiteSpace);
			
			if (urls.containsKey(url))
				urls.put(url, urls.get(url)+1.0);
			else
				urls.put(url, 1.0);
			
			index = tweets.indexOf(protocol, indexOfWhiteSpace);
		}
		
		return urls;
	}
	
	public static String ExtractURL(String tweets, Map<String, Double> urlCounts) {
		if (urlCounts == null)
			urlCounts = new HashMap<String, Double>();
		
		Map<String, Double> httpURL = ExtractURL(tweets, "http://");
		Map<String, Double> httpsURL = ExtractURL(tweets, "https://");
		
		urlCounts = CombineCounts(urlCounts, httpURL);
		urlCounts = CombineCounts(urlCounts, httpsURL);
		
		String removedURLTweets = tweets;
		
		for (Map.Entry<String, Double> entry : urlCounts.entrySet()) {
			removedURLTweets = removedURLTweets.replace(entry.getKey(), "");
		}

		return removedURLTweets;
	}

	public static void RemoveDigit(Map<String, Integer> map) {
		Iterator<Map.Entry<String, Integer>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Integer> entry = iter.next();
			if (isInteger(entry.getKey())) {
				iter.remove();
			}
		}
		iter = null;
	}
	
    public static boolean isInteger( String input )  
    {  
       try  
       {  
          Integer.parseInt( input );  
          return true;  
       }  
       catch( Exception e)  
       {  
          return false;  
       }  
    }
 
	public static void SaveWordCount(Map<String, Integer> map, String filename, int tweetcount) {
		try {
			//FileWriter fw = new FileWriter(filename);
			//BufferedWriter writer = new BufferedWriter(fw);
			
			BufferedWriter writer = new BufferedWriter(
					   new OutputStreamWriter(
			                      new FileOutputStream(filename), "UTF8"));
			
		
			Map<String, Integer> sortedMap = SortMap(map);
			
			writer.write("// # of Tweets: " + tweetcount + "\r\n");
			for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
				writer.write(entry.getKey() + "\t" + entry.getValue() + "\r\n");
			}
			
			writer.close();
			
			//fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void SaveWordCount(Map<String, Double> map, String filename) {
		try {
			
			// FileWriter fw = new FileWriter(filename);
			
			Writer fw = new OutputStreamWriter( new FileOutputStream (filename), "UTF-8" );
			BufferedWriter writer = new BufferedWriter(fw);
		
			DoubleValueComparator bvc = new DoubleValueComparator(map);
			TreeMap<String, Double> tMap = new TreeMap<String, Double>(bvc);
			tMap.putAll(map);
					
			for (Map.Entry<String, Double> entry : tMap.entrySet()) {
				writer.write(entry.getKey() + "\t" + String.format("%.2f", entry.getValue()) + "\r\n");
			}
			
			writer.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void SaveNormWordCount(Map<String, Double> map, String filename) {
		try {
			//FileWriter fw = new FileWriter(filename);
			//BufferedWriter writer = new BufferedWriter(fw);
		
			Writer fw = new OutputStreamWriter( new FileOutputStream (filename), "UTF-8" );
			BufferedWriter writer = new BufferedWriter(fw);

			Map<String, Double> sortedMap = SortDoubleMap(map);
			
			for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
				writer.write(entry.getKey() + "\t" + entry.getValue() + "\r\n");
			}
			
			writer.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static HashMap<String, String> ReadUserIDMap(String filename) {
		HashMap<String, String> userIDMap = new HashMap<String, String>();
		try 
		{
			//FileReader fr = new FileReader(filename);
			//BufferedReader reader = new BufferedReader(fr);
	
			Reader fr = new InputStreamReader( new FileInputStream (filename), "UTF-8" );
			BufferedReader reader = new BufferedReader(fr);
			
			String data = "";
			while ((data = reader.readLine()) != null) {
				String[] parsedLine = data.split("\t");
				userIDMap.put(parsedLine[1], parsedLine[2]);
			}
			
			reader.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return userIDMap;
	}
	
	public static void RemoveUnImportantWords(Map<String, Integer> nounCounts, double frequency) {
		int totalCount = 0;
		
		for (Map.Entry<String, Integer> nounCount : nounCounts.entrySet()) {
			totalCount += nounCount.getValue();
		}
		
		System.out.print(nounCounts.size() + "\t");
		
		Iterator<Map.Entry<String, Integer>> iter = nounCounts.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Integer> entry = iter.next();
			if (entry.getValue()/(double)totalCount < frequency) {
				iter.remove();
			}
		}		
		iter = null;
		
		System.out.print(nounCounts.size() + "\t");
	}
	
	public static void RemoveStopWords(Map<String, Integer> nounCounts) {
		StopWord stopwords = StopWord.GetInstance();
		stopwords.Initialize("D:/Development/Data/Naver News/StopWords.txt");
	
		for (Iterator<String> iter = stopwords.GetIterator(); iter.hasNext();) {
			nounCounts.remove(iter.next());
		}
	}
	
	public static Map<String, Double> readWordCountFile(String wordcountfilename) {
		Map<String, Double> wordCounts = new HashMap<String, Double>();

		try {
			
			Reader fr = new InputStreamReader( new FileInputStream (wordcountfilename), "UTF-8" );
			BufferedReader reader = new BufferedReader(fr);
			
			String data = "";
			
			boolean isFirst = true;
			
			while ((data = reader.readLine()) != null) {
				
				if(isFirst){
					data = removeUTF8BOM(data);
					isFirst = false;
				}
				
				String[] parsedData = data.split("\t");
				if (parsedData.length == 2) {
					
					wordCounts.put(parsedData[0], Double.parseDouble(parsedData[1]));
				}
			}
			
			reader.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return wordCounts;
	}
	
	public static Map<String, Double> readWordCountFileANSI(String wordcountfilename) {
		Map<String, Double> wordCounts = new HashMap<String, Double>();

		try {
			
			// Reader fr = new InputStreamReader( new FileInputStream (wordcountfilename), "UTF-8" );
			
			FileReader fr = new FileReader(new File(wordcountfilename));
			BufferedReader reader = new BufferedReader(fr);
			
			String data = "";
			
			boolean isFirst = true;
			
			while ((data = reader.readLine()) != null) {
				
				if(isFirst){
					data = removeUTF8BOM(data);
					isFirst = false;
				}
				
				String[] parsedData = data.split("\t");
				if (parsedData.length == 2) {
					
					wordCounts.put(parsedData[0], Double.parseDouble(parsedData[1]));
				}
			}
			
			reader.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return wordCounts;
	}
	
	
	
	public static Map<String, Double> readNormWordCountFile(String wordcountfilename) {
		Map<String, Double> wordCounts = new HashMap<String, Double>();
		Map<String, Double> normWordCounts = new HashMap<String, Double>();

		
		try {
			// FileReader fr = new FileReader(wordcountfilename);
			// BufferedReader reader = new BufferedReader(fr);
			
			Reader fr = new InputStreamReader( new FileInputStream (wordcountfilename), "UTF-8" );
			BufferedReader reader = new BufferedReader(fr);
			
			String data = "";
			
			Double countSum = 0.0;
			
			while ((data = reader.readLine()) != null) {
				String[] parsedData = data.split("\t");
				if (parsedData.length == 2) {
					wordCounts.put(parsedData[0], Double.parseDouble(parsedData[1]));
					countSum += Double.parseDouble(parsedData[1]);
				}
			}
			
			for (Map.Entry<String, Double> wordCount : wordCounts.entrySet()) {
				normWordCounts.put(wordCount.getKey(), wordCount.getValue()/countSum  );	
			}
			
			
			
			reader.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return normWordCounts;
	}
	
	// All the weight is 1 for IDF
	public static Map<String, Double> readUniWordCountFile(String wordcountfilename) {
		Map<String, Double> wordCounts = new HashMap<String, Double>();

		try {
			// FileReader fr = new FileReader(wordcountfilename);
			// BufferedReader reader = new BufferedReader(fr);
			
			Reader fr = new InputStreamReader( new FileInputStream (wordcountfilename), "UTF-8" );
			BufferedReader reader = new BufferedReader(fr);
			
			String data = "";
			while ((data = reader.readLine()) != null) {
				String[] parsedData = data.split("\t");
				if (parsedData.length == 2) {
					wordCounts.put(parsedData[0], 1.0 );
				}
			}
			
			reader.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return wordCounts;
	}
	
	
	
	
	
}
