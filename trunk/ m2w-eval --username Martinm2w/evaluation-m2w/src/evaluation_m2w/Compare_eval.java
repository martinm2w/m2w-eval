package evaluation_m2w;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author m2w
 *
 */


public class Compare_eval {
	
	public void compareEval(BufferedWriter bw, String[] speakers, int[] auto_qscore, int[] human_qscore){
		
		ArrayList<String> compListH = new ArrayList<String>();
		ArrayList<String> compListA = new ArrayList<String>();
		int equalCount = 0;
		
		 /*Relations*/
        try {
        	
			bw.write("***   Relations ***");
			bw.newLine();
	        bw.write("       Auto_file       \t" + "      Human_file       ");
	        bw.newLine();
			
	        for(int i = 0; i < speakers.length; i++){
	        
	        	for(int j = i+1; j < speakers.length; j++){
	        		
	        		String tempStra = null;
	        		String tempStrh = null;
	        		
	        		/*auto_file logic*/
	        		if(auto_qscore[i] < auto_qscore[j]){
	        			tempStra = speakers[i] + "<" + speakers[j];
	        		}else if (auto_qscore[i] > auto_qscore[j]){
	        			tempStra = speakers[i] + ">" + speakers[j];
	        		}else{
	        			tempStra = speakers[i] + "=" + speakers[j];
	        		}
	        		
	        		/*human_file logic*/
	        		if(human_qscore[i] < human_qscore[j]){
	        			tempStrh = speakers[i] + "<" + speakers[j];
	        		}else if (human_qscore[i] > human_qscore[j]){
	        			tempStrh = speakers[i] + ">" + speakers[j];
	        		}else{
	        			tempStrh = speakers[i] + "=" + speakers[j];
	        		}
	        		
	        		compListA.add(tempStra);// use to print result
	        		compListH.add(tempStrh);
	        		
	        		bw.write(tempStra + "\t" + tempStrh);
	        		bw.newLine();
	        	}
	        	
	        } // for all speakers
	        
	        /*result eval line*/
	        for(int i = 0; i < compListA.size(); i ++){
	        	
	        	if(compListA.get(i).equals(compListH.get(i))){
	        		
	        		equalCount++;
	        		
	        	}
	        	
	        }
	        
	        
	        
	        
	        bw.write("precentage :" + String.valueOf(equalCount / compListA.size()));
	        
	        
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
}
