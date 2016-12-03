package label;

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
import java.util.Map;
import java.util.StringTokenizer;

public class LabelKappa {
	
	public static void main(String args[]) throws Exception{
		
		String labelPath = "D:\\Development\\DATA\\LabelResult\\compare\\LabelResultFB_kjysmu.txt";
		String labelPath2 = "D:\\Development\\DATA\\LabelResult\\compare\\LabelResultFB_wjchoi.txt";
			
		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(new File(labelPath)), "UTF8"));
		
		BufferedReader br2 = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(new File(labelPath2)), "UTF8"));
		
		String line = "";
		String line2 = "";
		
		Map<String, Integer> kappaMap = new HashMap<String, Integer>();
		
		
		int ct = 0;
		
		int ct_correct = 0;
		int ct_incorrect = 0;
		
		int ct_halfcorrect = 0;
		int ct_halfcorrect_N = 0;
		
		while(true){
			//ID - MSG - TIME - CAT1 - SUBCAT1 - CAT2 - SUBCAT2
			
			line = br.readLine();
			line2 = br2.readLine();
			
			//System.out.println(line);
			//System.out.println(line2);
			
			if(line==null || line2==null) break;
			
			StringTokenizer st = new StringTokenizer(line, "\t");
			StringTokenizer st2 = new StringTokenizer(line2, "\t");
			
			if(st.countTokens() == 7 && st2.countTokens() == 7){
				
				ct++;
				
				String id = st.nextToken();
				String msg = st.nextToken();
				String time = st.nextToken();
				
				String catFirst = st.nextToken();
				String subcatFirst = st.nextToken();
				
				String catSecond = st.nextToken();
				String subcatSecond = st.nextToken();
				
				String id2 = st2.nextToken();
				String msg2 = st2.nextToken();
				String time2 = st2.nextToken();
				
				String catFirst2 = st2.nextToken();
				String subcatFirst2 = st2.nextToken();
				String catSecond2 = st2.nextToken();
				String subcatSecond2 = st2.nextToken();
				
				
				String categoryFirst = catFirst+" "+subcatFirst;
				String categorySecend = catSecond+" "+subcatSecond;
				
				String categoryFirst2 = catFirst2+" "+subcatFirst2;
				String categorySecend2 = catSecond2+" "+subcatSecond2;
				
				int correctNum = 0;
				
				if(!id.equals(id2)){
					System.out.println("ERROR : inconsistency");
					System.exit(0);
				}else{}
				
			}else{
				
			}
			
			//if(ct_compare == ct) break;
			
		}
		
		br.close();
		br2.close();
		
		System.out.println("halfcorrect(NONE) :" + ct_halfcorrect_N);
		System.out.println("halfcorrect :" + ct_halfcorrect);
		System.out.println("correct :" + ct_correct);
		System.out.println("incorrect :" + ct_incorrect);
		
		System.out.println("ct :" + ct);
		
		
		System.out.println("Done");
		
	}

}
