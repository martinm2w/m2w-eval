package util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * this method is
 * known as the SCIL evaluation method
 * @author m2w
 *
 */


public class CompareEval {
	
	/**
	 * m2w: this method is for the NIST evaluation.
	 * @param bw
	 * @param speakers
	 * @param auto_qscore
	 * @param human_qscore
	 */
	public void compareEval(BufferedWriter bw, String[] speakers, int[] auto_qscore, int[] human_qscore){
		
		ArrayList<String> compListH = new ArrayList<String>();
		ArrayList<String> compListA = new ArrayList<String>();
		int equalCount = 0;
		
		/*checking input*/
		for (int k = 0; k < auto_qscore.length; k ++ ){
			System.out.println("auto_qscore: " + auto_qscore[k]);
		}

		System.out.println("----------------------------------------");

		for (int k = 0; k < human_qscore.length; k ++ ){
			System.out.println("human_qsore: " + human_qscore[k]);
		}

		System.out.println("========================================");
		
		 /*Relations*/
        try {
        	
			bw.write("          -=        Relations      =-             ");
			bw.newLine();
	        bw.write("Auto_file\t\t" + "Human_file");
	        bw.newLine();
			
	        for(int i = 0; i < (speakers.length - 1); i++){
	        
	        	/*1st round check*/
	        	if(auto_qscore == null || human_qscore == null){
	                continue;
	            }
	            if(auto_qscore[i] == 0 && human_qscore[i] == 0){
	                continue;
	            }
	            
	        	for(int j = i+1; j < speakers.length; j++){
	        		
	        		
	        		 /*2 if discard speaker not in subfile*/
	        		if(auto_qscore == null || human_qscore == null){
                        continue;
                    }
                    if(auto_qscore[j] == 0 && human_qscore[j] == 0){ //maybe -1
                        continue;
                    }
                                   
	        		String tempStra = null;
	        		String tempStrh = null;
	        		
	        		/*auto_file logic*/
	        		if(auto_qscore[i] < auto_qscore[j]){
	        			tempStra = speakers[i] + " < " + speakers[j];
	        		}
	        		
	        		if(auto_qscore[i] > auto_qscore[j]){
	        			tempStra = speakers[i] + " > " + speakers[j];
	        		}
	        		
	        		if(auto_qscore[i] == auto_qscore[j]){
	        			tempStra = speakers[i] + " = " + speakers[j];
	        		}
	        		
	        		/*human_file logic*/
	        		if (human_qscore[i] < human_qscore[j]){
	        			tempStrh = speakers[i] + " < " + speakers[j];
	        		
	        		}
	        		if (human_qscore[i] > human_qscore[j]){
	        			tempStrh = speakers[i] + " > " + speakers[j];
	        		}
	        		if (human_qscore[i] == human_qscore[j]){
	        			tempStrh = speakers[i] + " = " + speakers[j];
	        		}
	        		
	        		
	        		compListA.add(tempStra);// use to print result
	        		compListH.add(tempStrh);
	        		
	        		/*calculate equalcount*/
	        		if(tempStra.equals(tempStrh)){
	        			equalCount++;
	        		}
	        		
	        		bw.write(tempStra + "\t\t" + tempStrh);
	        		bw.newLine();
	        	}
	        	
	        } // for all speakers
	        
	        /*result eval line*/
	        bw.write("::Equalcount\t\tTotalcount\t\tAccuracy");
	        bw.newLine();
	        bw.write("::" + equalCount + "\t\t" + compListA.size() + "\t\t" + ((double)equalCount / (double)compListA.size()));
	        bw.newLine();
	        
	        compListA.clear();
	        compListH.clear();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	/**
	 * m2w: this is for simple output
	 * @date 2011-02-22
	 * @param bw
	 * @param speakers
	 * @param auto_qscore
	 * @param human_qscore
	 */
	public void nistEval_simple(BufferedWriter bw, String[] speakers, int[] auto_qscore, int[] human_qscore){
		
		ArrayList<String> compListH = new ArrayList<String>();
		ArrayList<String> compListA = new ArrayList<String>();
		int equalCount = 0;
		
		/*checking input*/
		for (int k = 0; k < auto_qscore.length; k ++ ){
			System.out.println("auto_qscore: " + auto_qscore[k]);
		}

		System.out.println("----------------------------------------");

		for (int k = 0; k < human_qscore.length; k ++ ){
			System.out.println("human_qsore: " + human_qscore[k]);
		}

		System.out.println("========================================");
		
        try {
        	
	        for(int i = 0; i < (speakers.length - 1); i++){
	        
	        	/*1st round check*/
	        	if(auto_qscore == null || human_qscore == null){
	                continue;
	            }
	            if(auto_qscore[i] == 0 && human_qscore[i] == 0){
	                continue;
	            }
	            
	        	for(int j = i+1; j < speakers.length; j++){
	        		
	        		
	        		 /*2 if discard speaker not in subfile*/
	        		if(auto_qscore == null || human_qscore == null){
                        continue;
                    }
                    if(auto_qscore[j] == 0 && human_qscore[j] == 0){ //maybe -1
                        continue;
                    }
                                   
	        		String tempStra = null;
	        		String tempStrh = null;
	        		
	        		/*auto_file logic*/
	        		if(auto_qscore[i] < auto_qscore[j]){
	        			tempStra = speakers[i] + " < " + speakers[j];
	        		}
	        		
	        		if(auto_qscore[i] > auto_qscore[j]){
	        			tempStra = speakers[i] + " > " + speakers[j];
	        		}
	        		
	        		if(auto_qscore[i] == auto_qscore[j]){
	        			tempStra = speakers[i] + " = " + speakers[j];
	        		}
	        		
	        		/*human_file logic*/
	        		if (human_qscore[i] < human_qscore[j]){
	        			tempStrh = speakers[i] + " < " + speakers[j];
	        		
	        		}
	        		if (human_qscore[i] > human_qscore[j]){
	        			tempStrh = speakers[i] + " > " + speakers[j];
	        		}
	        		if (human_qscore[i] == human_qscore[j]){
	        			tempStrh = speakers[i] + " = " + speakers[j];
	        		}
	        		
	        		
	        		compListA.add(tempStra);// use to print result
	        		compListH.add(tempStrh);
	        		
	        		/*calculate equalcount*/
	        		if(tempStra.equals(tempStrh)){
	        			equalCount++;
	        		}
	        		
//	        		bw.write(tempStra + "\t\t" + tempStrh);
//	        		bw.newLine();
	        	}
	        	
	        } // for all speakers
	        
	        /*result eval line*/
	        bw.write("match\ttotal\tacc");
	        bw.newLine();
	        bw.write(equalCount + "\t" + compListA.size() + "\t" + ((double)equalCount / (double)compListA.size()));
	        bw.newLine();
	        
	        compListA.clear();
	        compListH.clear();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

        /**
	 * m2w: this is for simple output , Overloaded nist simple method, deal with doubles.
	 * @date 2011-02-22
	 * @param bw
	 * @param speakers
	 * @param auto_qscore String array
	 * @param human_qscore String array
	 */
	public void nistEval_simple(BufferedWriter bw, String[] speakers, String[] auto_ascore, String[] human_ascore){

		ArrayList<String> compListH = new ArrayList<String>();
		ArrayList<String> compListA = new ArrayList<String>();
		int equalCount = 0;
                String[] a_temp = auto_ascore;
                String[] h_temp = human_ascore;
                System.out.println(Arrays.asList(a_temp));
                System.out.println(Arrays.asList(h_temp));
                //naming like this is to make the old method useful
                Double[] auto_qscore = new Double[a_temp.length];
                Double[] human_qscore = new Double[h_temp.length];
                
//                turn the string array into double array
                for (int i = 0; i < a_temp.length; i++){
                    if(a_temp[i] == null){
                        auto_qscore[i] = null;
                    }else{
                        auto_qscore[i] = Double.parseDouble(a_temp[i]);
                    }
                    
                }

                for (int j = 0; j < h_temp.length; j++){
                    if(h_temp[j] == null){
                        human_qscore[j] = null;
                    }else{
                        human_qscore[j] = Double.parseDouble(h_temp[j]);
                    }
                }

                
//             =========================try to use the old method for double==========================================
//		/*checking input*/
//		for (int k = 0; k < auto_qscore.length; k ++ ){
//			System.out.println("auto_qscore: " + auto_qscore[k]);
//		}
//
//		System.out.println("----------------------------------------");
//
//		for (int k = 0; k < human_qscore.length; k ++ ){
//			System.out.println("human_qsore: " + human_qscore[k]);
//		}
//
//		System.out.println("========================================");

        try {

	        for(int i = 0; i < (speakers.length - 1); i++){

	        	/*1st round check*/
	        	if(auto_qscore == null || human_qscore == null){
                            continue;
                        }
                        if(auto_qscore[i] == null || human_qscore[i] == null){
                            continue;
                        }

	        	for(int j = i+1; j < speakers.length; j++){


	        		 /*2 if discard speaker not in subfile*/
	        		if(auto_qscore == null || human_qscore == null){
                                    continue;
                                }
                                if(auto_qscore[j] == null || human_qscore[j] == null){ //maybe -1
                                    continue;
                                }

	        		String tempStra = null;
	        		String tempStrh = null;

	        		/*auto_file logic*/
	        		if(auto_qscore[i] < auto_qscore[j]){
	        			tempStra = speakers[i] + " < " + speakers[j];
	        		}

	        		if(auto_qscore[i] > auto_qscore[j]){
	        			tempStra = speakers[i] + " > " + speakers[j];
	        		}

	        		if(auto_qscore[i] == auto_qscore[j]){
	        			tempStra = speakers[i] + " = " + speakers[j];
	        		}

	        		/*human_file logic*/
	        		if (human_qscore[i] < human_qscore[j]){
	        			tempStrh = speakers[i] + " < " + speakers[j];

	        		}
	        		if (human_qscore[i] > human_qscore[j]){
	        			tempStrh = speakers[i] + " > " + speakers[j];
	        		}
	        		if (human_qscore[i] == human_qscore[j]){
	        			tempStrh = speakers[i] + " = " + speakers[j];
	        		}

                                System.out.println(tempStra);
                                System.out.println(tempStrh);

                                // use to print result
                                if(!tempStra.contains("null") && tempStra != null){
                                    compListA.add(tempStra);
                                }

                                if(!tempStrh.contains("null") && tempStrh != null){
                                    compListH.add(tempStrh);
                                }
	        		

	        		/*calculate equalcount*/
                                if (!tempStra.contains("null") 
                                     && !tempStrh.contains("null")
                                     &&  tempStrh != null
                                     &&  tempStra != null

                                     ){
                                    if(tempStra.equals(tempStrh)){
	        			equalCount++;
                                    }
	        		}

	        		bw.write(tempStra + "\t\t" + tempStrh);
	        		bw.newLine();
	        	}

	        } // for all speakers

	        /*result eval line*/
	        bw.write("match\ttotal\tacc");
	        bw.newLine();
	        bw.write(equalCount + "\t" + compListA.size() + "\t" + ((double)equalCount / (double)compListA.size()));
	        bw.newLine();

	        compListA.clear();
	        compListH.clear();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
//	
//	public void compareEval_tsk_ctrl(BufferedWriter bw, String[] speakers, int[] auto_qscore, int[] human_qscore){ArrayList<String> compListH = new ArrayList<String>();
//	ArrayList<String> compListA = new ArrayList<String>();
//	int equalCount = 0;
//	
//	/*checking input*/
//	for (int k = 0; k < auto_qscore.length; k ++ ){
//		System.out.println("auto qscore : " + auto_qscore[k]);
//	}
//	
//	System.out.println("----------------------------------------");
//	
//	for (int k = 0; k < human_qscore.length; k ++ ){
//		System.out.println("human qscore : " + human_qscore[k]);
//	}
//	
//	System.out.println("========================================");
//	
//	 /*Relations*/
//    try {
//    	
//		bw.write("||||||||||||||||| Relations ||||||||||||||||||||");
//		bw.newLine();
//        bw.write("Auto_file\t\t" + "Human_file");
//        bw.newLine();
//		
//        for(int i = 0; i < (speakers.length - 1); i++){
//        	
//        	/*1st round check*/
//        	if(auto_qscore == null || human_qscore == null){
//                continue;
//            }
//            if(auto_qscore[i] == 0 && human_qscore[i] == 0){
//                continue;
//            }
//        
//        	for(int j = i+1; j < speakers.length; j++){
//        		
//        		
//        		 /*2 if discard speaker not in subfile*/
//        		if(auto_qscore == null || human_qscore == null){
//                    continue;
//                }
//                if(auto_qscore[j] == 0 && human_qscore[j] == 0){
//                    continue;
//                }
////        		if(auto_qscore == null || human_qscore == null){
////                    continue;
////                }
////        		
////                if(auto_qscore[j] == 0  && human_qscore[j] == 0){ //maybe -1
////                	System.out.println("here is an zero");
////                    continue;
////                }
//                               
//        		String tempStra = null;
//        		String tempStrh = null;
//        		
//        		/*auto_file logic*/
//        		if(auto_qscore[i] < auto_qscore[j]){
//        			tempStra = speakers[i] + " < " + speakers[j];
//        		}
//        		
//        		if(auto_qscore[i] > auto_qscore[j]){
//        			tempStra = speakers[i] + " > " + speakers[j];
//        		}
//        		
//        		if(auto_qscore[i] == auto_qscore[j]){
//        			tempStra = speakers[i] + " = " + speakers[j];
//        		}
//        		
//        		/*human_file logic*/
//        		if (human_qscore[i] < human_qscore[j]){
//        			tempStrh = speakers[i] + " < " + speakers[j];
//        		
//        		}
//        		if (human_qscore[i] > human_qscore[j]){
//        			tempStrh = speakers[i] + " > " + speakers[j];
//        		}
//        		if (human_qscore[i] == human_qscore[j]){
//        			tempStrh = speakers[i] + " = " + speakers[j];
//        		}
//        		
//        		
//        		compListA.add(tempStra);// use to print result
//        		compListH.add(tempStrh);
//        		
//        		/*calculate equalcount*/
//        		if(tempStra.equals(tempStrh)){
//        			equalCount++;
//        		}
//        		
//        		bw.write(tempStra + "\t\t" + tempStrh);
//        		bw.newLine();
//        	}
//        	
//        } // for all speakers
//        
//        /*result eval line*/
//        bw.write("::Equalcount\t\tAccuracy\t\tTotalcount");
//        bw.newLine();
//        bw.write("::" + equalCount + "\t\t" + ((double)equalCount / (double)compListA.size()) + "\t\t" + compListA.size());
//        bw.newLine();
//        
//        compListA.clear();
//        compListH.clear();
//        
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	}
}