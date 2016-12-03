package miningMinds;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import parameter.*;
import modules.*;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

public class MiningMindsMain {

	static int exp_ct = 0;
	static int total_exp_ct = 0;
	
	static WritableWorkbook workbook;
	static WritableSheet sheet; // for cosine similarity
	static WritableSheet sheet2; // for short text classification accuracy
	
	static int col;
	static WritableCellFormat cellFormat;
	static WritableFont cellFont;

	public static void init() throws Exception{
		File dir = new File(Path.RESULT_PATH, Exp.resultPathAppend);
		if(!dir.exists()) dir.mkdir();
		workbook = Workbook.createWorkbook(new File( dir.getCanonicalPath() + "\\result_"+Exp.resultPathAppend+".xls"));
		sheet = workbook.createSheet("sheet 0",  0);
		sheet2 = workbook.createSheet("sheet 1",  1);
		
	    cellFont = new WritableFont(WritableFont.TAHOMA, 12);
	    cellFormat = new WritableCellFormat(cellFont);
	}
	
	public static void main(String args[]) throws Exception{
		System.out.println("Start");
		
		Path2 path = new Path2(1);

		for(int i=1; i<=1; i++){
			
			System.out.println( "Dataset index : " + i );
			
			path = new Path2(i);
			
			Exp.CorpusIndex  = i;
			
			//System.out.println(path.CATEGORY_MODEL_TFIDF_PATH);
			
			init();
			
			//--------------Exp Parameters------------------//
			
			String str1 = "msg_threshold";
			String str2 = "msg_threshold2";
			
			Double parameter1[] = { 0.0 };		
			Double parameter2[] = { 0.2 };
			
			total_exp_ct = parameter1.length * parameter2.length ;
			
			//-----------------------------------------------//
			sheet.setColumnView(1, 25);
			sheet2.setColumnView(1, 25);

			sheet.mergeCells(1, 1, 1 + total_exp_ct, 1);
			sheet2.mergeCells(1, 1, 1 + total_exp_ct, 1);
			
			Label label_title = new Label(1, 1, Exp.resultPathAppend, cellFormat);
			sheet.addCell(label_title);
			label_title = new Label(1, 1, Exp.resultPathAppend, cellFormat);
			sheet2.addCell(label_title);

			Label label_th1 = new Label(1, 2, str1, cellFormat);
			sheet.addCell(label_th1);
			label_th1 = new Label(1, 2, str1, cellFormat);
			sheet2.addCell(label_th1);

			Label label_th2 = new Label(1, 3, str2, cellFormat);
			sheet.addCell(label_th2);
			label_th2 = new Label(1, 3, str2, cellFormat);
			sheet2.addCell(label_th2);
			
			for(Double th1 : parameter1){
				for(Double th2 : parameter2){
					exp_ct++;
					Label label_th1_value = new Label(1 + exp_ct, 2, th1.toString(), cellFormat);
					sheet.addCell(label_th1_value);
					label_th1_value = new Label(1 + exp_ct, 2, th1.toString(), cellFormat);
					sheet2.addCell(label_th1_value);
					
					Label label_th2_value = new Label(1 + exp_ct, 3, th2.toString(), cellFormat);
					sheet.addCell(label_th2_value);
					label_th2_value = new Label(1 + exp_ct, 3, th2.toString(), cellFormat);
					sheet2.addCell(label_th2_value);
					
					setParameters(str1, th1);
					setParameters(str2, th2);
					setParameters("w_cs", 1.0 - th1 - th2);
					
					Exp.resultPathTestAppend = th1.toString() + " " + th2.toString();
					
					if( th1 + th2 > 1.0 ){
					}else{
						runExp( i );	
					}
				}
			}
			workbook.write();
			workbook.close();
		}
		
		System.out.println("Complete"); 
	}
	public static void runExp(int index) throws Exception{
		
		//--------------Initialize Main Class------------------//
		
		SNSLoader sloader = new SNSLoader(Exp.SNSType, true);
		MiningMindsEngine engine = new MiningMindsEngine(Exp.SNSType, index);
		Map<String, SNSUserBean> userMap = new HashMap<String, SNSUserBean>();
		
		//-----------------------------------------------------//

		userMap = sloader.getUserMap();
		int totalct = userMap.size();
		
		int ct = 0;
		
		int totalNum = 0;
		
		for (Map.Entry<String, SNSUserBean> userMapEntry : userMap.entrySet()){
			ct++;
			String userID = userMapEntry.getKey();
			if(exp_ct == 1){
				Label label_ID = new Label(1, 4 + ct, userID, cellFormat);
				sheet.addCell(label_ID);
				label_ID = new Label(1, 4 + ct, userID, cellFormat);
				sheet2.addCell(label_ID);
			}
			SNSUserBean userBean = userMapEntry.getValue();
			Map<String, Double> recomTotalCategoryMap;
			
			if(Exp.approach.contains("total")){
				recomTotalCategoryMap = engine.getCategoryList(userBean);
				
				totalNum += userBean.getTotalTermCountNum();
				
			}else{
				Map<String, SNSUserMsgBean> msgMap = userBean.getMsgMap();
				for (Map.Entry<String, SNSUserMsgBean> msgMapEntry : msgMap.entrySet()){
					SNSUserMsgBean userMsgBean = msgMapEntry.getValue();
					Map<String, Double> categoryMap = engine.getCategoryList(userMsgBean, userBean);
					userMsgBean.setRecomCategoryMap(categoryMap);
				}
				recomTotalCategoryMap = userBean.computeRecomTotalCategoryMap();
				
				
			}
			
			Map<String, Double> labelTotalCategoryMap = userBean.computeLabelTotalCategoryMap();
			
			userBean.setLabelTotalCategoryMap(labelTotalCategoryMap);
			
			userBean.setRecomTotalCategoryMap(recomTotalCategoryMap);
			
			double sim = engine.getCosineSimilarity(recomTotalCategoryMap, labelTotalCategoryMap);
			
			userBean.setSimilarity(sim);
			double acc = userBean.getMsgAccuracy();
			
			System.out.print("[EXP:"+exp_ct +"/"+ total_exp_ct+"] ");
			System.out.println("userid : " + userID + " , similarity : "+ String.format("%.4f", sim) + " , (" + ct + "/" + totalct +")");
			
			Label label_sim = new Label(1 + exp_ct, 4 + ct, String.format("%.4f", sim) , cellFormat);
			Label label_acc = new Label(1 + exp_ct, 4 + ct, String.format("%.4f", acc) , cellFormat);
			
			sheet.addCell(label_sim);
			sheet2.addCell(label_acc);
			
		}
		
		if(Exp.approach.contains("total")){
			
			Double avTotalNum = totalNum / 20.0 ;	
			System.out.println("TotalNum : "  + totalNum);
			System.out.println("AvrTotalNum : "  + avTotalNum);
			
		}

		System.out.println("Generating Result...");
		
		if(!Exp.approach.contains("total")){
			sloader.generateResultHTML( index );
		}
		
		sloader.generateParameterFile( index );
		sloader.generateResultFile( index );
		sloader.generateRecomCategoryResultFile( index );
		sloader.generateMsgAccuracyResultFile( index );
		
		double averageSim = sloader.getAverageSimilarity();
		double averageAcc = sloader.getAverageMsgAccuracy();
		
		System.out.println("average similarity : "+ String.format("%.4f", averageSim));
		Label label_avr_sim = new Label(1 + exp_ct, 4 + ct + 2, String.format("%.4f", averageSim) , cellFormat);
		Label label_avr_acc = new Label(1 + exp_ct, 4 + ct + 2, String.format("%.4f", averageAcc) , cellFormat);
		
		sheet.addCell(label_avr_sim);
		sheet2.addCell(label_avr_acc);
		
		//engine.exportWikimap();

	}
	
	public static void setParameters( String parameter, Double value  ) throws Exception{

		if(parameter.equals("msg_threshold")){
			Exp.msg_threshold = value;
		}else if(parameter.equals("msg_threshold2")){
			Exp.msg_threshold2 = value;
		}else if(parameter.equals("subCategoryWeight_Naver")){
			Exp.subCategoryWeight_Naver = value;
		}else if(parameter.equals("subCategoryWeight_User")){
			Exp.subCategoryWeight_User = value;
		}else if(parameter.equals("subCategoryWeight")){
			Exp.subCategoryWeight_User = value;
			Exp.subCategoryWeight_Naver = value;
		}else if(parameter.equals("enrichmentWeight")){
			Exp.enrichmentWeight = value;
		}else if(parameter.equals("clusterWeight")){
			Exp.clusterWeight = value;
		}else if(parameter.equals("ESA_CategoryWeight")){
			Exp.ESA_CategoryWeight = value;
		}else if(parameter.equals("ESA_threshold")){
			Exp.ESA_threshold = value;
		}else if(parameter.equals("ESA_Weight")){
			Exp.ESA_Weight = value;
		}else if(parameter.equals("w_cficf")){
			Exp.w_cficf = value;
		}else if(parameter.equals("w_afiaf")){
			Exp.w_afiaf = value;
		}else if(parameter.equals("w_cs")){
			Exp.w_cs = value;
		}		
		
	}


	
}
