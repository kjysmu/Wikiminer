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
import java.util.StringTokenizer;

public class LabelMain {
	
	public static void main(String args[]) throws Exception{
		
		String labelPath = "D:\\Development\\DATA\\LabelResult\\wonjun\\LabelResultFB.txt";
		String labelPath2 = "D:\\Development\\DATA\\LabelResult\\wonjun\\LabelResultFB2.txt";
		
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(new File(labelPath)), "UTF8"));
		
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(new File(labelPath2)), "UTF8"));
		
		String line = "";
		
		while(true){
			//ID - MSG - TIME - CAT1 - SUBCAT1 - CAT2 - SUBCAT2
			
			line = br.readLine();
			
			if(line==null) break;
			
			StringTokenizer st = new StringTokenizer(line, "\t");
			
			if(st.countTokens() == 7){
				String id = st.nextToken();
				String msg = st.nextToken();
				String time = st.nextToken();
				
				String cat1 = st.nextToken();
				String subcat1 = st.nextToken();
				String cat2 = st.nextToken();
				String subcat2 = st.nextToken();
				
				cat1 = "NONE";
				subcat1 = "NONE";
				cat2 = "NONE";
				subcat2 = "NONE";
				
				bw.write(id+"\t"+msg+"\t"+time+"\t"+cat1+"\t"+subcat1+"\t"+cat2+"\t"+subcat2);
				bw.newLine();
				
				
			}else{
				
				bw.write(line);
				bw.newLine();
				
			}
			
		}
		
		br.close();
		
		bw.close();
		
		System.out.println("Done");
		
	}

}
