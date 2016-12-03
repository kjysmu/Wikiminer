package label;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

import util.KomoranAnalyzer;
import util.TermFunction;

public class GenTermCountStr {
	
	public static void main(String args[]) throws Exception{
		
		String labelPath = "D:\\Development\\DATA\\LabelResult\\LabelResultFB_final.txt";
		String labelPathOutput = "D:\\Development\\DATA\\LabelResult\\TermCounts_FB_final.txt";
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(new File(labelPath)), "UTF-8"));
		
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(new File(labelPathOutput)), "UTF-8"));
		
		
		KomoranAnalyzer kom = new KomoranAnalyzer();
		
		String line = "";
		
		
		int ct = 0;
		
		while(true){
			//ID - MSG - TIME - CAT1 - SUBCAT1 - CAT2 - SUBCAT2
			
			line = br.readLine();
			
			if(ct % 500 == 0) System.out.println(ct);
			
			if(line==null) break;
			
			StringTokenizer st = new StringTokenizer(line, "\t");
			
			if(st.countTokens() == 7 ){
				ct++;
				
				String id = st.nextToken();
				String msg = st.nextToken();
				
				String tcountStr = TermFunction.convertTermCountStr( TermFunction.getTermCounts(msg, kom) );
				
				bw.write(id + "\t" + tcountStr);
				bw.newLine();
				
			}else{
				
			}
			
		}
		bw.close();
		br.close();
		
		
		System.out.println("Complete");
		
		
	}
	

}
