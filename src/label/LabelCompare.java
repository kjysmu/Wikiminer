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

public class LabelCompare {
	
	public static void main(String args[]) throws Exception{
		
		String labelPath = "D:\\Development\\DATA\\LabelResult\\compare\\LabelResultFB_kjysmu.txt";
		String labelPath2 = "D:\\Development\\DATA\\LabelResult\\compare\\LabelResultFB_wjchoi.txt";
		
		String labelPath3 = "D:\\Development\\DATA\\LabelResult\\compare\\result.html";
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(new File(labelPath)), "UTF8"));
		
		BufferedReader br2 = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(new File(labelPath2)), "UTF8"));
		
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(new File(labelPath3)), "UTF8"));
		
		String line = "";
		String line2 = "";
		
		int ct = 0;
		int ct_compare = 1100; // how many tweets to be compared
		
		int ct_correct = 0;
		int ct_incorrect = 0;
		
		int ct_halfcorrect = 0;
		int ct_halfcorrect_N = 0;
		
		
		bw.write("<html>\n");
		bw.write("<head>\n");
		bw.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
		bw.write("<title>Recommendation Result</title>\n");
		bw.write("</head>\n");
		bw.write("<body>\n");
		
		
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
				}else{
					

					bw.write("<table width=\"1000\" height=\"100\" border=\"1\">\n");
					bw.write("<tr>\n");
					bw.write("<td height=\"10\" colspan=\"2\">" + "Msg ID : "+ id + " (index:"+ct+")"+ "</td>\n");
					bw.write("</tr>\n");
					// RAW MSG
					bw.write("<tr>\n");
					bw.write("<td height=\"60\" colspan=\"2\">" + msg + "</td>\n");
					bw.write("</tr>\n");
					
					bw.write("<tr>\n");
					bw.write("<td width=\"500\" height=\"60\">");
					
					if( categoryFirst.equals(categoryFirst2) || categoryFirst.equals(categorySecend2) ){
						
						bw.write("<font color=\"#0066FF\"><strong>\n");
						bw.write("<p>"+ categoryFirst +"</p>\n");
						bw.write("</strong></font>\n");
						
						correctNum++;
						
					}else{
						bw.write("<font color=\"#CC0000\"><strong>\n");
						bw.write("<p>"+ categoryFirst +"</p>\n");
						bw.write("</strong></font>\n");
					}

					if( categorySecend.equals(categoryFirst2) || categorySecend.equals(categorySecend2) ){
						bw.write("<font color=\"#0066FF\"><strong>\n");
						bw.write("<p>"+ categorySecend +"</p>\n");
						bw.write("</strong></font>\n");
						
						correctNum++;
						
					}else{
						bw.write("<font color=\"#CC0000\"><strong>\n");
						bw.write("<p>"+ categorySecend +"</p>\n");
						bw.write("</strong></font>\n");
					}
					
					bw.write("</td>\n");
					bw.write("<td width=\"500\" height=\"60\">");

					if( categoryFirst2.equals(categoryFirst) || categoryFirst2.equals(categorySecend) ){
						bw.write("<font color=\"#0066FF\"><strong>\n");
						bw.write("<p>"+ categoryFirst2 +"</p>\n");
						bw.write("</strong></font>\n");
						
						correctNum++;

					}else{
						bw.write("<font color=\"#CC0000\"><strong>\n");
						bw.write("<p>"+ categoryFirst2 +"</p>\n");
						bw.write("</strong></font>\n");
					}
					

					if( categorySecend2.equals(categoryFirst) || categorySecend2.equals(categorySecend) ){
						bw.write("<font color=\"#0066FF\"><strong>\n");
						bw.write("<p>"+ categorySecend2 +"</p>\n");
						bw.write("</strong></font>\n");
						
						correctNum++;
						
					}else{
						bw.write("<font color=\"#CC0000\"><strong>\n");
						bw.write("<p>"+ categorySecend2 +"</p>\n");
						bw.write("</strong></font>\n");
					}
		
					bw.write("</td>\n");
					bw.write("</tr>\n");
					
					bw.write("<tr>\n");

					if(correctNum == 4){
						
						ct_correct++;
						bw.write("<td height=\"10\" colspan=\"2\">" + "Correct(4/4)" + "</td>\n");

					}else if(correctNum ==0){
						
						ct_incorrect++;
						bw.write("<td height=\"10\" colspan=\"2\">" + "Incorrect(0/4)" + "</td>\n");
						
					}else{
						
						if(correctNum == 2){
							boolean isNone = false;
							if(categoryFirst.contains("NONE") || categorySecend.contains("NONE")) {
								if(categoryFirst2.contains("NONE") || categorySecend2.contains("NONE")) {
									
									isNone = true;
									
								}
							}
							
							if(isNone){
								ct_halfcorrect_N++;
								bw.write("<td height=\"10\" colspan=\"2\">" + "Halfcorrect-Case-None" + "</td>\n");
							}else{
								ct_halfcorrect++;
								bw.write("<td height=\"10\" colspan=\"2\">" + "Halfcorrect" + "</td>\n");
							}
							
							
						}else{
							ct_halfcorrect_N++;
							bw.write("<td height=\"10\" colspan=\"2\">" + "Halfcorrect-Case-None" + "</td>\n");
						}
						
					}
		
					bw.write("</tr>\n");
					bw.write("</table>\n");
					bw.write("<br />\n");
				}
				
			}else{
				
			}
			
			//if(ct_compare == ct) break;
			
		}
		
		br.close();
		br2.close();
		bw.close();
		
		System.out.println("halfcorrect(NONE) :" + ct_halfcorrect_N);
		System.out.println("halfcorrect :" + ct_halfcorrect);
		System.out.println("correct :" + ct_correct);
		System.out.println("incorrect :" + ct_incorrect);
		
		System.out.println("ct :" + ct);
		
		
		System.out.println("Done");
		
	}

}
