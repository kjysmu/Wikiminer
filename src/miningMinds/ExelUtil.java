package miningMinds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import parameter.Exp;
import parameter.Path;

public class ExelUtil {
	
	public static void main(String args[]) throws Exception{
		
		
		

		
		int col;
		WritableCellFormat cellFormat;
		WritableFont cellFont;


		cellFont = new WritableFont(WritableFont.TAHOMA, 12);
		cellFormat = new WritableCellFormat(cellFont);
		

		
		
		Map<String, String> modelOpMap = new HashMap<String, String>();
		
		
		
		
		String path = "D:\\Development\\result\\";
		
		Set<String> modelSet = new HashSet<String>();
		
		Set<String> thresholdSet = new HashSet<String>();
		
		
		modelSet.add("wiki-cficf-cs-mix-v2.7-CFICF");
		modelSet.add("wiki-cficf-cs-mix-v2.7-CS");
		modelSet.add("wiki-cficf-cs-mix-v2.7-AFIAF");
		modelSet.add("wiki-cficf-cs-mix-v2.7-MIX");
		
		modelSet.add("tfidf-lda-v2.7-CFICF");
		modelSet.add("tfidf-v2.7-CFICF");
		modelSet.add("tfidf-total-v2.0-l2");
		
		
		thresholdSet.add("0.0 0.0");
		thresholdSet.add("0.0 0.05");
		thresholdSet.add("0.0 0.1");
		thresholdSet.add("0.0 0.15");
		thresholdSet.add("0.0 0.2");
		thresholdSet.add("0.0 0.25");
		thresholdSet.add("0.0 0.3");
		thresholdSet.add("0.3333333333333333 0.3333333333333333");
		
		modelOpMap.put("tfidf-total-v2.0-l2", "0.3333333333333333 0.3333333333333333");
		modelOpMap.put("tfidf-v2.7-CFICF", "0.0 0.15");
		modelOpMap.put("tfidf-lda-v2.7-CFICF", "0.0 0.1");
		modelOpMap.put("wiki-cficf-cs-mix-v2.7-CFICF", "0.0 0.2");
		modelOpMap.put("wiki-cficf-cs-mix-v2.7-CS", "0.0 0.15");
		modelOpMap.put("wiki-cficf-cs-mix-v2.7-AFIAF", "0.0 0.2");
		modelOpMap.put("wiki-cficf-cs-mix-v2.7-MIX", "0.0 0.2");
		
		WritableWorkbook workbook;
		WritableSheet sheet1; // for cosine similarity
		WritableSheet sheet2; // for cosine similarity
		WritableSheet sheet3; // for cosine similarity
		WritableSheet sheet4; // for cosine similarity
		WritableSheet sheet5; // for cosine similarity
		WritableSheet sheet6; // for cosine similarity
		WritableSheet sheet7; // for cosine similarity
		WritableSheet sheet8; // for cosine similarity
		WritableSheet sheet9; // for cosine similarity
		WritableSheet sheet10; // for cosine similarity
		WritableSheet sheet11; // for cosine similarity
		WritableSheet sheet12; // for cosine similarity
		WritableSheet sheet13; // for cosine similarity
		WritableSheet sheet14; // for cosine similarity
		WritableSheet sheet15; // for cosine similarity

		workbook = Workbook.createWorkbook(new File("D:\\excelres2\\topk.xls"));
		
		sheet1 = workbook.createSheet("1",  0);
		sheet2 = workbook.createSheet("2",  1);
		sheet3 = workbook.createSheet("3",  2);
		sheet4 = workbook.createSheet("4",  3);
		sheet5 = workbook.createSheet("5",  4);
		sheet6 = workbook.createSheet("6",  5);
		sheet7 = workbook.createSheet("7",  6);
		sheet8 = workbook.createSheet("8",  7);
		sheet9 = workbook.createSheet("9",  8);
		sheet10 = workbook.createSheet("10",  9);
		sheet11 = workbook.createSheet("11",  10);
		sheet12 = workbook.createSheet("12",  11);
		sheet13 = workbook.createSheet("13",  12);
		sheet14 = workbook.createSheet("14",  13);
		sheet15 = workbook.createSheet("15",  14);
		
		
		int modelct = 1;
		
		for(String model : modelSet){
			
			
			Label label_ID1 = new Label(0, modelct, model, cellFormat);
			Label label_ID2 = new Label(0, modelct, model, cellFormat);
			Label label_ID3 = new Label(0, modelct, model, cellFormat);
			Label label_ID4 = new Label(0, modelct, model, cellFormat);
			Label label_ID5 = new Label(0, modelct, model, cellFormat);
			Label label_ID6 = new Label(0, modelct, model, cellFormat);
			Label label_ID7 = new Label(0, modelct, model, cellFormat);
			Label label_ID8 = new Label(0, modelct, model, cellFormat);
			Label label_ID9 = new Label(0, modelct, model, cellFormat);
			Label label_ID10 = new Label(0, modelct, model, cellFormat);
			Label label_ID11 = new Label(0, modelct, model, cellFormat);
			Label label_ID12 = new Label(0, modelct, model, cellFormat);
			Label label_ID13 = new Label(0, modelct, model, cellFormat);
			Label label_ID14 = new Label(0, modelct, model, cellFormat);
			Label label_ID15 = new Label(0, modelct, model, cellFormat);
			
			sheet1.addCell(label_ID1);
			sheet2.addCell(label_ID2);
			sheet3.addCell(label_ID3);
			sheet4.addCell(label_ID4);
			sheet5.addCell(label_ID5);
			sheet6.addCell(label_ID6);
			sheet7.addCell(label_ID7);
			sheet8.addCell(label_ID8);
			sheet9.addCell(label_ID9);
			sheet10.addCell(label_ID10);
			sheet11.addCell(label_ID11);
			sheet12.addCell(label_ID12);
			sheet13.addCell(label_ID13);
			sheet14.addCell(label_ID14);
			sheet15.addCell(label_ID15);

			for(String th : thresholdSet){
				
				if( !th.equals(modelOpMap.get(model)) ) continue ;
				

				for(int i=1; i<=20; i++){
					
					String simPath = path + "News" + i + "\\" + model + "\\" + th + "\\topk.txt";
					
					FileReader fr = new FileReader(new File(simPath));
					BufferedReader br = new BufferedReader(fr);
					
					String line = "";
					int ct = 1;
					
					while(true){
						
						line = br.readLine();
						if(line == null) break;
						
						StringTokenizer token = new StringTokenizer(line, "\t");
						if(token.countTokens() >= 2){
							String token1= token.nextToken();
							String token2= token.nextToken();
							
							String score = String.format("%.4f", Double.parseDouble( token2 ))	;
							
							Label label_ID = new Label(i, modelct, score.trim(), cellFormat);
							
							if(token1.trim().equals("1")){
								sheet1.addCell(label_ID);
								
							}else if(token1.trim().equals("2")){
								sheet2.addCell(label_ID);

							}else if(token1.trim().equals("3")){
								sheet3.addCell(label_ID);

							}else if(token1.trim().equals("4")){
								sheet4.addCell(label_ID);

							}else if(token1.trim().equals("5")){
								sheet5.addCell(label_ID);

							}else if(token1.trim().equals("6")){
								sheet6.addCell(label_ID);

							}else if(token1.trim().equals("7")){
								sheet7.addCell(label_ID);

							}else if(token1.trim().equals("8")){
								sheet8.addCell(label_ID);

							}else if(token1.trim().equals("9")){
								sheet9.addCell(label_ID);

							}else if(token1.trim().equals("10")){
								sheet10.addCell(label_ID);

							}else if(token1.trim().equals("11")){
								sheet11.addCell(label_ID);

							}else if(token1.trim().equals("12")){
								sheet12.addCell(label_ID);

							}else if(token1.trim().equals("13")){
								sheet13.addCell(label_ID);

							}else if(token1.trim().equals("14")){
								sheet14.addCell(label_ID);

							}else if(token1.trim().equals("15")){
								sheet15.addCell(label_ID);

							}
							
							
							ct++;
							
						}
						
						
						
					}
					br.close();
					fr.close();
					
					
				}
				

					
			}
			
			modelct ++;

		}

		workbook.write();
		workbook.close();
		
		System.out.println("complete");
		
	}

}
