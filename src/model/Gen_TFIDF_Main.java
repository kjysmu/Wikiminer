/**
 * 
 */
package model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import util.NewsFileReader;
import util.TweetFunction3;

/**
 * @author kjysmu
 *
 */
public class Gen_TFIDF_Main {
	public static void main(String[] args) throws Exception{
		
		//Gen_IDF idf = new Gen_IDF();
		Gen_TFIDF tfidf = new Gen_TFIDF();
		
		//idf.run();
		tfidf.run();
		
		
		//GenICF genICF = new GenICF();
		//GenTFICF genTFICF = new GenTFICF();		
		//genICF.run();
		//genTFICF.run();
		
		System.out.println("complete");
		
	}
}
