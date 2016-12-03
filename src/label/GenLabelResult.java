package label;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import util.FileFunction;

public class GenLabelResult {
	
	public static void main(String args[]) throws Exception{
		
		String path = "D:\\Development\\DATA\\LabelResult\\LabelResultFB_Correct.txt";
		
		String labelPath = "D:\\Development\\DATA\\LabelResult\\compare\\LabelResultFB_kjysmu.txt";
		String labelPath2 = "D:\\Development\\DATA\\LabelResult\\compare\\LabelResultFB_wjchoi.txt";
		
		String OutputPath = "D:\\Development\\DATA\\LabelResult\\compare\\LabelResultFB_final.txt";
		
		BufferedReader br = new BufferedReader(
				   new InputStreamReader(
		                      new FileInputStream(path), "UTF-8"));
		
		String line = "";
		String line2 = "";
		
		Map<Integer, String> ctMap = new HashMap<Integer,String>();
		
		boolean isFirst = true;
		while ((line = br.readLine()) != null) {
			
			if(isFirst){
				line = FileFunction.removeUTF8BOM(line);
				isFirst = false;
			}
			
			if(line.startsWith("A[") || line.startsWith("D[") ){
				String ctStr = line.substring(line.indexOf("[")+1, line.indexOf("]"));
				
				ctMap.put(Integer.parseInt(ctStr), line.substring(0, 1));
				
			}
			
			
		}
		br.close();
		
		//System.exit(0);
		
		
		BufferedReader br1 = new BufferedReader(
				   new InputStreamReader(
		                      new FileInputStream(labelPath), "UTF-8"));
		
		BufferedReader br2 = new BufferedReader(
				   new InputStreamReader(
		                      new FileInputStream(labelPath2), "UTF-8"));
		
		
		BufferedWriter bw = new BufferedWriter(
				   new OutputStreamWriter(
		                      new FileOutputStream(OutputPath), "UTF-8"));
		
		isFirst = true;
		
		
		int ct = 0;
		
		
		while(true){
			
			
			//ID - MSG - TIME - CAT1 - SUBCAT1 - CAT2 - SUBCAT2
			
			line = br1.readLine();
			line2 = br2.readLine();
			
			//System.out.println(line);
			//System.out.println(line2);
			
			if(line==null || line2==null) break;
			
			if(isFirst){
				line = FileFunction.removeUTF8BOM(line);
				isFirst = false;
			}
			
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
				}else{

				}
				
				if(ctMap.containsKey(ct)){
					
					String resStr = ctMap.get(ct);
					
					if(resStr.equals("A")){
						
						bw.write(id+"\t"+msg+"\t"+time+"\t"+catFirst+"\t"+subcatFirst+"\t"+catSecond+"\t"+subcatSecond);
						bw.newLine();
						
					}else if(resStr.equals("D")){
						
						bw.write(id+"\t"+msg+"\t"+time+"\t"+catFirst2+"\t"+subcatFirst2+"\t"+catSecond2+"\t"+subcatSecond2);
						bw.newLine();
						
					}else{
						
					}
					
					
				}else{
					bw.write(line);
					bw.newLine();
				}

			}else{
				bw.write(line);
				bw.newLine();
				
			}
		
		}

		br1.close();
		br2.close();
		
		bw.close();
		
		System.out.println("Complete");
		
	}

}
