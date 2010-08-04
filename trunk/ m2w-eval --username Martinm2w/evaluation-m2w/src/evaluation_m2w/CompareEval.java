package evaluation_m2w;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author m2w
 *
 */


public class CompareEval {
	
	public void compareEval(BufferedWriter bw, String[] speakers, int[] auto_qscore, int[] human_qscore){
		
		ArrayList<String> compListH = new ArrayList<String>();
		ArrayList<String> compListA = new ArrayList<String>();
		int equalCount = 0;
		
		 /*Relations*/
        try {
        	
			bw.write("********************   Relations ************************");
			bw.newLine();
	        bw.write("Auto_file\t\t" + "Human_file");
	        bw.newLine();
			
	        for(int i = 0; i < (speakers.length - 1); i++){
	        
	        	for(int j = i+1; j < speakers.length; j++){
	        		
	        		if(auto_qscore == null || human_qscore == null){
                        continue;
                    }
                    if(auto_qscore[j] == 0 && human_qscore[j] == 0){
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
	        bw.write("|||| Equalcount : "+ equalCount +"||||");
	        bw.newLine();
	        bw.write("|||| Accuracy :" + ((double)equalCount / (double)compListA.size()) + "||||");
	        bw.newLine();
	        
	        compListA.clear();
	        compListH.clear();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
}
